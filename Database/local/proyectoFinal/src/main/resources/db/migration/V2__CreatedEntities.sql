CREATE TABLE `character`
(
    id          INT AUTO_INCREMENT NOT NULL,
    name        VARCHAR(255) NOT NULL,
    max_health  INT          NOT NULL,
    max_stamina INT          NOT NULL,
    damage      INT          NOT NULL,
    CONSTRAINT pk_character PRIMARY KEY (id)
);

CREATE TABLE enemy
(
    id         INT AUTO_INCREMENT NOT NULL,
    name       VARCHAR(255) NULL,
    max_health INT NULL,
    damage     INT NULL,
    CONSTRAINT pk_enemy PRIMARY KEY (id)
);

CREATE TABLE `kill`
(
    character_id INT NOT NULL,
    enemy_id     INT NOT NULL
);

CREATE TABLE `match`
(
    id             INT AUTO_INCREMENT NOT NULL,
    date           datetime NOT NULL,
    length DOUBLE NOT NULL,
    char_winner_id INT NULL,
    char_loser_id  INT NULL,
    user_winner_id INT NULL,
    user_loser_id  INT NULL,
    CONSTRAINT pk_match PRIMARY KEY (id)
);

CREATE TABLE object
(
    id            INT AUTO_INCREMENT NOT NULL,
    name          VARCHAR(255) NOT NULL,
    `description` VARCHAR(255) NOT NULL,
    CONSTRAINT pk_object PRIMARY KEY (id)
);

CREATE TABLE `role`
(
    id   INT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255) NULL,
    CONSTRAINT pk_role PRIMARY KEY (id)
);

CREATE TABLE roles
(
    role_id INT NOT NULL,
    user_id INT NOT NULL
);

CREATE TABLE stadium
(
    id           INT AUTO_INCREMENT NOT NULL,
    name         VARCHAR(255) NOT NULL,
    character_id INT          NOT NULL,
    CONSTRAINT pk_stadium PRIMARY KEY (id)
);

CREATE TABLE tutorial
(
    id            INT AUTO_INCREMENT NOT NULL,
    title         VARCHAR(255) NOT NULL,
    `description` VARCHAR(255) NOT NULL,
    CONSTRAINT pk_tutorial PRIMARY KEY (id)
);

CREATE TABLE `use`
(
    character_id INT NOT NULL,
    object_id    INT NOT NULL
);

CREATE TABLE user
(
    id       INT AUTO_INCREMENT NOT NULL,
    name     VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE `role`
    ADD CONSTRAINT uc_role_name UNIQUE (name);

ALTER TABLE stadium
    ADD CONSTRAINT uc_stadium_character UNIQUE (character_id);

ALTER TABLE user
    ADD CONSTRAINT uc_user_name UNIQUE (name);

ALTER TABLE `match`
    ADD CONSTRAINT FK_MATCH_ON_CHARLOSERID FOREIGN KEY (char_loser_id) REFERENCES `character` (id);

ALTER TABLE `match`
    ADD CONSTRAINT FK_MATCH_ON_CHARWINNERID FOREIGN KEY (char_winner_id) REFERENCES `character` (id);

ALTER TABLE `match`
    ADD CONSTRAINT FK_MATCH_ON_USERLOSERID FOREIGN KEY (user_loser_id) REFERENCES user (id);

ALTER TABLE `match`
    ADD CONSTRAINT FK_MATCH_ON_USERWINNERID FOREIGN KEY (user_winner_id) REFERENCES user (id);

ALTER TABLE stadium
    ADD CONSTRAINT FK_STADIUM_ON_CHARACTER FOREIGN KEY (character_id) REFERENCES `character` (id);

ALTER TABLE `kill`
    ADD CONSTRAINT fk_kill_on_character FOREIGN KEY (character_id) REFERENCES `character` (id);

ALTER TABLE `kill`
    ADD CONSTRAINT fk_kill_on_enemy FOREIGN KEY (enemy_id) REFERENCES enemy (id);

ALTER TABLE roles
    ADD CONSTRAINT fk_roles_on_role FOREIGN KEY (role_id) REFERENCES `role` (id);

ALTER TABLE roles
    ADD CONSTRAINT fk_roles_on_user FOREIGN KEY (user_id) REFERENCES user (id);

ALTER TABLE `use`
    ADD CONSTRAINT fk_use_on_character FOREIGN KEY (character_id) REFERENCES `character` (id);

ALTER TABLE `use`
    ADD CONSTRAINT fk_use_on_object FOREIGN KEY (object_id) REFERENCES object (id);