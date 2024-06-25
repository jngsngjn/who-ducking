document.addEventListener('DOMContentLoaded', function() {
    const orderRecentBtn = document.getElementById('ordered-recent');
    const orderLikeBtn = document.getElementById('ordered-review');

    orderRecentBtn.classList.add('active');

    function handleButtonClick(event) {
        if (event.target === orderRecentBtn) {
            orderRecentBtn.classList.add('active');
            orderLikeBtn.classList.remove('active');
        } else if (event.target === orderLikeBtn) {
            orderLikeBtn.classList.add('active');
            orderRecentBtn.classList.remove('active');
        }
    }

    orderRecentBtn.addEventListener('click', handleButtonClick);
    orderLikeBtn.addEventListener('click', handleButtonClick);
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



// 최신순 정렬 함수
function sortByAnimationId() {
    console.log("최신순 정렬 버튼 클릭");
    const aniListContainer = document.getElementById('ani-list-container');
    const arrayAnimations = Array.from(aniListContainer.getElementsByClassName('ani-info-container'));
    const totalAniCount = arrayAnimations.length;
    console.log(typeof totalAniCount)

        console.log("최신순 정렬")
    arrayAnimations.map(e => {
        console.log(e)
    })

    arrayAnimations.sort((a, b) => {
        let aAnimationId = parseInt(a.getAttribute('data-animation-id'), 10);
        let bAnimationId = parseInt(b.getAttribute('data-animation-id'), 10);
        return bAnimationId - aAnimationId;
    });

    aniListContainer.innerHTML = '';
    arrayAnimations.forEach(container => aniListContainer.appendChild(container));

    renderPage(currentPage);
}

// 리뷰순 정렬 함수
function sortByReviewCount() {

    const aniListContainer = document.getElementById('ani-list-container');
    const arrayAnimations = Array.from(aniListContainer.getElementsByClassName('ani-info-container'));
    const totalAniCount = arrayAnimations.length;

    console.log("리뷰순 정렬")
    arrayAnimations.map(e => {
        console.log(e)
    })

    arrayAnimations.sort((a, b) => {
        let aReviewCount = parseInt(a.getAttribute('data-review-count'), 10);
        let bReviewCount = parseInt(b.getAttribute('data-review-count'), 10);
        return bReviewCount - aReviewCount;
    });

    aniListContainer.innerHTML = '';
    arrayAnimations.forEach(container => aniListContainer.appendChild(container));

    renderPage(currentPage);
}

