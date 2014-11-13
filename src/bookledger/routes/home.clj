(ns bookledger.routes.home
  (:require [hiccup.form :refer :all]
            [compojure.core :refer :all]
            [noir.session :as session]
            [noir.validation :as val]
            [noir.response :as resp]
            [bookledger.views.layout :as layout]
            [bookledger.models.db :as db]))


(defn show-books [userid]
  [:table
   [:tr
    [:td "Title"] [:td "Author"] [:td "Series" ]]
   (for [{:keys [title authorl authorf series seriesnum]} (db/read-books userid)]
     [:tr
      [:td title]
      [:td authorl "," authorf]
      [:td series " - " seriesnum]])])

(defn home [& userid]
  (layout/common
   (if-let [user (session/get :user)]
     (show-books (:userid user))
     [:h1 "Log In"])))

(defroutes home-routes
  (GET "/" [] (home)))
