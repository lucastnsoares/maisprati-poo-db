-- EXERCÍCIO 3.2 — Consultas SQL
-- 2. Listar os empréstimos em aberto, com o nome do usuário e o título do item.

SELECT usuario.nome AS usuario,
       item.titulo AS item,
       item.tipo,
       emprestimo.data_retirada,
       emprestimo.data_devolucao_prevista
FROM emprestimo
         INNER JOIN item ON item.id = emprestimo.item_id
         INNER JOIN usuario ON usuario.id = emprestimo.usuario_id
WHERE emprestimo.data_devolucao IS NULL;
