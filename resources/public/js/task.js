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
            notifyInfo("New task added");
          }
        );

        return false; // important
      }
    });
  });

  // Update task completion when its checkbox is clicked
  _.each($(".task-checkbox"), function(cb){
    cb.onclick = function(){
      var url = "/tasks/" + cb.value
      $.ajax({
        url: url,
        type: 'PUT',
        data: {
          id: cb.value,
          completed: cb.checked
        },
        success: function(result) {
          // Change class
          $(cb.closest("div.task-box")).toggleClass("completed-true");

          // Notify
          notifyInfo(result);
        }
      });
    };
  });

  _.each($(".task-title-link"), function(link){
    var link = $(link);

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
