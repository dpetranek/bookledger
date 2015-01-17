(ns bookledger.routes.library
  (:require [hiccup.form :refer :all]
            [compojure.core :refer :all]
            [noir.session :as session]
            [noir.validation :as val]
            [noir.response :as resp]
            [bookledger.views.layout :as layout]
            [bookledger.models.db :as db]
            [bookledger.util :refer :all]))


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
            [:div.tags
             (label "tags" "Tags")
             (text-field {:tabindex 7} "tags")]
            [:div.date
             (label "date" "Date")
             (text-field {:tabindex 8 :placeholder "MM/dd/yyyy"} "date")]
            [:div.synopsis
             (label "synopsis" "Synopsis")
             (text-area {:tabindex 9 :rows 10 :cols 100} "synopsis")]
            [:div.comment
             (label "comment" "Comment")
             (text-area {:tabindex 10 :rows 10 :cols 100} "comment")]
            (submit-button {:tabindex 11 :class "button"} "Add Book"))))

(defn handle-library [request]
  (if-let [bookid (db/dup? request (:userid (session/get :user)))]
    (try
      (db/update-book {:series    (:series request)
                       :seriesnum (char->int (:seriesnum request))
                       :synopsis  (:synopsis request)}
                      bookid)
      (db/add-review {:bookid bookid
                      :rating (char->int (:rating request))
                      :date   (when (:date request)
                                (str->date (:date request)))
                      :comment (:comment request)})
      (resp/redirect "/")
      (catch Exception ex
        (str "There was an error updating your book. " (.getMessage ex))))

    (try
      (db/add-book {:authorl (:authorl request)
                    :authorf (:authorf request)
                    :title   (:title request)
                    :series  (:series request)
                    :seriesnum  (char->int (:seriesnum request))
                    :synopsis (:synopsis request)
                    :userid  (:userid (session/get :user))})
      (let [bookid (first (db/get-bookid))]
        (db/add-review {:bookid (:max bookid)
                        :rating (char->int (:rating request))
                        :date   (when (:date request)
                                  (str->date (:date request)))
                        :comment (:comment request)}))
      (resp/redirect "/")
      (catch Exception ex
        (str "There was an error adding your book. " (.getMessage ex))))))

(defroutes library-routes
  (GET "/library" [] (add-book))
  (POST "/library" [:as request]
        (handle-library (clean-request (:params request)))))


