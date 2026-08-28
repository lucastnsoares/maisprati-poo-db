package model;

public class Aluno extends Usuario{
    private final int LIMITEITENSEMPRESTADOS = 3;

    public Aluno(String nome) {
        super(nome);
    }

    @Override
    public int getLimiteItens() {
        return LIMITEITENSEMPRESTADOS;
    }

    @Override
    public String getTipo() {
        return "Aluno";
    }
}
