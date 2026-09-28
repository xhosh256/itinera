--liquibase formatted sql

--changeset cephei:1
CREATE TABLE event_participants (
    event_id BIGINT NOT NULL ,
    user_id BIGINT NOT NULL ,
    PRIMARY KEY (event_id, user_id)
);