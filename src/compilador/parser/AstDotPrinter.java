package compilador.parser;
public class AstDotPrinter {

    private final StringBuilder sb = new StringBuilder();
    private int nextId = 0;

    public static String toDot(Program p) {
        AstDotPrinter printer = new AstDotPrinter();
        printer.sb.append("digraph AST {\n");
        printer.sb.append("  node [shape=box, fontname=\"Consolas\"];\n");

        int rootId = printer.node("Program");

        // Decls
        int declsId = printer.node("Decls");
        printer.edge(rootId, declsId);
        for (Decl d : p.decls) {
            int dId = printer.visitDecl(d);
            printer.edge(declsId, dId);
        }

        // Stmts
        int stmtsId = printer.node("Stmts");
        printer.edge(rootId, stmtsId);
        for (Stmt s : p.stmts) {
            int sId = printer.visitStmt(s);
            printer.edge(stmtsId, sId);
        }

        printer.sb.append("}\n");
        return printer.sb.toString();
    }

    // ----- Node & edge helpers -----

    private int node(String label) {
        int id = nextId++;
        sb.append("  n").append(id)
          .append(" [label=\"")
          .append(escape(label))
          .append("\"];\n");
        return id;
    }

    private void edge(int from, int to) {
        sb.append("  n").append(from)
          .append(" -> n").append(to)
          .append(";\n");
    }

    private String escape(String s) {
        return s
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }

    // ----- Visit methods -----

    private int visitDecl(Decl d) {
        String lbl = "Decl " + d.type + " " + d.name;
        return node(lbl);
    }

    private int visitStmt(Stmt s) {
        if (s instanceof AssignStmt a) {
            int id = node("Assign " + a.name);
            int exprId = visitExpr(a.expr);
            edge(id, exprId);
            return id;
        } else if (s instanceof PrintStmt p) {
            int id = node("Print");
            int exprId = visitExpr(p.expr);
            edge(id, exprId);
            return id;
        } else {
            return node("UnknownStmt " + s.getClass().getSimpleName());
        }
    }

    private int visitExpr(Expr e) {
        if (e instanceof IntLiteral il) {
            return node("Int " + il.value);
        }
        if (e instanceof RealLiteral rl) {
            return node("Real " + rl.value);
        }
        if (e instanceof VarExpr ve) {
            return node("Var " + ve.name);
        }
        if (e instanceof BinExpr be) {
            int id = node("Op " + be.op);
            int leftId = visitExpr(be.left);
            int rightId = visitExpr(be.right);
            edge(id, leftId);
            edge(id, rightId);
            return id;
        }
        return node("UnknownExpr " + e.getClass().getSimpleName());
    }
}
