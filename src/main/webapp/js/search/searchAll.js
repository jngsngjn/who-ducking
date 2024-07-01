document.addEventListener("DOMContentLoaded", function () {
    // 제목 글자 수 제한 및 더보기 버튼 추가
    document.querySelectorAll(".search_result_title").forEach(function (item) {
        const maxLength = 15; // 제목에 대한 최대 길이 설정
        const text = item.childNodes[0].textContent.trim(); // 텍스트를 가져옵니다.
        const moreBtnTitle = item.querySelector(".searchAll_more_title_btn");

        if (text.length > maxLength) {
            const shortText = text.slice(0, maxLength) + " . . .";
            item.childNodes[0].textContent = shortText; // 텍스트를 자른 텍스트로 설정합니다.
            moreBtnTitle.style.display = "inline"; // 더보기 버튼을 표시합니다.

            moreBtnTitle.addEventListener("click", function () {
                if (moreBtnTitle.textContent === "더보기") {
                    item.childNodes[0].textContent = text;
                    moreBtnTitle.textContent = "간단히";
                } else {
                    item.childNodes[0].textContent = shortText;
                    moreBtnTitle.textContent = "더보기";
                }
            });
        }
    });

    // 내용 글자 수 제한 및 더보기 버튼 추가
    document.querySelectorAll(".search_result_content").forEach(function (item) {
        const maxLength = 100; // 내용에 대한 최대 길이 설정
        const text = item.textContent.trim();
        const moreBtnContent = item.nextElementSibling;

        if (text.length > maxLength) {
            const shortText = text.slice(0, maxLength) + " . . .";
            item.textContent = shortText;
            moreBtnContent.style.display = "inline"; // 더보기 버튼을 표시합니다.

            moreBtnContent.addEventListener("click", function () {
                if (moreBtnContent.textContent === "더보기") {
                    item.textContent = text;
                    moreBtnContent.textContent = "간단히";
                } else {
                    item.textContent = shortText;
                    moreBtnContent.textContent = "더보기";
                }
            });
        }
    });
});

document.addEventListener("DOMContentLoaded", function () {
    const items = document.querySelectorAll(".searchAll_ani_list_item");
    const moreBtn = document.querySelector(".searchAll_ani_more_btn");
    const dataEmpty = document.querySelector(".searchAll_ani_list_data_empty");
    const dataExist = document.querySelector(".searchAll_ani_list_data_exist");

    if (items.length === 0) {
        dataEmpty.style.display = "block";
        dataExist.style.display = "none";
    } else {
        dataEmpty.style.display = "none";
        dataExist.style.display = "block";

        if (items.length > 4) {
            // 처음 4개의 항목만 표시
            items.forEach((item, index) => {
                if (index >= 4) {
                    item.style.display = "none";
                }
            });

            // '더보기' 버튼 표시
            moreBtn.style.display = "inline";
        }
    }
});

document.addEventListener("DOMContentLoaded", function () {
    const items = document.querySelectorAll(".searchAll_freeBoard_list_item");
    const moreBtn = document.querySelector(".searchAll_freeBoard_more_btn");
    const dataEmpty = document.querySelector(".searchAll_freeBoard_list_data_empty");
    const dataExist = document.querySelector(".searchAll_freeBoard_list_data_box");

    if (items.length === 0) {
        dataEmpty.style.display = "block";
        dataExist.style.display = "none";
    } else {
        dataEmpty.style.display = "none";
        dataExist.style.display = "block";

        if (items.length > 3) {
            // 처음 3개의 항목만 표시
            items.forEach((item, index) => {
                if (index >= 3) {
                    item.style.display = "none";
                }
            });

            // '더보기' 버튼 표시
            moreBtn.style.display = "inline";
        }
    }
});

document.addEventListener("DOMContentLoaded", function () {
    const items = document.querySelectorAll(".searchAll_notice_list_item");
    const moreBtn = document.querySelector(".searchAll_notice_more_btn");
    const dataEmpty = document.querySelector(".searchAll_notice_list_data_empty");
    const dataExist = document.querySelector(".searchAll_notice_list_data_box");

    if (items.length === 0) {
        dataEmpty.style.display = "block";
        dataExist.style.display = "none";
    } else {
        dataEmpty.style.display = "none";
        dataExist.style.display = "block";

        if (items.length > 3) {
            // 처음 3개의 항목만 표시
            items.forEach((item, index) => {
                if (index >= 3) {
                    item.style.display = "none";
                }
            });

            // '더보기' 버튼 표시
            moreBtn.style.display = "inline";
        }
    }
});
