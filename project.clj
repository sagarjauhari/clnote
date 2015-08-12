(defproject clnote "0.1.0-SNAPSHOT"

  :description "FIXME: write description"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [selmer "0.8.2"]
                 [com.taoensso/timbre "4.0.2"]
                 [com.taoensso/tower "3.0.2"]
                 [hiccup "1.0.5"]
                 [markdown-clj "0.9.67"]
                 [environ "1.0.0"]
                 [compojure "1.4.0"]
                 [ring/ring-defaults "0.1.5"]
                 [ring/ring-session-timeout "0.1.0"]
                 [ring "1.4.0"]
                 [metosin/ring-middleware-format "0.6.0"]
                 [metosin/ring-http-response "0.6.3"]
                 [bouncer "0.3.3"]
                 [prone "0.8.2"]
                 [org.clojure/tools.nrepl "0.2.10"]
                 [migratus "0.8.2"]
                 [org.clojure/java.jdbc "0.3.7"]
                 [instaparse "1.4.1"]
                 [yesql "0.5.0-rc3"]
                 [org.postgresql/postgresql "9.3-1102-jdbc41"]
                 [http-kit "2.1.19"]]

  :npm {:dependencies [[dragula "2.1.0"]]}

  ; :offline? true

  :min-lein-version "2.5.1"
  :uberjar-name "clnote.jar"
  :jvm-opts ["-server"]

  :main clnote.core
  :migratus {:store :database}

  :plugins [[lein-environ "1.0.0"]
            [lein-ancient "0.6.5"]
            [migratus-lein "0.1.5"]
            [lein-npm "0.6.0"]
            [lein-ring "0.9.6"]]
  :ring  {:handler clnote.handler/app}

  :profiles
  {:uberjar {:omit-source true
             :env {:production true}}
   :dev {:dependencies [[ring/ring-devel "1.4.0"]
                        [pjstadig/humane-test-output "0.7.0"]
                        [org.clojure/tools.namespace "0.2.11"]]

         :source-paths ["dev"]
         :repl-options {:init-ns clnote.core}
         :injections [(require 'pjstadig.humane-test-output)
                      (pjstadig.humane-test-output/activate!)]
         :env {:dev true}}})
