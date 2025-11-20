package oldStuff;
// Comando para executar via terminal no VS Code:
// java -cp ".;./lib/jflex-1.9.1.jar;./src" Generator
//java -cp ".;./lib/jflex-1.8.2.jar;./src" Generator
//javac -cp ".;./lib/jflex-1.8.2.jar" src/*.java
//java -cp ".;./lib/jflex-1.8.2.jar;./src" LexerTest

import java.nio.file.Paths;

public class Generator {

  public static void main(String[] args) {

    // L10: Obtém o caminho absoluto da raiz do projeto
    String rootPath = Paths.get("").toAbsolutePath().toString();

    // L11: Define o caminho do arquivo .lex
    String lexFile = rootPath + "/src/simple.lex";

    // L13: Exibe o caminho do arquivo .lex
    System.out.println("Gerando analisador léxico a partir de: " + lexFile);

    try {
      // L18: Chama o método generate com array de String (caminho do .lex)
      String[] jflexArgs = { lexFile };
      jflex.Main.generate(jflexArgs);

      // L19: Mensagem de sucesso
      System.out.println("Analisador gerado com sucesso!");

    } catch (Exception e) {
      // L21-22: Mensagem de erro
      System.err.println("Erro ao gerar analisador: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
