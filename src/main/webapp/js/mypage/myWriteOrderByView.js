$(document).ready(function() {
    var orderByWriteDateBtn = $('#orderByWriteDateBtn');
    var orderByViewCountBtn = $('#orderByViewCountBtn');

    orderByWriteDateBtn.click(function() {
        window.location.href = 'http://localhost:8080/myPage/boards';
    });

    orderByWriteDateBtn.css('background-color', '#fff');
    orderByWriteDateBtn.css('color', '#ff8b00');

    orderByViewCountBtn.css('background-color', '#ff8b00');
    orderByViewCountBtn.css('color', '#fff');
});