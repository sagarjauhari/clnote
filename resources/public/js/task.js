$(document).ready(function() {
  // UI Notify INFO
  var notifyInfo = function (message) {
    $.notify({
      message: message
    },{
      delay: 3000,
      type: 'success',
      placement: {
        from: "bottom",
        align: "right"
      },
    });
  };
  
  // Toggle task completion if checkbox is clicked
  var completeTaskOnClick = function(checkbox) {
    checkbox.click(function(){
      var url = "/tasks/" + checkbox.attr("value");

      $.ajax({
        url: url,
        type: 'PUT',
        data: {
          id: checkbox.attr("value"),
          completed: checkbox.prop("checked")
        },
        success: function(result) {
          // Change class
          $(checkbox.closest("div.task-box")).toggleClass("completed-true");

          // Notify
          notifyInfo(result);
        }
      });
    });
  };

  // Activate a task-item and open its children-grp when it is
  // clicked
  var activateTaskOnClick = function (link) {
    link.click(function(){
      // Make this list-item active
      link.closest(".task-item").
        addClass("active").
        siblings().removeClass("active");

      // Show only children tasks of this parent
      var taskId = link.parent().attr("taskId");
      $("#children-grp-" + taskId).
        removeClass("invisible").
        siblings().
        addClass("invisible");
    });
  }

  // Add a new task when the add button of the form is clicked
  newTaskOnEnterPress = function(taskInput) {
    collId = taskInput.attr("collectionId");
    url = "/" + collId + "/tasks"

    taskInput.keypress(function (e) {
      if (e.which == 13) {
        $.post(
          url,
          {
            title: taskInput.val(),
            rank: taskInput.attr("rank"),
            parentId: taskInput.attr("parentId")
          },
          function (data) {
            notifyInfo("New task added");

            jsonData = $.parseJSON(data);

            $taskBox = $(jsonData.taskItem);
            $childrenGrp = $(jsonData.childrenGrp);

            // insert the new task-box
            $taskBox.insertBefore(taskInput.closest(".new-task-line"));

            // add its children-grp to the correct column
            $("#col-rank-" + (parseInt(taskInput.attr("rank")) + 1)).
              find(".list-group").
              append($childrenGrp);

            // add new-task creation listeners to the children-grp's
            // new task line
            newTaskOnEnterPress($childrenGrp.find(".new-task-line input"));

            // register task completion listener
            checkBoxId = $taskBox.find(".task-checkbox").attr("id");
            completeTaskOnClick($("#" + checkBoxId));

            // register the task activate listener
            activateTaskOnClick($taskBox.find(".task-title-link"));
          }
        );

        // Clear the input
        taskInput.val("");

        return false; // important
      }
    });
  };

  // Add required callbacks to each new-task form
  _.each($(".new-task-line input"), function(taskInput){
    newTaskOnEnterPress($(taskInput));
  });
  

  // Add task complete listener to each checkbox
  _.each($(".task-checkbox"), function(cb){
    completeTaskOnClick($(cb));
  });

  // Add click listener to each task title
  _.each($(".task-title-link"), function(link){
    activateTaskOnClick($(link));
  });

  // Find chidlren group from task-item
  var childrenGrp = function(taskItem){
    return $("#children-grp-" + taskItem.children(".task-box").attr("taskid"));
  };

  // Make first parent active and show its children
  var showFirstParentChildren = function(){
    // Show first drag container
    var container = $("#left1").children(".drag-container")
    container.removeClass("invisible");

    // Make first parent active
    var firstParent = container.children(".task-item").first()
    firstParent.addClass("active");

    childrenGrp(firstParent).removeClass("invisible");
  };

  showFirstParentChildren();


  // Add event listeners for the search box
  $("#live-search-box input").keypress(function(e){
    console.log($(this).val() + String.fromCharCode(e.which));
  });
});
