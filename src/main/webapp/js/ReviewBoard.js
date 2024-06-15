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
});

//
const aniInPage = 9;
let currentPage = 1;
const totalItems = 0;
const totalPages = Math.ceil(totalItems / aniInPage);

function renderPage(page) {
    const aniListContainer = document.getElementById('ani-list-container');
    const start = (page - 1) * aniInPage;
    const end = page * aniInPage;

    const items = aniListContainer.querySelectorAll('.ani-info-container');
    for (let i = 0; i < items.length; i++) {
        items[i].style.display = 'none';
    }

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

renderPage(1);

