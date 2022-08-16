CREATE TABLE IF NOT EXISTS task (

    id SERIAL NOT NULL PRIMARY KEY,
    name varchar(255) NOT NULL,
    tier varchar(16) NOT NULL,
    info varchar(255) NOT NULL
);

INSERT INTO task (name, tier, info) (
VALUES
    ('EASY task', 1, 'This is an EASY task'),
    ('MEDIUM task', 2, 'This is an MEDIUM task'),
    ('HARD task', 3, 'This is an HARD task'),
    ('ELITE task', 4, 'This is an ELITE task'),
    ('PETS task', 5, 'This is an PETS task'),
    ('PASSIVE task', 6, 'This is an PASSIVE task'),
    ('EXTRA task', 7, 'This is an EXTRA task')
);