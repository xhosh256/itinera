CREATE TABLE profiles (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY ,
    user_id INT NOT NULL UNIQUE ,
    firstname VARCHAR(256) NOT NULL ,
    lastname VARCHAR(256) DEFAULT '',
    birth_date DATE ,
    bio varchar(512) DEFAULT '',
)