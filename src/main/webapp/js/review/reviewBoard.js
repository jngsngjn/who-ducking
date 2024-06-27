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

function updateURL(page) {
    const url = new URL(window.location);
    url.searchParams.set('page', page);
    history.replaceState(null, '', url);
}

function renderPage(page) {
    const aniListContainer = document.getElementById('ani-list-container');
    const animations = aniListContainer.querySelectorAll('.ani-info-container');
    const buttonWrapper = document.getElementById('button-wrapper');

    currentPage = page;

    updateURL(page);

    buttonWrapper.innerHTML = '';

    for (let i = 1; i <= totalPages; i++) {
        const pageButton = document.createElement('li');
        pageButton.className = 'page-number page-btn';
        pageButton.innerText = i;
        pageButton.onclick = () => changePage(i);

        if (i === currentPage) {
            pageButton.style.backgroundColor = '#ff8b00';
            pageButton.style.color = 'white';
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

    const urlParams = new URLSearchParams(window.location.search);
    const pageParam = parseInt(urlParams.get('page'), 10);

    if (!isNaN(pageParam) && pageParam > 0 && pageParam <= totalPages) {
        currentPage = pageParam;
    }

    renderPage(currentPage);
});



// 최신순 정렬 함수
function sortByAnimationId() {

    const aniListContainer = document.getElementById('ani-list-container');
    const arrayAnimations = Array.from(aniListContainer.getElementsByClassName('ani-info-container'));
    const totalAniCount = arrayAnimations.length;

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


    arrayAnimations.sort((a, b) => {
        let aReviewCount = parseInt(a.getAttribute('data-review-count'), 10);
        let bReviewCount = parseInt(b.getAttribute('data-review-count'), 10);
        return bReviewCount - aReviewCount;
    });

    aniListContainer.innerHTML = '';
    arrayAnimations.forEach(container => aniListContainer.appendChild(container));

    renderPage(currentPage);
}


// 장르 선택시 보여질 애니메이션 컨트롤
let selectedGenres = [];

// 장르 체크박스 클릭 시 필터링 함수
function filterAnimations() {

    let animations = document.querySelectorAll('.ani-info-container');

    animations.forEach(function(animation) {

        let genreId1 = animation.querySelector('h4:nth-of-type(1)').textContent.trim();
        let genreId2 = animation.querySelector('h4:nth-of-type(2)').textContent.trim();

        let showAnimation = selectedGenres.includes(genreId1) || selectedGenres.includes(genreId2);

        if (showAnimation) {
            animation.style.display = 'block';
        } else {
            animation.style.display = 'none';
        }
    });
}

// 장르 체크박스 클릭 시 호출
document.addEventListener('DOMContentLoaded', function() {
    let genreCheckboxes = document.querySelectorAll('.genre-list');

    genreCheckboxes.forEach(function(checkbox) {
        checkbox.addEventListener('change', function() {
            let genreId = checkbox.id.replace('genre-', '');

            if (checkbox.checked) {
                selectedGenres.push(genreId);
                console.log("선택된 장르 : " + selectedGenres)
            } else {
                let index = selectedGenres.indexOf(genreId);
                if (index !== -1) {
                    selectedGenres.splice(index, 1);
                    console.log("선택 해제후 남은 장르 : " + selectedGenres)
                }
            }

            filterAnimations();
        });
    });
});