DELETE FROM public_voters;
DELETE FROM poll_private_voters;
DELETE FROM poll;
DELETE FROM bill_supporters;
DELETE FROM bill;
DELETE FROM delegate_theme_voters;
DELETE FROM delegate_theme;
DELETE FROM theme;
DELETE FROM citizen;

INSERT INTO theme(id, designation, parent_theme_id) VALUES (1, 'Leis', NULL);
INSERT INTO theme(id, designation, parent_theme_id) VALUES (2, 'Segurança', NULL);
INSERT INTO theme(id, designation, parent_theme_id) VALUES (3, 'Desastres Naturais', 2);
INSERT INTO theme(id, designation, parent_theme_id) VALUES (4, 'Criação de Leis', 1);

INSERT INTO citizen(dtype, id, citizen_card_number, name) VALUES ('delegate', 1, 1, 'Thiago D.');
INSERT INTO citizen(dtype, id, citizen_card_number, name) VALUES ('citizen', 2, 2, 'Voter 1');
INSERT INTO citizen(dtype, id, citizen_card_number, name) VALUES ('delegate', 3, 3, 'Roberto C.');

INSERT INTO bill( num_supporters, id, description, expiration_date, file_data, status, title, delegate_id, theme_id)
VALUES(20,10, 'Mock - Bill 10 - PDF BYTES = NULL', '2023-05-27', NULL, 'OPEN', 'Criar uma Comissão Nacional para Debates Eleitorais e alterar a Lei da cobertura eleitoral', 1, 4);
INSERT INTO bill( num_supporters, id, description, expiration_date, file_data, status, title, delegate_id, theme_id)
VALUES(40,20, 'Mock - Bill 20 - PDF BYTES = NULL', '2023-05-29', NULL, 'OPEN', 'Indicador de Risco em caso de Sismo', 3, 3);
INSERT INTO bill( num_supporters, id, description, expiration_date, file_data, status, title, delegate_id, theme_id)
VALUES(100,30, 'Mock - Bill 30 - PDF BYTES = NULL', '2023-05-28', NULL, 'CLOSED', 'Criação da Lei 4093 $2A', 1, 1);
INSERT INTO bill(num_supporters, id, description, expiration_date, file_data, status, title, delegate_id, theme_id)
VALUES(9999, 40, 'Mock - Bill 40 - PDF BYTES = NULL', '2023-06-01', NULL, 'OPEN', 'Votação contra aeroportos perto de urbanizações', 1, 4);

INSERT INTO bill(num_supporters, id, description, expiration_date, file_data, status, title, delegate_id, theme_id)
VALUES(4, 50, 'Mock - Bill 50 - PDF BYTES = NULL', '2023-05-27', NULL, 'CLOSED', 'Teste Poll com data passada', 1, 3);


INSERT INTO poll(id, closing_date, num_negative_votes, num_positive_votes, status, associated_bill_id) VALUES (999, NOW(), 0, 0, 'ACTIVE', 10);
INSERT INTO poll(id, closing_date, num_negative_votes, num_positive_votes, status, associated_bill_id) VALUES (9999, NOW(), 0, 0, 'ACTIVE', 30);
INSERT INTO poll(id, closing_date, num_negative_votes, num_positive_votes, status, associated_bill_id) VALUES (99999, '2023-05-27', 0, 0, 'ACTIVE', 50);

INSERT INTO citizen(dtype, id, citizen_card_number, name) VALUES ('citizen', 401, 401, 'Voter 11');
INSERT INTO citizen(dtype, id, citizen_card_number, name) VALUES ('citizen', 402, 402, 'Voter 2');
INSERT INTO citizen(dtype, id, citizen_card_number, name) VALUES ('delegate', 403, 403, 'Voter 3');
INSERT INTO delegate_theme(id, delegate_id, theme_id) VALUES(100,1,3);
INSERT INTO delegate_theme_voters(delegate_themes_id,voters_id) VALUES(100,401);
INSERT INTO delegate_theme_voters(delegate_themes_id,voters_id) VALUES(100,402);
INSERT INTO public_voters(poll_id, vote_type,delegate_id) VALUES (99999, 'NEGATIVE', 403);
