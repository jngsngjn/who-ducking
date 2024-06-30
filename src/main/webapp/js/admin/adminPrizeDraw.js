$(document).ready(function() {
    const drawButton = $('#drawButton');

    $("#drawButton").click(function () {
        drawButton.addClass("loading");
        drawButton.prop("disabled", true);

        $.ajax({
            url: '/admin/prize-draw/random',
            type: 'POST',
            data: { prizeId: prizeId },
            success: function(response) {
                if (response === true) {
                    alert("추첨이 완료되었습니다.");
                    drawButton.removeClass("loading");
                    drawButton.prop("disabled", false);
                } else {
                    alert("추첨 실패!");
                    drawButton.removeClass("loading");
                    drawButton.prop("disabled", false);
                }
            },
            error: function(xhr, status, error) {
                console.error("Error occurred:", status, error);
                drawButton.removeClass("loading");
                drawButton.prop("disabled", false);
            }
        });
    });

    $('#backButton').click(function() {
        window.history.back();
    });
});