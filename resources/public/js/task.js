$(document).ready(function() {
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
          $.notify({
            message: result
          },{
            delay: 3000,
            type: 'success',
            placement: {
              from: "bottom",
              align: "right"
            },
          });
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
});
