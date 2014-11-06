(ns bookledger.routes.home
  (:require [hiccup.form :refer :all]
            [compojure.core :refer :all]
            [noir.session :as session]
            [noir.validation :as val]
            [noir.response :as resp]
            [bookledger.views.layout :as layout]
            [bookledger.models.db :as db]))


(defn show-books []
  [:table
   (for [{:keys [title author series seriesnum]} (db/read-books)]
     [:tr
      [:td title]
      [:td author]
      [:td series]
      [:td seriesnum]])])

(defn home [& userid]
  (layout/common
   (form-to [:post "/"]
            [:table
             [:tr
              [:td
               (label "author" "Author")
               (text-field {:tabindex 1
                            :placeholder "Last Name"} "author")]]
             [:tr
              [:td
               (label "title" "Title")
               (text-field {:tabindex 2} "title")]]
             [:tr
              [:td
               (label "series" "Series")
               (text-field {:tabindex 3} "series")]
              [:td
               (label "seriesnum" "#")
               (text-field {:tabindex 4 :size 2} "seriesnum")]]]
            (submit-button {:tabindex 5} "Add Book"))
   (show-books)))

(defn handle-book [author title series seriesnum]
  (try
    (db/add-book {:author author
                  :title title
                  :series series
                  :seriesnum (bigdec seriesnum)})
    (resp/redirect "/")
    (catch Exception ex
      ("/"))))


(defroutes home-routes
  (GET "/" [] (home))
  (POST "/" [author title series seriesnum]
        (handle-book author title series seriesnum)))
