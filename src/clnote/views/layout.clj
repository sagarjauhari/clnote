(ns clnote.views.layout
  (:use [hiccup.page :only (html5 include-css include-js)]))

(defn application [title & content]
  (html5 {:lang "en"}
    [:head
      [:meta
        {:http-equiv "content-type", :content "text/html;charset=utf-8"}]
      [:title title]
      (include-css "//maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css")
      (include-css "//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css")
      (include-css "vendor/css/dragula.min.css")
      (include-css "css/screen.css")

      (include-js "//code.jquery.com/jquery-2.1.1.min.js")
      (include-js "//maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js")
      (include-js "//cdnjs.cloudflare.com/ajax/libs/dragula/3.0.3/dragula.min.js")
      (include-js "//cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore-min.js")
      (include-js "js/drag-drop.js")
      (include-js "js/task.js")
      (include-js "vendor/js/bootstrap-notify.min.js")]

      [:body
        [:div {:class "navbar navbar-inverse navbar-fixed-top"}  
         [:div {:class "container"}  
          [:div {:class "navbar-header"}  
           [:a {:class "navbar-brand", :href "/tasks"} "CLNote"] ]  
          [:div {:class "navbar-collapse collapse "}  
           [:ul {:class "nav navbar-nav"}  
            [:li
             [:a {:href "/tasks"} "Tasks"]]]]]]
           [:div {:class "container"} content]]))
