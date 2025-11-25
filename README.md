# Micro-compilador

Este microcompilador tem o objetivo de implementar um compilador educacional mínimo, contendo as fases léxica, sintática e semântica, além de executar o código. Ele foi desenvolvido para compilar uma linguagem mínima, limitando-se em alguns aspectos como tipos (apenas inteiro e real), operações (apenas +, -, \*, ), delimitadores( "(" e ")"), palavras-chave e funções (apenas imprimir).



No diretório src é possível encontrar:

* Main.java
  Arquivo principal que irá orquestrar e executar todas as etapas



* lexer/
  Contém o simple.lex, arquivo onde são descritos as regras para a análise léxica



* parser/
  Contém, além do parser.cup, arquivo onde é descrita a gramática do mini Java, as demais classes necessárias implementar o parser

* semantic/
  Contém o analizador semântico.

* exec/
  Contém o intepreter.java, classe responsável por interpretar a AST e executar o código.

* tests/
  Pasta de arquivos de teste e .txt com resultados esperados



## Compilação do micro-compilador

Com o terminal aberto no diretório principal (trabalho\_gb\_compiladores/) o comando:

python run.py --build


## Execução do micro-compilador

Com o terminal aberto no diretório principal (trabalho\_gb\_compiladores/):

*Geração tokens*

python run.py --tokens tests\arquivo.prog

*Geração AST*

python run.py --ast tests\arquivo.prog

*Roda teste*

python run.py tests\arquivo.prog
ou
python run.py --run tests\arquivo.prog


