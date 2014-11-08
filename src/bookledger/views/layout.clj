(ns bookledger.views.layout
  (:require [hiccup.page :refer [html5 include-css]]
            [hiccup.element :refer [link-to]]
            [hiccup.form :refer :all]
            [noir.session :as session]))


(defn base [& content]
  (html5
    [:head
     [:title "Bookledger"]
     (include-css "/css/screen.css")]
    [:body content]))

(defn nav []
  [:div ""])

(defn login-widget []
  (if-let [user (:username (session/get :user))]
    [:div (link-to "/logout" (str "Logout " user))]
    [:div (link-to "/register" "Register")
     (form-to [:post "/login"]
              (text-field {:placeholder "Username"} "username")
              (password-field {:placeholder "Password"} "pass")
              (submit-button "Log In"))]))

(defn common [& content]
  (base
   [:div {:id "container"}
    [:div.main
       [:div.column {:id "left"} (nav)]
       [:div.column {:id "center"} content]
       [:div.column {:id "right"} (login-widget)]]
    [:div#footer]]))


