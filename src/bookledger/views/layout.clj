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

(defn sidebar []
  [:div ""])

(defn login-widget []
  (if-let [user (:username (session/get :user))]
    [:nav
     (link-to "/" "Bookledger")
     (link-to "/library" "Add Book")
     (link-to "/logout" (str "Logout " user))]
    [:div (link-to "/register" "Register")
     (form-to [:post "/login"]
              (text-field {:placeholder "Username"} "username")
              (password-field {:placeholder "Password"} "pass")
              (submit-button "Log In"))]))

(defn common [& content]
  (base
   [:div {:id "container"}
    [:div#header (login-widget)]
    [:div.main
       [:div.column {:id "left"} (sidebar)]
       [:div.column {:id "center"} content]
       [:div.column {:id "right"} (sidebar)]]
    [:div#footer]]))


