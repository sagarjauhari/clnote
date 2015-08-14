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
    (link-to {:class "btn btn-primary"} "/" "Take me to Home")])

(defn about []
  [:p "This awesome app was created in August 2015"])

; TODO Error handling
(defn tasks [data]
  (let [tasks (data :tasks)]
    [:div.row
      [:div.span12 [:h3 "My Tasks"]]

      [:div.row
        ; Add task
        [:div.col-md-3
          [:form {:method "POST", :action "/"}  
            [:div.input-group
              (text-field {:class "form-control", :placeholder "Add a task"} "title")
              [:span.input-group-btn
                [:button.btn.btn-default {:type "submit", :value "+"}
                  [:i.fa.fa-plus]]]
              [:input {:type "hidden", :name "rank", :value "1"}]]]]]

      [:div.row
        [:div.drag-wrapper
          [:div.col-md-3
            [:div#left1.drag-container
              (map (fn [task]
                [:div {:class (str "task-box completed-" (task :completed))}
                  [:span.handle "+"]
                  [:input {:type "checkbox" :name "t" :value "t"} (task :title)]
                  [:a.task-delete-link {:href "#"}
                    [:i.fa.fa-times]]])
                      (filter #(= (% :rank) 1) tasks))]]
          [:div.col-md-3
            [:div#right1.drag-container
              (map (fn [task]
                [:div {:class (str "task-box completed-" (task :completed))}
                  [:span.handle "+"]
                  [:input {:type "checkbox" :name "t" :value "t"} (task :title)]
                  [:a.task-delete-link {:href "#"}
                    [:i.fa.fa-times]]])
                      (filter #(= (% :rank) 2) tasks))]]]]]))
