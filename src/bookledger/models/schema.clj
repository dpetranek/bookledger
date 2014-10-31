(ns bookledger.models.schema
  (:require [bookledger.models.db :refer :all]
            [clojure.java.jdbc :as sql]))

(defn create-users-table []
  (sql/with-connection db
    (sql/create-table
     :users
     [:userid :integer :serial "PRIMARY KEY"]
     [:username "varchar(50)"]
     [:pass "varchar(100)"])))

