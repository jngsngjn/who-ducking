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


// 체크박스 리스트 클릭시 체크박스 활성화
function checkGenre(element) {
    const genreCheckbox = element.querySelector('input[type="checkbox"]');
    genreCheckbox.checked = !genreCheckbox.checked;

    const genreId = genreCheckbox.id.replace('genre-', '');
    const index = selectedGenres.indexOf(genreId);

    if (genreCheckbox.checked) {
        if (index === -1) {
            selectedGenres.push(genreId);
        }
    } else {
        if (index !== -1) {
            selectedGenres.splice(index, 1);
        }
    }

    currentPage = 1;
    filterAnimations();
}

// 전체 페이지네이션과 장르별 페이지네이션
let selectedGenres = [];
let currentPage = 1;
const animationsPerPage = 12;
let totalPages = 0;

function updateURL(page) {
    const url = new URL(window.location);
    url.searchParams.set('page', page);
    history.replaceState(null, '', url);
}

function filterAnimations() {
    let animations = document.querySelectorAll('.ani-info-container');
    let filteredAnimations = [];

    if (selectedGenres.length === 0) {
        filteredAnimations = Array.from(animations);
    } else {
        animations.forEach(function(animation) {
            let genreId1 = animation.querySelector('h4:nth-of-type(1)').textContent.trim();
            let genreId2 = animation.querySelector('h4:nth-of-type(2)').textContent.trim();

            let showAnimation = selectedGenres.includes(genreId1) || selectedGenres.includes(genreId2);
            if (showAnimation) {
                filteredAnimations.push(animation);
            }
        });
    }

    animations.forEach(animation => animation.style.display = 'none');

    let startIndex = (currentPage - 1) * animationsPerPage;
    let endIndex = startIndex + animationsPerPage;
    filteredAnimations.forEach((animation, index) => {
        if (index >= startIndex && index < endIndex) {
            animation.style.display = 'block';
        }
    });

    renderPagination(filteredAnimations.length);
    window.scrollTo(0, 0);
    updateURL(currentPage);
}

function renderPagination(totalAnimations) {
    let pageNumberBox = document.querySelector('.page-button-wrapper');
    pageNumberBox.innerHTML = '';

    totalPages = Math.ceil(totalAnimations / animationsPerPage);

    for (let i = 1; i <= totalPages; i++) {
        let pageButton = document.createElement('li');
        pageButton.textContent = i;
        pageButton.classList.add('page-number', 'page-btn');
        if (i === currentPage) {
            pageButton.style.backgroundColor = '#ff8b00';
            pageButton.style.color = 'white';
        }
        pageButton.addEventListener('click', function() {
            currentPage = i;
            filterAnimations();
        });
        pageNumberBox.appendChild(pageButton);
    }
}

function changePage(page) {
    if (page >= 1 && page <= totalPages) {
        currentPage = page;
        filterAnimations();
    }
}

function prevPage() {
    if (currentPage > 1) {
        currentPage--;
        filterAnimations();
    }
}

function nextPage() {
    if (currentPage < totalPages) {
        currentPage++;
        filterAnimations();
    }
}

document.addEventListener('DOMContentLoaded', function() {
    let genreCheckboxes = document.querySelectorAll('.genre-list');

    genreCheckboxes.forEach(function(checkbox) {
        checkbox.addEventListener('change', function() {
            let genreId = checkbox.id.replace('genre-', '');
            let index = selectedGenres.indexOf(genreId);

            if (checkbox.checked) {
                if (index === -1) {
                    selectedGenres.push(genreId);
                }
            } else {
                if (index !== -1) {
                    selectedGenres.splice(index, 1);
                }
            }

            currentPage = 1;
            filterAnimations();
        });
    });

    // 이전페이지 기억용
    const urlParams = new URLSearchParams(window.location.search);
    const pageParam = parseInt(urlParams.get('page'));
    if (!isNaN(pageParam) && pageParam > 0) {
        currentPage = pageParam;
    }

    filterAnimations();
});
