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
                if (response !== "fail") {
                    swal({
                        title: "추첨 완료",
                        text: "추첨이 완료되었습니다.\n당첨자는 [" + response + "]입니다.",
                        icon: "success",
                        button: "확인"
                    });
                } else {
                    swal({
                        title: "추첨 실패",
                        text: "추첨에 실패했습니다.",
                        icon: "error",
                        button: "확인"
                    });
                }
                drawButton.removeClass("loading");
                drawButton.prop("disabled", false);
            },
            error: function(xhr, status, error) {
                console.error("Error occurred:", status, error);
                swal({
                    title: "오류 발생",
                    text: "추첨 중 오류가 발생했습니다.",
                    icon: "error",
                    button: "확인"
                });
                drawButton.removeClass("loading");
                drawButton.prop("disabled", false);
            }
        });
    });

    $('#backButton').click(function() {
        window.history.back();
    });
});
