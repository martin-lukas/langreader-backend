/* Creates user role with password */
-- CREATE ROLE langreader WITH LOGIN NOSUPERUSER CREATEDB INHERIT PASSWORD 'langreader';

/* Creates DB with default locale for the current user */
-- CREATE DATABASE langreader;

/*
    The order of these drops matters because of foreign key references.
    The higher in the list it is, the more references to other tables it carries.
*/
DROP TABLE IF EXISTS words;
DROP TABLE IF EXISTS texts;

DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS user_langs;

DROP TABLE IF EXISTS users;

DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS langs;
DROP TABLE IF EXISTS word_types;

CREATE TABLE roles (
    id INT PRIMARY KEY,
    role_type VARCHAR(50) NOT NULL
);

CREATE TABLE langs (
    id SERIAL PRIMARY KEY,
    code VARCHAR(2) UNIQUE NOT NULL,
    full_name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE word_types (
    id INT PRIMARY KEY,
    type VARCHAR(50) NOT NULL
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    native_lang_id INT NOT NULL,
    chosen_lang_id INT,
    FOREIGN KEY (native_lang_id) REFERENCES langs (id),
    FOREIGN KEY (chosen_lang_id) REFERENCES langs (id)
);

CREATE TABLE user_roles (
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (role_id) REFERENCES roles (id)
);

CREATE TABLE user_langs (
    user_id INT NOT NULL,
    lang_id INT NOT NULL,
    PRIMARY KEY (user_id, lang_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (lang_id) REFERENCES langs (id)
);

CREATE TABLE texts (
    id SERIAL PRIMARY KEY,
    title TEXT NOT NULL,
    text TEXT NOT NULL,
    user_id INT NOT NULL,
    lang_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (lang_id) REFERENCES langs (id)
);

CREATE TABLE words (
    id BIGSERIAL PRIMARY KEY,
    value VARCHAR(50) NOT NULL,
    type_id INT NOT NULL,
    user_id INT NOT NULL,
    lang_id INT NOT NULL,
    FOREIGN KEY (type_id) REFERENCES word_types (id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (lang_id) REFERENCES langs (id),
    UNIQUE (value, user_id, lang_id)
);