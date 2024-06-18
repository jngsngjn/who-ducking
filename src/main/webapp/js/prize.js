// prize.js
$(document).ready(function() {
    let modal = $('#prizeModal');
    let btn = $('#openModalBtn');
    let span = $('.close');

    btn.click(function() {
        modal.show();
    });

    span.click(function() {
        modal.hide();
    });

    $(window).click(function(event) {
        if (event.target.id === 'prizeModal') {
            modal.hide();
        }
    });
});