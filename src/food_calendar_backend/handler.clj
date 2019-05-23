(ns food-calendar-backend.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [schema.core :as s]))

(s/defschema Dish
  {:name s/Str
   :dishtype (s/enum :MEAL :BREAKFAST :SNACK :DESSERT :SOUP :DRINK)
  })

(def hernari
  {
   :name "Hernekeitto"
   :dishtype "SOUP"
   })

(def dishes [hernari])

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
        (ok (first (filter (fn [x]
                             (= (get x :name) dish))
                             dishes))))

      (POST "/echo" []
        :return Dish
        :body [dish Dish]
        :summary "echoes a Dish"
        (conj dishes dish)
        (ok dish)))))
