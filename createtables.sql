CREATE TABLE dishes (
  id SERIAL PRIMARY KEY,
  name text NOT NULL unique,
  dishtype text NOT NULL,
  created_at timestamptz NOT NULL DEFAULT now()
);
