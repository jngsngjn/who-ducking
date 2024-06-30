$(document).ready(function() {
    var viewCountBtn = $('#viewCountBtn');

    viewCountBtn.click(function() {
        window.location.href = 'http://localhost:8080/myPage/boards/viewCount';
    });
});