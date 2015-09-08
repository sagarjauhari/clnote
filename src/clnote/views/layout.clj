(ns clnote.views.layout
  (:use [hiccup.page :only (html5 include-css include-js)]
        [hiccup.element :only (link-to)]
        [hiccup.form]
        [clnote.db.core :as db]))

(defn collection-picker [coll-id]
  (let [collections (db/get-collections)]
    [:li.dropdown
      [:a.dropdown-toggle
        {:role "button"
          :data-toggle "dropdown"
          :aria-haspopup true
          :aria-expanded false}
        (if (> coll-id 0)
          (str ((first (filter #(= (% :id) coll-id) collections)) :title) " ")
          "All ")
        [:span.caret]]
      [:ul.dropdown-menu
        (concat
          (map
            (fn [coll]
              [:li (link-to (str "/" (coll :id) "/tasks") (coll :title))])
            collections)
          [[:li.divider{:role "separator"}]
           ; id = 0 means return all tasks in all collections
           [:li (link-to "/0/tasks" "All")]])]]))

(defn live-search-box
  "Filters the tasks based on what is typed in box"
  []
  [:form.navbar-form.navbar-left{:role "Search"}
    [:div#live-search-box
     (text-field {:placeholder "Search tasks"
                  :type "text"
                  :class "form-control"} "search")]])

(defn application [title coll-id & content]
  (html5 {:lang "en"}
    [:head
      [:meta
        {:http-equiv "content-type", :content "text/html;charset=utf-8"}]
      [:title title]
      (include-css "//maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css")
      (include-css "//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css")
      (include-css "/vendor/css/dragula.min.css")
      (include-css "/css/screen.css")

      (include-js "//code.jquery.com/jquery-2.1.1.min.js")
      (include-js "//maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js")
      (include-js "//cdnjs.cloudflare.com/ajax/libs/dragula/3.0.3/dragula.min.js")
      (include-js "//cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore-min.js")
      (include-js "/js/drag-drop.js")
      (include-js "/js/task.js")
      (include-js "/js/collection.js")
      (include-js "/vendor/js/bootstrap-notify.min.js")]

      [:body
        [:div.navbar.navbar-default.navbar-fixed-top
         [:div.container
          [:div.navbar-header
           [:a.navbar-brand {:href "/0/tasks"} "CLNote"]]
          [:div.navbar-collapse.collapse
           [:ul.nav.navbar-nav
             (collection-picker coll-id)
             (live-search-box)]]]]
           [:div.container content]]))
