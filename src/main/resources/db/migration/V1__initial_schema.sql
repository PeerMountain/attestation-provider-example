CREATE TABLE user_data
(
    id      BIGSERIAL PRIMARY KEY,
    address TEXT UNIQUE
);

CREATE TABLE challenge
(
    id        BIGSERIAL PRIMARY KEY,
    user_id   BIGINT REFERENCES user_data (id),
    challenge TEXT,
    used      BOOLEAN
);