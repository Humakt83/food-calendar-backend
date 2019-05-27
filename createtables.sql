CREATE TABLE dishes (
  id SERIAL PRIMARY KEY,
  name VARCHAR (255) NOT NULL unique,
  dishtype VARCHAR (20) NOT NULL,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE foodCalendarDay (
  id SERIAL PRIMARY KEY,
  date timestamptz NOT NULL,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE foodDaySection (
  dish INTEGER REFERENCES dishes(id) NOT NULL,
  foodCalendarDay INTEGER REFERENCES foodCalendarDay(id) NOT NULL,
  section VARCHAR (20) NOT NULL,
  created_at timestamptz NOT NULL DEFAULT now(),
  UNIQUE (dish, foodCalendarDay, section)
);