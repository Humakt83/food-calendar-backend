 (defproject food-calendar-backend "0.1.0-SNAPSHOT"
   :description "FIXME: write description"
   :dependencies [[org.clojure/clojure "1.8.0"]
                  [metosin/compojure-api "1.1.11"]
                  [org.postgresql/postgresql "9.4.1212"]
                  [com.layerware/hugsql "0.4.9"]
                  [org.clojure/java.jdbc "0.7.9"]]
   :ring {:handler food-calendar-backend.handler/app}
   :uberjar-name "server.jar"
   :profiles {:dev {:dependencies [[javax.servlet/javax.servlet-api "3.1.0"]
                                  [cheshire "5.5.0"]                                  
                                  [ring/ring-mock "0.3.0"]]
                   :plugins [[lein-ring "0.12.0"]]}})
