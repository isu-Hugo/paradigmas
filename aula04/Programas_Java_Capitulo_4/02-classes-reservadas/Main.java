import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Main {
    static final String ALUNO = "SUBSTITUA_PELO_SEU_NOME";
    static final String RA = "00000000";

    static final String IDENTIFICADOR_PERSONALIZADO = "nota_mg";

    static final Set<String> PALAVRAS_RESERVADAS = new HashSet<>(
            Arrays.asList("int", "if", "else", "while", "return")
    );

    public static void main(String[] args) {
        System.out.println("ALUNO: " + ALUNO + " | RA: " + RA);
        executarTeste("TESTE A - entrada compacta",
                "int total=valor+10;");
        executarTeste("TESTE B - espaços e comentário",
                "  int   total = valor + 10;  // este comentario sera ignorado");
        executarTeste("TESTE C - reservadas e identificadores",
                "int inteiro = 0; while (inteiro < intValor) inteiro = inteiro + 1;");
        executarTeste("TESTE C2 - identificador personalizado",
                "int " + IDENTIFICADOR_PERSONALIZADO + " = 10;");
        executarTeste("TESTE D - caractere inválido",
                "int valor# = 1;");
    }

    static void executarTeste(String titulo, String entrada) {
        System.out.println();
        System.out.println("=== " + titulo + " ===");
        System.out.println("ENTRADA: " + entrada);
        System.out.printf("%-22s %-22s %s%n", "LEXEMA", "CLASSE", "TOKEN");
        System.out.println("----------------------------------------------------------------");
        analisar(entrada);
    }

    static void analisar(String fonte) {
        int posicao = 0;

        while (posicao < fonte.length()) {
            char atual = fonte.charAt(posicao);

            if (Character.isWhitespace(atual)) {
                posicao++;
                continue;
            }

            if (atual == '/' && posicao + 1 < fonte.length()
                    && fonte.charAt(posicao + 1) == '/') {
                String comentario = fonte.substring(posicao);
                imprimir(comentario, "COMENTARIO", "COMENTARIO_IGNORADO");
                break;
            }

            if (Character.isLetter(atual) || atual == '_') {
                int inicio = posicao;
                posicao++;
                while (posicao < fonte.length()) {
                    char proximo = fonte.charAt(posicao);
                    if (!Character.isLetterOrDigit(proximo) && proximo != '_') {
                        break;
                    }
                    posicao++;
                }
                String lexema = fonte.substring(inicio, posicao);
                String token = PALAVRAS_RESERVADAS.contains(lexema)
                        ? "PALAVRA_RESERVADA"
                        : "IDENTIFICADOR";
                imprimir(lexema, "LETRA", token);
                continue;
            }

            if (Character.isDigit(atual)) {
                int inicio = posicao;
                posicao++;
                while (posicao < fonte.length()
                        && Character.isDigit(fonte.charAt(posicao))) {
                    posicao++;
                }
                imprimir(fonte.substring(inicio, posicao), "DIGITO", "INTEIRO");
                continue;
            }

            String token = lookup(atual);
            if (token.equals("ERRO_LEXICO")) {
                imprimir(String.valueOf(atual), "DESCONHECIDO", token);
            } else {
                imprimir(String.valueOf(atual), "SIMBOLO", token);
            }
            posicao++;
        }
    }

    static String lookup(char caractere) {
        switch (caractere) {
            case '=': return "ATRIBUICAO";
            case '+': return "SOMA";
            case '-': return "SUBTRACAO";
            case '*': return "MULTIPLICACAO";
            case '/': return "DIVISAO";
            case '<': return "MENOR_QUE";
            case '>': return "MAIOR_QUE";
            case ';': return "PONTO_E_VIRGULA";
            case '(': return "ABRE_PARENTESES";
            case ')': return "FECHA_PARENTESES";
            case '{': return "ABRE_CHAVE";
            case '}': return "FECHA_CHAVE";
            default: return "ERRO_LEXICO";
        }
    }

    static void imprimir(String lexema, String classe, String token) {
        System.out.printf("%-22s %-22s %s%n", lexema, classe, token);
    }
}
