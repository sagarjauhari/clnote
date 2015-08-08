<!DOCTYPE html>
<html>
  <head>
    <META http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Welcome to clnote</title>
  </head>
  <body>
    <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <a class="navbar-brand" href="{{servlet-context}}/">CLNote</a>
        </div>
        <div class="navbar-collapse collapse ">
          <ul class="nav navbar-nav">
            <li {% ifequal page "home.html" %} class="active"{%endifequal%}>
              <a href="{{servlet-context}}/">Home</a>
            </li>
            <li {% ifequal page "about.html" %} class="active"{%endifequal%}>
              <a href="{{servlet-context}}/about">About</a>
            </li>
          </ul>
        </div>
      </div>
    </div>

    <div class="container">
      {% block content %}
      {% endblock %}
    </div>
    <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap-theme.min.css">
    <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">
    <link href="/vendor/css/dragula.min.css" rel="stylesheet" type="text/css" />
    <link href="/css/screen.css" rel="stylesheet" type="text/css" />

    <script src="//code.jquery.com/jquery-2.1.1.min.js" type="text/javascript"></script>
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
    <script src="/vendor/js/dragula.min.js"></script>
    <script src="/js/drag-drop.js"></script>

    <script type="text/javascript">
      var context = "{{servlet-context}}";
    </script>
  </body>
</html>

