CREATE TABLE nft_settings
(

    id          BIGSERIAL PRIMARY KEY,
    nft_type    INTEGER NOT NULL UNIQUE,
    price       INT     NOT NULL,
    expiration  BIGINT  NOT NULL,
    name        TEXT    NOT NULL,
    description TEXT,
    image_url   TEXT    NOT NULL
);

INSERT INTO nft_settings(nft_type, price, expiration, name, description, image_url)
VALUES (1, 100, 1640991600,
        'Crypto Raptors',
        'Raptor finance is a decentralized, financial ecosystem designed by holders for holders. Our mission is to heal planet earth and stop climate change by allowing our holders to stake their tokens to generate yield for themselves and ecological projects.',
        'https://images.app.goo.gl/pUR3zTectgrqczhC6'),
       (2, 150, 1640991600,
        'Crypto Diamonds',
        'Several U.S. listed gold miners have shed a fifth of their market value in 2021 as the appeal has dulled due to a strengthening dollar, and a rise in bond yields has impacted the demand for the precious metal. With market risks such as the COVID-19 Delta variant spooking investors, traders, and companies, many are focused on diversifying with hard assets, as a safe store of wealth and hedge against inflation.',
        'https://images.app.goo.gl/817hZFkpJY82j1C1A');