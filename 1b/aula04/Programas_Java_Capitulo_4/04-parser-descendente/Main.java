public class Main {
    static final String ALUNO = "SUBSTITUA_PELO_SEU_NOME";
    static final String RA = "00000000";

    static final String ENTRADA = "2 + 3 * 4";
    static final boolean DEMONSTRAR_RECURSAO_ESQUERDA = false;

    public static void main(String[] args) {
        System.out.println("ALUNO: " + ALUNO + " | RA: " + RA);
        System.out.println("ENTRADA: " + ENTRADA);
        System.out.println();

        if (DEMONSTRAR_RECURSAO_ESQUERDA) {
            demonstrarRecursaoEsquerda(0);
            System.out.println();
        }

        try {
            Parser parser = new Parser(ENTRADA);
            int resultado = parser.analisar();
            System.out.println();
            System.out.println("ENTRADA ACEITA");
            System.out.println("RESULTADO DA EXPRESSAO: " + resultado);
        } catch (ErroSintatico erro) {
            System.out.println();
            System.out.println("ERRO SINTATICO: " + erro.getMessage());
        }
    }

    static void demonstrarRecursaoEsquerda(int nivel) {
        System.out.println(recuo(nivel)
                + "expr -> expr + term  (chamada " + (nivel + 1) + ")");

        if (nivel == 7) {
            System.out.println(recuo(nivel + 1)
                    + "DEMONSTRACAO INTERROMPIDA: a regra chamou expr novamente"
                    + " sem consumir a entrada.");
            return;
        }

        demonstrarRecursaoEsquerda(nivel + 1);
    }

    static String recuo(int nivel) {
        StringBuilder espacos = new StringBuilder();
        for (int i = 0; i < nivel; i++) {
            espacos.append("  ");
        }
        return espacos.toString();
    }

    enum TipoToken {
        NUMERO, MAIS, VEZES, ABRE_PARENTESES, FECHA_PARENTESES, FIM
    }

    static class Token {
        final TipoToken tipo;
        final String lexema;

        Token(TipoToken tipo, String lexema) {
            this.tipo = tipo;
            this.lexema = lexema;
        }
    }

    static class Lexer {
        final String fonte;
        int posicao;

        Lexer(String fonte) {
            this.fonte = fonte;
        }

        Token proximoToken() {
            while (posicao < fonte.length()
                    && Character.isWhitespace(fonte.charAt(posicao))) {
                posicao++;
            }

            if (posicao >= fonte.length()) {
                return new Token(TipoToken.FIM, "$FIM$");
            }

            char atual = fonte.charAt(posicao);
            if (Character.isDigit(atual)) {
                int inicio = posicao;
                while (posicao < fonte.length()
                        && Character.isDigit(fonte.charAt(posicao))) {
                    posicao++;
                }
                return new Token(TipoToken.NUMERO,
                        fonte.substring(inicio, posicao));
            }

            posicao++;
            switch (atual) {
                case '+': return new Token(TipoToken.MAIS, "+");
                case '*': return new Token(TipoToken.VEZES, "*");
                case '(': return new Token(TipoToken.ABRE_PARENTESES, "(");
                case ')': return new Token(TipoToken.FECHA_PARENTESES, ")");
                default:
                    throw new ErroSintatico("caractere inesperado '" + atual + "'");
            }
        }
    }

    static class Parser {
        final Lexer lexer;
        Token lookahead;
        int nivel;

        Parser(String fonte) {
            lexer = new Lexer(fonte);
            lookahead = lexer.proximoToken();
        }

        int analisar() {
            int valor = expr();
            if (lookahead.tipo != TipoToken.FIM) {
                falhar("era esperado o fim da entrada");
            }
            return valor;
        }

        // expr -> term { + term }
        int expr() {
            entrar("expr");
            int valor = term();

            while (lookahead.tipo == TipoToken.MAIS) {
                evento("Encontrado operador +");
                consumir(TipoToken.MAIS);
                valor += term();
            }

            sair("expr");
            return valor;
        }

        // term -> factor { * factor }
        int term() {
            entrar("term");
            int valor = factor();

            while (lookahead.tipo == TipoToken.VEZES) {
                evento("Encontrado operador *");
                consumir(TipoToken.VEZES);
                valor *= factor();
            }

            sair("term");
            return valor;
        }

        // factor -> NUMERO | ( expr )
        int factor() {
            entrar("factor");
            int valor;

            if (lookahead.tipo == TipoToken.NUMERO) {
                valor = Integer.parseInt(lookahead.lexema);
                evento("Numero reconhecido: " + lookahead.lexema);
                consumir(TipoToken.NUMERO);
            } else if (lookahead.tipo == TipoToken.ABRE_PARENTESES) {
                evento("Abrindo parenteses");
                consumir(TipoToken.ABRE_PARENTESES);
                valor = expr();
                consumir(TipoToken.FECHA_PARENTESES);
                evento("Fechando parenteses");
            } else {
                falhar("era esperado um numero ou '('");
                return 0;
            }

            sair("factor");
            return valor;
        }

        void consumir(TipoToken esperado) {
            if (lookahead.tipo != esperado) {
                falhar("era esperado " + esperado);
            }
            lookahead = lexer.proximoToken();
            evento("Novo lookahead: " + lookahead.lexema);
        }

        void entrar(String regra) {
            System.out.println(recuo(nivel) + "ENTRANDO em <" + regra
                    + "> | lookahead = " + lookahead.lexema);
            nivel++;
        }

        void sair(String regra) {
            nivel--;
            System.out.println(recuo(nivel) + "SAINDO de <" + regra
                    + "> | lookahead = " + lookahead.lexema);
        }

        void evento(String mensagem) {
            System.out.println(recuo(nivel) + mensagem);
        }

        void falhar(String detalhe) {
            throw new ErroSintatico(detalhe + "; lookahead = " + lookahead.lexema);
        }
    }

    static class ErroSintatico extends RuntimeException {
        ErroSintatico(String mensagem) {
            super(mensagem);
        }
    }
}
