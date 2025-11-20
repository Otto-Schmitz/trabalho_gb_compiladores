import java.util.HashMap;
import java.util.Map;

public class SemanticAnalyzer {

    // tabela de símbolos: nome -> tipo
    private final Map<String, TypeNode> symbols = new HashMap<>();

    public void analyze(Program prog) {
        // 1) processar declarações
        for (Decl d : prog.decls) {
            declare(d);
        }

        // 2) processar comandos
        for (Stmt s : prog.stmts) {
            analyzeStmt(s);
        }
    }

    private void declare(Decl d) {
        if (symbols.containsKey(d.name)) {
            ErrorReporter.semanticError(
                "Variável '" + d.name + "' já foi declarada."
            );
        } else {
            symbols.put(d.name, d.type);
        }
    }

    private void analyzeStmt(Stmt s) {
        if (s instanceof AssignStmt asg) {
            analyzeAssign(asg);
        } else if (s instanceof PrintStmt ps) {
            analyzePrint(ps);
        } else {
            // só pra proteção, não deve cair aqui
            ErrorReporter.semanticError(
                "Comando desconhecido: " + s.getClass().getSimpleName()
            );
        }
    }

    private void analyzeAssign(AssignStmt asg) {
        TypeNode varType = symbols.get(asg.name);
        if (varType == null) {
            ErrorReporter.semanticError(
                "Variável '" + asg.name + "' não foi declarada."
            );
            return;
        }

        TypeNode exprType = analyzeExpr(asg.expr);
        if (exprType == null) {
            // erro já reportado em algum lugar interno
            return;
        }

        // regra simples: tipos devem bater exatamente
        if (varType != exprType) {
            ErrorReporter.semanticError(
                "Atribuição incompatível: variável '" + asg.name +
                "' é do tipo " + varType +
                " mas a expressão é do tipo " + exprType + "."
            );
        }
    }

    private void analyzePrint(PrintStmt ps) {
        TypeNode t = analyzeExpr(ps.expr);
        if (t == null) return;

        // como só temos int e real, qualquer um é aceitável.
        // se depois tiver tipos não-numéricos, pode restringir aqui.
    }

    // retorna o tipo da expressão ou null se já teve erro dentro
    private TypeNode analyzeExpr(Expr e) {
        if (e instanceof IntLiteral il) {
            return TypeNode.INT;
        }

        if (e instanceof RealLiteral rl) {
            return TypeNode.REAL;
        }

        if (e instanceof VarExpr ve) {
            TypeNode t = symbols.get(ve.name);
            if (t == null) {
                ErrorReporter.semanticError(
                    "Uso de variável não declarada: '" + ve.name + "'."
                );
                return null;
            }
            return t;
        }

        if (e instanceof BinExpr be) {
            TypeNode leftType  = analyzeExpr(be.left);
            TypeNode rightType = analyzeExpr(be.right);

            if (leftType == null || rightType == null) {
                return null;
            }

            // só permitimos operações entre int e real
            if (!isNumeric(leftType) || !isNumeric(rightType)) {
                ErrorReporter.semanticError(
                    "Operação '" + be.op + "' não suportada para tipos " +
                    leftType + " e " + rightType + "."
                );
                return null;
            }

            // regra de promoção: se algum é REAL, resultado REAL, senão INT
            if (leftType == TypeNode.REAL || rightType == TypeNode.REAL) {
                return TypeNode.REAL;
            } else {
                return TypeNode.INT;
            }
        }

        ErrorReporter.semanticError(
            "Expressão de tipo desconhecido: " + e.getClass().getSimpleName()
        );
        return null;
    }

    private boolean isNumeric(TypeNode t) {
        return t == TypeNode.INT || t == TypeNode.REAL;
    }
}
