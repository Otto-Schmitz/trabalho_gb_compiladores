

public class AssignStmt implements Stmt {
    public final String name;
    public final Expr expr;

    public AssignStmt(String name, Expr expr) {
        this.name = name;
        this.expr = expr;
    }
}