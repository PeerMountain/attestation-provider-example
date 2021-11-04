CREATE TABLE nft_settings
(

    id         BIGSERIAL PRIMARY KEY,
    nft_type   INTEGER NOT NULL UNIQUE,
    perpetuity BOOLEAN NOT NULL,
    price      INT     NOT NULL,
    expiration BIGINT  NOT NULL
);

INSERT INTO nft_settings(nft_type, perpetuity, price, expiration)
VALUES (1, true, 100, 1640991600),
       (2, true, 100, 1640991600);