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

(defn show-library [userid]
  (layout/common
   [:h1 "Bookledger"]
   (form-to [:post "/library"]
            [:div
             (label "author" "Author")
             (text-field {:tabindex 1 :placeholder "Last Name"} "author")]
            [:div
             (label "title" "Title")
             (text-field {:tabindex 2} "title")]
            [:div
             (label "series" "Series")
             (text-field {:tabindex 3} "series")]
            [:div
             (label "seriesnum" "#")
             (text-field {:tabindex 4 :size 2} "seriesnum")]
            (submit-button {:tabindex 5} "Add Book"))
   (show-books (session/get :userid))))


(defn handle-book [author title series seriesnum]
  (try
    (db/add-book {:author author
                  :title title
                  :series series
                  :seriesnum (bigdec seriesnum)
                  :userid (session/get :userid)})
    (resp/redirect "/")
    (catch Exception ex
      ("/"))))

(defroutes library-routes
  (GET "/library" [userid]
       (show-library userid))
  (POST "/library" [author title series seriesnum]
        (handle-book author title series seriesnum)))
