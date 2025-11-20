import java.io.*;

public class TesteSuite {
    public static void main(String[] args) {
        System.out.println("=== SESSÃO DE TESTES DO ANALISADOR LÉXICO ===\n");
        
        // Teste 1: Código básico
        testar("Teste 1 - Código básico", "int x = 10;");
        
        // Teste 2: Estrutura condicional
        testar("Teste 2 - If/Else", "if (x > 5) { x = x + 1; } else { x = 0; }");
        
        // Teste 3: Loop
        testar("Teste 3 - While", "while (i < 10) { i = i + 1; }");
        
        // Teste 4: Tipos diferentes
        testar("Teste 4 - Tipos", "int a; float b; String c;");
        
        // Teste 5: Operadores
        testar("Teste 5 - Operadores", "x == y + a != b - c * d / e");
        
        // Teste 6: String
        testar("Teste 6 - String", "String nome = \"João\";");
        
        // Teste 7: Erro - caractere inválido
        testar("Teste 7 - Erro", "int x = 10@;");
        
        // Teste 8: For loop
        testar("Teste 8 - For loop", "for (int i = 0; i < 10; i++) { return i; }");
    }
    
    private static void testar(String nomeTeste, String codigo) {
        System.out.println("--- " + nomeTeste + " ---");
        System.out.println("Código: " + codigo);
        System.out.println("Tokens:");
        
        try {
            JavaLexer lexer = new JavaLexer(new StringReader(codigo));
            Token token;
            int count = 0;
            
            while ((token = lexer.yylex()) != null) {
                System.out.println("  " + (++count) + ". " + token);
            }
            
            if (count == 0) {
                System.out.println("  Nenhum token encontrado!");
            }
            
        } catch (Exception e) {
            System.out.println("  ERRO: " + e.getMessage());
        }
        
        System.out.println();
    }
}
