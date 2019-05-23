(ns food-calendar-backend.sql
  (:require [hugsql.core :as hugsql]))

(hugsql/def-db-fns "food.sql")