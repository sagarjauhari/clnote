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

(defn task-box
  "Given a task, create a task box for it"
  [task]
  [:div {:taskId (task :id)
         :class (str "task-box " (if (task :completed) "completed-true"))}
        [:span.handle "+"]
        (check-box
          {:class "task-checkbox"}
          "completed"
          (task :completed) (task :id))
        (str " ")
        (link-to {:class "task-title-link"} "#" (task :title))])

(defn task-items
  "Converts a list of tasks into a list of list-group-items"
  [tasks]
  (map
    (fn [task] [:div.list-group-item.task-item (task-box task)])
    tasks))

(defn new-task-line
  "TODO Use AJAX to post"
  [rank parent]
  [:div
    [:form {:method "POST", :action "/tasks"}  
      [:div.input-group
        (text-field {:class "form-control", :placeholder "Add a task"} "title")
        [:span.input-group-btn
          [:button.btn.btn-default {:type "submit", :value "+"}
            [:i.fa.fa-plus]]]]
      [:input {:type "hidden", :name "rank", :value rank}]
      (if
        (some? parent)
        [:input {:type "hidden", :name "parentId", :value (parent :id)}])]])

(defn children-grp
  "Takes as input a parent task and returns the list of list-items of children
  NESTED UNDER a div with id of parent"
  [task]
  [:div.drag-container.invisible {:id (str "children-grp-" (task :id))}
    [:h4 (task :title)]
    [:div.description (task :description)]
    (if (> (count (task :children)) 0) (task-items (task :children)))
    (new-task-line 2 task)])

(defn tasks [data]
  (let [tasks (data :tasks) errors (data :errors)]
    [:div.panel
      [:div.panel-body
        [:div.row
          [:div.drag-wrapper
            [:div.col-md-3
              [:h4 "Tasks"]
              [:div#left1.drag-container.list-group (task-items tasks)]
              (new-task-line 1 nil)]
            [:div.col-md-3
              [:div.list-group (map #(children-grp %) tasks)]]]]]]))

