import java.util.List;

public class AstPrinter {

    public static void print(Program p) {
        System.out.println("=== AST ===");
        System.out.println("Program");

        System.out.println("  Declarations:");
        for (Decl d : p.decls) {
            System.out.printf("    [%s] %s%n", d.type, d.name);
        }

        System.out.println("  Statements:");
        for (Stmt s : p.stmts) {
            printStmt(s, "    ");
        }
        System.out.println("=== end AST ===");
    }

    private static void printStmt(Stmt s, String indent) {
        if (s instanceof AssignStmt a) {
            System.out.printf("%sAssign %s :=%n", indent, a.name);
            printExpr(a.expr, indent + "  ");

        } else if (s instanceof PrintStmt p) {
            System.out.printf("%sPrint%n", indent);
            printExpr(p.expr, indent + "  ");

        } else {
            System.out.printf("%s[Unknown Stmt: %s]%n", indent, s.getClass().getSimpleName());
        }
    }

    private static void printExpr(Expr e, String indent) {
        if (e instanceof IntLiteral il) {
            System.out.printf("%sIntLiteral %d%n", indent, il.value);

        } else if (e instanceof RealLiteral rl) {
            System.out.printf("%sRealLiteral %f%n", indent, rl.value);

        } else if (e instanceof VarExpr ve) {
            System.out.printf("%sVarExpr %s%n", indent, ve.name);

        } else if (e instanceof BinExpr be) {
            System.out.printf("%sBinExpr '%s'%n", indent, be.op);
            printExpr(be.left, indent + "  ");
            printExpr(be.right, indent + "  ");

        } else {
            System.out.printf("%s[Unknown Expr: %s]%n", indent, e.getClass().getSimpleName());
        }
    }
}

