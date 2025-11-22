package compilador.lexer;

import java_cup.runtime.Symbol;
import compilador.parser.sym; 
import compilador.semantic.ErrorReporter;

%%
%public
%class MiniLexer
%cup
%line
%column

%{

  private Symbol symbol(int type) {
    return new Symbol(type, yyline + 1, yycolumn + 1, yytext());
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
COMMENT  = "//".*

%%

{WS}              { /* ignora espaços/brancos */ }
{COMMENT}         { /* ignora comentários // */ }

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
","               { return symbol(sym.COMMA); }

{NUM_REAL}        { return symbol(sym.NUM_REAL, Double.valueOf(yytext())); }
{NUM_INT}         { return symbol(sym.NUM_INT, Integer.valueOf(yytext())); }

{IDENT}           { return symbol(sym.IDENT, yytext()); }

.                 { ErrorReporter.lexicalError(yyline + 1, yycolumn + 1, yytext()); 
                    // Não retorne null, apenas continue
                  }

<<EOF>>           { return new Symbol(sym.EOF); }
