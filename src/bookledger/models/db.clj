(ns bookledger.models.db
  (:require [clojure.java.jdbc :as sql]))

(def db
  {:subprotocol "postgresql"
   :subname "//localhost/bookledger"
   :user "bookledger"
   :password "admin"})
