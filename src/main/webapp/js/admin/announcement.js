$(document).ready(function() {
    var modal = $('#myModal');
    var openModalBtn = $('#openModalBtn');
    var closeModalBtn = $('.close');

    openModalBtn.on('click', function() {
        modal.show();
    });

    closeModalBtn.on('click', function() {
        modal.hide();
    });

    $(window).on('click', function(event) {
        if ($(event.target).is(modal)) {
            modal.hide();
        }
    });
});