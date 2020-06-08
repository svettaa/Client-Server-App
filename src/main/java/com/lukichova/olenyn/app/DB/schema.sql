CREATE TABLE groups
(
    id          integer PRIMARY KEY autoincrement,
    name        varchar(100),
    description VARCHAR(255)
);

CREATE TABLE goods
(
    id          integer PRIMARY KEY autoincrement,
    name        varchar(100),
    price       numeric NOT NULL,
    left_amount integer NOT NULL,
    producer    VARCHAR(255),
    description VARCHAR(255),
    group_id    integer NOT NULL REFERENCES groups (id) ON DELETE CASCADE
);