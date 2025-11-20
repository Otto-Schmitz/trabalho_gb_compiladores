import java.io.*;
import java_cup.runtime.Symbol;

public class Main {
    public static void main(String[] args) throws Exception {
        MiniLexer lexer;

        if (args.length > 0) {
            lexer = new MiniLexer(new FileReader(args[0]));
        } else {
            lexer = new MiniLexer(new InputStreamReader(System.in));
        }

        Parser parser = new Parser(lexer);  // Parser gerado pelo CUP
        
        ErrorReporter.reset();

        Program prog = null;
        try {
            Symbol result = parser.parse();

            // se o parser não retornou AST (por erro), result pode ser null
            if (!ErrorReporter.hasErrors()) {
                prog = (Program) result.value;
            }
        } catch (Exception e) {
            // CUP normalmente lança exceção em erro não recuperado
            // não imprimimos stack trace; só marcamos que deu ruim
            if (!ErrorReporter.hasErrors()) {
                // se por algum motivo ainda não marcamos, marca aqui
                ErrorReporter.syntaxError(-1, -1, "Erro de sintaxe fatal: " + e.getMessage());
            }
        }

        // Se houve qualquer erro léxico ou sintático, não executa
        if (ErrorReporter.hasErrors() || prog == null) {
            System.err.println("Compilação encerrada devido a erros.");
            return;
        }

         // --- NOVO: análise semântica ---
        SemanticAnalyzer sema = new SemanticAnalyzer();
        sema.analyze(prog);

        if (ErrorReporter.hasErrors()) {
            System.err.println("Compilação encerrada devido a erros semânticos.");
            return;
        }

        AstPrinter.print(prog);

        String dot = AstDotPrinter.toDot(prog);
        try (PrintWriter out = new PrintWriter("ast.dot")) {
            out.print(dot);
        }
        System.out.println("AST DOT saved to ast.dot");

        // --- NOVO: gerar código intermediário ---
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
