(ns clnote.views.contents
  (:require [taoensso.timbre :as timbre])
  (:use [hiccup.form]
        [hiccup.element :only (link-to)]))

(defn index []
  [:div {:id "content"}
    [:h1 {:class "text-success"} "Hello Hiccup"]])

(defn not-found []
  [:div
    [:h1 "Page Not Found"]
    [:p "The requested page does not exist."]
    (link-to {:class "btn btn-primary"} "/tasks" "Take me to Home")])

(defn about []
  [:p "This awesome app was created in August 2015"])

(defn task-box [task]
  [:div {:taskId (task :id)
         :class (str "task-box " (if (task :completed) "completed-true"))}
        [:span.handle "+"]
        (check-box
          {:class "task-checkbox"}
          "completed"
          (task :completed) (task :id))
        (str " ")
        (link-to {:class "task-title-link"} "#" (task :title))])

; Converts a list of tasks into a list of list-group-items
(defn task-items [tasks]
  (conj
    (map
      (fn [task]
        [:div.list-group-item.task-item (task-box task)])
      (rest tasks))
    [:div.list-group-item.task-item.active (task-box (first tasks))]))

; Takes as input a parent task and returns the list of list-items of children
; NESTED UNDER a div with id of parent
(defn children-grp [task]
  [:div.drag-container.invisible {:id (str "children-grp-" (task :id))}
    [:div.description (task :description)]
    (if (> (count (task :children)) 0) (task-items (task :children)))])

(defn new-task-form [rank errors]
  [:div
    [:form {:method "POST", :action "/tasks"}  
      [:div.input-group
        (text-field {:class "form-control", :placeholder "Add a task"} "title")
        [:span.input-group-btn
          [:button.btn.btn-default {:type "submit", :value "+"}
            [:i.fa.fa-plus]]]]
      [:input {:type "hidden", :name "rank", :value rank}]]
    (if-not (nil? errors)
              [:span.help-inline (errors :title)])])

(defn tasks [data]
  (let [tasks (data :tasks) errors (data :errors)]
    [:div.panel
      [:div.panel-body
        [:div.row
          [:div.drag-wrapper
            [:div.col-md-3
              (new-task-form 1 errors)
              [:div#left1.drag-container.list-group (task-items tasks)]]
            [:div.col-md-3
              (new-task-form 2 errors)
              [:div#right1.list-group (map #(children-grp %) tasks)]]]]]]))

