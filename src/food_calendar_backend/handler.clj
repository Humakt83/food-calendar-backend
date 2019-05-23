(ns food-calendar-backend.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [schema.core :as s]
            [food-calendar-backend.database :refer [db]]
            [food-calendar-backend.sql :as sql]
            [clojure.java.jdbc :as jdbc]))

(s/defschema Dish
  {:name s/Str
   :dishtype (s/enum :MEAL :BREAKFAST :SNACK :DESSERT :SOUP :DRINK)
  })

(def app
  (api
    {:swagger
     {:ui "/"
      :spec "/swagger.json"
      :data {:info {:title "Food-calendar-backend"
                    :description "Food Calendar backend api"}
             :tags [{:name "api", :description "some apis"}]}}}

    (context "/api" []
      :tags ["api"]

      (GET "/dish" []
        :query-params [dish :- s/Str]
        :summary "returns a stored dish"
        (ok (sql/dishes-by-name db {:name (name dish)})))
      
      (GET "/dish/all" []
        :summary "returns all dishes"
        (def dishes (group-by :dishtype (sql/dishes-all db)))
        (ok dishes))
      
      (GET "/dish/:dishtype" []
        :path-params [dishtype :- s/Str]
        :summary "returns dishes by dishtype"
        (ok (sql/dishes-by-dishtype db {:dishtype (name dishtype)})))

      (POST "/echo" []
        :return Dish
        :body [dish Dish]
        :summary "echoes a Dish"
        (jdbc/with-db-transaction [tx db]
          (sql/new-dishes tx {:name (get dish :name), :dishtype (name (get dish :dishtype))}))
        (conj dishes dish)
        (ok dish)))))
