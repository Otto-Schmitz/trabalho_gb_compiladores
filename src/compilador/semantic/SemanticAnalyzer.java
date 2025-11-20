package compilador.semantic;
import java.util.HashMap;
import java.util.Map;

import compilador.parser.*;

public class SemanticAnalyzer {

    // tabela de símbolos: nome -> tipo
    private final Map<String, TypeNode> symbols = new HashMap<>();

    public void analyze(Program prog) {
        symbols.clear();

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
                d.line,
                d.column,
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
            int line = -1, col = -1;
            if(s instanceof Node n){
                line = n.line;
                col = n.column;
            }
            // só pra proteção, não deve cair aqui
            ErrorReporter.semanticError(
                line,
                col,
                "Comando desconhecido: " + s.getClass().getSimpleName()
            );
        }
    }

    private void analyzeAssign(AssignStmt asg) {
        TypeNode varType = symbols.get(asg.name);
        if (varType == null) {
            ErrorReporter.semanticError(
                asg.line,
                asg.column,
                "Variável '" + asg.name + "' não foi declarada."
            );
            return;
        }

        TypeNode exprType = analyzeExpr(asg.expr);
        if (exprType == null) {
            return;
        }

        if (varType != exprType) {
            ErrorReporter.semanticError(
                asg.line,
                asg.column,
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
                    ve.line,
                    ve.column,
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

            if (!isNumeric(leftType) || !isNumeric(rightType)) {
                ErrorReporter.semanticError(
                    be.line,
                    be.column,
                    "Operação '" + be.op + "' não suportada para tipos " +
                    leftType + " e " + rightType + "."
                );
                return null;
            }

            if (leftType == TypeNode.REAL || rightType == TypeNode.REAL) {
                return TypeNode.REAL;
            } else {
                return TypeNode.INT;
            }
        }

        int line = -1, col = -1;
        if (e instanceof Node n) {
            line = n.line;
            col = n.column;
        }
        ErrorReporter.semanticError(
            line,
            col,
            "Expressão de tipo desconhecido: " + e.getClass().getSimpleName()
        );
        return null;
    }

    private boolean isNumeric(TypeNode t) {
        return t == TypeNode.INT || t == TypeNode.REAL;
    }
}
