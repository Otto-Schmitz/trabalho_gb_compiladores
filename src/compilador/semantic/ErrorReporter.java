package compilador.semantic;
public class ErrorReporter {

    private static boolean hasError = false;

    public static void reset() {
        hasError = false;
    }

    public static boolean hasErrors() {
        return hasError;
    }

    public static void lexicalError(int line, int column, String lexeme) {
        hasError = true;
        System.err.printf(
            "Erro léxico na linha %d, coluna %d: caractere inválido '%s'%n",
            line, column, lexeme
        );
    }

    public static void syntaxError(int line, int column, String message) {
        hasError = true;
        System.err.printf(
            "Erro sintático na linha %d, coluna %d: %s%n",
            line, column, message
        );
    }

    public static void semanticError(int line, int column, String message) {
        hasError = true;
        if (line > 0 && column > 0) {
            System.err.printf(
                "Erro semântico na linha %d, coluna %d: %s%n",
                line, column, message
            );
        } else {
            System.err.printf("Erro semântico: %s%n", message);
        }
    }
}