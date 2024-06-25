$(document).ready(function() {
    $("#drawButton").click(function () {
        $.ajax({
            url: '/admin/prize-draw/random',
            type: 'POST',
            data: { prizeId: prizeId },
            success: function(response) {
                if (response === true) {
                    alert("추첨 완료!");
                } else {
                    alert("추첨 실패!");
                }
            },
            error: function(xhr, status, error) {
                console.error("Error occurred:", status, error);
            }
        });
    });

    $('#backButton').click(function() {
        window.history.back();
    });
});