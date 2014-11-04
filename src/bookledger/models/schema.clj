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


