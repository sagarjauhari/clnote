ALTER TABLE tasks
  ADD parent_id int
  REFERENCES tasks(id);
