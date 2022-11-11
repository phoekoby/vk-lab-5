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