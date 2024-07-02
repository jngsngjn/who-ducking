$(document).ready(function() {
    // 제목 글자 수 제한 및 더보기 버튼 추가
    $(".search_result_title").each(function() {
        const maxLength = 25; // 제목에 대한 최대 길이 설정
        const text = $(this).contents().filter(function() { return this.nodeType === 3; }).text().trim();

        if (text.length > maxLength) {
            const shortText = text.slice(0, maxLength) + "...";
            $(this).contents().filter(function() { return this.nodeType === 3; }).get(0).nodeValue = shortText;
        }
    });

    // 애니메이션 항목 처리
    const aniItems = $(".searchAll_ani_list_item");
    const aniMoreBtn = $(".searchAll_ani_more_btn");
    const aniDataEmpty = $(".searchAll_ani_list_data_empty");
    const aniDataExist = $(".searchAll_ani_list_data_exist");

    if (aniItems.length === 0) {
        aniDataEmpty.show();
        aniDataExist.hide();
    } else {
        aniDataEmpty.hide();
        aniDataExist.show();

        if (aniItems.length > 4) {
            // 처음 4개의 항목만 표시
            aniItems.each(function(index) {
                if (index >= 4) {
                    $(this).hide();
                    $(this).next(".searchAll_ani_list_line").hide(); // 하단 라인도 숨기기
                }
            });

            // '더보기' 버튼 표시
            aniMoreBtn.show();
        }
    }

    // 자유게시판 항목 처리
    const freeBoardItems = $(".searchAll_freeBoard_list_item");
    const freeBoardMoreBtn = $(".searchAll_freeBoard_more_btn");
    const freeBoardDataEmpty = $(".searchAll_freeBoard_list_data_empty");
    const freeBoardDataExist = $(".searchAll_freeBoard_list_data_box");

    if (freeBoardItems.length === 0) {
        freeBoardDataEmpty.show();
        freeBoardDataExist.hide();
    } else {
        freeBoardDataEmpty.hide();
        freeBoardDataExist.show();

        if (freeBoardItems.length > 3) {
            // 처음 3개의 항목만 표시
            freeBoardItems.each(function(index) {
                if (index >= 3) {
                    $(this).hide();
                    $(this).next(".searchAll_freeBoard_list_line").hide(); // 하단 라인도 숨기기
                }
            });

            // '더보기' 버튼 표시
            freeBoardMoreBtn.show();
        }
    }

    // 공지사항 항목 처리
    const noticeItems = $(".searchAll_announcement_list_item");
    const noticeMoreBtn = $(".searchAll_announcement_more_btn");
    const noticeDataEmpty = $(".searchAll_announcement_list_data_empty");
    const noticeDataExist = $(".searchAll_announcement_list_data_box");

    if (noticeItems.length === 0) {
        noticeDataEmpty.show();
        noticeDataExist.hide();
    } else {
        noticeDataEmpty.hide();
        noticeDataExist.show();

        if (noticeItems.length > 3) {
            // 처음 3개의 항목만 표시
            noticeItems.each(function(index) {
                if (index >= 3) {
                    $(this).hide();
                    $(this).next(".searchAll_announcement_list_line").hide(); // 하단 라인도 숨기기
                }
            });

            // '더보기' 버튼 표시
            noticeMoreBtn.show();
        }
    }

    $('.searchAll_announcement_list_item_one').on('click', function(e) {
        e.preventDefault();
        var announcementId = $(this).attr('data-id');
        console.log('Announcement ID:', announcementId); // 로그로 ID 확인
        getAnnouncementPage(announcementId);
    });

    function getAnnouncementPage(id) {
        $.ajax({
            url: '/api/announcements/page-number',
            method: 'GET',
            data: { id: id },
            success: function(response) {
                console.log(response);
                var pageNumber = response;
                window.location.href = '/announcement?page=' + pageNumber + '&id=' + id;
            },
            error: function(error) {
                console.error('Error fetching page number:', error);
            }
        });
    }
});