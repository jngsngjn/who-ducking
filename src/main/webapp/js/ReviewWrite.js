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
document.addEventListener("DOMContentLoaded", function () {
    function countText() {
        let reviewContent = document.getElementById("review-content");
        let countSpan = document.getElementById("count");
        let count = reviewContent.value.length;

        countSpan.textContent = count + "/500";

        if (count > 500) {
            reviewContent.value = reviewContent.value.substring(0, 500);
            reviewContent.style.border = "1px solid red";
        } else {
            reviewContent.style.border = "";
        }
    }

    document
        .getElementById("review-content")
        .addEventListener("input", countText);
});

// ... 클릭시 수정 삭제 모달
document.addEventListener("DOMContentLoaded", function () {
    var showModal = document.getElementById("show-modal");
    var modalContainer = document.getElementById("modal-container");

    showModal.addEventListener("click", function () {
        modalContainer.style.display = "block";
    });

    document.addEventListener("click", function (event) {
        if (
            event.target === showModal ||
            modalContainer.contains(event.target)
        ) {
            return;
        }
        modalContainer.style.display = "none";
    });
});





