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

    function getSelectedRating() {
        return selectedRating;
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

// 클릭시 수정 삭제 모달
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


// 애니메이션 정보 GET 요청 -> html에서는 되는데 왜 여기서는 안되는거지?
// $(document).ready(function() {
//     // ani-poster 클래스를 가진 이미지를 클릭했을 때 실행될 함수
//     $('.ani-poster').click(function() {
//         console.log("애니 포스터 click");
//
//         let imageId = $(this).siblings('#animation-id').text().trim();
//         console.log(imageId);
//         let animationId = parseInt(imageId);
//         console.log(animationId);
//
//         $.ajax({
//             type: 'GET',
//             url: '/community/' + animationId,
//             success: function(response) {
//                 console.log('GET 요청 성공');
//                 window.location.href = '/community/' + animationId;
//             },
//             error: function(xhr, status, error) {
//                 console.error('GET 요청 실패');
//             }
//         });
//     });
// });



// form을 안쓴다면..?
// 계정 닉네임과 아이디 찾기
$(document).ready(function() {
    $('.submit-btn').click(function() {
        // 닉네임 가져오기
        let nickname = $('.h-profile_menu_nickname').text().trim();
        console.log('닉네임:', nickname);

        // 체크박스의 체크 여부 가져오기
        let isChecked = $('#check-spoiler').prop('checked');
        if (isChecked) {
            console.log('체크됨');
        } else {
            console.log('체크되지 않음');
        }

        // 리뷰 내용 가져오기
        let reviewContent = $('#review-content').val();
        console.log('리뷰 내용:', reviewContent);

        // 별점 가져오기
        let scoreValue = $(".score").text().trim();
        console.log('현재 별점:', scoreValue);

        let data ={
            nickname: nickname,
            isChecked: isChecked,
            reviewContent: reviewContent,
            score: scoreValue,
        };

        // Ajax 요청 보내기
        $.ajax({
            type: 'POST',
            url: '/postReview/{id}',
            data: data,
            success: function (res) {
                console.log('POST 성공');
                window.location.reload();
            },
            error: function (xhr, status, error) {
                console.log('POST 실패');
            }
        });
    });




});

