-- EXERCÍCIO 3.4 — Consultas SQL
-- 4. Listar os itens que nunca foram emprestados.

SELECT item.codigo, item.titulo, item.tipo
FROM item
         LEFT JOIN emprestimo ON item.id = emprestimo.item_id
WHERE emprestimo.item_id IS NULL;
