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


let currentPage = 1;
let totalAni;
let totalPages;

function renderPage(page) {
    const aniListContainer = document.getElementById('ani-list-container');
    const animations = aniListContainer.querySelectorAll('.ani-info-container');
    const buttonWrapper = document.getElementById('button-wrapper');

    // 현재 페이지 업데이트
    currentPage = page;

    // 기존 버튼 지우기
    buttonWrapper.innerHTML = '';

    // 동적으로 페이지 버튼 생성
    for (let i = 1; i <= totalPages; i++) {
        const pageButton = document.createElement('li');
        pageButton.className = 'page-number page-btn';
        pageButton.innerText = i;
        pageButton.onclick = () => changePage(i);

        // 현재 페이지 버튼 강조
        if (i === currentPage) {
            pageButton.classList.add('current-page');
        }

        buttonWrapper.appendChild(pageButton);
    }

    // 모든 애니메이션 숨기기
    animations.forEach(animation => {
        animation.style.display = 'none';
    });

    // 현재 페이지에 해당하는 애니메이션 보여주기
    const start = (page - 1) * 9;
    const end = page * 9;
    for (let i = start; i < end && i < animations.length; i++) {
        animations[i].style.display = 'block';
    }
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

document.addEventListener("DOMContentLoaded", function () {
    const aniListContainer = document.getElementById('ani-list-container');
    totalAni = aniListContainer.querySelectorAll('.ani-info-container').length;
    totalPages = Math.ceil(totalAni / 9);
    renderPage(currentPage);
});

