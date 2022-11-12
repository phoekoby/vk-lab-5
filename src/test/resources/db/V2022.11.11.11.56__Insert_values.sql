
INSERT INTO organization(inn, checking_account) VALUES (234532545, 6234522435);
INSERT INTO organization(inn, checking_account) VALUES (854923945, 23485828);
INSERT INTO organization(inn, checking_account) VALUES (76830242, 4859234689);
INSERT INTO organization(inn, checking_account) VALUES (621113248, 12348125);

INSERT INTO product(name, internal_code) VALUES ('Product 1', 1);
INSERT INTO product(name, internal_code) VALUES ('Product 2', 2);
INSERT INTO product(name, internal_code) VALUES ('Product 3', 3);
INSERT INTO product(name, internal_code) VALUES ('Product 4', 4);

INSERT INTO invoice(number, invoice_date, sender_id) VALUES (1, DATE '2022-11-09', 1);
INSERT INTO invoice(number, invoice_date, sender_id) VALUES (2, DATE '2022-11-10', 1);
INSERT INTO invoice(number, invoice_date, sender_id) VALUES (3, DATE '2022-11-11', 2);
INSERT INTO invoice(number, invoice_date, sender_id) VALUES (4, DATE '2022-11-12', 3);
INSERT INTO invoice(number, invoice_date, sender_id) VALUES (5, DATE '2022-11-13', 3);
INSERT INTO invoice(number, invoice_date, sender_id) VALUES (6, DATE '2022-11-14', 2);

INSERT INTO invoice_position(price, product_id, invoice_id, amount) VALUES (100, 1, 1, 1000);
INSERT INTO invoice_position(price, product_id, invoice_id, amount) VALUES (1200, 2, 1, 10);
INSERT INTO invoice_position(price, product_id, invoice_id, amount) VALUES (10, 3, 1, 100000);
INSERT INTO invoice_position(price, product_id, invoice_id, amount) VALUES (100, 1, 2, 10);
INSERT INTO invoice_position(price, product_id, invoice_id, amount) VALUES (10, 1, 3, 1000);
INSERT INTO invoice_position(price, product_id, invoice_id, amount) VALUES (100, 1, 1, 1000);
INSERT INTO invoice_position(price, product_id, invoice_id, amount) VALUES (100, 1, 1, 1000);
INSERT INTO invoice_position(price, product_id, invoice_id, amount) VALUES (100, 1, 4, 10000);
INSERT INTO invoice_position(price, product_id, invoice_id, amount) VALUES (100, 1, 5, 10000);
INSERT INTO invoice_position(price, product_id, invoice_id, amount) VALUES (100, 1, 6, 1000);
INSERT INTO invoice_position(price, product_id, invoice_id, amount) VALUES (100, 1, 1, 1000);
INSERT INTO invoice_position(price, product_id, invoice_id, amount) VALUES (100, 1, 1, 1000);