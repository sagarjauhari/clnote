(ns clnote.routes.home
  (:require [bouncer.core :as b]
            [bouncer.validators :as v]
            [clnote.layout :as layout]
            [clojure.java.io :as io]
            [compojure.core :refer [defroutes GET POST]]
            [clnote.db.core :as db]
            [ring.util.http-response :refer [ok]]
            [ring.util.response :refer [redirect]]
            [taoensso.timbre :as timbre]))

(defn validate-task [params]
  (first
    (b/validate
      params
      :title [v/required [v/min-count 3]]
      :completed v/required
      :rank v/required)))

(defn create-task! [{:keys [params]}]
  (if-let [errors (validate-task params)]
    (-> (redirect "/")
        (assoc :flash (assoc params :errors errors)))
    (do
      (db/create-task! (assoc params :timestamp (java.util.Date.)))
      (redirect "/"))))

(defn home-page [{:keys [flash]}]
  (layout/render
   "home.html"
   (merge {:tasks (db/get-tasks)}
          (select-keys flash [:title :description :completed :rank :errors]))))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" request
       (timbre/info "Got request" request)
       (home-page request))
  (POST "/" request 
        (timbre/info "Got request" request)
        (create-task! request))
  (GET "/about" [] (about-page)))

