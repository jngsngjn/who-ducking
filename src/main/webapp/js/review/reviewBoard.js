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


// 전체 페이지네이션과 장르별 페이지네이션 10개씩  + 최신순, 리뷰순 합침
let selectedGenres = [];
let currentPage = 1;
const animationsPerPage = 12;
let totalPages = 0;

// 최대 보여줄 페이지네이션 개수
const showMaxPageNum = 10;

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

    let emptyContainer = document.getElementById('empty-container');
    let pageBtnBox = document.getElementById('page-btn-box');
    let orderContainer = document.querySelector('.order-container');

    if (filteredAnimations.length === 0) {
        emptyContainer.style.display = "block";
        pageBtnBox.style.display = "none";
        orderContainer.style.display = "none";
    } else {
        emptyContainer.style.display = "none";
        pageBtnBox.style.display = "flex";
        orderContainer.style.display = "flex";
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

    let startPage = Math.floor((currentPage - 1) / showMaxPageNum) * showMaxPageNum + 1;
    let endPage = Math.min(startPage + showMaxPageNum - 1, totalPages);

    for (let i = startPage; i <= endPage; i++) {
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
    updateNavigationButtons();
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

function updateNavigationButtons() {
    let firstPageBtn = document.getElementById('to-first');
    let prevPageBtn = document.getElementById('to-prev');
    let nextPageBtn = document.getElementById('to-next');
    let lastPageBtn = document.getElementById('to-end');

    let startPage = Math.floor((currentPage - 1) / showMaxPageNum) * showMaxPageNum + 1;
    let endPage = Math.min(startPage + showMaxPageNum - 1, totalPages);

    if (currentPage === 1) {
        firstPageBtn.style.pointerEvents = 'none';
        prevPageBtn.style.pointerEvents = 'none';
    } else {
        firstPageBtn.style.pointerEvents = '';
        prevPageBtn.style.pointerEvents = '';
    }

    if (currentPage === totalPages) {
        nextPageBtn.style.pointerEvents = 'none';
        lastPageBtn.style.pointerEvents = 'none';
    } else {
        nextPageBtn.style.pointerEvents = '';
        lastPageBtn.style.pointerEvents = '';
    }
}

function sortByAnimationId() {
    let animations = Array.from(document.querySelectorAll('.ani-info-container'));
    animations.sort((a, b) => {
        return parseInt(b.getAttribute('data-animation-id')) - parseInt(a.getAttribute('data-animation-id'));
    });
    updateAnimationOrder(animations);
}

function sortByReviewCount() {
    let animations = Array.from(document.querySelectorAll('.ani-info-container'));
    animations.sort((a, b) => {
        return parseInt(b.getAttribute('data-review-count')) - parseInt(a.getAttribute('data-review-count'));
    });
    updateAnimationOrder(animations);
}

function updateAnimationOrder(sortedAnimations) {
    let container = document.querySelector('.ani-list-container');
    container.innerHTML = '';
    sortedAnimations.forEach(animation => {
        container.appendChild(animation);
    });
    currentPage = 1;
    filterAnimations();
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

    const urlParams = new URLSearchParams(window.location.search);
    const pageParam = parseInt(urlParams.get('page'));
    if (!isNaN(pageParam) && pageParam > 0) {
        currentPage = pageParam;
    }

    filterAnimations();
});
