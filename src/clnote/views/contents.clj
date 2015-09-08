(ns clnote.views.contents
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

(defn task-item
  "Given a task, create a task box for it.
   Also used by post request to append new item to existing list"
  [task]
  [:div.list-group-item.task-item
    [:div {:taskId (task :id)
         :class (str "task-box " (if (task :completed) "completed-true"))}
        [:span.handle "+"]
        (check-box {:class "task-checkbox"}
          (str "task-checkbox-" (task :id))
          (task :completed)
          (task :id))
        (str " ")
        (link-to {:class "task-title-link"} "#" (task :title))]])

(defn task-items
  "Converts a list of tasks into a list of list-group-items"
  [tasks]
  (map (fn [task] (task-item task)) tasks))

(defn new-task-line
  "Add new task using AJAX"
  [rank parent]
  [:div.new-task-line
    (text-field
      {:class "form-control"
       :placeholder "New task"
       :parentId (parent :id)
       :collectionId (parent :collection_id)
       :rank rank}
      "title")])

(defn children-grp
  "Takes as input a parent task and returns the list of list-items of children
  NESTED UNDER a div with id of parent"
  [task tasks]
  (let [children (filter #(= (% :parent_id) (task :id)) tasks)]
    [:div.drag-container.invisible {:id (str "children-grp-" (task :id))}
      [:h4 (task :title)]
      [:div.description (task :description)]
      (if (> (count children) 0) (task-items children))
      (new-task-line (inc (task :rank)) task)]))

(defn tasks [data]
  (let [tasks    (data :tasks)
        errors   (data :errors)
        coll-id  (data :coll-id)]
    [:div.panel
      [:div.panel-body
        [:div.row
          [:div.drag-wrapper
            ; rank 1
            [:div#col-rank-1.col-md-3
              [:h4 "Tasks"]
              [:div#left1.list-group
                (children-grp {:id nil, :rank 0, :collection_id coll-id} tasks)]]
            ; rank 2
            [:div#col-rank-2.col-md-3
              [:div.list-group
                (map
                  #(children-grp % tasks)
                  (filter #(= (% :rank) 1) tasks))]]
            ; rank 3
            [:div#col-rank-3.col-md-3
              [:div.list-group
                (map
                  #(children-grp % tasks)
                  (filter #(= (% :rank) 2) tasks))]]
            ; rank 4
            [:div#col-rank-4.col-md-3
              [:div.list-group
                (map
                  #(children-grp % tasks)
                  (filter #(= (% :rank) 3) tasks))]]]]]]))
