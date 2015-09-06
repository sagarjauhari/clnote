(ns clnote.routes.home
  (:require [bouncer.core :as b]
            [bouncer.validators :as v]
            [clnote.controllers.task :refer :all]
            [clojure.java.io :as io]
            [clojure.data.json :as json]
            [compojure.core :refer [defroutes context ANY DELETE GET POST PUT]]
            [compojure.coercions :refer [as-int]]
            [compojure.route :as route]
            [clnote.db.core :as db]
            [clnote.views.layout :as layout]
            [clnote.views.contents :as contents]
            [hiccup.core :refer [html]]
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

(defn create-task! [{:keys [params]} coll-id]
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
          (Integer/parseInt parentId))
        :coll_id coll-id})

    (if-let [errors (validate-task pparams)]
      ; (-> (redirect (str "/" coll-id "/tasks"))
      ;     (assoc :flash (assoc pparams :errors errors)))
      (str {:errors errors})
      (do
        ; TODO Handle DB error if task cannot be created
        (if-let [new-task (db/create-task<! pparams)]
          (json/write-str
            {:taskItem (html (contents/task-item new-task))
             :childrenGrp (html (contents/children-grp new-task {}))}))))))

; TODO: Send errors to notifier
(defn tasks-page [coll-id flash]
  (layout/application
    "Tasks"
    (contents/tasks
      (merge
        {:tasks
         (if
           (> coll-id 0)
           (db/get-tasks {:coll_id coll-id})
           (db/get-all-tasks))
         :colls (db/get-collections)
         :coll-id coll-id}
        (select-keys flash [:title :description :completed :rank :errors])))))

(defroutes app-routes
  (GET "/" request (redirect "/0/tasks"))
  (DELETE "/" request (delete-task! request))
  (POST "/" request (create-task! request))

  (PUT "/tasks/:id" request (update-task request))

  (context "/:coll-id/tasks" [coll-id :<< as-int] (defroutes tasks-routes
    (GET "/" [flash] (tasks-page coll-id flash))
    (POST "/" request (create-task! request coll-id))))

  (ANY "*" []
    (route/not-found
      (layout/application "Page Not Found" (contents/not-found)))))
