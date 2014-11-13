(ns bookledger.handler
  (:require [compojure.core :refer [defroutes]]
            [compojure.route :as route]
            [noir.util.middleware :as noir-middleware]
            [bookledger.routes.home :refer [home-routes]]
            [bookledger.routes.auth :refer [auth-routes]]
            [bookledger.routes.library :refer [library-routes]]))

(defn init []
  (println "bookledger is starting"))

(defn destroy []
  (println "bookledger is shutting down"))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (noir-middleware/app-handler
   [auth-routes
    home-routes
    library-routes
    app-routes]))
