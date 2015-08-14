$(document).ready(function() {
  _.each($(".task-checkbox"), function(cb){
    cb.onclick = function(){
      //Post request to update task with cb.checked as value of completed
      var url = "/tasks/" + cb.value
      $.ajax({
        url: url,
        type: 'PUT',
        data: {
          id: cb.value,
          completed: cb.checked
        },
        success: function(result) {
          // Update checkbox
          true;
        }
      });
    };
  });
});
