(ns clnote.routes.home
  (:require [bouncer.core :as b]
            [bouncer.validators :as v]
            [clnote.layout :as layout]
            [clnote.controllers.task :refer :all]
            [clojure.java.io :as io]
            [compojure.core :refer [defroutes GET POST DELETE ANY]]
            [compojure.route :as route]
            [clnote.db.core :as db]
            [clnote.views.layout :as hic-layout]
            [clnote.views.contents :as contents]
            [ring.util.http-response :refer [ok]]
            [ring.util.response :refer [redirect]]
            [taoensso.timbre :as timbre]))

; TODO Test method if it works
(defn delete-task! [{:keys [params]}]
  (db/delete-task! params))

(defn create-task! [{:keys [params]}]
  (timbre/info "params: " params)
  (let [{:keys [rank title description completed __anti-forgery-token]} params]
    (def pparams
      {:__anti-forgery-token __anti-forgery-token
       :title title
       :description description
       :completed (Boolean/valueOf (if completed completed "false"))
       :rank (Integer/parseInt (if rank rank "1"))})

    (if-let [errors (validate-task pparams)]
      (-> (redirect "/tasks")
          (assoc :flash (assoc pparams :errors errors)))
      (do
        (db/create-task! pparams)  
        (redirect "/tasks")))))

(defn home-page [{:keys [flash]}]
  (let [tasks (db/get-tasks)] 
    (layout/render
      "home.html"
      (merge {:tasks (group-tasks-by-rank tasks)}
        (select-keys flash [:title :description :completed :rank :errors])))))

(defn tasks-page [{:keys [flash]}]
  (hic-layout/application
    "Tasks"
    (contents/tasks (merge {:tasks (db/get-tasks)}
      (select-keys flash [:title :description :completed :rank :errors])))))

(defn about-page []
  (layout/render "about.html"))

(defroutes app-routes
  ; TODO redirect to tasks page
  (GET "/" request
       (home-page request))

  (POST "/" request
        (create-task! request))

  (POST "/tasks" request
        (create-task! request))

  (DELETE "/" request
        (delete-task! request))

  (GET "/about" []
       (hic-layout/application "About CLnote" (contents/about)))

  (GET "/tasks" request
       (tasks-page request))

  (ANY "*" []
    (route/not-found
      (hic-layout/application "Page Not Found" (contents/not-found)))))
