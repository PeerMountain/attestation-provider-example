CREATE TABLE nft_settings
(

    id         BIGSERIAL PRIMARY KEY,
    nft_type   INTEGER NOT NULL UNIQUE,
    price      INT     NOT NULL,
    expiration BIGINT  NOT NULL
);

INSERT INTO nft_settings(nft_type, price, expiration)
VALUES (1, 100, 1640991600),
       (2, 100, 1640991600);