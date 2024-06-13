// 별점
document.addEventListener("DOMContentLoaded", function () {
    const totalStars = 5;
    const starsContainer = document.querySelector(".stars-container");

    const starsDiv = document.createElement("div");
    starsDiv.classList.add("stars");

    let selectedRating = 0;
    let scoreSpan;

    for (let i = 0; i < totalStars; i++) {
        const starIcon = document.createElement("i");
        starIcon.classList.add("fa-solid", "fa-star");
        starIcon.dataset.index = i;

        starIcon.addEventListener("click", function () {
            selectedRating = parseInt(this.dataset.index) + 1;
            updateStars();
        });

        starsDiv.appendChild(starIcon);
    }

    scoreSpan = document.createElement("span");
    scoreSpan.classList.add("score");
    scoreSpan.textContent = `${selectedRating} `;

    starsContainer.appendChild(starsDiv);
    starsContainer.appendChild(scoreSpan);

    function updateStars() {
        const stars = starsDiv.querySelectorAll("i");
        stars.forEach((star, index) => {
            if (index < Math.floor(selectedRating)) {
                star.classList.add("filled");
                star.classList.remove("half");
            } else if (
                index === Math.floor(selectedRating) &&
                selectedRating % 1 !== 0
            ) {
                star.classList.add("half");
                star.classList.remove("filled");
            } else {
                star.classList.remove("filled");
                star.classList.remove("half");
            }
        });
        scoreSpan.textContent = `${selectedRating} `;
    }
});

// 글자수 체크
function countText() {
    let textarea = document.getElementById("review-content");
    let countSpan = document.getElementById("count");
    let count = textarea.value.length;
    countSpan.textContent = count + "/500";
}

document.getElementById("review-content").addEventListener("input", countText);

// ... 클릭시 수정 삭제 모달

// 최신순, 좋아요순 구현하기(아직안함)
document.querySelector(".order_btn").addEventListener("click", function () {
    this.classList.add("clicked");
});