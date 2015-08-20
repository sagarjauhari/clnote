$(document).ready(function() {
  // Convert nodelist to array for dragula since it needs to
  // call indexOf method over the containers
  var containers = Array.prototype.slice.call(
    $(".drag-container")
  );

  dragula(containers, {
    moves: function (el, container, handle) {
      return handle.className === 'handle';
    }
  });
});
