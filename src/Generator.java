import java.nio.file.Paths;

public class Generator {

  public static void main(String[] args) {

    String rootPath = Paths.get("").toAbsolutePath().toString();
    String lexFile = rootPath + "/src/lexico/simple.lex";  

    System.out.println("Gerando analisador léxico a partir de: " + lexFile);

    try {
      String[] jflexArgs = { 
        "-d", rootPath + "/src",  // Gera em src/, JFlex cria subpasta lexico automaticamente
        lexFile 
      };
      jflex.Main.generate(jflexArgs);

      System.out.println("Analisador gerado com sucesso em src/lexico/JavaLexer.java!");

    } catch (Exception e) {
      System.err.println("Erro ao gerar analisador: " + e.getMessage());
      e.printStackTrace();
    }
  }
}