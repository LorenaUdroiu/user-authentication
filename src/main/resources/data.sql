INSERT INTO USER_ADDRESS (id, city, zipCode, state, country, street, houseNumber)
    VALUES (1, 'Bucharest', '100510', 'Bucharest', 'Romania', 'Mihai Viteazul', '5');
INSERT INTO USER_ADDRESS (id, city, zipCode, state, country, street, houseNumber)
    VALUES (2, 'Otopeni', '230510', 'Ilfov', 'Romania', 'Oituz', '21');

INSERT INTO USER (id, username, password, firstName, lastName, birthDate, address, createDate, updateDate, failedLogins, lastLogin, state)
    VALUES (1, 'lorena.danciu@yahoo.com', '$2a$10$zalfUjwGQMIjIytT89W4EuU1OJA76Iq0bF6F4Hwz/BoLCB2afYtnS', 'Lorena', 'Udroiu', null, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 'ACTIVE');
INSERT INTO USER (id, username, password, firstName, lastName, birthDate, address, createDate, updateDate, failedLogins, lastLogin, state)
    VALUES (2, 'test@yahoo.com', '$2a$10$zalfUjwGQMIjIytT89W4EuU1OJA76Iq0bF6F4Hwz/BoLCB2afYtnS', 'TestUserFirstName', 'TestUserLastName', null, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3, CURRENT_TIMESTAMP, 'LOCKED');