package compilador.parser;
import java.io.*;

public class LexerTest {
    public static void main(String[] args) {
        try {
            FileReader input;
            input = new FileReader("src/TestLexer.txt"); // caminho fixo
            JavaLexer lexer = new JavaLexer(input);
            

            System.out.println("Análise léxica do código!");

            
            Token token;
            while ((token = lexer.yylex()) != null) {
                System.out.println(token);
            }
            input.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}