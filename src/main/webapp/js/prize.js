$(document).ready(function() {
    var modal = $('#prizeModal');
    var btn = $('#openModalBtn');
    var span = $('.close');
    var currentPrizesBtn = $('#currentPrizesBtn');
    var expiredPrizesBtn = $('#expiredPrizesBtn');
    var currentPrizesTable = $('#currentPrizesTable');
    var expiredPrizesTable = $('#expiredPrizesTable');
    var currentPrizes = $('#currentPrizes');
    var expiredPrizes = $('#expiredPrizes');

    currentPrizesBtn.css('background-color', '#ff8b00');
    currentPrizesBtn.css('color', '#fff');

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

    currentPrizesBtn.click(function() {
        currentPrizesTable.show();
        expiredPrizesTable.hide();
        $(this).css('background-color', '#ff8b00');
        $(this).css('color', '#fff');
        expiredPrizesBtn.css('background-color', '#fff');
        expiredPrizesBtn.css('color', '#ff8b00');
    });

    expiredPrizesBtn.click(function() {
        expiredPrizesTable.show();
        currentPrizesTable.hide();
        $(this).css('background-color', '#ff8b00');
        $(this).css('color', '#fff');
        currentPrizesBtn.css('background-color', '#fff');
        currentPrizesBtn.css('color', '#ff8b00');
    });
});