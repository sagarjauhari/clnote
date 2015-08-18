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

(defn about []
  [:p "This awesome app was created in August 2015"])

(defn task-box [task]
  [:div {:class (str "task-box " (if (task :completed) "completed-true"))}
        [:span.handle "+"]
        (check-box {:class "task-checkbox"} "completed" (task :completed) (task :id))
        (str " ")
        (link-to {:class "task-title-link"} "#" (task :title))])

(defn task-items [tasks]
  (conj
  (map
    (fn [task]
      [:div.list-group-item.task-item (task-box task)])
    (rest tasks))
  
  [:div.list-group-item.task-item.active (task-box (first tasks))]
  ))

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
              ;; Show children of first task
              [:div#right1.drag-container.list-group (task-items ((first tasks) :children))]]]]]]))

