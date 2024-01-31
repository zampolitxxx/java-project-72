DROP TABLE IF EXISTS url_checks;
DROP TABLE IF EXISTS urls;


create table urls
(
    id         SERIAL PRIMARY KEY,
    name       varchar(255)             NOT NULL,
    created_at timestamp WITH TIME ZONE NOT NULL
);

CREATE TABLE url_checks
(
id          SERIAL PRIMARY KEY,
url_id      SERIAL REFERENCES urls (id) ON DELETE CASCADE,
status_code INT                         NOT NULL,
h1          VARCHAR                     NOT NULL,
title       VARCHAR                     NOT NULL,
description VARCHAR                     NOT NULL,
created_at  TIMESTAMP WITH TIME ZONE    NOT NULL
);
