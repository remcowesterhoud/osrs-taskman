CREATE TABLE IF NOT EXISTS task (

    id SERIAL NOT NULL PRIMARY KEY,
    name varchar(255) NOT NULL,
    tier varchar(16) NOT NULL,
    info varchar(255) NOT NULL
);

INSERT INTO task (name, tier, info) (
VALUES
    ('EASY task', 'EASY', 'This is an EASY task'),
    ('MEDIUM task', 'MEDIUM', 'This is an MEDIUM task'),
    ('HARD task', 'HARD', 'This is an HARD task'),
    ('ELITE task', 'ELITE', 'This is an ELITE task'),
    ('PETS task', 'PETS', 'This is an PETS task'),
    ('PASSIVE task', 'PASSIVE', 'This is an PASSIVE task'),
    ('EXTRA task', 'EXTRA', 'This is an EXTRA task')
);