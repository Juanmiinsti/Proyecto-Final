-- ==== INSERTS PARA ROLES ====
INSERT INTO `role` (name)
VALUES ('PLAYER');
INSERT INTO `role` (name)
VALUES ('MOD');
INSERT INTO `role` (name)
VALUES ('ADMIN');

-- ==== INSERTS PARA CHARACTERS ====
INSERT INTO `character` (name, max_health, max_stamina, damage)
VALUES ('Huntress', 120, 100, 35);

INSERT INTO `character` (name, max_health, max_stamina, damage)
VALUES ('Martial', 110, 90, 30);

INSERT INTO `character` (name, max_health, max_stamina, damage)
VALUES ('Medieval', 130, 85, 40);


INSERT INTO `user` (name, password)
VALUES ('admintest', '$2a$10$T0dTu8mMQwzPqgA1NRwCKevF3PYEN.WGDNwJKsoqbA0Gn2mQzH7Wa'), -- adminpass
       ('modtest', '$2a$10$STdM7mc7fsXZFbxdwpV7uOtVpwhsf5oGmsvF3fRhVrhmuK20mFlP6'),   -- modpass
       ('playertest', '$2a$10$qlKcJoXYfUqf54lIbGy6nuzBieH9F8ESuQpCr3UFiYjxCcT9E1Whq');

INSERT INTO `user_role` (user_id, role_id)
VALUES ((SELECT id FROM `user` WHERE name = 'admintest'), (SELECT id FROM `role` WHERE name = 'PLAYER')),
       ((SELECT id FROM `user` WHERE name = 'admintest'), (SELECT id FROM `role` WHERE name = 'MOD')),
       ((SELECT id FROM `user` WHERE name = 'admintest'), (SELECT id FROM `role` WHERE name = 'ADMIN'));


INSERT INTO `user_role` (user_id, role_id)
VALUES ((SELECT id FROM `user` WHERE name = 'modtest'), (SELECT id FROM `role` WHERE name = 'MOD'));


INSERT INTO `user_role` (user_id, role_id)
VALUES ((SELECT id FROM `user` WHERE name = 'playertest'), (SELECT id FROM `role` WHERE name = 'PLAYER'));
