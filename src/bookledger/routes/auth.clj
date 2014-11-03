(ns bookledger.routes.auth
  (:require [hiccup.form :refer :all]
            [compojure.core :refer :all]
            [bookledger.routes.home :refer :all]
            [bookledger.views.layout :as layout]
            [noir.session :as session]
            [noir.response :as resp]))


(defn registration-page []
  ())


(defroutes auth-routes
  (GET "/register" []
       (registration-page))
  (POST "/register" [id pass pass1]
        (handle-registration id pass pass1)))

