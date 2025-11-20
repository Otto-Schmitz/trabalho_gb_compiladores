package compilador.exec;
import java.util.ArrayList;
import java.util.List;
import compilador.parser.*;

public class CodeGenerator {

    private final List<Instruction> code = new ArrayList<>();
    private int tempCount = 0;

    public List<Instruction> generate(Program prog) {
        // Decls: se quiser, pode gerar "decl inteiro x", etc.
        // Aqui vamos só focar em código executável a partir dos comandos.
        for (Stmt s : prog.stmts) {
            genStmt(s);
        }
        return code;
    }

    private String newTemp() {
        return "t" + (tempCount++);
    }

    // ---- Statements ----

    private void genStmt(Stmt s) {
        if (s instanceof AssignStmt asg) {
            String rhs = genExpr(asg.expr);
            // x := expr  ->  x = rhs
            code.add(new Instruction(asg.name, rhs, "ASSIGN", null));

        } else if (s instanceof PrintStmt ps) {
            String val = genExpr(ps.expr);
            code.add(new Instruction(null, val, "PRINT", null));

        } else {
            // não deveria acontecer
            throw new RuntimeException("Stmt desconhecido em CodeGenerator: " + s.getClass());
        }
    }

    // ---- Expressions ----

    /**
     * Gera código para a expressão e retorna o "local" (variável ou temporário)
     * onde o resultado dessa expressão está.
     */
    private String genExpr(Expr e) {
        if (e instanceof IntLiteral il) {
            // Usa literal direto como operando
            return Integer.toString(il.value);
        }

        if (e instanceof RealLiteral rl) {
            // Usa literal direto como operando
            return Double.toString(rl.value);
        }

        if (e instanceof VarExpr ve) {
            // Referência a variável já existente
            return ve.name;
        }

        if (e instanceof BinExpr be) {
            String left = genExpr(be.left);
            String right = genExpr(be.right);

            String temp = newTemp();

            // be.op já é "+", "-", "*", "/"
            code.add(new Instruction(temp, left, be.op, right));
            return temp;
        }

        throw new RuntimeException("Expr desconhecida em CodeGenerator: " + e.getClass());
    }
}
