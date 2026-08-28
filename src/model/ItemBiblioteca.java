package model;

public abstract class ItemBiblioteca {
    private final String codigo;
    private final String titulo;
    private Boolean isDisponivel;

    protected ItemBiblioteca(String codigo, String titulo) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.isDisponivel = true;
    }

    public abstract int getPrazo();

    public abstract double getMultaPorDia();

    public abstract String getCategoria();

    public String getCodigo() {
        return codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public Boolean getDisponivel() {
        return isDisponivel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ItemBiblioteca outro = (ItemBiblioteca) o;
        return codigo.equals(outro.codigo);
    }

    public boolean emprestar() {
        if (isDisponivel) {
            this.isDisponivel = false;
            return true;
        }
        return false;
    }

    public boolean devolver() {
        if (isDisponivel) {
            return false;
        }
        this.isDisponivel = true;
        return true;
    }

    @Override
    public String toString() {
        return String.format("""
                CÓDIGO: %s
                TIPO: %s
                TÍTULO: %s
                STATUS: %s
                """, this.codigo, this.getCategoria(), this.titulo, this.isDisponivel ? "Disponível" : "Emprestado");
    }

}
