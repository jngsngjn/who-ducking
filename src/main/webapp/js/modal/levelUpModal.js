// levelUpModal.js
$(document).ready(function() {
    var modal = $('#levelUp_modal');
    var closeBtn = modal.find('.levelUp_modal_close');

    // 모달 창 열기 함수
    function openModal() {
        modal.show();
    }

    // 모달 창 닫기 함수
    function closeModal() {
        modal.hide();
        // 세션 삭제
        deleteSession();
    }

    // 모달 닫기 버튼에 클릭 이벤트 추가
    closeBtn.on('click', function() {
        closeModal();
    });

    // 모달 외부 클릭 시 닫기
    $(window).on('click', function(event) {
        if (event.target === modal[0]) {
            closeModal();
        }
    });

    // 세션 삭제 요청 함수
    function deleteSession() {
        $.ajax({
            url: '/delete-levelUp-session',
            type: 'POST',
            success: function(response) {
                console.log('세션 삭제 완료');
            },
            error: function(xhr, status, error) {
                console.error('AJAX 오류 발생:', status, error);
            }
        });
    }

    // 서버에서 세션 상태를 확인하는 함수
    function checkSessionStatus() {
        console.log('checkSessionStatus');
        $.ajax({
            url: '/check-levelUp-session',
            type: 'POST',
            success: function(response) {
                if (response) {
                    console.log('openModal()')
                    openModal();
                } else {
                    console.log('세션이 유효하지 않습니다.');
                }
            },
            error: function(xhr, status, error) {
                console.error('AJAX 오류 발생:', status, error);
            }
        });
    }

    checkSessionStatus();
});