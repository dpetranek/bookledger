(ns bookledger.routes.home
  (:require [hiccup.form :refer :all]
            [compojure.core :refer :all]
            [noir.session :as session]
            [noir.validation :as val]
            [noir.response :as resp]
            [bookledger.views.layout :as layout]
            [bookledger.models.db :as db]
            [bookledger.routes.library :refer [show-library]]))

(defn home [& userid]
  (layout/common
   (if-let [user (session/get :user)]
     (show-library (session/get :userid))
     [:h1 "Log In"])))

(defroutes home-routes
  (GET "/" [] (home)))
