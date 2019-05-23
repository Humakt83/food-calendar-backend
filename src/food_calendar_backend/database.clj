(ns food-calendar-backend.database)

(def db
  {:classname "org.postgresql.Driver"
   :subprotocol "postgresql"
   :subname "//localhost:5432/food"
   :user "food"
   :password "testfood"
   :sslmode "require"
   })