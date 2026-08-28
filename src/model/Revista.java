package model;

public class Revista extends ItemBiblioteca {
    private final int PRAZO = 7;
    private final double MULTA = 1.00;

    public Revista(String codigo, String titulo) {
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
        return "Revista";
    }
}
