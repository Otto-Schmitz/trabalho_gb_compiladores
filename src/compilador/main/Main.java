package compilador.main;

import java.io.*;
import java_cup.runtime.Symbol;

import compilador.lexer.MiniLexer;
import compilador.parser.*;
import compilador.semantic.*;
import compilador.exec.*;

public class Main {

    public static void main(String[] args) throws Exception {
        boolean modeTokens = false;
        boolean modeAst    = false;
        boolean modeRun    = false;
        String filename    = null;

        for (String arg : args) {
            switch (arg) {
                case "--tokens" -> modeTokens = true;
                case "--ast"    -> modeAst    = true;
                case "--run"    -> modeRun    = true;
                default         -> filename   = arg;
            }
        }

        if (filename == null) {
            System.err.println("Uso: Main [--tokens] [--ast] [--run] arquivo.prog");
            return;
        }

        // se nenhum modo foi passado, assume --run
        if (!modeTokens && !modeAst && !modeRun) {
            modeRun = true;
        }

        if (modeTokens || modeRun) {
            System.out.println("ENTROU NO TOKES");
            runTokens(filename);
            // se só --tokens foi passado, paramos aqui
            if (!modeAst && !modeRun) return;
        }

        runCompilePipeline(filename, modeAst, modeRun);
    }

    // ---- Modo --tokens: só o lexer ----
    private static void runTokens(String filename) throws Exception {
        System.out.println("== TOKENS ==");
        try (Reader r = new FileReader(filename)) {
            MiniLexer lexer = new MiniLexer(r);
            while (true) {
                Symbol s = lexer.next_token();
                 if (s == null ||s.sym == 0) break;

                String lexeme = (s.value != null) ? s.value.toString() : "";
                System.out.printf(
                    "token=%s lexeme='%s' (%d,%d)%n",
                    getSymbolName(s.sym), lexeme, s.left, s.right
                );
            }
        } catch (Exception e) {
            String message = "Exception: " + e;
            System.out.println(message);
        }
        
    }

    private static String getSymbolName(int sym) {
        switch (sym) {
            case compilador.parser.sym.INICIO:  return "INICIO";
            case compilador.parser.sym.FIM:     return "FIM";
            case compilador.parser.sym.INTEIRO: return "INTEIRO";
            case compilador.parser.sym.REAL:    return "REAL";
            case compilador.parser.sym.IMPRIMA: return "IMPRIMA";
            case compilador.parser.sym.IDENT:   return "IDENT";
            case compilador.parser.sym.NUM_INT: return "NUM_INT";
            case compilador.parser.sym.NUM_REAL: return "NUM_REAL";
            case compilador.parser.sym.PLUS:    return "PLUS";
            case compilador.parser.sym.MINUS:   return "MINUS";
            case compilador.parser.sym.TIMES:   return "TIMES";
            case compilador.parser.sym.DIV:     return "DIV";
            case compilador.parser.sym.ATRIB:   return "ATRIB";
            case compilador.parser.sym.SEMI:    return "SEMI";
            case compilador.parser.sym.LPAREN:  return "LPAREN";
            case compilador.parser.sym.RPAREN:  return "RPAREN";
            case compilador.parser.sym.DOT:     return "DOT";
            case compilador.parser.sym.COMMA:   return "COMMA";
            case compilador.parser.sym.EOF:     return "EOF";
            default: return "UNKNOWN";
        }
    }

    // ---- Pipeline completo: parser + semântico + (ast/ir/run) ----
    private static void runCompilePipeline(String filename,
                                           boolean modeAst,
                                           boolean modeRun) throws Exception {
        ErrorReporter.reset();

        Program prog = null;

        try (Reader r = new FileReader(filename)) {
            MiniLexer lexer = new MiniLexer(r);
            Parser parser   = new Parser(lexer);

            Symbol result = parser.parse();
            if (!ErrorReporter.hasErrors() && result != null) {
                prog = (Program) result.value;
            }
        } catch (Exception e) {
            if (!ErrorReporter.hasErrors()) {
                ErrorReporter.syntaxError(-1, -1,
                    "Erro de sintaxe fatal: " + e.getMessage());
            }
        }

        if (ErrorReporter.hasErrors() || prog == null) {
            System.err.println("Compilação encerrada devido a erros léxicos/sintáticos.");
            return;
        }

        // Análise semântica
        SemanticAnalyzer sema = new SemanticAnalyzer();
        sema.analyze(prog);

        if (ErrorReporter.hasErrors()) {
            System.err.println("Compilação encerrada devido a erros semânticos.");
            return;
        }

        // AST textual e DOT
        if (modeAst || modeRun) {
            AstPrinter.print(prog);

            String dot = AstDotPrinter.toDot(prog);
            try (PrintWriter out = new PrintWriter("ast.dot")) {
                out.print(dot);
            }
            System.out.println("AST DOT salva em ast.dot");
        }

        // Código intermediário + execução só no --run
        if (modeRun) {
            CodeGenerator cg = new CodeGenerator();
            java.util.List<Instruction> code = cg.generate(prog);

            System.out.println("=== Código intermediário (3 endereços) ===");
            for (Instruction instr : code) {
                System.out.println(instr);
            }
            System.out.println("=== fim do código intermediário ===");

            Interpreter interp = new Interpreter();
            interp.run(prog);
        }
    }
}
