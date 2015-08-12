(ns clnote.views.layout
  (:use [hiccup.page :only (html5 include-css include-js)]))

(defn application [title & content]
  (html5 {:lang "en"}
         [:head
          [:title title]
          (include-css "//maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css")

          (include-js "//code.jquery.com/jquery-2.1.1.min.js")
          (include-js "//maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js")
          (include-js "js/script.js")

          [:body
           [:div {:class "container"} content ]]]))
