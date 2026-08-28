package model;

public class DVD extends ItemBiblioteca {
    private final int PRAZO = 3;
    private final double MULTA = 2.00;

    public DVD(String codigo, String titulo) {
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
        return "DVD";
    }
}
