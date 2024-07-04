//좋아요
$(document).ready(function () {
    localStorage.removeItem("searchInput");
    loadLikesDislikes();
    loadBookmarkState();

    $("#board-bookmark-button").click(function () {
        var boardId = $(this).data("board-id");
        var button = $(this);

        $.ajax({
            url: "/board/" + boardId + "/bookmark",
            type: "POST",
            dataType: "json",
            success: function (response) {
                var isBookmarked = response.bookmarked;
                button.toggleClass("bookmarked", isBookmarked);

                // 로컬 스토리지에 북마크 상태 저장
                saveBookmarkState(boardId, isBookmarked);
            },
            error: function (xhr, status, error) {
                console.log("Error:", error);
                swal({
                    title: "북마크 에러",
                    text: "북마크 상태를 업데이트하는 중 오류가 발생했습니다.",
                    icon: "error",
                    button: "확인",
                });
            },
        });
    });

    // 댓글 수정
    $(".comment-edit-form").each(function () {
        const editForm = $(this);
        const originalContent = editForm
            .closest(".showFreeBoard_comment_list_item")
            .find(".showFreeBoard_comment_list_item_middle_result")
            .text()
            .trim();
        editForm.find('input[name="contentUpdate"]').val(originalContent);
    });

    // 신고 라디오 버튼
    document.querySelectorAll(".showFreeBoard_modal_list_item, .showFreeBoard_modal_list_item_etc").forEach((label) => {
        label.addEventListener("click", function () {
            document
                .querySelectorAll(".showFreeBoard_modal_list_item, .showFreeBoard_modal_list_item_etc")
                .forEach((lbl) => {
                    lbl.classList.remove("selected");
                });
            this.classList.add("selected");

            const otherReasonInput = document.getElementById("otherReasonInput");
            if (this.classList.contains("showFreeBoard_modal_list_item_etc")) {
                otherReasonInput.style.display = "block";
            } else {
                otherReasonInput.style.display = "none";
            }
        });
    });
});

function toggleCommentMenu(icon) {
    const commentMenu = icon
        .closest(".showFreeBoard_comment_list_item")
        .querySelector(".showFreeBoard_comment_list_item_menu_box");
    commentMenu.style.display = commentMenu.style.display === "block" ? "none" : "block";
}

// 좋아요 토글
function toggleLike(button) {
    let commentId = button.closest(".showFreeBoard_comment_list_item_bottom_btn_box").dataset.commentId;

    if (button.classList.contains("liked")) {
        button.classList.remove("liked");
    } else {
        button.classList.add("liked");
        let dislikeButton = button.nextElementSibling;
        if (dislikeButton.classList.contains("disliked")) {
            dislikeButton.classList.remove("disliked");
        }
    }
    saveLikeDislikeState(commentId, "like", button.classList.contains("liked"));
    saveLikeDislikeState(commentId, "dislike", false);
}

// 싫어요 토글
function toggleDislike(button) {
    let commentId = button.closest(".showFreeBoard_comment_list_item_bottom_btn_box").dataset.commentId;

    if (button.classList.contains("disliked")) {
        button.classList.remove("disliked");
    } else {
        button.classList.add("disliked");
        let likeButton = button.previousElementSibling;
        if (likeButton.classList.contains("liked")) {
            likeButton.classList.remove("liked");
        }
    }
    saveLikeDislikeState(commentId, "dislike", button.classList.contains("disliked"));
    saveLikeDislikeState(commentId, "like", false); // Ensure like is removed
}

// 좋아요/싫어요 상태 저장
function saveLikeDislikeState(commentId, type, state) {
    let stateData = JSON.parse(localStorage.getItem("likeDislikeState")) || {};
    if (!stateData[commentId]) {
        stateData[commentId] = {};
    }
    stateData[commentId][type] = state;
    localStorage.setItem("likeDislikeState", JSON.stringify(stateData));
}

function removeLikeDislikeState() {
    const commentItem = $(this).closest(".showFreeBoard_comment_list_item");
    const commentId = commentItem.find(".showFreeBoard_comment_list_item_bottom_btn_box").data("comment-id");
    let stateData = JSON.parse(localStorage.getItem("likeDislikeState")) || {};
    if (stateData.hasOwnProperty(commentId)) {
        delete stateData[commentId];
        localStorage.setItem("likeDislikeState", JSON.stringify(stateData));
        console.log(`Removed like/dislike state for comment ${commentId}`);
    }
}

// 좋아요/싫어요 상태 로드
function loadLikesDislikes() {
    let stateData = JSON.parse(localStorage.getItem("likeDislikeState")) || {};
    document.querySelectorAll(".showFreeBoard_comment_list_item_bottom_btn_box").forEach((meta) => {
        let commentId = meta.dataset.commentId;
        if (stateData[commentId]) {
            if (stateData[commentId].like) {
                let likeButton = meta.querySelector(".showFreeBoard_comment_list_item_bottom_likeBtn");
                likeButton.classList.add("liked");
            }
            if (stateData[commentId].dislike) {
                let dislikeButton = meta.querySelector(".showFreeBoard_comment_list_item_bottom_dislikeBtn");
                dislikeButton.classList.add("disliked");
            }
        }
    });
}

$(document).ready(function () {
    localStorage.removeItem("searchInput");
    // 북마크 상태를 로드
    loadBookmarkState();
    loadLikesDislikes();

    $(".showFreeBoard_top_menu_delete_btn").click(function () {
        let boardId = $(this).data("board-id");

        // 삭제 전 모든 댓글의 좋아요 및 싫어요 상태와 북마크 상태를 삭제
        removeLikeDislikeState();
        removeBookmarkState(boardId);

        // 게시글 삭제 폼 제출
        $(this).closest("form").submit();
    });

    $("#board-bookmark-button").click(function () {
        var boardId = $(this).data("board-id");
        var button = $(this);

        $.ajax({
            url: "/board/" + boardId + "/bookmark",
            type: "POST",
            dataType: "json",
            success: function (response) {
                var isBookmarked = response.bookmarked;
                button.toggleClass("bookmarked", isBookmarked);
            },
        });
    });
});

// 북마크 상태 저장
function saveBookmarkState(boardId, isBookmarked) {
    let bookmarkState = JSON.parse(localStorage.getItem("bookmarkState")) || {};
    bookmarkState[boardId] = isBookmarked;
    localStorage.setItem("bookmarkState", JSON.stringify(bookmarkState));
}

function removeBookmarkState(boardId) {
    let bookmarkState = JSON.parse(localStorage.getItem("bookmarkState")) || {};
    if (bookmarkState.hasOwnProperty(boardId)) {
        delete bookmarkState[boardId];
        localStorage.setItem("bookmarkState", JSON.stringify(bookmarkState));
    }
}

// 북마크 상태 로드
function loadBookmarkState() {
    var boardId = $("#board-bookmark-button").data("board-id");

    $.ajax({
        url: "/board/" + boardId + "/bookmark/status",
        type: "GET",
        dataType: "json",
        success: function (response) {
            var isBookmarked = response.bookmarked;
            $("#board-bookmark-button").toggleClass("bookmarked", isBookmarked);
        },
        error: function (xhr, status, error) {
            console.log("Error:", error);
        },
    });
}

// 신고 후 리디렉션
function incrementReportCountAndRedirect(boardId) {
    $.post("/board/" + boardId + "/report", function () {
        swal({
            title: "신고 완료",
            text: "신고가 성공적으로 접수되었습니다.",
            icon: "success",
            button: "확인",
        }).then(() => {
            window.location.href = "/board/" + boardId;
        });
    });
}

document.addEventListener("DOMContentLoaded", function () {
    const commentInput = document.getElementById("comment-input");
    const charCounter = document.getElementById("char-counter");
    const boardImagePreview = document.getElementById("boardImagePreview");
    // 0/500 내용 카운트
    if (commentInput && charCounter) {
        commentInput.addEventListener("input", function () {
            const maxLength = commentInput.getAttribute("maxlength");
            const currentLength = commentInput.value.length;
            charCounter.textContent = `${currentLength} / ${maxLength}`;
        });
    }
    loadLikesDislikes();
});

document.addEventListener("DOMContentLoaded", function () {
    const toggleButtons = document.querySelectorAll(".showFreeBoard_top_toggleIcon");
    toggleButtons.forEach((button) => {
        button.addEventListener("click", function () {
            toggleBoardMenu(this);
        });
    });

    const editButtons = document.querySelectorAll(".showFreeBoard_comment_list_item_editBtn");
    editButtons.forEach((button) => {
        button.addEventListener("click", function () {
            const commentItem = this.closest(".showFreeBoard_comment_list_item");
            const commentText = commentItem.querySelector(".showFreeBoard_comment_list_item_middle_result");
            const editTextarea = commentItem.querySelector(".showFreeBoard_comment_list_item_middle_edit");
            const saveButton = commentItem.querySelector(".showFreeBoard_comment_list_item_middle_editBtn");
            const cancelButton = commentItem.querySelector(".showFreeBoard_comment_list_item_middle_cancelBtn");

            editTextarea.value = commentText.textContent;
            commentText.style.display = "none";
            editTextarea.style.display = "block";
            saveButton.style.display = "inline-block";
            cancelButton.style.display = "inline-block";

            saveButton.addEventListener("click", function () {
                const newText = editTextarea.value;
                commentText.textContent = newText;
                commentText.style.display = "block";
                editTextarea.style.display = "none";
                saveButton.style.display = "none";
                cancelButton.style.display = "none";
            });

            cancelButton.addEventListener("click", function () {
                commentText.style.display = "block";
                editTextarea.style.display = "none";
                saveButton.style.display = "none";
                cancelButton.style.display = "none";
            });
        });
    });

    document.addEventListener("click", function (event) {
        const commentMenus = document.querySelectorAll(".showFreeBoard_comment_list_item_menu_box");
        commentMenus.forEach((menu) => {
            if (
                !menu.contains(event.target) &&
                !menu.closest(".showFreeBoard_comment_list_item").contains(event.target)
            ) {
                menu.style.display = "none";
            }
        });
    });

    const openModalBtn = document.querySelector(".showFreeBoard_top_menu_report_btn");
    const closeModalBtn = document.querySelector(".showFreeBoard_modal_cancel_btn");
    const modal = document.getElementById("reportModalOverlay");

    openModalBtn.addEventListener("click", function (e) {
        e.preventDefault();
        modal.style.display = "block";
    });

    closeModalBtn.addEventListener("click", function () {
        modal.style.display = "none";
    });

    window.addEventListener("click", function (e) {
        if (e.target === modal) {
            modal.style.display = "none";
        }
    });

    document.querySelectorAll(".showFreeBoard_modal_list_item").forEach((label) => {
        label.addEventListener("click", function () {
            document.querySelectorAll(".showFreeBoard_modal_list_item").forEach((lbl) => {
                lbl.classList.remove("selected");
            });
            this.classList.add("selected");
        });
    });
});

document.getElementById("delete-board-button").addEventListener("click", function () {
    const boardId = this.getAttribute("data-board-id");
    localStorage.removeItem(`board_like_${boardId}`);
    localStorage.removeItem(`board_bookmark_${boardId}`);
    this.closest("form").submit();
});

function toggleBoardMenu(icon) {
    const boardMenu = icon.nextElementSibling;
    if (boardMenu) {
        boardMenu.style.display = boardMenu.style.display === "block" ? "none" : "block";
    } else {
        console.error("Board menu not found");
    }
}
