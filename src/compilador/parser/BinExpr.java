package compilador.parser;
// BinExpr.java
public class BinExpr extends Node implements Expr {
    public final String op;  // "+", "-", "*", "/"
    public final Expr left;
    public final Expr right;

    public BinExpr(String op, Expr left, Expr right, int line, int column) {
        super(line, column);
        this.op = op;
        this.left = left;
        this.right = right;
    }
}
