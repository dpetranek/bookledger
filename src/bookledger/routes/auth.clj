(ns bookledger.routes.auth
  (:require [hiccup.form :refer :all]
            [compojure.core :refer :all]
            [bookledger.routes.home :refer :all]
            [bookledger.views.layout :as layout]
            [bookledger.models.db :as db]
            [noir.session :as session]
            [noir.response :as resp]
            [noir.validation :as val]
            [noir.util.crypt :as crypt]))


;; helper functions
(defn error-item [[error]]
  [:div.error error])

(defn control [username label field]
  (list
   (val/on-error username error-item)
   label field
   [:br]))

(defn format-error [username ex]
  (cond
   (and (instance? org.postgresql.util.PSQLException ex)
        (= 0 (.getErrorCode ex)))
   (str "That username is not available!")
   :else
   "An error has occured while processing the request."))

(defn valid? [username pass pass1]
  (val/rule (val/has-value? username)
            [:username "Invalid username"])
  (val/rule (val/min-length? pass 6)
            [:pass "Password must be at least 6 characters"])
  (val/rule (= pass pass1)
            [:pass "Passwords don't match"])
  (not (val/errors? :username :pass :pass1)))


;; registration
(defn registration-page [& [username]]
  (layout/base
   (form-to [:post "/register"]
            (control :username
                     (label "username" "Username")
                     (text-field {:tabindex 1} "username" username))
            (control :pass
                     (label "pass" "Password")
                     (password-field {:tabindex 2} "pass"))
            (control :pass1
                     (label "pass1" "Verify Password")
                     (password-field {:tabindex 3} "pass1"))
            (submit-button {:tabindex 4} "Create Account"))))

(defn handle-registration [username pass pass1]
  (if (valid? username pass pass1)
    (try
      (db/create-user {:username username :pass (crypt/encrypt pass)})
      (session/put! :user username)
      (resp/redirect "/")
      (catch Exception ex
        (val/rule false [:username (format-error username ex)])
        (registration-page)))
    (registration-page username)))


;; login
(defn handle-login [username pass]
  (let [user (db/get-user username)]
    (if (and user (crypt/compare pass (:pass user)))
      (session/put! :user username)))
  (resp/redirect "/"))

;; logout
(defn handle-logout []
  (session/clear!)
  (resp/redirect "/"))


;;routes
(defroutes auth-routes
  (GET "/register" []
       (registration-page))
  (POST "/register" [username pass pass1]
        (handle-registration username pass pass1))
  (POST "/login" [username pass]
        (handle-login username pass))
  (GET "/logout" []
       (handle-logout)))

