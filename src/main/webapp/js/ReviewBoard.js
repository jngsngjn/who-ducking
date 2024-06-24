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
});



/* @ 페이지 네이션 */
let currentPage = 1;
let totalAni;
let totalPages;

function renderPage(page) {
    const aniListContainer = document.getElementById('ani-list-container');
    const animations = aniListContainer.querySelectorAll('.ani-info-container');
    const buttonWrapper = document.getElementById('button-wrapper');

    currentPage = page;

    buttonWrapper.innerHTML = '';

    for (let i = 1; i <= totalPages; i++) {
        const pageButton = document.createElement('li');
        pageButton.className = 'page-number page-btn';
        pageButton.innerText = i;
        pageButton.onclick = () => changePage(i);

        if (i === currentPage) {
            pageButton.classList.add('current-page');
        }

        buttonWrapper.appendChild(pageButton);
    }

    animations.forEach(animation => {
        animation.style.display = 'none';
    });

    const start = (page - 1) * 12;
    const end = page * 12;
    for (let i = start; i < end && i < animations.length; i++) {
        animations[i].style.display = 'block';
    }
    window.scrollTo(0, 0);
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
    totalPages = Math.ceil(totalAni / 12);
    renderPage(currentPage);
});

