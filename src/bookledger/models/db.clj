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
  (first (sql/query db-spec ["SELECT * FROM users WHERE username = ?"
  username])))

(defn add-book [book]
  (sql/insert! db-spec :books book))

(defn read-books []
  (sql/query db-spec ["SELECT * FROM books"]))


