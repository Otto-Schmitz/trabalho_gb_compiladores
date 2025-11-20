package compilador.exec;
public class Instruction {

    // result = arg1 op arg2
    public final String result;
    public final String arg1;
    public final String op;   // "+", "-", "*", "/", "ASSIGN", "PRINT"
    public final String arg2; // pode ser null em ASSIGN/PRINT

    public Instruction(String result, String arg1, String op, String arg2) {
        this.result = result;
        this.arg1 = arg1;
        this.op = op;
        this.arg2 = arg2;
    }

    @Override
    public String toString() {
        // print result = arg1 op arg2
        if ("PRINT".equals(op)) {
            return "print " + arg1;
        } else if ("ASSIGN".equals(op)) {
            return result + " = " + arg1;
        } else if (arg2 == null) {
            // para o caso de op unário, se um dia quiser
            return result + " = " + op + " " + arg1;
        } else {
            return result + " = " + arg1 + " " + op + " " + arg2;
        }
    }
}
