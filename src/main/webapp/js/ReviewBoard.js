document.addEventListener("DOMContentLoaded", function () {
    let buttons = document.querySelectorAll(".side-container-button");

    buttons.forEach((btn) => {
        btn.addEventListener("click", function () {
            buttons.forEach(function (btn) {
                btn.classList.remove("active");
                btn.classList.add("inactive");
            });

            this.classList.remove("inactive");
            this.classList.add("active");
        });
    });

    let reviewBoardBtn = document.getElementById("review-board");
    if (reviewBoardBtn) {
        reviewBoardBtn.click();
    }

    // 통합되었을떄 자유게시판 클릭시 그 게시판이 나오는 함수 넣기
});


// 페이지 네이션 -> 나중에 하자..
const aniInPage = 9;
let currentPage = 1;
let totalItems = document.querySelectorAll('.ani-info-container').length;
const totalPages = Math.ceil(totalItems / aniInPage);

function renderPage(page) {
    const aniListContainer = document.getElementById('ani-list-container');
    const items = aniListContainer.querySelectorAll('.ani-info-container');

    for (let i = 0; i < items.length; i++) {
        items[i].style.display = 'none';
    }

    const start = (page - 1) * aniInPage;
    const end = page * aniInPage;

    for (let i = start; i < end && i < items.length; i++) {
        items[i].style.display = 'block';
    }

    document.getElementById('current-page').innerText = page;
    currentPage = page;
}

function nextPage() {
    if (currentPage < totalPages) {
        renderPage(currentPage + 1);
    }
}

function prevPage() {
    if (currentPage > 1) {
        renderPage(currentPage - 1);
    }
}

function changePage(page) {
    renderPage(page);
}

document.addEventListener("DOMContentLoaded", function() {
    renderPage(1);
});


