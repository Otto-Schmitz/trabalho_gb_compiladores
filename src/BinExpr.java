

public class BinExpr implements Expr {
    public final String op; // "+", "-", "*", "/"
    public final Expr left;
    public final Expr right;

    public BinExpr(String op, Expr left, Expr right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }
}
