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
    (link-to {:class "btn btn-primary"} "/" "Take me to Home")])

(defn about []
  [:p "This awesome app was created in August 2015"])

(defn tasks [data]
  (let [tasks (data :tasks) errors (data :errors)]
    (timbre/info "params: " data)
    [:div.row
      [:div.span12 [:h3 "My Tasks"]]

      ; Add task
      [:div.row
        [:div.col-md-3
          [:form {:method "POST", :action "/tasks"}  
            [:div.input-group
              (text-field {:class "form-control", :placeholder "Add a task"} "title")
              [:span.input-group-btn
                [:button.btn.btn-default {:type "submit", :value "+"}
                  [:i.fa.fa-plus]]]
              [:input {:type "hidden", :name "rank", :value "1"}]]]]
        ; Validation errors
        (if-not (nil? errors)
          [:span.help-inline (errors :title)])]

      ; Task columns
      [:div.row
        [:div.drag-wrapper
          [:div.col-md-3
            [:div#left1.drag-container.list-group
              (map (fn [task]
                [:div {:class (str "list-group-item completed-" (task :completed))}
                  [:span.handle "+"]
                  (check-box {:value "t"} "completed" (task :completed) "completed" ) (task :title)
                  ])
                      (filter #(= (% :rank) 1) tasks))]]

          [:div.col-md-3
            [:div#right1.drag-container.list-group
              (map (fn [task]
                [:div {:class (str "list-group-item completed-" (task :completed))}
                  [:span.handle "+"]
                  (check-box {:value "t"} "completed" (task :completed) "completed" ) (task :title)
                  ])
                      (filter #(= (% :rank) 2) tasks))]]]]]))
