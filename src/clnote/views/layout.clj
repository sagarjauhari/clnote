(ns clnote.views.layout
  (:use [hiccup.page :only (html5 include-css include-js)]))

(defn application [title & content]
  (html5 {:lang "en"}
    [:head
      [:title title]
      (include-css "//maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css")
      (include-css "//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css")
      (include-css "css/screen.css")

      (include-js "//code.jquery.com/jquery-2.1.1.min.js")
      (include-js "//maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js")
      (include-js "js/script.js")
      (include-js "//cdnjs.cloudflare.com/ajax/libs/dragula/3.0.3/dragula.min.js")
      (include-js "js/drag-drop.js")

      [:body
        [:div {:class "navbar navbar-inverse navbar-fixed-top"}  
         [:div {:class "container"}  
          [:div {:class "navbar-header"}  
           [:a {:class "navbar-brand", :href "/"} "CLNote"] ]  
          [:div {:class "navbar-collapse collapse "}  
           [:ul {:class "nav navbar-nav"}  
            [:li
             [:a {:href "/"} "Home"] ]  
            [:li
             [:a {:href "about"} "About"]]]]]]

          [:div {:class "container"} "Hello world" ]

           [:div {:class "container"} content ]]]))
