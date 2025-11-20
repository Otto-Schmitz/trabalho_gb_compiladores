package compilador.parser;
// VarExpr.java
public class VarExpr extends Node implements Expr {
    public final String name;

    public VarExpr(String name, int line, int column) {
        super(line, column);
        this.name = name;
    }
}
