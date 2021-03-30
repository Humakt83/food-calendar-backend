(ns food-calendar-backend.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [ring.middleware.cors :refer [wrap-cors]]
            [schema.core :as s]
            [food-calendar-backend.database :refer [db]]
            [food-calendar-backend.sql :as sql]
            [buddy.auth.middleware :refer [wrap-authentication]]
            [buddy.auth.backends.session :refer [session-backend]]
            [clojure.java.jdbc :as jdbc])
  (:import java.util.Date)
  (:import java.time.ZoneId)
  (:import java.time.LocalDateTime))

(s/defschema Dish
  {:name s/Str
   :dishtype (s/enum :MEAL :BREAKFAST :SNACK :DESSERT :SOUP :DRINK)
  })

(s/defschema FoodCalendarDay
  {:date Date
   :sections [{:section (s/enum :BREAKFAST :LUNCH :DINNER :SNACK)
               :dishes [s/Str]}]
  })

(def food-routes
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
	      
	   (GET "/food-calendar-day/all" []
	     :summary "returns all food-calendar-days"
	     (def food-calendar-days (group-by :date (sql/food-calendar-day-all db)))
	     (ok food-calendar-days))
	  
	   (GET "/food-calendar-day/:day" []
	     :path-params [day :- s/Str]
	     :summary "returns food-calendar-day by day (format 2019-11-01)"
	     (def dayStart (LocalDateTime/parse (.concat day "T00:00:00")))
	     (def nextDay (.plusDays dayStart 1))
	     (ok (sql/food-calendar-day-by-date db {:date dayStart, :nextday nextDay})))
	  
	   (DELETE "/food-calendar-day/dish/:dish/:day" []
	     :path-params [day :- s/Str dish :- s/Str]
	     :summary "remove dish from date"
	     (jdbc/with-db-transaction [tx db]
	       (def dayStart (LocalDateTime/parse (.concat day "T00:00:00")))         
	       (def nextDay (.plusDays dayStart 1))
	       (sql/remove-food-from-day tx {:dish dish, :date dayStart, :nextday nextDay})
	       (ok)))
	          
	   (POST "/food-calendar-day" []
	     :return FoodCalendarDay
	     :body [food-calendar-day FoodCalendarDay]
	     :summary "posts a FoodCalendarDay"
	     (jdbc/with-db-transaction [tx db]
	       (def convertedDate (.withHour (.withMinute (.withSecond (.withNano (LocalDateTime/ofInstant (.toInstant (get food-calendar-day :date)) (ZoneId/systemDefault)) 0) 0) 0) 0))
	       (def day (first (sql/new-food-calendar-day tx {:date convertedDate})))
	       (def dishes (flatten (map :dishes (get food-calendar-day :sections))))
	       (defn fetchDish [dish]
	         (sql/dishes-by-name tx {:name dish}))
	       (def dishesFetched (flatten (map fetchDish dishes)))
	       (defn insertSection [dish, section]
	         (def dishId (:id (first (filter (fn [x] (= dish (:name x))) dishesFetched))))
	         (def sectionName (name (:section section)))
	         (sql/new-food-day-section tx {:dish dishId, :foodCalendarDay (:id day), :section sectionName}))
	       (doall (for [section (:sections food-calendar-day)
	                    dish (:dishes section)]
	                  (insertSection dish section))))
	     (ok food-calendar-day))

   (POST "/dish" []
     :return Dish
     :body [dish Dish]
     :summary "posts a Dish"
     (jdbc/with-db-transaction [tx db]
       (sql/new-dishes tx {:name (get dish :name), :dishtype (name (get dish :dishtype))}))
     (ok dish)))))

(def app
  (wrap-authentication
    (wrap-cors food-routes
		  :access-control-allow-origin [#".*"]
		  :access-control-allow-methods [:get :post :delete])))
        

