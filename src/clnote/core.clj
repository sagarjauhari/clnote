(ns clnote.core
  (:require [clnote.handler :refer [app init destroy]]
            [org.httpkit.server :as http-kit]
            [clnote.db.migrations :as migrations]
            [taoensso.timbre :as timbre]
            [environ.core :refer [env]])
  (:gen-class))

(defonce server (atom nil))

(defn parse-port [[port]]
  (Integer/parseInt (or port (env :port) "5001")))

(defn start-server [port]
  (init)
  (reset! server
          (http-kit/run-server app {:port port})))

(defn stop-server []
  (when @server
    (destroy)
    (@server :timeout 100)
    (reset! server nil)))

(defn start-app [args]
  (let [port (parse-port args)]
    (.addShutdownHook (Runtime/getRuntime) (Thread. stop-server))
    (timbre/info "server is starting on port " port)
    (start-server port)))

(defn -main [& args]
  (cond
    (some #{"migrate" "rollback"} args) (migrations/migrate args)
    :else (start-app args)))
  
(defn handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Hello World."})
