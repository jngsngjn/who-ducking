$(document).ready(function() {
    // 제목 글자 수 제한 및 더보기 버튼 추가
    $(".search_result_title").each(function() {
        const maxLength = 15; // 제목에 대한 최대 길이 설정
        const text = $(this).contents().filter(function() { return this.nodeType === 3; }).text().trim(); // 텍스트를 가져옵니다.
        const moreBtnTitle = $(this).find(".searchAll_more_title_btn");

        if (text.length > maxLength) {
            const shortText = text.slice(0, maxLength) + " . . .";
            $(this).contents().filter(function() { return this.nodeType === 3; }).get(0).nodeValue = shortText; // 텍스트를 자른 텍스트로 설정합니다.
            moreBtnTitle.show(); // 더보기 버튼을 표시합니다.

            moreBtnTitle.on("click", function() {
                if ($(this).text() === "더보기") {
                    $(this).siblings().filter(function() { return this.nodeType === 3; }).get(0).nodeValue = text;
                    $(this).text("간단히");
                } else {
                    $(this).siblings().filter(function() { return this.nodeType === 3; }).get(0).nodeValue = shortText;
                    $(this).text("더보기");
                }
            });
        }
    });

    // 내용 글자 수 제한 및 더보기 버튼 추가
    $(".search_result_content").each(function() {
        const maxLength = 100; // 내용에 대한 최대 길이 설정
        const text = $(this).text().trim();
        const moreBtnContent = $(this).next();

        if (text.length > maxLength) {
            const shortText = text.slice(0, maxLength) + " . . .";
            $(this).text(shortText);
            moreBtnContent.show(); // 더보기 버튼을 표시합니다.

            moreBtnContent.on("click", function() {
                if ($(this).text() === "더보기") {
                    $(this).prev().text(text);
                    $(this).text("간단히");
                } else {
                    $(this).prev().text(shortText);
                    $(this).text("더보기");
                }
            });
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
                }
            });

            // '더보기' 버튼 표시
            freeBoardMoreBtn.show();
        }
    }

    // 공지사항 항목 처리
    const noticeItems = $(".searchAll_notice_list_item");
    const noticeMoreBtn = $(".searchAll_notice_more_btn");
    const noticeDataEmpty = $(".searchAll_notice_list_data_empty");
    const noticeDataExist = $(".searchAll_notice_list_data_box");

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
                }
            });

            // '더보기' 버튼 표시
            noticeMoreBtn.show();
        }
    }
});