package compilador.parser;
public abstract class Node {
    public final int line;
    public final int column;

    public Node(int line, int column) {
        this.line = line;
        this.column = column;
    }
}
