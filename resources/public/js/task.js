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
  _.each($(".new-task-line input"), function(taskInput){
    collId = $(taskInput).attr("collectionId");
    url = "/" + collId + "/tasks"

    $(taskInput).keypress(function (e) {
      if (e.which == 13) {
        $.post(
          url,
          {
            title: $(taskInput).val(),
            rank: $(taskInput).attr("rank"),
            parentId: $(taskInput).attr("parentId")
          },
          function (data) {
            $taskBox = $(data)

            notifyInfo("New task added");

            // insert the new element
            $taskBox.insertBefore($(taskInput).closest(".new-task-line"));

            // register task completion listener
            checkBoxId = $taskBox.find(".task-checkbox").attr("id");
            completeTaskOnClick($("#" + checkBoxId));

            // register the task activate listener
            activateTaskOnClick($taskBox.find(".task-title-link"));
          }
        );

        // Clear the input
        $(taskInput).val("");

        return false; // important
      }
    });
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
});
