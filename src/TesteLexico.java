import java.io.*;

public class TesteLexico {
    public static void main(String[] args) {
        System.out.println("=== TESTES LEXICOS ===\n");
        
        // Teste 1: Separação de NUM_INT e NUM_REAL
        testar("Teste 1 - Números Inteiros e Reais", 
               "int x = 42; double pi = 3.14; float y = 2.5;");
        
        // Teste 2: Token PRINT (System.out.println e System.out.print)
        testar("Teste 2 - Comandos de Impressão", 
               "System.out.println(x); System.out.print(y);");
        
        // Teste 3: Novos tipos (double, boolean, long, short, byte)
        testar("Teste 3 - Novos Tipos", 
               "double d = 1.5; boolean flag = true; long l = 100; short s = 10; byte b = 5;");
        
        // Teste 4: Novas palavras-chave (private, protected, static, new, this, super)
        testar("Teste 4 - Novas Palavras-chave", 
               "private static int count; protected String name; this.x = new Object(); super.method();");
        
        // Teste 5: Delimitador ponto em chamadas de método
        testar("Teste 5 - Ponto como Delimitador", 
               "System.out.println(obj.getValue());");
        
        // Teste 6: IDENT - Identificadores diversos
        testar("Teste 6 - Identificadores (IDENT)", 
               "int soma; float _valor; String nome123; boolean isValid_Flag;");
        
        // Teste 7: Literais booleanos e null
        testar("Teste 7 - Literais Especiais", 
               "boolean a = true; boolean b = false; Object obj = null;");
        
        // Teste 8: Expressões complexas com números inteiros e reais
        testar("Teste 8 - Expressões Mistas", 
               "double resultado = 10 + 3.5 * 2.0 - 1;");
        
        // Teste 9: Código completo com todas as features
        testar("Teste 9 - Código Completo", 
               "public class Teste { private static double PI = 3.14159; " +
               "public void calcular() { int x = 10; float y = 2.5; " +
               "double z = x + y; System.out.println(z); } }");
        
        // Teste 10: Apenas números para verificar separação correta
        testar("Teste 10 - Apenas Números", 
               "123 456.789 0.5 1000 99.99 0 0.0");
        
        // Teste 11: System.out.print vs System.out.println
        testar("Teste 11 - Diferença entre print e println", 
               "System.out.print(x); System.out.println(y);");
        
        // Teste 12: Encadeamento de pontos (acesso a membros)
        testar("Teste 12 - Encadeamento de Membros", 
               "obj.campo.metodo().valor;");

        // Teste 13: Teste de Precedência de Operadores
       //  testar("Teste 13 - Teste de Precedência",
       //  "x = 10 + 3.5 * 2.0 - 1;");
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
            e.printStackTrace();
        }
        
        System.out.println();
    }
    
    // Método adicional para testar com arquivo
    public static void testarArquivo(String caminhoArquivo) {
        System.out.println("=== TESTE DE ARQUIVO: " + caminhoArquivo + " ===\n");
        
        try {
            FileReader input = new FileReader(caminhoArquivo);
            JavaLexer lexer = new JavaLexer(input);
            Token token;
            int count = 0;
            
            while ((token = lexer.yylex()) != null) {
                System.out.println((++count) + ". " + token);
            }
            
            input.close();
            System.out.println("\nTotal de tokens: " + count);
            
        } catch (Exception e) {
            System.out.println("ERRO ao ler arquivo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
