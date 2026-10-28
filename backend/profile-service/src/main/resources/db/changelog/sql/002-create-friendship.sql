CREATE TABLE friendships (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY ,
    user_id INT NOT NULL,
    friend_id INT NOT NULL,
    status VARCHAR(128) NOT NULL,
    UNIQUE (user_id, friend_id)
);