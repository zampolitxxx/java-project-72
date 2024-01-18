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
urlId      SERIAL REFERENCES urls (id) ON DELETE CASCADE,
statusCode INT                         NOT NULL,
h1          VARCHAR                     NOT NULL,
title       VARCHAR                     NOT NULL,
description VARCHAR                     NOT NULL,
createdAt  TIMESTAMP WITH TIME ZONE    NOT NULL
);
