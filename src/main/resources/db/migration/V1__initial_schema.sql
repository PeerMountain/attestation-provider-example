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

CREATE TABLE attestation_url
(
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT REFERENCES user_data (id),
    token           TEXT      NOT NULL,
    expiration_time TIMESTAMP NOT NULL
);