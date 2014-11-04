(ns bookledger.models.db
  (:require [clojure.java.jdbc :as sql]))

(def db-spec
  {:subprotocol "postgresql"
   :subname "//localhost/bookledger"
   :user "bookledger"
   :password "admin"})

(defn create-user [user]
  (sql/insert! db-spec :users user))

(defn get-user [username]
  (first (sql/query db-spec ["SELECT * FROM users where username = ?" username])))


