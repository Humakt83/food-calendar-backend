 (defproject food-calendar-backend "0.1.0-SNAPSHOT"
   :description "Back end for food-calendar application"
   :dependencies [[org.clojure/clojure "1.10.3"]
                  [metosin/compojure-api "1.1.13"]
                  [ring-cors "0.1.13"]
                  [org.postgresql/postgresql "9.4.1212"]
                  [com.layerware/hugsql "0.5.1"]
                  [buddy/buddy-auth "2.1.0"]
                  [org.clojure/java.jdbc "0.7.9"]]
   :ring {:handler food-calendar-backend.handler/app}
   :uberjar-name "server.jar"
   :profiles {:dev {:dependencies [[javax.servlet/javax.servlet-api "3.1.0"]
                                  [cheshire "5.5.0"]                                  
                                  [ring/ring-mock "0.3.0"]]
                   :plugins [[lein-ring "0.12.0"]]}})
