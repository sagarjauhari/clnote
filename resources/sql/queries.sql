-- name: create-user!
-- creates a new user record
INSERT INTO users
(id, first_name, last_name, email, pass)
VALUES (:id, :first_name, :last_name, :email, :pass)

-- name: update-user!
-- update an existing user record
UPDATE users
SET first_name = :first_name, last_name = :last_name, email = :email
WHERE id = :id

-- name: get-user
-- retrieve a user given the id
SELECT * FROM users
WHERE id = :id

--name: get-users
-- selects all available users
SELECT * from users

-- name: create-task!
-- creates a new task record
INSERT INTO tasks
(title, description, completed, rank)
VALUES (:title, :description, :completed, :rank)

-- name: update-task!
-- update an existing task record
UPDATE tasks
SET title = :title, description = :description, completed = :completed,
  rank = :rank
WHERE id = :id

-- name: get-task
-- retrieve a task given the id
SELECT * FROM tasks
WHERE id = :id

-- name: get-tasks
-- selects all available tasks
SELECT * from tasks

-- name: delete-task!
-- deletes a task
DELETE FROM tasks
WHERE id = :id
