package compilador.parser;
// AssignStmt.java
public class AssignStmt extends Node implements Stmt {
    public final String name;
    public final Expr expr;

    public AssignStmt(String name, Expr expr, int line, int column) {
        super(line, column);
        this.name = name;
        this.expr = expr;
    }
}
