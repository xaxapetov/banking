--liquibase formatted sql

--changeset nikita.ryadnov:1
INSERT INTO banking."user" (id, name, date_of_birth, password) VALUES (1, 'Tome', '11.11.2001', '$2a$12$4A1ObiT9JMRxPh9cKdCdje4JaUkjosQ.Mj4uEaUq3dx29f.Ihv99i');
INSERT INTO banking."user" (id, name, date_of_birth, password) VALUES (2, 'Toni', '10.11.2023', '$2a$12$4A1ObiT9JMRxPh9cKdCdje4JaUkjosQ.Mj4uEaUq3dx29f.Ihv99i');
SELECT SETVAL('banking.user_id_seq', (SELECT MAX(id) FROM banking.user));
--changeset nikita.ryadnov:2
INSERT INTO banking.account (id, user_id, balance) VALUES (1, 1, 800.00);
INSERT INTO banking.account (id, user_id, balance) VALUES (2, 2, 1000.00);
SELECT SETVAL('banking.account_id_seq', (SELECT MAX(id) FROM banking.account));
--changeset nikita.ryadnov:3
INSERT INTO banking.phone_data (id, user_id, phone) VALUES (1, 1, '79781234567');
INSERT INTO banking.phone_data (id, user_id, phone) VALUES (2, 2, '79791234111');
SELECT SETVAL('banking.phone_data_id_seq', (SELECT MAX(id) FROM banking.phone_data));
--changeset nikita.ryadnov:4
INSERT INTO banking.email_data (id, user_id, email) VALUES (1, 1, 'tom@test.ru');
INSERT INTO banking.email_data (id, user_id, email) VALUES (2, 2, 'toni@test.ru');
SELECT SETVAL('banking.email_data_id_seq', (SELECT MAX(id) FROM banking.email_data));