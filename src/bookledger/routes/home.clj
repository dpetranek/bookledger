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
        [:div [:div.label "Author: "] [:div.data authorl ", " authorf]]
        [:div [:div.label "Title: "] [:div.data title]]
        (when series [:div [:div.label "Series: "]
                      [:div.data series " " seriesnum]])
        [:div [:div.label "Rating: "] [:div.data rating]]
        [:div [:div.label "Synopsis: "] [:div.data synopsis]]
        [:div [:div.label "Comment: "] [:div.data comment]]]])]])

(defn home [& userid]
  (layout/common
   (if-let [user (session/get :user)]
     (show-books (:userid user))
     [:h1 "Bookledger"])))

(defroutes home-routes
  (GET "/" [] (home)))
