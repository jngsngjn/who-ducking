// 닉네임 검증 시작
let duplicateChecked = false;

function checkDuplicate() {
    const nickname = $("#nickname").val().trim();

    if (nickname === "") {
        alert("닉네임을 입력해주세요.");
        duplicateChecked = false;
        return;
    }

    // 정규식 검사 추가해야 함

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
function sendCode() {
    const phone = $('#phone').val();
    console.log("Phone number to send:", phone);

    $.ajax({
        type: "POST",
        url: "/register/send-code",
        contentType: "text/plain;charset=UTF-8",
        data: phone,
        success: function() {
            alert("인증 코드가 발송되었습니다.");
            verifyPhone = false;
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

    let termsChecked = document.getElementById('terms').checked;
    let privacyChecked = document.getElementById('privacy').checked;

    if (!termsChecked || !privacyChecked) {
        alert('모든 약관에 동의해 주세요.');
        return false;
    }

    return true;
}