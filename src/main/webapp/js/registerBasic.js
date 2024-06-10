// 닉네임 검증 시작
let duplicateChecked = false;

function checkDuplicate() {
    const nickname = $("#nickname").val().trim();

    if (nickname === "") {
        alert("닉네임을 입력해주세요.");
        duplicateChecked = false;
        return;
    }

    if (nickname.length < 2 || nickname.length > 12) {
        alert("닉네임은 2자에서 12자 사이여야 합니다.");
        duplicateChecked = false;
        return;
    }

    const regex = /^[가-힣a-zA-Z0-9]+$/;
    if (!regex.test(nickname)) {
        alert("닉네임은 한글, 영어, 숫자만 사용할 수 있습니다.");
        duplicateChecked = false;
        return;
    }

    $.ajax({
        url: "/register/check-duplicate-nickname",
        type: "POST",
        data: {nickname: nickname},
        success: function(response) {
            if (response) {
                if (confirm("사용 가능한 닉네임입니다. 사용하시겠습니까?")) {
                    // 사용 버튼을 클릭한 경우
                    $("#nickname").prop("readonly", true);
                    $("#duplicateCheckButton").hide();
                    $("#changeButton").show();
                    duplicateChecked = true;
                }
            } else {
                alert("이미 사용 중인 닉네임입니다.");
                duplicateChecked = false;
            }
        },
        error: function() {
            alert("중복 확인 중 오류가 발생했습니다.");
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

    const regex = /^010\d{8}$/;
    if (!regex.test(phone)) {
        alert("전화번호 형식이 올바르지 않습니다.");
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
                alert("이미 " + socialType + " 계정으로 가입되어 있습니다.");
                verifyPhone = false;
            } else {
                alert("인증 코드가 발송되었습니다.");
                verifyPhone = false;
            }
        },
        error: function() {
            alert("인증 코드 발송에 실패했습니다.");
            verifyPhone = false;
        }
    });
}

function checkCode() {
    const verificationCode = $('#verification-code').val();
    const phone = $('#phone').val();

    $.ajax({
        type: "POST",
        url: "/register/verify-code",
        contentType: "application/json;charset=UTF-8",
        data: JSON.stringify({ phone: phone, code: verificationCode }),
        success: function(response) {
            if (response === true) {
                alert("인증 성공!");
                $("#phone").prop("readonly", true);
                $("#verification-code").prop("readonly", true);
                $("#sendCodeButton").hide();
                $("#checkCodeButton").hide();
                verifyPhone = true;
            } else {
                alert("인증 실패. 다시 시도해 주세요.");
                verifyPhone = false;
            }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log("Request failed:", textStatus, errorThrown);
            alert("인증 처리에 실패했습니다.");
            verifyPhone = false;
        }
    });
}
// 문자 인증 끝

// 추천인 검증 시작
let recommenderChecked = false;

function checkRecommender() {
    const referrer = $('#referrer').val().trim();

    if (referrer === "") {
        alert("추천인의 닉네임을 입력해 주세요.");
        return;
    }

    $.ajax({
        url: '/register/check-recommender',
        type: 'POST',
        contentType: "text/plain;charset=UTF-8",
        data: referrer,
        success: function(response) {
            if (response) {
                alert("추천인 닉네임이 확인되었습니다.");
                recommenderChecked = true;
            } else {
                alert("존재하지 않는 회원입니다.");
                recommenderChecked = false;
            }
        }
    });
}

// 추천인 검증 끝

// 폼 제출 검증
function validateForm() {
    if (!duplicateChecked) {
        alert("닉네임 중복 검사를 완료해 주세요.");
        return false;
    }

    if (!verifyPhone) {
        alert("전화번호 인증을 완료해 주세요.");
        return false;
    }

    const referrer = $('#referrer').val().trim();
    if (referrer !== "" && !recommenderChecked) {
        alert("추천인 확인을 완료해 주세요.");
        return false;
    }

    let termsChecked = document.getElementById('terms').checked;
    let privacyChecked = document.getElementById('privacy').checked;

    if (!termsChecked || !privacyChecked) {
        alert('모든 약관에 동의해 주세요.');
        return false;
    }

    return true;
}