$(document).ready(function() {
    $('.noticePage-main-content-list-elements-moreview').on('click', function() {
        // 모든 드롭다운 닫기 및 버튼 텍스트 +로 변경
        $('.noticePage-main-content-dropdown').slideUp();
        $('.noticePage-main-content-list-elements-moreview').text('+');

        // 클릭된 요소의 드롭다운 토글 및 버튼 텍스트 변경
        var dropdown = $(this).closest('.noticePage-main-content-list-elements').find('.noticePage-main-content-dropdown');
        if (dropdown.is(':visible')) {
            dropdown.slideUp();
            $(this).text('+');
        } else {
            dropdown.slideDown();
            $(this).text('-');
        }
    });
});