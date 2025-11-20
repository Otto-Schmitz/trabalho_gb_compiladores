package compilador.parser;

// Program.java
import java.util.List;

public class Program {
    public final List<Decl> decls;
    public final List<Stmt> stmts;

    public Program(List<Decl> decls, List<Stmt> stmts) {
        this.decls = decls;
        this.stmts = stmts;
    }
}

