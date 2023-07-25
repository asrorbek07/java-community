--liquibase formatted sql

--changeset liquibase:1 labels:ddl
--comment: Create user table
CREATE TABLE IF NOT EXISTS user
(
    username    text,
    roles       frozen<set<text>>,
    age         int,
    info        text,
    image_url    text,
    created_date  timestamp,
    modified_date  timestamp,
    PRIMARY KEY (username)
);
-- rollback DROP TABLE user

--changeset liquibase:2 labels:ddl
--comment: Create login table
CREATE TABLE IF NOT EXISTS login
(
    username text,
    password text,
    PRIMARY KEY (username)
);
-- rollback DROP TABLE login

--changeset liquibase:3 labels:ddl
--comment: Create category table
CREATE TABLE IF NOT EXISTS category
(
    id uuid,
    name text,
    parent_id uuid,
    created_by    text,
    created_date  timestamp,
    modified_by   text,
    modified_date timestamp,
    PRIMARY KEY (id)
);
-- rollback DROP TABLE category

--changeset liquibase:4 labels:ddl
--comment: Create article table
CREATE TABLE IF NOT EXISTS article
(
    id uuid,
    category_id uuid,
    name text,
    created_by    text,
    created_date  timestamp,
    modified_by   text,
    modified_date timestamp,
    PRIMARY KEY (id)
);
-- rollback DROP TABLE article

--changeset liquibase:5 labels:ddl
--comment: Create sub_article table
CREATE TABLE IF NOT EXISTS sub_article
(
    id uuid,
    article_id uuid,
    parent_sub_article_id uuid,
    name text,
    created_by    text,
    created_date  timestamp,
    modified_by   text,
    modified_date timestamp,
    PRIMARY KEY (id)
);
-- rollback DROP TABLE sub_article

--changeset liquibase:6 labels:ddl
--comment: Create sub_article_content table
CREATE TABLE IF NOT EXISTS sub_article_content
(
    id uuid,
    sub_article_id uuid,
    content text,
    is_paragraph boolean,
    created_by    text,
    created_date  timestamp,
    modified_by   text,
    modified_date timestamp,
    PRIMARY KEY (id)
);
-- rollback DROP TABLE sub_article_content