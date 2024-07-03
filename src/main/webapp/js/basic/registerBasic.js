// 닉네임 검증 시작
let duplicateChecked = false;

function checkDuplicate() {
    const nickname = $("#nickname").val().trim();

    if (nickname === "") {
        swal({
            title: "입력 오류",
            text: "닉네임을 입력해주세요.",
            icon: "warning",
            buttons:"확인"
        });
        duplicateChecked = false;
        return;
    }

    if (nickname.length < 2 || nickname.length > 12) {
        swal({
            title: "길이 오류",
            text: "닉네임은 2자에서 12자 사이여야 합니다.",
            icon: "error",
        });
        duplicateChecked = false;
        return;
    }

    const regex = /^[가-힣a-zA-Z0-9]+$/;
    if (!regex.test(nickname)) {
        swal({
            title: "형식 오류",
            buttons: "확인",
            text: "닉네임은 한글, 영어, 숫자만 사용할 수 있습니다.",
            icon: "error",
        });
        duplicateChecked = false;
        return;
    }

   $.ajax({
       url: "/register/check-duplicate-nickname",
       type: "POST",
       data: {nickname: nickname},
       success: function(response) {
           if (response) {
               swal({
                   title: "닉네임 확인",
                   text: "사용 가능한 닉네임입니다. 사용하시겠습니까?",
                   icon: "success",
                   buttons: "사용",
                   dangerMode: false,
               }).then((willUse) => {
                   if (willUse) {
                       // 사용 버튼을 클릭한 경우
                       $("#nickname").prop("readonly", true);
                       $("#duplicateCheckButton").hide();
                       $("#changeButton").show();
                       duplicateChecked = true;
                   }
               });
           } else {
               swal("중복 확인", "이미 사용 중인 닉네임입니다.", "error");
               duplicateChecked = false;
           }
       },
       error: function() {
           swal("오류", "중복 확인 중 오류가 발생했습니다.", "error");
           duplicateChecked = false;
       }
   });
}

function changeNickname() {
    $("#nickname").prop("readonly", false);
    $("#duplicateCheckButton").show();
    $("#changeButton").hide();
    duplicateChecked = false;
}
// 닉네임 검증 끝

// 문자 인증 시작
let verifyPhone = false;

$(document).ready(function() {
    $("#phone").on("input", function() {
        let currentValue = $(this).val();

        // 값이 12자를 초과하면 초과된 부분을 잘라냄
        if (currentValue.length > 11) {
            $(this).val(currentValue.slice(0, 11));
        }
    });
});

function sendCode() {
    const phone = $('#phone').val().trim();

    if (phone === "") {
        swal({
            title: "입력 오류",
            buttons: "확인",
            text: "전화번호를 입력해 주세요.",
            icon: "warning",
        });
        return;
    }

    const regex = /^010\d{8}$/;
    if (!regex.test(phone)) {
        swal({
            title: "형식 오류",
            buttons: "확인",
            text: "전화번호 형식이 올바르지 않습니다.",
            icon: "error",
        });
        verifyPhone = false;
        return;
    }

    $.ajax({
        type: "POST",
        url: "/register/send-code",
        contentType: "text/plain;charset=UTF-8",
        data: phone,
        success: function(result) {
            if (result.isDuplicate) {
                const socialType = result.socialType;
                swal({
                    title: "계정 중복",
                    buttons: "확인",
                    text: "이미 " + socialType + " 계정으로 가입되어 있습니다.",
                    icon: "info",
                });
                verifyPhone = false;
            } else {
                swal({
                    title: "인증 코드 발송",
                    buttons: "확인",
                    text: "인증 코드가 발송되었습니다.",
                    icon: "success",
                });
                verifyPhone = false;
            }
        },
        error: function() {
            swal({
                title: "발송 실패",
                buttons: "확인",
                text: "인증 코드 발송에 실패했습니다.",
                icon: "error",
            });
            verifyPhone = false;
        }
    });
}

function checkCode() {
    const verificationCode = $('#verification-code').val();
    const phone = $('#phone').val();

    if (verificationCode === "") {
        swal({
            title: "입력 오류",
            buttons: "확인",
            text: "인증번호를 입력해 주세요.",
            icon: "warning",
        });
        return;
    }

    $.ajax({
        type: "POST",
        url: "/register/verify-code",
        contentType: "application/json;charset=UTF-8",
        data: JSON.stringify({ phone: phone, code: verificationCode }),
        success: function(response) {
            if (response === true) {
                swal({
                    title: "인증 성공",
                    buttons: "확인",
                    text: "인증이 성공적으로 완료되었습니다.",
                    icon: "success",
                });
                $("#phone").prop("readonly", true);
                $("#verification-code").prop("readonly", true);
                $("#sendCodeButton").hide();
                $("#checkCodeButton").hide();
                verifyPhone = true;
            } else {
                swal({
                    title: "인증 실패",
                    buttons: "확인",
                    text: "인증 실패. 다시 시도해 주세요.",
                    icon: "error",
                });
                verifyPhone = false;
            }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log("Request failed:", textStatus, errorThrown);
            swal({
                title: "오류",
                buttons: "확인",
                text: "인증번호 확인 중 오류가 발생했습니다.",
                icon: "error",
            });
            verifyPhone = false;
        }
    });
}

let recommenderChecked = false;

function checkRecommender() {
    const referrer = $('#referrer').val().trim();

    if (referrer === "") {
        swal({
            title: "입력 오류",
            buttons: "확인",
            text: "추천인의 닉네임을 입력해 주세요.",
            icon: "warning",
        });
        return;
    }

    $.ajax({
        url: '/register/check-recommender',
        type: 'POST',
        contentType: "text/plain;charset=UTF-8",
        data: referrer,
        success: function(response) {
            if (response) {
                swal({
                    title: "확인 완료",
                    buttons: "확인",
                    text: "추천인 닉네임이 확인되었습니다.",
                    icon: "success",
                });
                recommenderChecked = true;
            } else {
                swal({
                    title: "확인 실패",
                    buttons: "확인",
                    text: "존재하지 않는 회원입니다.",
                    icon: "error",
                });
                recommenderChecked = false;
            }
        }
    });
}

function validateForm() {
    if (!duplicateChecked) {
        swal({
            title: "확인 필요",
            buttons: "확인",
            text: "닉네임 중복 검사를 완료해 주세요.",
            icon: "warning",
        });
        return false;
    }

    if (!verifyPhone) {
        swal({
            title: "인증 필요",
            buttons: "확인",
            text: "전화번호 인증을 완료해 주세요.",
            icon: "warning",
        });
        return false;
    }

    const referrer = $('#referrer').val().trim();
    if (referrer !== "" && !recommenderChecked) {
        swal({
            title: "확인 필요",
            buttons: "확인",
            text: "추천인 확인을 완료해 주세요.",
            icon: "warning",
        });
        return false;
    }

    let termsChecked = document.getElementById('terms').checked;
    let privacyChecked = document.getElementById('privacy').checked;

    if (!termsChecked || !privacyChecked) {
        swal({
            title: "동의 필요",
            buttons: "확인",
            text: "모든 약관에 동의해 주세요.",
            icon: "warning",
        });
        return false;
    }

    return true;
}

$(document).ready(function () {
    $(".register_modal").hide();

    $("#modal-terms1").click(function () {
        $(".register_modal-1").show();
    });
    $("#modal-terms2").click(function () {
        $(".register_modal-2").show();
    });

    $(".register_modal-close").click(function () {
        $(".register_modal").hide();
    });
});