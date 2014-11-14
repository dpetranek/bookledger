(ns bookledger.routes.home
  (:require [hiccup.form :refer :all]
            [compojure.core :refer :all]
            [noir.session :as session]
            [noir.validation :as val]
            [noir.response :as resp]
            [bookledger.views.layout :as layout]
            [bookledger.models.db :as db]
            [bookledger.routes.library :refer [date->str]]))


(defn show-books [userid]
  [:div
   [:h1 "Bookledger"]
   [:table
    (for [{:keys [title date authorl authorf series seriesnum rating synopsis comment]}
          (db/read-books userid)]
      [:tr
       [:td title]
       [:td (date->str date)]])]])

(defn home [& userid]
  (layout/common
   (if-let [user (session/get :user)]
     (show-books (:userid user))
     [:h1 "Bookledger"])))

(defroutes home-routes
  (GET "/" [] (home)))
