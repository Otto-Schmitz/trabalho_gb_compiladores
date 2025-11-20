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

        if (modeTokens) {
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
                if (s == null) break;

                String lexeme = (s.value != null) ? s.value.toString() : "";
                System.out.printf(
                    "token=%d lexeme='%s' (%d,%d)%n",
                    s.sym, lexeme, s.left, s.right
                );
            }
        }
        System.out.println("== FIM TOKENS ==");
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
