package compilador.parser;
// IntLiteral.java
public class IntLiteral extends Node implements Expr {
    public final int value;

    public IntLiteral(int value, int line, int column) {
        super(line, column);
        this.value = value;
    }
}
