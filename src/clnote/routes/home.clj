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

(defn validate-message [params]
  (first
    (b/validate
      params
      :name v/required
      :message [v/required [v/min-count 10]])))

(defn save-message! [{:keys [params]}]
  (if-let [errors (validate-message params)]
    (-> (redirect "/guestbook")
        (assoc :flash (assoc params :errors errors)))
    (do
      (timbre/info (assoc params :timestamp (java.util.Date.)))
      (db/save-message!
        (assoc params :timestamp (java.util.Date.)))
      (redirect "/guestbook"))))

(defn guestbook [{:keys [flash]}]
  (layout/render
    "guestbook.html"
   (merge {:messages (db/get-messages)}
          (select-keys flash [:name :message :errors]))))

(defn validate-task [params]
  (first
    (b/validate
      params
      :title [v/required [v/min-count 3]]
      :completed [v/required v/boolean]
      :rank [v/required v/number v/positive])))

(defn create-task! [{:keys [params]}]
  (timbre/info "params: " params)
  (let [{:keys [rank title description completed __anti-forgery-token]} params]
    (def pparams
      {:__anti-forgery-token __anti-forgery-token
       :title title
       :description description
       :completed (Boolean/valueOf completed)
       :rank (Integer/parseInt rank)})

    (if-let [errors (validate-task pparams)]
      (-> (redirect "/")
          (assoc :flash (assoc pparams :errors errors)))
      (do
        (db/create-task! pparams)  
        (redirect "/")))))

(defn home-page [{:keys [flash]}]
  (layout/render
   "home.html"
   (merge {:tasks (db/get-tasks)}
          (select-keys flash [:title :description :completed :rank :errors]))))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" request
       (home-page request))

  (POST "/" request
        (create-task! request))

  (GET "/about" []
       (about-page))

  (GET "/guestbook" request
       (guestbook request))

  (POST "/guestbook" request
       (save-message! request)))
