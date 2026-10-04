--liquibase formatted sql

--changeset cephei:1
CREATE TABLE event_components (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY ,
    event_id BIGINT NOT NULL REFERENCES events,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    description VARCHAR(1000),
    address VARCHAR(1024)
)