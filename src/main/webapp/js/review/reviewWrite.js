// 별점 -> (o)
document.addEventListener("DOMContentLoaded", function () {
    const totalStars = 5;
    const starsContainer = document.querySelector(".stars-container");
    const scoreInput = document.getElementById("score-number");

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
            } else if (index === Math.floor(selectedRating) && selectedRating % 1 !== 0) {
                star.classList.add("half");
                star.classList.remove("filled");
            } else {
                star.classList.remove("filled");
                star.classList.remove("half");
            }
        });
        scoreSpan.textContent = `${selectedRating} `;
        scoreInput.value = selectedRating;
    }
});


// 글자수 체크 -> (o)
document.addEventListener("DOMContentLoaded", function () {
    function countText() {
        let reviewContent = document.getElementById("review-content");
        let countSpan = document.getElementById("count");
        let count = reviewContent.value.length;

        countSpan.textContent = count + "/500";

        if (count > 500) {
            reviewContent.value = reviewContent.value.substring(0, 500);
            reviewContent.style.border = "2px solid red";
            reviewContent.style.borderRadius ="10px";
            reviewContent.style.padding="1rem";
        } else {
            reviewContent.style.border = "";
        }
    }

    document
        .getElementById("review-content")
        .addEventListener("input", countText);
});



// 최신순 인기순 버튼 활성 -> (o) / 기능 -> (o)
document.addEventListener('DOMContentLoaded', function() {
    const recentBtn = document.getElementById('recent');
    const likeBtn = document.getElementById('like');

    recentBtn.classList.add('active');

    function handleButtonClick(event) {

        recentBtn.classList.remove('active');
        likeBtn.classList.remove('active');

        event.target.classList.add('active');
    }

    recentBtn.addEventListener('click', handleButtonClick);
    likeBtn.addEventListener('click', handleButtonClick);
});


// 클릭시 수정 삭제 모달창 -> (o)
document.addEventListener("DOMContentLoaded", function () {
    const showModalButtons = document.querySelectorAll("[id^='show-modal-']");
    const modalContainers = document.querySelectorAll("[id^='modal-container-']");

    showModalButtons.forEach((showModalButton, index) => {
        const modalContainer = modalContainers[index];

        showModalButton.addEventListener("click", function () {
            modalContainer.style.display = "block";
        });

        document.addEventListener("click", function (event) {
            if (event.target === showModalButton || modalContainer.contains(event.target)) {
                return;
            }
            modalContainer.style.display = "none";
        });
    });
});




// 리뷰 삭제 -> (o)
document.addEventListener("DOMContentLoaded", function () {
    const deleteButtons = document.querySelectorAll(".delete-review-btn");

    deleteButtons.forEach(deleteButton => {
        deleteButton.addEventListener("click", function () {
            const reviewId = this.getAttribute("data-review-id");
            deleteReview(reviewId);
        });
    });

    function deleteReview(reviewId) {
        if (confirm("정말로 이 리뷰를 삭제하시겠습니까?")) {
            fetch(`/deleteReview/${reviewId}`, {
                method: "DELETE"
            })
                .then(res => {
                    if (res.ok) {
                        alert("리뷰가 삭제되었습니다.");
                        window.location.reload();
                    } else {
                        alert("리뷰 삭제에 실패했습니다.");
                    }
                })
                .catch(error => {
                    console.error("server Error deleting review:", error);
                });
        }
    }
});



// 좋아요 요청 함수 ->(o)
function likeReview(reviewId) {
    let url = `/reviews/${reviewId}/like`;

    fetch(url, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(res => {
        if (!res.ok) {
            return res.text().then(text => {
                let error = new Error(text);
                error.status = res.status;
                throw error;
            });
        }
        return res.json();
    }).then(data => {
        window.location.reload();
    }).catch(error => {
        console.error('서버 에러: 좋아요 액션 중 오류 발생', error);
    });
}


// 싫어요 요청 함수
function dislikeReview(reviewId) {
    let url = `/reviews/${reviewId}/dislike`;

    fetch(url, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(res => {
        if (!res.ok) {
            return res.text().then(text => {
                let error = new Error(text);
                error.status = res.status;
                throw error;
            });
        }
        return res.json();
    }).then(data => {
        window.location.reload();
    }).catch(error => {
        console.error('서버 에러: 싫어요 액션 중 오류 발생', error.status);
    });
}


// 리뷰 작성 시 빈 값 요청 불가
document.addEventListener("DOMContentLoaded", function() {
    const form = document.querySelector(".write-box");
    const reviewContent = document.getElementById("review-content");

    form.addEventListener("submit", function(event) {
        let isValid = true;
        let errorMessage = "";

        if (reviewContent.value.trim() === "") {
            isValid = false;
            errorMessage += "리뷰 내용을 입력해주세요.\n";
        }

        if (!isValid) {
            event.preventDefault();
            alert(errorMessage);
        }
    });
});


// 스포일러 체크 여부에따라 보여주기
document.addEventListener('DOMContentLoaded', function () {
    const spoilerCheckbox = document.getElementById('show-spoiler');

    spoilerCheckbox.addEventListener('change', function () {
        const showSpoiler = this.checked;
        const reviews = document.querySelectorAll('.review-list');

        reviews.forEach(review => {
            const isSpoiler = review.querySelector('.is-reviews-spoiler').value === 'true';
            if (showSpoiler) {
                review.classList.remove('hidden');
            } else {
                if (isSpoiler) {
                    review.classList.add('hidden');
                } else {
                    review.classList.remove('hidden');
                }
            }
        });
    });

    spoilerCheckbox.dispatchEvent(new Event('change'));
});


// 리뷰 정렬하기 &  수정 하기
document.addEventListener("DOMContentLoaded", function() {
    let currentOrder = 'recent';

    function showReviews(order) {
        currentOrder = order;
        if (order === 'recent') {
            document.getElementById('recent-reviews').style.display = 'block';
            document.getElementById('like-reviews').style.display = 'none';
        } else if (order === 'like') {
            document.getElementById('recent-reviews').style.display = 'none';
            document.getElementById('like-reviews').style.display = 'block';
        }
    }

    document.body.addEventListener("click", function(event) {
        if (event.target.classList.contains("order-btn")) {
            let order = event.target.id;
            console.log(order);
            showReviews(order);
        }

        if (event.target.classList.contains("update-review-btn")) {
            let reviewId = event.target.dataset.reviewId;
            reviewUpdate(reviewId);
        }
    });

    // 리뷰의 모달창 수정버튼 클릭시 id 찾는 함수
    function clickUpdateBtn(e) {
        let reviewId = e.target.dataset.reviewId;
        console.log("수정 버튼을 눌렀을때의 리뷰 id = " + reviewId)
        reviewUpdate(reviewId);
    }

    // 리뷰 업데이트 처리 함수
    function reviewUpdate(reviewId) {
        let reviewBox = document.querySelector(`#recent-reviews #review-${reviewId}`);
        let reviewLikeBox = document.querySelector(`#like-reviews #review-${reviewId}`);

        if (!reviewBox || !reviewLikeBox) {
            console.error('Review box not found for review ID:', reviewId);
            return;
        }

        function updateReview(reviewBox) {
            let reviewComment = reviewBox.querySelector(".review-comment .short-content");
            let fullContent = reviewComment.getAttribute('data-full-content');

            let currentText = document.createElement("textarea");
            currentText.value = fullContent;
            currentText.className = "update-comment-textarea";

            reviewComment.replaceWith(currentText);

            let saveButton = document.createElement("button");
            saveButton.innerText = "수정";
            saveButton.className = "save-review-btn";

            let showMoreBtn = reviewBox.querySelector(".toggle-read-on");
            let offMoreBtn = reviewBox.querySelector(".toggle-read-off");
            let updateModalContainer = reviewBox.querySelector(".update-modal-container");
            let likeBtn = reviewBox.querySelector("#recommend-like");
            let dislikeBtn = reviewBox.querySelector("#recommend-dislike");
            showMoreBtn.style.display = 'none';
            offMoreBtn.style.display = 'none';
            updateModalContainer.style.display = 'none';
            likeBtn.style.display = 'none';
            dislikeBtn.style.display = 'none';

            // 엔터 입력때문에 임치 주석 처리
            // currentText.addEventListener("keydown", function(event) {
            //     if (event.keyCode === 13) {
            //         event.preventDefault();
            //         saveButton.click();
            //     }
            // });

            // 글자수 체크 함수
            currentText.addEventListener('input', function(){
                let updatedContent = currentText.value.trim();
                let saveBtn = reviewBox.querySelector(".save-review-btn");

                if (updatedContent.length === 0) {
                    alert("내용을 입력하세요.");
                    currentText.focus();
                    saveBtn.disabled = true;
                } else if(updatedContent.length > 500) {
                    alert("500자를 초과할 수 없습니다. 현재 글자수 : " + updatedContent.length)
                    currentText.focus();
                    saveBtn.disabled = true;
                } else {
                    saveBtn.disabled = false;
                }
            });

            saveButton.addEventListener("click", function() {
                let updatedContent = currentText.value;

                fetch(`/reviews/patch/${reviewId}`, {
                    method: 'PATCH',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        content: updatedContent
                    })
                })
                    .then(res => res.text())
                    .then(data => {
                        console.log('Response text:', data);

                        if (data === "Review updated successfully") {
                            window.location.reload();
                        } else {
                            console.error('Error updating review:', data);
                        }
                    })
                    .catch(error => {
                        console.error('Failed to fetch when updating review:', error);
                    });
            });

            currentText.after(saveButton);
        }

        updateReview(reviewBox);

        updateReview(reviewLikeBox);
    }

    let likeReviewUpdateButtons = document.querySelectorAll('#like-reviews .update-review-btn');
    likeReviewUpdateButtons.forEach(button => {
        button.addEventListener('click', clickUpdateBtn);
    });

    showReviews('recent');
});


// @ 리뷰 작성 불가시 textarea 비활성화
document.addEventListener("DOMContentLoaded", function() {
    function getQueryParams() {
        const params = {};
        window.location.search.substring(1).split("&").forEach(function(part) {
            const [key, value] = part.split("=");
            if (key) {
                params[decodeURIComponent(key)] = decodeURIComponent(value.replace(/\+/g, " "));
            }
        });
        return params;
    }

    const params = getQueryParams();
    if (params.error) {
        // 에러 메시지가 있으면 textarea를 비활성화
        document.getElementById("review-content").disabled = true;
    }
});

// 보여지는 리뷰의 글자수에따라 토글 활성화
document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('.review-comment').forEach(function(reviewComment) {
        const shortReview = reviewComment.querySelector('.short-content');
        const hiddenReview = reviewComment.querySelector('.extra-content');
        const showMoreReview = reviewComment.querySelector('.toggle-read-on');
        const hideReview = reviewComment.querySelector('.toggle-read-off');
        const fullContent = shortReview.getAttribute('data-full-content');

        console.log("fullContent" +fullContent)

        if (fullContent.length > 200) {
            shortReview.innerText = fullContent.substring(0, 200) + '...';
            hiddenReview.innerText = fullContent.substring(200);
        } else {
            showMoreReview.style.display = 'none';
        }

        showMoreReview.addEventListener('click', function() {
            reviewComment.classList.add('expanded');
            shortReview.innerText = fullContent;
            hiddenReview.style.display = 'none';
            showMoreReview.style.display = 'none';
            hideReview.style.display = 'inline';
        });

        hideReview.addEventListener('click', function() {
            reviewComment.classList.remove('expanded');
            shortReview.innerText = fullContent.substring(0, 200) + '...';
            hiddenReview.style.display = 'none';
            showMoreReview.style.display = 'inline';
            hideReview.style.display = 'none';
        });
    });
});

