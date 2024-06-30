$(document).ready(function(){
    $('.myBookmark_list_bookmark_btn').click(function(){
        var $button = $(this);
        var boardId = $button.closest('.myBookmark_list_data_box').find('a').attr('href').match(/\/board\/(\d+)/)[1];
        var isBookmarked = $button.find('i').hasClass('fa-solid');

        if (isBookmarked) {
            // 북마크 삭제 요청
            $.ajax({
                url: '/myPage/bookmark/delete',
                method: 'POST',
                data: { id: boardId },
                success: function(response) {
                    $button.find('i').removeClass('fa-solid').addClass('fa-regular');
                    console.log('북마크 삭제 성공');
                },
                error: function() {
                    console.log('북마크 삭제 실패');
                }
            });
        } else {
            // 북마크 추가 요청
            $.ajax({
                url: '/myPage/bookmark/add',
                method: 'POST',
                data: { id: boardId },
                success: function(response) {
                    $button.find('i').removeClass('fa-regular').addClass('fa-solid');
                    console.log('북마크 등록 성공');
                },
                error: function() {
                    console.log('북마크 등록 실패');
                }
            });
        }
    });
});