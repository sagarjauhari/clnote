$(document).ready(function() {
  dragula([left1, right1], {
    moves: function (el, container, handle) {
      return handle.className === 'handle';
    }
  });
});
