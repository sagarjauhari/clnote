(ns clnote.routes.home
  (:require [bouncer.core :as b]
            [bouncer.validators :as v]
            [clnote.controllers.task :refer :all]
            [clojure.java.io :as io]
            [compojure.core :refer [defroutes context ANY DELETE GET POST PUT]]
            [compojure.route :as route]
            [clnote.db.core :as db]
            [clnote.views.layout :as layout]
            [clnote.views.contents :as contents]
            [ring.util.http-response :refer [ok]]
            [ring.util.response :refer [redirect]]
            [taoensso.timbre :as timbre]))

; TODO Test method if it works
(defn delete-task! [{:keys [params]}]
  (db/delete-task! params))

(defn update-task [{:keys [params]}]
  (def pparams
    {:id (Integer/parseInt (params :id))
     :completed (Boolean/valueOf(params :completed))})
  (db/update-task-completed! pparams)
  (str "Task updated"))

(defn create-task! [{:keys [params]}]
  (let [{:keys [rank parentId title description completed __anti-forgery-token]} params]
    (def pparams
      {:__anti-forgery-token __anti-forgery-token
       :title title
       :description description
       :completed (Boolean/valueOf (if completed completed "false"))
       :rank (Integer/parseInt (if rank rank "1"))
       :parent_id
        (if
          (clojure.string/blank? parentId)
          nil
          (Integer/parseInt parentId))})

    (if-let [errors (validate-task pparams)]
      (-> (redirect "/tasks")
          (assoc :flash (assoc pparams :errors errors)))
      (do
        (db/create-task! pparams)  
        (redirect "/tasks")))))

; TODO: Send errors to notifier
(defn tasks-page [{:keys [flash]}]
  (layout/application
    "Tasks"
    (contents/tasks (merge {:tasks (db/get-tasks)}
      (select-keys flash [:title :description :completed :rank :errors])))))

(defroutes app-routes
  (GET "/" request (tasks-page request))
  (DELETE "/" request (delete-task! request))
  (POST "/" request (create-task! request))

  (context "/tasks" [] (defroutes tasks-routes
    (GET "/" request (tasks-page request))
    (POST "/" request (create-task! request))

    (context "/:id" [id] (defroutes task-routes
      (PUT "/" request (update-task request))))))

  (context "/angular" [] (defroutes tasks-routes
    (GET "/" request (tasks-page request))
    (POST "/" request (create-task! request))

    (context "/:id" [id] (defroutes task-routes
      (PUT "/" request (update-task request))))))

  (ANY "*" []
    (route/not-found
      (layout/application "Page Not Found" (contents/not-found)))))
