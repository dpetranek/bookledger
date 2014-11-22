(defproject bookledger "0.1.0-SNAPSHOT"
  :description "A ledger for books"
  :url "https://bookledger.herokuapp.com/"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.1.6"]
                 [hiccup "1.0.5"]
                 [ring-server "0.3.1"]
                 [postgresql/postgresql "9.1-901.jdbc4"]
                 [org.clojure/java.jdbc "0.3.6"]
                 [lib-noir "0.8.2"]
                 [clj-time "0.8.0"]]
  :plugins [[lein-ring "0.8.12"]]
  :ring {:handler bookledger.handler/app
         :init bookledger.handler/init
         :destroy bookledger.handler/destroy}
  :uberjar-name "bookledger.jar"
  :min-lein-version "2.0.0"
  :profiles
  {:uberjar {:aot :all}
   :production
   {:ring
    {:open-browser? false,
     :stacktraces? false,
     :auto-reload? false}
    :env {:port 3000
          :db-url "//localhost/bookledger"
          :db-user "bookledger"
          :db-pass "admin"}}
   :dev
   {:dependencies [[ring-mock "0.1.5"] [ring/ring-devel "1.3.1"]]
    :env {:port 3000
          :db-url "//localhost/bookledger"
          :db-user "bookledger"
          :db-pass "admin"}}})
