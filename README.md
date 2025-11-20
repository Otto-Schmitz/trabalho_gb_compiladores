### Micro-compilador

Este microcompilador tem o objetivo de implementar um compilador educacional mínimo, contendo as fases léxica, sintática e semântica, além de executar o código. Ele foi desenvolvido para compilar uma espécie de mini Java, limitando-se em alguns aspectos como tipos (apenas inteiro e double), operações (apenas +, -, \*, ), delimitadores( "(" e ")"), palavras-chave e funções (apenas imprimir).



No diretório src é possível encontrar:

* Main.java
  Arquivo principal que irá orquestrar e executar todas as etapas



* lexer/
  Contém o simple.lex, arquivo onde são descritos as regras para a análise léxica]



* parser/
  Contém, além do parser.cup, arquivo onde é descrita a gramática do mini Java, as demais classes necessárias implementar o parser
* semantic/



* exec/
  Contém o intepreter.java, classe responsável por interpretar a AST e executar o código.



* tests/
  Pasta de arquivos de teste e .txt com resultados esperados



# Compilação do micro-compilador

Com o terminal aberto no diretório principal (trabalho\_gb\_compiladores/), insira os seguintes comandos na ordem dada:

cd src
java -jar ..\\lib\\jflex-1.8.2.jar compilador\\lexer\\MiniLexer.lex

java -jar ..\\lib\\java-cup-11b.jar -parser Parser -symbols sym -destdir compilador\\parser compilador\\parser\\parser.cup

javac -cp .;..\\lib\\java-cup-11b.jar -d ..\\out compilador\\lexer\\\*.java compilador\\parser\\\*.java compilador\\semantic\\\*.java compilador\\exec\\\*.java compilador\\main\\\*.java



# Execução do micro-compilador

Com o terminal aberto no diretório principal (trabalho\_gb\_compiladores/), insira os seguintes comandos na ordem dada:

java -cp .;..\\lib\\java-cup-11b.jar Main teste.prog



Opcional se quiser gerar a AST graficamente:
dot -Tpng ast.dot -o ast.png

