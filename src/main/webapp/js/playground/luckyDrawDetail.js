/** 응모하기 버튼을 눌렀을 때
 1. 오늘 응모했는지 확인
 2. 레벨에 맞는 경품인지 확인
 3. 포인트 있는지 확인
 4. 주소 입력되어 있는지 확인
 */

$(document).ready(function () {
    // 응모하기 버튼 클릭 시
    $("#entry-button").click(function () {
        $.ajax({
            url: "/check-user/" + prizeId,
            type: "POST",
            success: function (response) {
                if (response.alreadyEntry) {
                    swal({
                        title: "알림",
                        text: "이미 오늘 경품에 응모하셨습니다.",
                        icon: "info",
                        buttons: "확인"

                    });
                } else if (!response.validGradeAndLevel) {
                    swal({
                        title: "금지",
                        text: "해당 경품에 응모할 수 있는 레벨이 아닙니다.",
                        icon: "error",
                        buttons: "확인"
                    });
                } else if (!response.hasEnoughPoints) {
                    swal({
                        title: "경고",
                        text: "포인트가 부족합니다.",
                        icon: "warning",
                        buttons: "확인"
                    });
                } else if (response.addressEmpty) {
                    swal({
                        title: "알림",
                        text: "경품에 응모하기 전\n 마이페이지에서 주소를 입력해 주세요.",
                        icon: "info",
                        buttons: "확인"
                    }).then((value) => {
                        window.location.href = "/myPage";
                    });
                } else {
                    swal({
                        title: "확인",
                        text: "해당 경품에 응모하시겠습니까? (30P 차감)\n응모는 하루에 한 번만 가능합니다.",
                        icon: "info",
                        buttons: {
                            confirm: "응모",
                            cancel: "취소"
                        },
                        dangerMode: true
                    })
                    .then((willEntry) => {
                        if (willEntry) {
                            $.ajax({
                                url: "/entry-prize/" + prizeId,
                                type: "POST",
                                success: function (response) {
                                    swal({
                                        title: "응모 성공",
                                        text: response,
                                        icon: "success",
                                        buttons: "확인"
                                    });
                                },
                                error: function (error) {
                                    swal({
                                        title: "응모 실패",
                                        text: "응모 처리 중 오류가 발생했습니다. 나중에 다시 시도해주세요.",
                                        icon: "error",
                                        buttons: "확인"
                                    });
                                },
                            });
                        }
                    });
                }
            },
            error: function (error) {
                swal({
                    title: "오류",
                    text: "서버 오류가 발생했습니다. 나중에 다시 시도해주세요.",
                    icon: "error",
                    buttons: "확인"
                });
            },
        });
    });
});