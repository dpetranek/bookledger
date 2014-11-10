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
   [:tr
    [:td "Title"] [:td "Author"] [:td "Series" ]]
   (for [{:keys [title authorl authorf series seriesnum]} (db/read-books userid)]
     [:tr
      [:td title]
      [:td authorl "," authorf]
      [:td series " - " seriesnum]])])

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


(defn handle-book [authorl authorf title series seriesnum]
  (try
    (db/add-book {:authorl authorl
                  :authorf authorf
                  :title title
                  :series series
                  :seriesnum (bigdec seriesnum)
                  :userid (:userid (session/get :user))})
    (println (str authorl authorf title series seriesnum))
    (resp/redirect "/")
    (catch Exception ex
      [:p "Error submitting book"]
      ("/"))))

(defn check-param [param-key map]
  (if (param-key map)
    (param-key map)
    nil))

(defn handle-request [request]
  (db/add-book {:authorl (:authorl request)
                :authorf (:authorf request)
                :title (:title request)
                :series (checkparam :series request)
                :seriesnum (if (check-param :seriesnum request)
                             (bigdec (:seriesnum request))
                             nil)
                :userid (:userid (session/get :user))})
  (resp/redirect "/"))


