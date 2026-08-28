package model;

public class Livro extends ItemBiblioteca {
    private final int PRAZO = 14;
    private final double MULTA = 0.50;

    public Livro(String codigo, String titulo) {
        super(codigo, titulo);
    }

    @Override
    public int getPrazo() {
        return this.PRAZO;
    }

    @Override
    public double getMultaPorDia() {
        return this.MULTA;
    }

    @Override
    public String getCategoria() {
        return "Livro";
    }
}
