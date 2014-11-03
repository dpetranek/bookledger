(ns bookledger.models.schema
  (:require [bookledger.models.db :refer :all]
            [clojure.java.jdbc :as sql]))

(defn create-users-table []
  (sql/db-do-commands db
                      (sql/create-table-ddl
                       :users
                       [:userid :serial "PRIMARY KEY"]
                       [:username "varchar(50)"]
                       [:pass "varchar(100)"])))

