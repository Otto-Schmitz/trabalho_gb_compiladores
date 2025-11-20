package compilador.parser;
// PrintStmt.java
public class PrintStmt extends Node implements Stmt {
    public final Expr expr;

    public PrintStmt(Expr expr, int line, int column) {
        super(line, column);
        this.expr = expr;
    }
}
