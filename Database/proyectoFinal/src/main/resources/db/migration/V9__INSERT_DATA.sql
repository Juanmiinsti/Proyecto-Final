-- ==== INSERTS PARA ROLES ====
INSERT INTO `role` (name) VALUES ('Player');
INSERT INTO `role` (name) VALUES ('Mod');
INSERT INTO `role` (name) VALUES ('Admin');

-- ==== INSERTS PARA CHARACTERS ====
INSERT INTO `character` (name, max_health, max_stamina, damage)
VALUES ('Huntress', 120, 100, 35);

INSERT INTO `character` (name, max_health, max_stamina, damage)
VALUES ('Martial', 110, 90, 30);

INSERT INTO `character` (name, max_health, max_stamina, damage)
VALUES ('Medieval', 130, 85, 40);
