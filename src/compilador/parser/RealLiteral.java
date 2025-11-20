package compilador.parser;
// RealLiteral.java
public class RealLiteral extends Node implements Expr {
    public final double value;

    public RealLiteral(double value, int line, int column) {
        super(line, column);
        this.value = value;
    }
}
