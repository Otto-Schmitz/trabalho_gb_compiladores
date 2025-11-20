import java.util.HashMap;
import java.util.Map;

public class Interpreter {

    // valor com tipo
    public static class Value {
        public final TypeNode type;
        public final double value; // usamos double para tudo, int é valor inteiro

        public Value(TypeNode type, double value) {
            this.type = type;
            this.value = value;
        }
    }

    // ambiente: tipos e valores das variáveis
    private final Map<String, TypeNode> types = new HashMap<>();
    private final Map<String, Value> env   = new HashMap<>();

    public void run(Program p) {
        // declarações: cria variáveis com valor 0
        for (Decl d : p.decls) {
            types.put(d.name, d.type);
            env.put(d.name, new Value(d.type, 0.0));
        }

        // executa comandos
        for (Stmt s : p.stmts) {
            execStmt(s);
        }
    }

    private void execStmt(Stmt s) {
        if (s instanceof AssignStmt asg) {
            Value v = evalExpr(asg.expr);
            TypeNode varType = types.get(asg.name);
            if (varType == null) {
                throw new RuntimeException("Variável não declarada: " + asg.name);
            }

            // conversão simples: se inteiro, trunca
            double val = v.value;
            if (varType == TypeNode.INT) {
                val = (int) val;
            }
            env.put(asg.name, new Value(varType, val));

        } else if (s instanceof PrintStmt ps) {
            Value v = evalExpr(ps.expr);
            if (v.type == TypeNode.INT) {
                System.out.println((int) v.value);
            } else {
                System.out.println(v.value);
            }
        } else {
            throw new RuntimeException("Stmt desconhecido: " + s.getClass());
        }
    }

    private Value evalExpr(Expr e) {
        if (e instanceof IntLiteral il) {
            return new Value(TypeNode.INT, il.value);
        }
        if (e instanceof RealLiteral rl) {
            return new Value(TypeNode.REAL, rl.value);
        }
        if (e instanceof VarExpr ve) {
            Value v = env.get(ve.name);
            if (v == null) {
                throw new RuntimeException("Variável não declarada: " + ve.name);
            }
            return v;
        }
        if (e instanceof BinExpr be) {
            Value v1 = evalExpr(be.left);
            Value v2 = evalExpr(be.right);

            // promoção de tipo: se algum é real, resultado é real
            TypeNode resType = (v1.type == TypeNode.REAL || v2.type == TypeNode.REAL)
                               ? TypeNode.REAL : TypeNode.INT;

            double a = v1.value;
            double b = v2.value;
            double r;

            switch (be.op) {
                case "+" -> r = a + b;
                case "-" -> r = a - b;
                case "*" -> r = a * b;
                case "/" -> r = a / b;
                default  -> throw new RuntimeException("Operador desconhecido: " + be.op);
            }

            if (resType == TypeNode.INT) {
                r = (int) r;
            }

            return new Value(resType, r);
        }

        throw new RuntimeException("Expr desconhecida: " + e.getClass());
    }
}
