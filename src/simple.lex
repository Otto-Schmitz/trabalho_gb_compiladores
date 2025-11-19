%%

%class JavaLexer
%type Token
%line
%column
%state STR

%{
    StringBuffer string = new StringBuffer();
    private int strStartLine, strStartCol;

    private Token createToken(String tipo, String valor) {
        return new Token(tipo, valor, yyline + 1, yycolumn + 1);
    }
%}

// Macros
IDENTIFIER = [a-zA-Z_][a-zA-Z0-9_]*
NUM_INT = [0-9]+
NUM_REAL = [0-9]+\.[0-9]+
WHITESPACE = [ \t\r\n]+

ASSIGNMENT = [=]
OPERATOR = [\+|\-|\*|\/|%|\+\+|\-\-|\+=|\-=|\*=|/=|%=|&|\||\^|~|<<|>>|&=|\|\^=|<<=|>>=]
COMPARISON = [==|!=|<=|>=|<|>]
DELIMITER = [(){}\\;,\.]
CHAR = \'([^\\'\n]|\\[nrt\"\'\\])\'
STRINGDELIMITER = \"

%%

<YYINITIAL>{
    // Palavras-chave de controle e estrutura
    "if"            { return createToken("KEYWORD", yytext()); }
    "else"          { return createToken("KEYWORD", yytext()); }
    "while"         { return createToken("KEYWORD", yytext()); }
    "for"           { return createToken("KEYWORD", yytext()); }
    "return"        { return createToken("KEYWORD", yytext()); }
    "public"        { return createToken("KEYWORD", yytext()); }
    "private"       { return createToken("KEYWORD", yytext()); }
    "protected"     { return createToken("KEYWORD", yytext()); }
    "static"        { return createToken("KEYWORD", yytext()); }
    "class"         { return createToken("KEYWORD", yytext()); }
    "void"          { return createToken("KEYWORD", yytext()); }
    "new"           { return createToken("KEYWORD", yytext()); }
    "this"          { return createToken("KEYWORD", yytext()); }
    "super"         { return createToken("KEYWORD", yytext()); }
    
    // Tipos - equivalentes a "inteiro" e "real"
    "int"           { return createToken("TYPE", yytext()); }
    "float"         { return createToken("TYPE", yytext()); }
    "double"        { return createToken("TYPE", yytext()); }
    "String"        { return createToken("TYPE", yytext()); }
    "char"          { return createToken("TYPE", yytext()); }
    "boolean"       { return createToken("TYPE", yytext()); }
    "long"          { return createToken("TYPE", yytext()); }
    "short"         { return createToken("TYPE", yytext()); }
    "byte"          { return createToken("TYPE", yytext()); }
    
    // Comando de impressão - equivalente a "imprima"
    "System.out.println" { return createToken("PRINT", yytext()); }
    "System.out.print"   { return createToken("PRINT", yytext()); }
    
    // Valores literais
    "true"          { return createToken("BOOLEAN_LIT", yytext()); }
    "false"         { return createToken("BOOLEAN_LIT", yytext()); }
    "null"          { return createToken("NULL_LIT", yytext()); }

    // Operadores e comparação
    {ASSIGNMENT}    { return createToken("ASSIGNMENT", yytext()); }
    {OPERATOR}      { return createToken("OPERATOR", yytext()); }
    {COMPARISON}    { return createToken("COMPARISON", yytext()); }

    // Delimitadores (agora inclui o ponto)
    {DELIMITER}     { return createToken("DELIMITER", yytext()); }

    // Números - SEPARADOS em inteiros e reais
    {NUM_REAL}      { return createToken("NUM_REAL", yytext()); }
    {NUM_INT}       { return createToken("NUM_INT", yytext()); }
    
    // Identificadores
    {IDENTIFIER}    { return createToken("IDENTIFIER", yytext()); }

    // Ignorar espaços em branco
    {WHITESPACE}    { /* ignorar */ }

    // Strings
    {STRINGDELIMITER}             { string.setLength(0); yybegin(STR); strStartLine = yyline + 1;  strStartCol  = yycolumn + 1; }

    // Char literal (corrigido para aceitar '\\')
    {CHAR}  { return createToken("CHAR", yytext().substring(1, yytext().length()-1)); }
}

<STR>{
    \"              { yybegin(YYINITIAL); return createToken("STRING", string.toString()); }
    [^\n\r\"\\]+    { string.append( yytext() ); }
    \\t             { string.append('\t'); }
    \\n             { string.append('\n'); }
    \\r             { string.append('\r'); }
    \\\"            { string.append('\"'); }
    \\\\            { string.append('\\'); }
    \r\n|\n|\r      { yybegin(YYINITIAL); return createToken("ERROR","Unterminated string starting at " + strStartLine + ":" + strStartCol); }
    \\[^\"nrt\\u]   { yybegin(YYINITIAL); return createToken("ERROR", "Invalid escape in string: " + yytext() + " at " + strStartLine + ":" + strStartCol); }
}

// Caracteres não reconhecidos
.               { return createToken("ERROR", yytext()); }

<<EOF>> {
  if (yystate() == STR) {
    yybegin(YYINITIAL);
    return createToken("ERROR",
      "Unterminated string at EOF (started at " + strStartLine + ":" + strStartCol + ")");
  }
  return null;
}
