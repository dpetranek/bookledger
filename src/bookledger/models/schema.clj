(ns bookledger.models.schema
  (:require [bookledger.models.db :refer :all]
            [clojure.java.jdbc :as sql]))

(defn create-users-table []
  (sql/db-do-commands db-spec
                      (sql/create-table-ddl
                       :users
                       [:userid :serial "PRIMARY KEY"]
                       [:username "varchar(50) UNIQUE"]
                       [:pass "varchar(100)"]
                       [:utags :text])))

(defn create-books-table []
  (sql/db-do-commands db-spec
                      (sql/create-table-ddl
                       :books
                       [:bookid :serial "PRIMARY KEY"]
                       [:userid :int]
                       [:title "varchar(100)"]
                       [:authorl "varchar(50)"]
                       [:authorf "varchar(50)"]
                       [:series "varchar(100)"]
                       [:seriesnum :int]
                       [:tags :text]
                       [:synopsis :text])))

(defn create-reviews-table []
  (sql/db-do-commands db-spec
                      (sql/create-table-ddl
                       :reviews
                       [:reviewid :serial "PRIMARY KEY"]
                       [:bookid :int]
                       [:rating :int]
                       [:date :timestamp]
                       [:comment :text])))



