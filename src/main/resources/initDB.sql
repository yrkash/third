DROP TABLE IF EXISTS person;
DROP TABLE IF EXISTS measurement;
DROP TABLE IF EXISTS sensor;
CREATE TABLE sensor(
                       id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                       name varchar(100) NOT NULL UNIQUE
);

CREATE TABLE measurement(
                     id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                     value double precision not null,
                     raining bool,
                     created_at timestamp not null,
                     sensor varchar(100) REFERENCES sensor(name)
);

CREATE TABLE person(
                       id int PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
                       username varchar(100) NOT NULL,
                       year_of_birth int NOT NULL,
                       password varchar NOT NULL,
                       role varchar NOT NULL
);


INSERT INTO sensor(name) VALUES ('nnov_1');
INSERT INTO sensor(name) VALUES ('mos_77');
INSERT INTO sensor(name) VALUES ('vsk_10');
INSERT INTO measurement(value, raining, created_at, sensor) VALUES (18.9, true, '2023-05-10 23:12:24.609670', 'vsk_10');
INSERT INTO person(username, year_of_birth, password, role) VALUES ('user', 1988, '12345','ROLE_USER');
INSERT INTO person(username, year_of_birth, password, role) VALUES ('admin', 1988, '12345','ROLE_USER');
UPDATE person SET role = 'ROLE_ADMIN' WHERE username = 'admin'