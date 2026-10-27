-- EXERCÍCIO 3.3 — Consultas SQL
-- 3. Calcular o total de multas acumuladas por usuário.

SELECT usuario.id,
       usuario.nome,
       SUM(emprestimo.valor_multa) AS total_multas
FROM usuario
         INNER JOIN emprestimo ON usuario.id = emprestimo.usuario_id
GROUP BY usuario.id, usuario.nome
ORDER BY total_multas DESC;
