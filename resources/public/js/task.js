$(document).ready(function() {
  _.each($(".task-checkbox"), function(cb){
    cb.onclick = function(){
      console.log(cb);
    };
  });
});
