# Sistema de Biblioteca — POO (Java) & Banco de Dados (PostgreSQL)

Este repositório contém a resolução da lista de exercícios de **Programação Orientada a Objetos em Java** e **Modelagem/Consultas SQL em PostgreSQL**, simulando o controle de acervo e empréstimos de uma biblioteca.

---

## 📁 Estrutura do Projeto

```text
maisprati-poo-db/
├── src/
│   ├── model/
│   │   ├── ItemBiblioteca.java   # Classe abstrata base para itens do acervo
│   │   ├── Livro.java            # Especialização de ItemBiblioteca (prazo: 14d, multa: R$ 0,50/dia)
│   │   ├── Revista.java          # Especialização de ItemBiblioteca (prazo: 7d, multa: R$ 1,00/dia)
│   │   ├── DVD.java              # Categoria adicional para validar extensibilidade/OCP
│   │   ├── Usuario.java          # Classe abstrata base para usuários
│   │   ├── Aluno.java            # Especialização de Usuario (limite: 3 itens)
│   │   └── Professor.java        # Especialização de Usuario (limite: 5 itens)
│   ├── service/
│   │   └── Biblioteca.java       # Gerenciamento de acervo e empréstimos com arrays de tamanho fixo
│   └── Main.java                 # Cenários de teste e demonstração do sistema
├── scripts/
│   ├── exercicio_2.sql           # DDL (CREATE TABLE) e DML (INSERT) das tabelas no PostgreSQL
│   ├── exercicio_3_1.sql         # Consulta 1: Listar todo o acervo
│   ├── exercicio_3_2.sql         # Consulta 2: Listar empréstimos em aberto
│   ├── exercicio_3_3.sql         # Consulta 3: Total de multas por usuário
│   └── exercicio_3_4.sql         # Consulta 4: Itens nunca emprestados
├── lista_tarefas_java_db.pdf     # Documento de especificação dos exercícios
└── README.md                     # Documentação do projeto
```

---

## ☕ Exercício 1 — Sistema de Biblioteca em Java (POO)

Implementação do controle de acervo e empréstimos aplicando os pilares da Orientação a Objetos (Herança, Polimorfismo e Encapsulamento) e o princípio Aberto/Fechado (OCP - Open/Closed Principle).

### 📋 Regras de Negócio

| Entidade | Especialização | Prazo de Devolução | Valor da Multa (por dia de atraso) | Limite de Empréstimos |
| :--- | :--- | :---: | :---: | :---: |
| **Item** | Livro | 14 dias | R$ 0,50 | — |
| **Item** | Revista | 7 dias | R$ 1,00 | — |
| **Item** | DVD *(extra)* | 3 dias | R$ 2,00 | — |
| **Usuário** | Aluno | — | — | Até 3 itens |
| **Usuário** | Professor | — | — | Até 5 itens |

### 💡 Destaques da Implementação
- **Polimorfismo:** Métodos como `getPrazo()`, `getMultaPorDia()` e `getLimiteItens()` são abstratos nas classes base e especializados nas classes filhas, dispensando condicionais (`if/switch`) baseadas em tipos.
- **Encapsulamento e OCP:** O método `listarAcervo()` itera sobre o array polimórfico `ItemBiblioteca[]` em um único laço. Uma nova categoria (como `DVD`) pôde ser criada e adicionada ao sistema sem alterar nenhuma linha do serviço `Biblioteca`.
- **Controle de Estado:** O atributo `isDisponivel` é modificado apenas internamente através dos métodos `emprestar()` e `devolver()`, sem expor setters públicos.

### ▶️ Como Executar o Código Java

Pelo terminal (a partir da raiz do projeto):

1. **Compilar os arquivos:**
   ```bash
   javac -d out/production/maisprati-poo-db src/model/*.java src/service/*.java src/Main.java
   ```

2. **Executar a aplicação:**
   ```bash
   java -cp out/production/maisprati-poo-db Main
   ```

---

## 🗄️ Exercício 2 — Modelagem do Banco de Dados (PostgreSQL)

Mapeamento relacional das entidades sem herança direta, utilizando colunas de discriminação (`tipo`) e restrições de integridade.

### 📐 Estrutura das Tabelas

1. **`item`**:
   - `id`: Chave primária gerada com `GENERATED ALWAYS AS IDENTITY`.
   - `codigo`, `titulo`, `tipo`, `autor`, `edicao`, `disponivel`.
   - Restrição `CHECK (tipo IN ('Livro', 'Revista'))`.

2. **`usuario`**:
   - `id`: Chave primária gerada com `GENERATED ALWAYS AS IDENTITY`.
   - `nome`, `tipo`, `limite_itens`.
   - Restrição `CHECK (tipo IN ('Aluno', 'Professor'))`.

3. **`emprestimo`**:
   - `id`: Chave primária.
   - `item_id` (`REFERENCES item(id)`) e `usuario_id` (`REFERENCES usuario(id)`).
   - `data_retirada`, `data_devolucao_prevista`, `data_devolucao` (aceita `NULL` para empréstimos em aberto), `valor_multa`.

### ▶️ Como Executar o Script DDL/DML

No PostgreSQL (via `psql`, pgAdmin, DBeaver ou IntelliJ Database Tool):
Execute o script localizado em:
```text
scripts/exercicio_2.sql
```

---

## 🔍 Exercício 3 — Consultas SQL

Consultas analíticas executadas sobre o modelo relacional criado no Exercício 2. Cada consulta está salva em seu respectivo script individual para facilitar a execução no PostgreSQL.

### 1. Listar todo o acervo com código, título, tipo e disponibilidade
- **Arquivo:** `scripts/exercicio_3_1.sql`
```sql
SELECT codigo, titulo, tipo, disponivel
FROM item;
```

### 2. Listar os empréstimos em aberto com nome do usuário e título do item
- **Arquivo:** `scripts/exercicio_3_2.sql`
```sql
SELECT usuario.nome AS usuario,
       item.titulo AS item,
       item.tipo,
       emprestimo.data_retirada,
       emprestimo.data_devolucao_prevista
FROM emprestimo
         INNER JOIN item ON item.id = emprestimo.item_id
         INNER JOIN usuario ON usuario.id = emprestimo.usuario_id
WHERE emprestimo.data_devolucao IS NULL;
```

### 3. Calcular o total de multas acumuladas por usuário
- **Arquivo:** `scripts/exercicio_3_3.sql`
```sql
SELECT usuario.id,
       usuario.nome,
       SUM(emprestimo.valor_multa) AS total_multas
FROM usuario
         INNER JOIN emprestimo ON usuario.id = emprestimo.usuario_id
GROUP BY usuario.id, usuario.nome
ORDER BY total_multas DESC;
```

### 4. Listar os itens que nunca foram emprestados
- **Arquivo:** `scripts/exercicio_3_4.sql`
```sql
SELECT item.codigo, item.titulo, item.tipo
FROM item
         LEFT JOIN emprestimo ON item.id = emprestimo.item_id
WHERE emprestimo.item_id IS NULL;
```

### ▶️ Como Executar as Consultas

Execute individualmente cada um dos scripts localizados em:
- `scripts/exercicio_3_1.sql`
- `scripts/exercicio_3_2.sql`
- `scripts/exercicio_3_3.sql`
- `scripts/exercicio_3_4.sql`

---

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java 21
- **Banco de Dados:** PostgreSQL
- **IDE:** IntelliJ IDEA
- **Controle de Versão:** Git
