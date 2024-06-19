// 페이지가 로드된 후 실행
$(document).ready(function () {
    // 모든 버튼 요소를 선택
    $('.myFaq_state').each(function () {
        // 버튼의 id에 따라 스타일 적용 및 텍스트 변경
        switch ($(this).attr('id')) {
            case 'APPROVED':
                $(this).css('background-color', 'rgba(16, 255, 11, 0.5)').text('승인'); // 파란색
                break;
            case 'REJECTED':
                $(this).css('background-color', 'rgba(255, 0, 0, 0.5)').text('반려'); // 빨간색
                break;
            default:
                $(this).css('background-color', '#ffc60b').text('접수'); // 기본 색상
                break;
        }
    });

    var modal = $('#myModal');
    var closeModalBtn = $('.custom_modal-close');
    var modalTitle = $('#modalTitle');
    var modalContent = $('#modalContent');
    var writeDate = $('#writeDate');
    var deleteForm = $('#deleteForm');
    var requestIdInput = $('#requestId');
    var responseDate = $('#responseDate');
    var responseDateValue = $('#responseDateValue');
    var responseText = $('#responseText');
    var response = $('#response');

    $('.myFaq_title').on('click', function() {
        var title = $(this).text();
        var content = $(this).attr('data-content');
        var status = $(this).attr('data-status');
        var requestId = $(this).attr('data-request-id');
        var responseDateText = $(this).attr('data-response-date');
        var writeDateText = $(this).attr('data-write-date');
        var responseContent = $(this).attr('data-response');

        modalTitle.text(title);
        modalContent.text(content);
        writeDate.text(writeDateText);
        requestIdInput.val(requestId);

        if (status === 'RECEIVED') {
            deleteForm.show();
            responseDate.hide();
            responseText.hide();
        } else if (status === 'APPROVED') {
            deleteForm.hide();
            responseDateValue.text(responseDateText);
            responseDate.show();
            responseText.hide();
        } else if (status === 'REJECTED') {
            deleteForm.hide();
            responseDateValue.text(responseDateText);
            responseDate.show();
            response.text(responseContent);
            responseText.show();
        } else {
            deleteForm.hide();
            responseDate.hide();
            responseText.hide();
        }

        // Get current scroll position
        var scrollTop = $(window).scrollTop();

        // Set the modal position based on current scroll position
        modal.css({
            'top': scrollTop + 50 + 'px',
            'left': '50%',
            'transform': 'translate(-50%, 0)'
        });

        modal.show();
    });

    closeModalBtn.on('click', function() {
        modal.hide();
    });

    $(window).on('click', function(event) {
        if ($(event.target).is(modal)) {
            modal.hide();
        }
    });
});