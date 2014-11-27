(ns bookledger.routes.library
  (:require [hiccup.form :refer :all]
            [compojure.core :refer :all]
            [noir.session :as session]
            [noir.validation :as val]
            [noir.response :as resp]
            [clj-time.core :as t]
            [clj-time.format :as tf]
            [clj-time.coerce :as tc]
            [bookledger.views.layout :as layout]
            [bookledger.models.db :as db]))


(defn blank? [s]
  (if (not= s "")
    s
    nil))

(defn char->int [c]
  (if (blank? c)
    (bigdec c)
    nil))

(def bformatter (tf/formatter "MM/dd/yyyy"))

(defn str->date [s]
  (->> s
       (tf/parse bformatter)
       (tc/to-sql-date)))

(defn date->str [d]
  (->> d
       (tc/from-sql-date)
       (tf/unparse bformatter)))

(defn add-book []
  (layout/common
   (form-to [:post "/library"]
            [:div.authorl
             (label "authorl" "Author")
             (text-field {:tabindex 1 :placeholder "Last Name"} "authorl")]
            [:div.authorf
             (label "authorf" ".")
             (text-field {:tabindex 2 :placeholder "First Name"} "authorf")]
            [:div.title
             (label "title" "Title")
             (text-field {:tabindex 3} "title")]
            [:div.series
             (label "series" "Series")
             (text-field {:tabindex 4} "series")]
            [:div.seriesnum
             (label "seriesnum" "#")
             (text-field {:tabindex 5 :size 2} "seriesnum")]
            [:div.rating
             (label "rating" "Rating")
             (text-field {:tabindex 6 :size 2} "rating")]
            [:div.date
             (label "date" "Date")
             (text-field {:tabindex 7 :placeholder "MM/dd/yyyy"} "date")]
            [:div.synopsis
             (label "synopsis" "Synopsis")
             (text-area {:tabindex 8 :rows 10 :cols 100} "synopsis")]
            [:div.comment
             (label "comment" "Comment")
             (text-area {:tabindex 9 :rows 10 :cols 100} "comment")]
            (submit-button {:tabindex 10} "Add Book"))))

(defn handle-library [request]
  (if-let [bookid (db/dup? request (:userid (session/get :user)))]
    (try
      (db/update-book {:series (blank? (:series request))
                       :seriesnum (char->int (:seriesnum request))
                       :synopsis (blank? (:synopsis request))}
                      bookid)
      (db/add-review {:bookid bookid
                      :rating (char->int (:rating request))
                      :date (when (blank? (:date request))
                              (str->date (:date request)))
                      :comment (blank? (:comment request))})
      (resp/redirect "/"))

    (try (db/add-book {:authorl (blank? (:authorl request))
                       :authorf (blank? (:authorf request))
                       :title (blank? (:title request))
                       :series (blank? (:series request))
                       :seriesnum  (char->int (:seriesnum request))
                       :synopsis (blank? (:synopsis request))
                       :userid (:userid (session/get :user))})
         (let [bookid (first (db/get-bookid))]
           (db/add-review {:bookid (:max bookid)
                           :rating (char->int (:rating request))
                           :date (when (blank? (:date request))
                                   (str->date (:date request)))
                           :comment (blank? (:comment request))}))
         (resp/redirect "/"))))

(defroutes library-routes
  (GET "/library" [] (add-book))
  (POST "/library" [:as request]
        (handle-library (:params request))))


