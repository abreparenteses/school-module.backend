CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

--;;

CREATE TABLE IF NOT EXISTS SUBJECTS (
  id uuid UNIQUE NOT NULL PRIMARY KEY,
  removed BOOLEAN DEFAULT FALSE,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  courses_id uuid NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_courses FOREIGN KEY (courses_id) REFERENCES courses(id) ON UPDATE CASCADE ON DELETE CASCADE
);
