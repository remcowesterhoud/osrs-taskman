CREATE TABLE IF NOT EXISTS account (

    id SERIAL NOT NULL PRIMARY KEY,
    username varchar(100) UNIQUE NOT NULL,
    password varchar(100) NOT NULL,
    role varchar(16) NOT NULL,
    enabled boolean NOT NULL,
    tier varchar(16) NOT NULL
);