import java.util.LinkedHashMap;
import java.util.Map;

public class Main {
    static final String ALUNO = "SUBSTITUA_PELO_SEU_NOME";
    static final String RA = "00000000";

    static final String ENTRADA = "resultado = somaAnterior - valor / 100;";

    private static final Map<Character, String> SIMBOLOS = new LinkedHashMap<>();

    static {
        SIMBOLOS.put('=', "ATRIBUICAO");
        SIMBOLOS.put('+', "SOMA");
        SIMBOLOS.put('-', "SUBTRACAO");
        SIMBOLOS.put('*', "MULTIPLICACAO");
        SIMBOLOS.put('/', "DIVISAO");
        SIMBOLOS.put(';', "PONTO_E_VIRGULA");
        SIMBOLOS.put('(', "ABRE_PARENTESES");
        SIMBOLOS.put(')', "FECHA_PARENTESES");
    }

    public static void main(String[] args) {
        System.out.println("ALUNO: " + ALUNO + " | RA: " + RA);
        System.out.println("ENTRADA: " + ENTRADA);
        System.out.println();
        System.out.printf("%-22s %s%n", "LEXEMA", "TOKEN");
        System.out.println("------------------------------------------");

        analisar(ENTRADA);
    }

    static void analisar(String fonte) {
        int posicao = 0;

        while (posicao < fonte.length()) {
            char atual = fonte.charAt(posicao);

            if (Character.isWhitespace(atual)) {
                posicao++;
                continue;
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
                imprimir(fonte.substring(inicio, posicao), "IDENTIFICADOR");
                continue;
            }

            if (Character.isDigit(atual)) {
                int inicio = posicao;
                posicao++;
                while (posicao < fonte.length()
                        && Character.isDigit(fonte.charAt(posicao))) {
                    posicao++;
                }
                imprimir(fonte.substring(inicio, posicao), "INTEIRO");
                continue;
            }

            String token = SIMBOLOS.get(atual);
            if (token != null) {
                imprimir(String.valueOf(atual), token);
            } else {
                imprimir(String.valueOf(atual), "ERRO_LEXICO");
            }
            posicao++;
        }
    }

    static void imprimir(String lexema, String token) {
        System.out.printf("%-22s %s%n", lexema, token);
    }
}
