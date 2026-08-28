import model.*;
import service.Biblioteca;

public class Main {
    private static Usuario aluno;
    private static Usuario professor;

    public static void main(String[] args) {
        Biblioteca biblioteca = new Biblioteca(10);
        cadastrarAcervoEUsuarios(biblioteca);

        executarCenario1_EmprestimosBemSucedidos(biblioteca);
        executarCenario2_EmprestimosRecusadosPorLimite(biblioteca);
        executarCenario3_DevolucoesEListagemFinal(biblioteca);
    }

    private static void cadastrarAcervoEUsuarios(Biblioteca biblioteca) {
        imprimeCabecalho("CADASTRO DE ITENS NO ACERVO E USUÁRIOS");

        biblioteca.adicionarItemAcervo(new Livro("L001", "Clean Code"));
        biblioteca.adicionarItemAcervo(new Livro("L002", "Estruturas de Dados em Java"));
        biblioteca.adicionarItemAcervo(new Livro("L003", "Engenharia de Software Moderna"));
        biblioteca.adicionarItemAcervo(new Livro("L004", "Padrões de Projetos"));
        biblioteca.adicionarItemAcervo(new Livro("L005", "Java Efetivo"));
        
        biblioteca.adicionarItemAcervo(new Revista("R001", "Mundo Java Ed. 42"));
        biblioteca.adicionarItemAcervo(new Revista("R002", "Developer Magazine Ed. 1"));
        biblioteca.adicionarItemAcervo(new Revista("R003", "Developer Magazine Ed. 2"));
        
        biblioteca.adicionarItemAcervo(new DVD("D001", "Matrix"));

        aluno = new Aluno("Lucas");        
        professor = new Professor("Carlos"); 

        biblioteca.listarAcervo();
    }

    private static void executarCenario1_EmprestimosBemSucedidos(Biblioteca biblioteca) {
        imprimeCabecalho("-> CENÁRIO 1: EMPRÉSTIMOS BEM-SUCEDIDOS");
        
        System.out.println("-> Aluno Lucas realizando empréstimo de Livro (L001):");
        biblioteca.emprestar("L001", aluno);      

        System.out.println("\n-> Professor Carlos realizando empréstimo de DVD (D001):");
        biblioteca.emprestar("D001", professor);  
    }

    private static void executarCenario2_EmprestimosRecusadosPorLimite(Biblioteca biblioteca) {
        imprimeCabecalho("CENÁRIO 2: EMPRÉSTIMOS RECUSADOS POR LIMITE ATINGIDO");

        // Aluno atinge o limite (3 itens)
        System.out.println("-> Aluno pegando mais 2 itens para atingir o limite de 3:");
        biblioteca.emprestar("L002", aluno); 
        biblioteca.emprestar("L003", aluno); 

        // Tentativa de 4º empréstimo para o Aluno (deve ser recusado)
        System.out.println("\n-> Tentativa de 4º empréstimo para o Aluno (Limite: 3) - REJEIÇÃO ESPERADA:");
        biblioteca.emprestar("R001", aluno); 

        // Professor atinge o limite (5 itens)
        System.out.println("\n-> Professor pegando mais 4 itens para atingir o limite de 5:");
        biblioteca.emprestar("L004", professor); 
        biblioteca.emprestar("L005", professor); 
        biblioteca.emprestar("R002", professor); 
        biblioteca.emprestar("R003", professor); 

        // Tentativa de 6º empréstimo para o Professor (deve ser recusado)
        System.out.println("\n-> Tentativa de 6º empréstimo para o Professor (Limite: 5) - REJEIÇÃO ESPERADA:");
        biblioteca.emprestar("R001", professor); 
    }

    private static void executarCenario3_DevolucoesEListagemFinal(Biblioteca biblioteca) {
        imprimeCabecalho("CENÁRIO 3: DEVOLUÇÃO E LISTAGEM FINAL DO ACERVO");

        // Devolução com cálculo de multas de acordo com a categoria
        System.out.println("-> Aluno devolvendo L001 (Livro - prazo 14 dias) após 18 dias (4 dias atraso):");
        biblioteca.devolver("L001", 18); 

        System.out.println("\n-> Professor devolvendo D001 (DVD - prazo 3 dias) após 5 dias (2 dias atraso):");
        biblioteca.devolver("D001", 5);  

        System.out.println("\n=== ESTADO FINAL DO ACERVO ===");
        biblioteca.listarAcervo();
    }

    private static void imprimeCabecalho(String titulo) {
        System.out.println("\n=================================================================================");
        System.out.printf("  %s\n", titulo);
        System.out.println("=================================================================================");
    }
}