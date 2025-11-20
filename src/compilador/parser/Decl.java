package compilador.parser;
// Decl.java
public class Decl extends Node {
    public final TypeNode type;
    public final String name;

    public Decl(TypeNode type, String name, int line, int column) {
        super(line, column);
        this.type = type;
        this.name = name;
    }
}
