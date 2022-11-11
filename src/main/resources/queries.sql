SELECT *
FROM organization org
ORDER BY (SELECT coalesce(sum(amount), 0)
          from invoice_position
          where invoice_id in
                (SELECT id
                 from invoice inv
                 where org.id = inv.sender_id) and product_id = 3) DESC
LIMIT 2;



SELECT *
FROM organization org
WHERE ((SELECT coalesce(sum(amount), 0)
        from invoice_position
        where invoice_id in
              (SELECT id
               from invoice inv
               where org.id = inv.sender_id) and product_id = 2) > 10000)
  and ((SELECT coalesce(sum(amount), 0)
      from invoice_position
      where invoice_id in
            (SELECT id
             from invoice inv
             where org.id = inv.sender_id) and product_id = 2) > 10000);



SELECT i.invoice_data,
       p.id                      as product_id,
       p.internal_code,
       p.name,
       sum(ip.amount)            as amount,
       sum(ip.amount * ip.price) as sum
FROM invoice_position ip
         LEFT JOIN product p on p.id = ip.product_id
         RIGHT JOIN invoice i on i.id = ip.invoice_id
WHERE i.invoice_data >= '2022-11-09'
  AND i.invoice_data <= '2022-11-12'
GROUP BY i.invoice_data, p.id;



SELECT p.id, p.internal_code, p.name, sum(ip.amount) as amount, sum(ip.amount * ip.price) as sum
FROM invoice_position ip
         LEFT JOIN product p on p.id = ip.product_id
         RIGHT JOIN invoice i on i.id = ip.invoice_id
WHERE i.invoice_data >= '2022-11-09'
  AND i.invoice_data <= '2022-11-12'
GROUP BY p.id;


SELECT avg(ip.price)
FROM invoice_position ip
         RIGHT JOIN invoice i on i.id = ip.invoice_id
WHERE ip.product_id = 1
  AND i.invoice_data >= '2022-11-09'
  AND i.invoice_data <= '2022-11-12';

SELECT p.id, p.name, p.internal_code, avg(ip.price)
FROM invoice_position ip
         LEFT JOIN product p on p.id = ip.product_id
         RIGHT JOIN invoice i on i.id = ip.invoice_id
WHERE i.invoice_data >= '2022-11-09'
  AND i.invoice_data <= '2022-11-12'
GROUP BY p.id;


--Вывести список товаров, поставленных организациями за период.
-- Если организация товары не поставляла, то она все равно должна быть отражена в списке.

SELECT o.id,
       o.inn,
       o.checking_account,
       ARRAY((SELECT p.id
              FROM product p
              WHERE p.id in (SELECT ip.product_id
                             FROM invoice_position ip
                             WHERE ip.invoice_id in (SELECT i.id
                                                     FROM invoice i
                                                     WHERE i.invoice_data >= '2022-11-09'
                                                       AND i.invoice_data <= '2022-11-11'
                                                       AND i.sender_id = o.id)))) as products
FROM organization o;

