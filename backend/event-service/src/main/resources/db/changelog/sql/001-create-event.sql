--liquibase formatted sql

--changeset cephei:1
CREATE TABLE events (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY ,
    host_id INT NOT NULL ,
    name VARCHAR(128) NOT NULL DEFAULT 'Undefined',
    capacity INT NOT NULL
);