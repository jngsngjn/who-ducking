// 페이지가 로드된 후 실행
$(document).ready(function () {
    // 모든 버튼 요소를 선택
    $('.state').each(function () {
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
});