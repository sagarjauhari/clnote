CREATE TABLE tasks
(id SERIAL PRIMARY KEY,
 title VARCHAR(30),
 description VARCHAR(100),
 completed BOOLEAN,
 rank INTEGER);
