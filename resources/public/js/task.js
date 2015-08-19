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
    link.onclick = function(){
      // $(link.closest("div.task-box"));

      // $('.menu>li').on('click',function(e){
      //   $('.container>.'+ e.target.classList[0]).show().siblings().hide();
      // });
      // Create div for each group of children tasks, and show only the div
      // corresponding to the children of the clicked parent
      // Hide the sibling divs
    }
  });
});
