-- EXERCÍCIO 2 — Modelagem do Banco de Dados
-- O mesmo cenário do Exercício 1, agora modelado como tabelas relacionais em
-- PostgreSQL — sem herança: um item é "livro" ou "revista" por uma coluna tipo, e o mesmo vale para o usuário.

-- O que fazer
-- 11. tabela item: id, codigo, titulo, tipo, autor, edicao, disponivel.
-- 12. tabela usuario: id, nome, tipo, limite_itens.
-- 13. tabela emprestimo: id, item_id, usuario_id, data_retirada, data_devolucao_prevista, data_devolucao, valor_multa.
-- 14. escreva o script CREATE TABLE das três tabelas, na ordem correta.
CREATE TABLE item
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    codigo     VARCHAR(30)  NOT NULL,
    titulo     VARCHAR(100) NOT NULL,
    tipo       VARCHAR(10)  NOT NULL,
    autor      VARCHAR(60),
    edicao     INT CHECK (edicao >= 1),
    disponivel BOOLEAN      NOT NULL DEFAULT TRUE,


    CONSTRAINT ck_item_tipo
        CHECK (tipo IN ('Livro', 'Revista'))
);

CREATE TABLE usuario
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome         VARCHAR(60) NOT NULL,
    tipo         VARCHAR(10) NOT NULL,
    limite_itens INT         NOT NULL CHECK (limite_itens > 0),

    CONSTRAINT ck_usuario_tipo
        CHECK (tipo IN ('Aluno', 'Professor'))
);

CREATE TABLE emprestimo
(
    id                      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    item_id                 BIGINT         NOT NULL,
    usuario_id              BIGINT         NOT NULL,
    data_retirada           DATE           NOT NULL DEFAULT CURRENT_DATE,
    data_devolucao_prevista DATE           NOT NULL,
    data_devolucao          DATE,
    valor_multa             NUMERIC(10, 2) NOT NULL DEFAULT 0,


    CONSTRAINT fk_emprestimo_item
        FOREIGN KEY (item_id) REFERENCES item (id),

    CONSTRAINT fk_emprestimo_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario (id),

    CONSTRAINT ck_emprestimo_data_devolucao_prevista
        CHECK (data_devolucao_prevista >= data_retirada),

    CONSTRAINT ck_emprestimo_data_devolucao
        CHECK (data_devolucao IS NULL OR data_devolucao >= data_retirada),

    CONSTRAINT ck_emprestimo_valor_multa
        CHECK (valor_multa >= 0)
);

-- 15. insira dados de teste: pelo menos 4 itens, 2 usuários e 2 empréstimos (um em aberto, um já devolvido).
INSERT INTO item (codigo, titulo, tipo, autor, edicao, disponivel)
VALUES ('L001', 'Clean Code', 'Livro', 'Robert C. Martin', 1, TRUE),
       ('L002', 'Engenharia de Software Moderna', 'Livro', 'Marco Tulio Valente', 1, FALSE),
       ('L003', 'Padrões de Projetos (GoF)', 'Livro', 'Erich Gamma et al.', 1, TRUE),
       ('L004', 'Java: Como Programar', 'Livro', 'Paul Deitel', 10, FALSE),
       ('L005', 'Domain-Driven Design', 'Livro', 'Eric Evans', 1, TRUE),
       ('R001', 'Mundo Java Ed. 42', 'Revista', NULL, 42, TRUE),
       ('R002', 'Superinteressante Ed. 450', 'Revista', NULL, 450, FALSE),
       ('R003', 'Scientific American Brasil Ed. 180', 'Revista', NULL, 180, TRUE);

INSERT INTO usuario (nome, tipo, limite_itens)
VALUES ('José da Silva', 'Aluno', 3),
       ('Maria Oliveira', 'Professor', 5),
       ('Lucas Ferreira', 'Aluno', 3),
       ('Ana Beatriz Souza', 'Professor', 5),
       ('Carlos Eduardo Santos', 'Aluno', 3);

INSERT INTO emprestimo (item_id, usuario_id, data_retirada, data_devolucao_prevista, data_devolucao, valor_multa)
VALUES ((SELECT id FROM item WHERE codigo = 'L001'),
        (SELECT id FROM usuario WHERE nome = 'José da Silva'),
        '2026-03-01', '2026-03-15', '2026-03-20', 2.50),
       ((SELECT id FROM item WHERE codigo = 'R002'),
        (SELECT id FROM usuario WHERE nome = 'Maria Oliveira'),
        '2026-04-01', '2026-04-08', NULL, 0.00),
       ((SELECT id FROM item WHERE codigo = 'L003'),
        (SELECT id FROM usuario WHERE nome = 'Lucas Ferreira'),
        '2026-03-10', '2026-03-24', '2026-03-22', 0.00),
       ((SELECT id FROM item WHERE codigo = 'L002'),
        (SELECT id FROM usuario WHERE nome = 'Lucas Ferreira'),
        '2026-04-10', '2026-04-24', NULL, 0.00),
       ((SELECT id FROM item WHERE codigo = 'R001'),
        (SELECT id FROM usuario WHERE nome = 'José da Silva'),
        '2026-03-15', '2026-03-22', '2026-03-25', 3.00),
       ((SELECT id FROM item WHERE codigo = 'L004'),
        (SELECT id FROM usuario WHERE nome = 'Ana Beatriz Souza'),
        '2026-04-12', '2026-04-26', NULL, 0.00);