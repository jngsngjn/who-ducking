$(document).ready(function () {
    // 메일 전송
    $("#sendCodeButton").click(function () {
        const email = $("#emailHidden").val();
        const button = $(this);
        button.addClass("loading");
        button.prop("disabled", true);

        $.ajax({
            url: "/myPage/sendVerificationCode",
            method: "POST",
            data: { email: email },
            success: function () {
                alert("인증 코드가 이메일로 전송되었습니다.");
                button.removeClass("loading");
                button.prop("disabled", false);
            },
            error: function () {
                alert("인증 코드 전송에 실패했습니다.");
                button.removeClass("loading");
                button.prop("disabled", false);
            },
        });
    });

    // 클라이언트 측 코드 검증
    $("#emailCodeForm").submit(function (event) {
        event.preventDefault(); // 기본 폼 제출 방지

        let email = $("#emailHidden").val();
        let code = $("#verificationCodeInput").val();

        if (code === "") {
            alert("인증 코드를 입력해 주세요.");
            return;
        }

        $.ajax({
            url: "/myPage/account-deletion/verifyCode-ajax",
            type: "POST",
            data: { email: email, code: code },
            success: function (response) {
                if (response) {
                    var confirmation = confirm("정말로 계정을 삭제하시겠습니까?\n이 작업은 되돌릴 수 없습니다.");
                    if (confirmation) {
                        $("form")[1].submit();
                    }
                } else {
                    alert("유효하지 않은 코드이거나 시간이 만료되었습니다.");
                }
            },
            error: function (response) {
                console.error("AJAX error:", response);
                alert("서버 오류가 발생했습니다. 나중에 다시 시도해주세요.");
            },
        });
    });

    // 취소 버튼 클릭 시 myPage.html로 이동
    $(".accountDelete_cancel_btn").click(function () {
        window.location.href = "/myPage";
    });
});