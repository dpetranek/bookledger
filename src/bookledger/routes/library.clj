(ns bookledger.routes.library
  (:require [hiccup.form :refer :all]
            [compojure.core :refer :all]
            [noir.session :as session]
            [noir.validation :as val]
            [noir.response :as resp]
            [bookledger.views.layout :as layout]
            [bookledger.models.db :as db]))


(defn show-books [userid]
  [:table
   (for [{:keys [title author series seriesnum]} (db/read-books userid)]
     [:tr
      [:td title]
      [:td author]
      [:td series]
      [:td seriesnum]])])

(defn show-library []
  [:div
   [:h1 (str (:username (session/get :user))) "'s Bookledger"]
   (form-to [:post "/library"]
            [:div
             (label "authorl" "Author")
             (text-field {:tabindex 1 :placeholder "Last Name"} "authorl")]
            [:div
             (text-field {:tabindex 2 :placeholder "First Name"} "authorf")]
            [:div
             (label "title" "Title")
             (text-field {:tabindex 3} "title")]
            [:div
             (label "series" "Series")
             (text-field {:tabindex 4} "series")]
            [:div
             (label "seriesnum" "#")
             (text-field {:tabindex 5 :size 2} "seriesnum")]
            (submit-button {:tabindex 6} "Add Book"))
   (show-books (:userid (session/get :user)))])


(defn handle-book [author title series seriesnum]
  (try
    (db/add-book {:author author
                  :title title
                  :series series
                  :seriesnum (bigdec seriesnum)
                  :userid (:userid (session/get :user))})
    (resp/redirect "/")
    (catch Exception ex
      [:p "Error submitting book"]
      ("/"))))

