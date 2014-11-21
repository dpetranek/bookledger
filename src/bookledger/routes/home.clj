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
   [:div.library
    (for [{:keys [title date authorl authorf series seriesnum rating synopsis comment]}
          (db/read-books userid)]
      [:div.results
       [:div.summary
        [:div.booktitle title]
        [:div.bookdate (date->str date)]]
       [:div.detail
        [:div "Author: " authorl ", " authorf]
        [:div "Title: " title]
        (when series [:div "Series: " series " " seriesnum])
        [:div "Rating: " rating]
        [:div "Synopsis: " synopsis]
        [:div "Comment: " comment]]])]])

(defn home [& userid]
  (layout/common
   (if-let [user (session/get :user)]
     (show-books (:userid user))
     [:h1 "Bookledger"])))

(defroutes home-routes
  (GET "/" [] (home)))
