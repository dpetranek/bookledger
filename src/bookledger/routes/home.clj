(ns bookledger.routes.home
  (:require [compojure.core :refer :all]
            [bookledger.views.layout :as layout]))

(defn home []
  (layout/common [:h1 "Bookledger"]))

(defroutes home-routes
  (GET "/" [] (home)))
