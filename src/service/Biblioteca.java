package service;

import model.ItemBiblioteca;
import model.Usuario;

public class Biblioteca {
    private final ItemBiblioteca[] acervo;
    private int totalItensAcervo;
    private final ItemBiblioteca[] itensEmprestados;
    private int totalItensEmprestados;
    private final Usuario[] usuariosComEmprestimo;
    private int totalIUsuariosComEmprestimo;

    public Biblioteca(int capacidadeBiblioteca) {
        this.acervo = new ItemBiblioteca[capacidadeBiblioteca];
        this.totalItensAcervo = 0;
        this.itensEmprestados = new ItemBiblioteca[capacidadeBiblioteca];
        this.totalItensEmprestados = 0;
        this.usuariosComEmprestimo = new Usuario[capacidadeBiblioteca];
        this.totalIUsuariosComEmprestimo = 0;
    }

    public boolean adicionarItemAcervo(ItemBiblioteca item) {
        if (totalItensAcervo < acervo.length) {
            acervo[totalItensAcervo] = item;
            totalItensAcervo++;
            return true;
        }
        return false;
    }

    public void emprestar(String codigoItem, Usuario usuario) {
        if (codigoItem == null || usuario == null) throw new IllegalArgumentException("O código do item e/ou o usuário deve ser informado.");

        int indiceAcervo = indiceItemAcervo(codigoItem);

        if(indiceAcervo == -1) {
            System.out.println("Item inexistente no acervo");
            return;
        }

        ItemBiblioteca itemEncontrado = this.acervo[indiceAcervo];

        if(!itemEncontrado.getDisponivel()){
            System.out.println("Item não disponível para empréstimo.");
            return;
        }

        if(!usuario.podeEmprestar()){
            System.out.println("Limite de empréstimo para o usuário atingido.");
            return;
        }

        itemEncontrado.emprestar();
        usuario.emprestarItem();

        this.itensEmprestados[this.totalItensEmprestados] = itemEncontrado;
        this.usuariosComEmprestimo[this.totalIUsuariosComEmprestimo] = usuario;
        this.totalItensEmprestados++;
        this.totalIUsuariosComEmprestimo++;

        System.out.printf("""

                ------------------------ DADOS DO EMPRÉSTIMO -----------------------
                CATEGORIA: %s
                TÍTULO: %s
                
                USUÁRIO: %s 
                TIPO: %s
                ----------------------------------------------------------------------
                """, itemEncontrado.getCategoria(), itemEncontrado.getTitulo(), usuario.getNome(), usuario.getTipo());
    }

    public void devolver(String codigoItem, int diasEmprestado) {
        if (codigoItem == null) throw new IllegalArgumentException("O código do item a ser devolvido deve ser informado.");
      
        int indiceItensEmprestados = indiceItemEmprestado(codigoItem);

        if(indiceItensEmprestados == -1) {
            System.out.println("Item não está emprestado");
            return;
        }

        ItemBiblioteca itemEncontrado = this.itensEmprestados[indiceItensEmprestados];
        Usuario usuario = usuariosComEmprestimo[indiceItensEmprestados];

        if(itemEncontrado.devolver()){
            usuario.devolverItem();

            for (int i = indiceItensEmprestados; i < this.totalItensEmprestados - 1; i++){
                this.itensEmprestados[i] = this.itensEmprestados[i + 1];
                this.usuariosComEmprestimo[i] = this.usuariosComEmprestimo[i + 1];
            }

            this.itensEmprestados[this.totalItensEmprestados - 1] = null;
            this.usuariosComEmprestimo[this.totalItensEmprestados - 1] = null;
            
            this.totalItensEmprestados--;
            this.totalIUsuariosComEmprestimo--;
        }

        int diasAtraso = diasEmprestado - itemEncontrado.getPrazo();
        double multa = 0.0;

        if(diasAtraso > 0){
            multa = diasAtraso * itemEncontrado.getMultaPorDia();
        } else {
            diasAtraso = 0;
        }


        System.out.printf("""
                    ------------------------- DADOS DA DEVOLUÇÃO -------------------------
                    CATEGORIA: %s
                    TÍTULO: %s
                    
                    USUÁRIO: %s
                    TIPO DE USUÁRIO: %s
                    DIAS EM ATRASO: %d
                    MULTA A SER PAGA: R$ %.2f
                    ----------------------------------------------------------------------
                    """, itemEncontrado.getCategoria(), itemEncontrado.getTitulo(), usuario.getNome(), usuario.getTipo(), diasAtraso, multa);
    }


    public int indiceItemAcervo(String codigoItem) {
        for(int i = 0; i < totalItensAcervo; i++){
            if(this.acervo[i].getCodigo().equals(codigoItem)){
                return i;
            }
        }
        return -1;
    }

    public int indiceItemEmprestado(String codigoItem) {
        for(int i = 0; i < totalItensEmprestados; i++){
            if(this.itensEmprestados[i].getCodigo().equals(codigoItem)){
                return i;
            }
        }
        return -1;
    }

    public void listarAcervo() {

        System.out.println("----------------------------- ACERVO DA BIBLIOTECA -----------------------------------");
        for(int i = 0; i < this.totalItensAcervo; i++){
            System.out.println(this.acervo[i]);
        }
        System.out.println("---------------------------------------------------------------------------------------");
    }
}
