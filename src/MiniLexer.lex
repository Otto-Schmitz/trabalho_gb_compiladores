import java_cup.runtime.Symbol;

%%

%class MiniLexer
%cup
%line
%column

%{

  private Symbol symbol(int type) {
    return new Symbol(type, yyline + 1, yycolumn + 1);
  }

  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline + 1, yycolumn + 1, value);
  }
%}

// Macros
IDENT    = [a-zA-Z_][a-zA-Z0-9_]*
NUM_INT  = [0-9]+
NUM_REAL = [0-9]+"."[0-9]+
WS       = [ \t\r\n]+

%%

{WS}              { /* ignora espaços/brancos */ }

"inicio"          { return symbol(sym.INICIO); }
"fim"             { return symbol(sym.FIM); }
"inteiro"         { return symbol(sym.INTEIRO); }
"real"            { return symbol(sym.REAL); }
"imprima"         { return symbol(sym.IMPRIMA); }

":="              { return symbol(sym.ATRIB); }

"+"               { return symbol(sym.PLUS); }
"-"               { return symbol(sym.MINUS); }
"*"               { return symbol(sym.TIMES); }
"/"               { return symbol(sym.DIV); }

";"               { return symbol(sym.SEMI); }
"("               { return symbol(sym.LPAREN); }
")"               { return symbol(sym.RPAREN); }
"."               { return symbol(sym.DOT); }

{NUM_REAL}        { return symbol(sym.NUM_REAL, Double.valueOf(yytext())); }
{NUM_INT}         { return symbol(sym.NUM_INT, Integer.valueOf(yytext())); }

{IDENT}           { return symbol(sym.IDENT, yytext()); }

.                 { ErrorReporter.lexicalError(yyline + 1, yycolumn + 1, yytext()); 
                    return null; 
                  }

<<EOF>>           { return null; }
