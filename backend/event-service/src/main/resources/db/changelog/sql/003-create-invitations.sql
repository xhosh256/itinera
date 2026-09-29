--liquibase formatted sql

--changeset cephei:1
CREATE TABLE invitations (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY ,
    event_id BIGINT NOT NULL ,
    invited_user_id INT NOT NULL ,
    status VARCHAR(64) NOT NULL
)