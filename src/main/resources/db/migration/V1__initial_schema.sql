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

CREATE TABLE attestation
(
    id               BIGSERIAL PRIMARY KEY,
    user_id          BIGINT REFERENCES user_data (id),
    attestation_data TEXT      NOT NULL,
    attestation_time TIMESTAMP NOT NULL,
    hash_key_array   TEXT      NOT NULL,
    hashed_data      TEXT      NOT NULL,
    signature        TEXT
)