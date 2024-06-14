$(document).ready(function() {

    let profileImagePreview = $('#profileImagePreview');
    let levelImagePreview = $('#levelImagePreview');
    let useDefaultImageButton = $('#useDefaultImage');

    // 초기 상태 설정
    // 사용 중인 프사가 없을 때
    if (!profileImagePreview.attr('src') || profileImagePreview.attr('src') === '') {
        profileImagePreview.hide();
        levelImagePreview.show();
        useDefaultImageButton.hide();
    } else {
        // 사용 중인 프사가 있을 때
        profileImagePreview.show();
        levelImagePreview.hide();
        useDefaultImageButton.show();
    }

    // 사진을 업로드했을 때
    $('#profileImageInput').on('change', function(event) {
        let file = event.target.files[0];
        if (file) {
            let reader = new FileReader();
            reader.onload = function(e) {
                $('#profileImagePreview').attr('src', e.target.result).show();
                $('#levelImagePreview').hide();
                $('#useDefaultImageFlag').val('false');
                useDefaultImageButton.show();
            }
            reader.readAsDataURL(file);
        }
    });

    // 기본 이미지 버튼을 클릭했을 때
    $('#useDefaultImage').on('click', function() {
        let useDefaultImage = $('#useDefaultImageFlag').val();

        // 이미 기본 이미지를 사용 중이라면 AJAX 요청을 보내지 않음
        if (useDefaultImage !== 'true') {
        $.ajax({
            url: '/myPage/user/level-image',
            type: 'POST',
            success: function(data) {
                let defaultImagePath = '/image/level/' + data;
                $('#profileImagePreview').attr('src', defaultImagePath).show();
                $('#levelImagePreview').hide();
                $('#profileImageInput').val('');
                $('#useDefaultImageFlag').val('true');
                useDefaultImageButton.hide();
            },
            error: function(xhr, status, error) {
                alert('기본 이미지 로드에 실패했습니다.');
            }
        });
        }
    });
});

// 닉네임
let duplicateChecked = true;
$(document).ready(function() {
    const currentNickname = $('#currentNickname').val();

    $('#changeNicknameButton').on('click', function() {
        let $button = $(this);
        let $inputField = $button.siblings('input[type="text"]');

        if ($button.text() === "변 경") {
            duplicateChecked = false;

            // input 필드 readonly 속성 제거
            $inputField.prop("readonly", false);

            // 새 중복확인 버튼 생성
            let $duplicateButton = $('<button>', {
                type: 'button',
                text: '중복 확인',
                class: 'check',
                click: checkDuplicate
            });

            // 기존 버튼을 숨기고 새 버튼을 추가
            $button.hide();
            $button.parent().append($duplicateButton);
        }
    });

    function checkDuplicate() {
        let $button = $(this);
        let inputValue = $button.siblings('input[type="text"]').val();

        if (inputValue === currentNickname) {
            alert("기존 닉네임과 동일합니다.");
            duplicateChecked = false;
            return;
        }

        if (inputValue === "") {
            alert("닉네임을 입력해주세요.");
            duplicateChecked = false;
            return;
        }

        if (inputValue.length < 2 || inputValue.length > 12) {
            alert("닉네임은 2자에서 12자 사이여야 합니다.");
            duplicateChecked = false;
            return;
        }

        let regex = /^[가-힣a-zA-Z0-9]+$/;
        if (!regex.test(inputValue)) {
            alert("닉네임은 한글, 영어, 숫자만 사용할 수 있습니다.");
            duplicateChecked = false;
            return;
        }

        $.ajax({
            url: "/register/check-duplicate-nickname",
            type: "POST",
            data: { nickname: inputValue },
            success: function(response) {
                if (response) {
                    if (confirm("사용 가능한 닉네임입니다. 사용하시겠습니까?")) {
                        // 사용 버튼을 클릭한 경우
                        $("#currentNickname").prop("readonly", true);
                        $button.remove();
                        $('#changeNicknameButton').show();
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
});

// 전화번호
let verifyPhone = true;
$(document).ready(function() {
    const currentPhone = $('#phone').val();

    window.changePhone = function(button) {
        let $button = $(button);
        if ($button.text() === "변 경") {
            let $inputField = $button.prev('input');
            $inputField.prop('readonly', false);

            let $verifyButton = $('<button>', {
                type: 'button',
                text: '인증 번호 전송',
                class: 'check',
                id: 'sendCodeButton',
                click: function () {
                    sendCode($inputField.val(), currentPhone);
                }
            });

            $button.replaceWith($verifyButton);
            $inputField.on('input', function() {
                if (this.value.length > 11) {
                    this.value = this.value.slice(0, 11);
                }
            });
        }
    }

    window.sendCode = function(input, currentPhone) {
        if (input === currentPhone) {
            alert("기존 전화번호와 동일합니다.");
            return;
        }

        if (input === "") {
            alert("전화번호를 입력해 주세요.");
            return;
        }

        const regex = /^010\d{8}$/;
        if (!regex.test(input)) {
            alert("전화번호 형식이 올바르지 않습니다.");
            return;
        }

        $.ajax({
            type: "POST",
            url: "/register/send-code",
            contentType: "text/plain;charset=UTF-8",
            data: input,
            success: function(result) {
                if (result.isDuplicate) {
                    alert("사용할 수 없는 번호입니다.");
                } else {
                    alert("인증 코드가 발송되었습니다.");
                    $('#verification-code').show();
                    $("#changePhoneFlag").val("true");
                    verifyPhone = false;
                }
            },
            error: function() {
                alert("인증 코드 발송에 실패했습니다.");
            }
        });
    }

    window.verifyCode = function() {
        const verificationCode = $('#verificationInput').val();
        const phone = $('#phone').val();

        if (verificationCode === "") {
            alert("인증번호를 입력해 주세요.");
            verifyPhone = false;
            return;
        }

        $.ajax({
            type: "POST",
            url: "/register/verify-code",
            contentType: "application/json;charset=UTF-8",
            data: JSON.stringify({ phone: phone, code: verificationCode }),
            success: function(response) {
                if (response === true) {
                    alert("인증이 성공적으로 완료되었습니다.");
                    $("#phone").prop("readonly", true);
                    $("#verificationInput").prop("readonly", true);
                    $("#checkCodeButton").hide();
                    $("#sendCodeButton").hide();
                    verifyPhone = true;
                } else {
                    alert("인증 실패. 다시 시도해 주세요.");
                    verifyPhone = false;
                }
            },
            error: function() {
                alert("인증번호 확인 중 오류가 발생했습니다.");
                verifyPhone = false;
            }
        });
    }

    // 입력 필드에 숫자 4자리 제한 추가
    $('#verificationInput').on('input', function() {
        if (this.value.length > 4) {
            this.value = this.value.slice(0, 4);
        }
    });
});

function selectGender(button) {
    // 모든 버튼의 선택된 스타일 제거
    const buttons = document.querySelectorAll('.gender');
    buttons.forEach(btn => btn.classList.remove('selected'));

    // 클릭된 버튼에 선택된 스타일 추가
    button.classList.add('selected');
}

function updateStyledText(input) {
    const styledText = document.getElementById(input.id + '-styled');
    if (input.value) {
        styledText.style.display = 'none';
    } else {
        styledText.style.display = 'block';
    }
}
// ----------------------------------------------------

function execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function (data) {
            // 팝업을 통한 검색 결과 항목 클릭 시 실행
            var addr = ''; // 주소_결과값이 없을 경우 공백
            var extraAddr = ''; // 참고항목

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 도로명 주소를 선택
                addr = data.roadAddress;
            } else { // 지번 주소를 선택
                addr = data.jibunAddress;
            }

            if (data.userSelectedType === 'R') {
                if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                    extraAddr += data.bname;
                }
                if (data.buildingName !== '' && data.apartment === 'Y') {
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                if (extraAddr !== '') {
                    extraAddr = ' (' + extraAddr + ')';
                }
            } else {
                document.getElementById("UserAdd1").value = '';
            }

            // 선택된 우편번호와 주소 정보를 input 박스에 넣는다.
            document.getElementById('postcode').value = data.zonecode;
            document.getElementById("address").value = addr;
            document.getElementById("address").value += extraAddr;
            document.getElementById("address-detail").focus(); // 우편번호 + 주소 입력이 완료되었음으로 상세주소로 포커스 이동
        }
    }).open();
}

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

    const postcode = $('#postcode').val();
    const detailAddress = $('#detail-address').val();

    if (postcode && detailAddress === "") {
        alert("상세 주소를 입력해 주세요.");
        return false;
    }

    return true;
}