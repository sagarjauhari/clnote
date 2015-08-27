ALTER TABLE tasks
  ADD collection_id INT
  REFERENCES collections(id);
