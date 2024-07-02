document.addEventListener("DOMContentLoaded", function () {
    // 제목 글자 수 제한 및 더보기 버튼 추가
    document.querySelectorAll(".search_result_title").forEach(function (item) {
        const maxLength = 15; // 제목에 대한 최대 길이 설정
        const text = item.childNodes[0].textContent.trim(); // 텍스트를 가져옵니다.
        const moreBtnTitle = item.querySelector(".searchAnnouncement_more_title_btn");

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
