(ns bookledger.models.schema
  (:require [bookledger.models.db :refer :all]
            [clojure.java.jdbc :as sql]))

(defn create-users-table []
  (sql/db-do-commands db-spec
                      (sql/create-table-ddl
                       :users
                       [:userid :serial "PRIMARY KEY"]
                       [:username "varchar(50) UNIQUE"]
                       [:pass "varchar(100)"])))

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
                       [:seriesnum :int])))

(defn create-reviews-table []
  (sql/db-do-commands db-spec
                      (sql/create-table-ddl
                       :reviews
                       [:reviewid :serial "PRIMARY KEY"]
                       [:bookid :int]
                       [:rating :int]
                       [:date :timestamp]
                       [:synopsis :text]
                       [:comment :text])))

(defn create-test-table []
  (sql/db-do-commands db-spec
                      (sql/create-table-ddl
                       :test
                       [:bookid :serial]
                       [:userid :int]
                       [:title "varchar(100)"]
                       [:authorl "varchar(50)"]
                       [:authorf "varchar(50)"]
                       [:series "varchar(100)"]
                       [:seriesnum :int]
                       [:constraint :u_book "PRIMARY KEY(userid,title,authorl,authorf)"])))


