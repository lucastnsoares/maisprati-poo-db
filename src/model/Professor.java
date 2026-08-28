package model;

public class Professor extends Usuario{
    private final int LIMITEITENSEMPRESTADOS = 5;
    public Professor(String nome) {
        super(nome);
    }

    @Override
    public int getLimiteItens() {
        return LIMITEITENSEMPRESTADOS;
    }

    @Override
    public String getTipo() {
        return "Professor";
    }
}
