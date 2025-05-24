ALTER TABLE roles
DROP
FOREIGN KEY fk_roles_on_role;

ALTER TABLE roles
DROP
FOREIGN KEY fk_roles_on_user;

CREATE TABLE user_role
(
    role_id INT NOT NULL,
    user_id INT NOT NULL
);

ALTER TABLE user_role
    ADD CONSTRAINT fk_user_role_on_role FOREIGN KEY (role_id) REFERENCES `role` (id);

ALTER TABLE user_role
    ADD CONSTRAINT fk_user_role_on_user FOREIGN KEY (user_id) REFERENCES user (id);

DROP TABLE roles;