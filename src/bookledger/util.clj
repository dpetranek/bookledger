(ns bookledger.util
  (:require [clj-time.core :as t]
            [clj-time.format :as tf]
            [clj-time.coerce :as tc]
            [clojure.string :as s]))

(defn char->int [c]
  (if (s/blank? c)
    (bigdec c)
    nil))

(def bformatter (tf/formatter "MM/dd/yyyy"))

(defn str->date [s]
  (->> s
       (tf/parse bformatter)
       (tc/to-sql-date)))

(defn date->str [d]
  (->> d
       (tc/from-sql-date)
       (tf/unparse bformatter)))

(defn clean-request [request]
  (let [keys (keys request)
        vals (vals request)]
    (zipmap keys (map #(if (= "" %) nil %) vals))))
