package compilador.parser;
public class Token {
    String tipo;
    String valor;
    int linha;
    int coluna;

    public Token(String tipo, String valor, int linha, int coluna) {
        this.tipo = tipo;
        this.valor = valor;
        this.linha = linha;
        this.coluna = coluna;
    }

    public String toString() {
        return tipo + " (" + valor + ") [" + linha + "," + coluna + "]";
    }
}