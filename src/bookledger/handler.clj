(ns bookledger.handler
  (:require [compojure.core :refer [defroutes]]
            [compojure.route :as route]
            [noir.util.middleware :as noir-middleware]
            [bookledger.routes.home :refer [home-routes]]))

(defn init []
  (println "bookledger is starting"))

(defn destroy []
  (println "bookledger is shutting down"))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (noir-middleware/app-handler
   [home-routes
    app-routes]))
