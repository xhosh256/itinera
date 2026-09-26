CREATE TABLE users (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY ,
    username VARCHAR(256) NOT NULL UNIQUE ,
    password VARCHAR(128) NOT NULL ,
    role varchar(64) NOT NULL ,
    email varchar(256) NOT NULL UNIQUE
)