import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    static final String ALUNO = "SUBSTITUA_PELO_SEU_NOME";
    static final String RA = "00000000";

    static final String ENTRADA_VALIDA = "id+id*id";
    static final String ENTRADA_INVALIDA = "id+*id";

    // Na primeira execução, use ENTRADA_VALIDA. Depois troque por ENTRADA_INVALIDA.
    static final String ENTRADA = ENTRADA_VALIDA;

    static final Map<Integer, Map<String, String>> ACTION = new HashMap<>();
    static final Map<Integer, Map<String, Integer>> GOTO = new HashMap<>();
    static final Map<Integer, Regra> REGRAS = new HashMap<>();

    static {
        // Gramática:
        // 1) E -> E + T    2) E -> T
        // 3) T -> T * F    4) T -> F
        // 5) F -> ( E )    6) F -> id
        regra(1, "E", 3, "E -> E + T");
        regra(2, "E", 1, "E -> T");
        regra(3, "T", 3, "T -> T * F");
        regra(4, "T", 1, "T -> F");
        regra(5, "F", 3, "F -> ( E )");
        regra(6, "F", 1, "F -> id");

        action(0, "id", "s5"); action(0, "(", "s4");
        action(1, "+", "s6"); action(1, "$", "acc");
        action(2, "+", "r2"); action(2, "*", "s7");
        action(2, ")", "r2"); action(2, "$", "r2");
        action(3, "+", "r4"); action(3, "*", "r4");
        action(3, ")", "r4"); action(3, "$", "r4");
        action(4, "id", "s5"); action(4, "(", "s4");
        action(5, "+", "r6"); action(5, "*", "r6");
        action(5, ")", "r6"); action(5, "$", "r6");
        action(6, "id", "s5"); action(6, "(", "s4");
        action(7, "id", "s5"); action(7, "(", "s4");
        action(8, "+", "s6"); action(8, ")", "s11");
        action(9, "+", "r1"); action(9, "*", "s7");
        action(9, ")", "r1"); action(9, "$", "r1");
        action(10, "+", "r3"); action(10, "*", "r3");
        action(10, ")", "r3"); action(10, "$", "r3");
        action(11, "+", "r5"); action(11, "*", "r5");
        action(11, ")", "r5"); action(11, "$", "r5");

        irPara(0, "E", 1); irPara(0, "T", 2); irPara(0, "F", 3);
        irPara(4, "E", 8); irPara(4, "T", 2); irPara(4, "F", 3);
        irPara(6, "T", 9); irPara(6, "F", 3);
        irPara(7, "F", 10);
    }

    public static void main(String[] args) {
        System.out.println("ALUNO: " + ALUNO + " | RA: " + RA);
        System.out.println("ENTRADA: " + ENTRADA);
        System.out.println();
        analisar(ENTRADA);
    }

    static void analisar(String fonte) {
        List<String> entrada;
        try {
            entrada = tokenizar(fonte);
        } catch (IllegalArgumentException erro) {
            System.out.printf("%-34s %-22s %s%n", "$ 0", fonte, "ERROR: " + erro.getMessage());
            return;
        }

        List<Object> pilha = new ArrayList<>();
        pilha.add("$");
        pilha.add(0);
        int posicao = 0;

        System.out.printf("%-34s %-22s %s%n", "PILHA", "ENTRADA", "ACAO");
        System.out.println("--------------------------------------------------------------------------------");

        while (true) {
            int estado = (Integer) pilha.get(pilha.size() - 1);
            String simbolo = entrada.get(posicao);
            String acao = consultarAction(estado, simbolo);

            if (acao == null) {
                linha(pilha, restante(entrada, posicao),
                        "ERROR: nenhuma ação para estado " + estado + " e símbolo " + simbolo);
                return;
            }

            if (acao.startsWith("s")) {
                int novoEstado = Integer.parseInt(acao.substring(1));
                linha(pilha, restante(entrada, posicao),
                        "SHIFT " + simbolo + " -> estado " + novoEstado);
                pilha.add(simbolo);
                pilha.add(novoEstado);
                posicao++;
                continue;
            }

            if (acao.startsWith("r")) {
                int numeroRegra = Integer.parseInt(acao.substring(1));
                Regra regra = REGRAS.get(numeroRegra);
                linha(pilha, restante(entrada, posicao),
                        "REDUCE " + regra.descricao);

                for (int i = 0; i < regra.tamanhoDireita * 2; i++) {
                    pilha.remove(pilha.size() - 1);
                }

                int estadoAnterior = (Integer) pilha.get(pilha.size() - 1);
                Integer destino = consultarGoto(estadoAnterior, regra.esquerda);
                if (destino == null) {
                    linha(pilha, restante(entrada, posicao), "ERROR em GOTO");
                    return;
                }
                pilha.add(regra.esquerda);
                pilha.add(destino);
                continue;
            }

            if (acao.equals("acc")) {
                linha(pilha, restante(entrada, posicao), "ACCEPT");
                return;
            }
        }
    }

    static List<String> tokenizar(String fonte) {
        List<String> tokens = new ArrayList<>();
        int posicao = 0;

        while (posicao < fonte.length()) {
            char atual = fonte.charAt(posicao);

            if (Character.isWhitespace(atual)) {
                posicao++;
            } else if (posicao + 1 < fonte.length()
                    && fonte.substring(posicao, posicao + 2).equals("id")) {
                tokens.add("id");
                posicao += 2;
            } else if (atual == '+' || atual == '*' || atual == '('
                    || atual == ')') {
                tokens.add(String.valueOf(atual));
                posicao++;
            } else {
                throw new IllegalArgumentException("símbolo desconhecido '" + atual + "'");
            }
        }

        tokens.add("$");
        return tokens;
    }

    static void linha(List<Object> pilha, String entrada, String acao) {
        System.out.printf("%-34s %-22s %s%n", exibirPilha(pilha), entrada, acao);
    }

    static String exibirPilha(List<Object> pilha) {
        StringBuilder texto = new StringBuilder();
        for (Object item : pilha) {
            if (texto.length() > 0) {
                texto.append(' ');
            }
            texto.append(item);
        }
        return texto.toString();
    }

    static String restante(List<String> entrada, int posicao) {
        StringBuilder texto = new StringBuilder();
        for (int i = posicao; i < entrada.size(); i++) {
            texto.append(entrada.get(i));
        }
        return texto.toString();
    }

    static String consultarAction(int estado, String simbolo) {
        Map<String, String> linha = ACTION.get(estado);
        return linha == null ? null : linha.get(simbolo);
    }

    static Integer consultarGoto(int estado, String simbolo) {
        Map<String, Integer> linha = GOTO.get(estado);
        return linha == null ? null : linha.get(simbolo);
    }

    static void action(int estado, String simbolo, String acao) {
        ACTION.computeIfAbsent(estado, chave -> new HashMap<>())
                .put(simbolo, acao);
    }

    static void irPara(int estado, String simbolo, int destino) {
        GOTO.computeIfAbsent(estado, chave -> new HashMap<>())
                .put(simbolo, destino);
    }

    static void regra(int numero, String esquerda, int tamanhoDireita,
                      String descricao) {
        REGRAS.put(numero, new Regra(esquerda, tamanhoDireita, descricao));
    }

    static class Regra {
        final String esquerda;
        final int tamanhoDireita;
        final String descricao;

        Regra(String esquerda, int tamanhoDireita, String descricao) {
            this.esquerda = esquerda;
            this.tamanhoDireita = tamanhoDireita;
            this.descricao = descricao;
        }
    }
}
