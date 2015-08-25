(ns clnote.routes.home
  (:require [bouncer.core :as b]
            [bouncer.validators :as v]
            [clnote.layout :as layout]
            [clnote.controllers.task :refer :all]
            [clojure.java.io :as io]
            [compojure.core :refer [defroutes context ANY DELETE GET POST PUT]]
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
       :parent_id (if parentId (Integer/parseInt parentId) nil)})

    (if-let [errors (validate-task pparams)]
      (-> (redirect "/tasks")
          (assoc :flash (assoc pparams :errors errors)))
      (do
        (db/create-task! pparams)  
        (redirect "/tasks")))))

(defn add-children [tasks, task]
  (let [children (filter #(= (task :id) (% :parent_id)) tasks)]
    (if (empty? children)
      ; Base case
      task
      ; Recursive case
      (merge task {:children (map #(add-children tasks %) children)}))))

(defn task-tree []
  (let [tasks (db/get-tasks)]
    (add-children tasks {:id nil})))

(defn task-tree-2level []
  (let [tasks (db/get-tasks)]
    (map
      (fn [parent] (merge
                     parent
                     {:children
                      (filter
                        #(and (= (% :rank) 2) (= (% :parent_id) (parent :id)))
                        tasks)}))
      (filter #(= (% :rank) 1) tasks))))

(defn tasks-page [{:keys [flash]}]
  (hic-layout/application
    "Tasks"
    (contents/tasks (merge {:tasks (task-tree-2level)}
      (select-keys flash [:title :description :completed :rank :errors])))))

(defn about-page []
  (layout/render "about.html"))

(defroutes app-routes
  ; TODO redirect to tasks page
  (GET "/" request (tasks-page request))
  (DELETE "/" request (delete-task! request))
  (POST "/" request (create-task! request))

  (context "/tasks" [] (defroutes tasks-routes
    (GET "/" request (tasks-page request))
    (POST "/" request (create-task! request))

    (context "/:id" [id] (defroutes task-routes
      (PUT "/" request (update-task request))))))

  (GET "/about" []
       (hic-layout/application "About CLnote" (contents/about)))

  (ANY "*" []
    (route/not-found
      (hic-layout/application "Page Not Found" (contents/not-found)))))
