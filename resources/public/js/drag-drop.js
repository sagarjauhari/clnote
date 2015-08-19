$(document).ready(function() {
  var containers = [left1, right1];
  dragula(containers, {
    moves: function (el, container, handle) {
      return handle.className === 'handle';
    }
  });
});
