$(document).ready(function () {
    localStorage.removeItem('searchInput');
    loadLikesDislikes();
    loadBookmarkState();

    $('#board-bookmark-button').click(function () {
        var boardId = $(this).data('board-id');
        var button = $(this);

        $.ajax({
            url: '/board/' + boardId + '/bookmark',
            type: 'POST',
            dataType: 'json',
            success: function (response) {
                var isBookmarked = response.bookmarked;
                button.toggleClass('bookmarked', isBookmarked);

                // 로컬 스토리지에 북마크 상태 저장
                saveBookmarkState(boardId, isBookmarked);
            },
            error: function (xhr, status, error) {
                console.log('Error:', error);
                swal({
                    title: "북마크 에러",
                    text: "북마크 상태를 업데이트하는 중 오류가 발생했습니다.",
                    icon: "error",
                    button: "확인"
                });
            }
        });
    });

    // 댓글 수정
    $('.comment-edit-form').each(function () {
        const editForm = $(this);
        const originalContent = editForm.closest('.comment-item').find('.comment-writer-result').text().trim();
        editForm.find('input[name="contentUpdate"]').val(originalContent);
    });

    // 신고 라디오 버튼
    document.querySelectorAll('.report-radio-label, .report-radio-label-other').forEach(label => {
        label.addEventListener('click', function () {
            document.querySelectorAll('.report-radio-label, .report-radio-label-other').forEach(lbl => {
                lbl.classList.remove('selected');
            });
            this.classList.add('selected');

            const otherReasonInput = document.getElementById('otherReasonInput');
            if (this.classList.contains('report-radio-label-other')) {
                otherReasonInput.style.display = 'block';
            } else {
                otherReasonInput.style.display = 'none';
            }
        });
    });
});

// 좋아요 토글
function toggleLike(button) {
    let commentId = button.closest('.comment-meta').dataset.commentId;

    if (button.classList.contains('liked')) {
        button.classList.remove('liked');
    } else {
        button.classList.add('liked');
        let dislikeButton = button.nextElementSibling;
        if (dislikeButton.classList.contains('disliked')) {
            dislikeButton.classList.remove('disliked');
        }
    }
    saveLikeDislikeState(commentId, 'like', button.classList.contains('liked'));
    saveLikeDislikeState(commentId, 'dislike', false);
}

// 싫어요 토글
function toggleDislike(button) {
    let commentId = button.closest('.comment-meta').dataset.commentId;

    if (button.classList.contains('disliked')) {
        button.classList.remove('disliked');
    } else {
        button.classList.add('disliked');
        let likeButton = button.previousElementSibling;
        if (likeButton.classList.contains('liked')) {
            likeButton.classList.remove('liked');
        }
    }
    saveLikeDislikeState(commentId, 'dislike', button.classList.contains('disliked'));
    saveLikeDislikeState(commentId, 'like', false);  // Ensure like is removed
}

// 좋아요/싫어요 상태 저장
function saveLikeDislikeState(commentId, type, state) {
    let stateData = JSON.parse(localStorage.getItem('likeDislikeState')) || {};
    if (!stateData[commentId]) {
        stateData[commentId] = {};
    }
    stateData[commentId][type] = state;
    localStorage.setItem('likeDislikeState', JSON.stringify(stateData));
}

// 좋아요/싫어요 상태 로드
function loadLikesDislikes() {
    let stateData = JSON.parse(localStorage.getItem('likeDislikeState')) || {};
    document.querySelectorAll('.comment-meta').forEach(meta => {
        let commentId = meta.dataset.commentId;
        if (stateData[commentId]) {
            if (stateData[commentId].like) {
                let likeButton = meta.querySelector('.comment-likes');
                likeButton.classList.add('liked');
            }
            if (stateData[commentId].dislike) {
                let dislikeButton = meta.querySelector('.comment-dislikes');
                dislikeButton.classList.add('disliked');
            }
        }
    });
}

// 북마크 상태 저장
function saveBookmarkState(boardId, isBookmarked) {
    let bookmarkState = JSON.parse(localStorage.getItem('bookmarkState')) || {};
    bookmarkState[boardId] = isBookmarked;
    localStorage.setItem('bookmarkState', JSON.stringify(bookmarkState));
}

// 북마크 상태 로드
function loadBookmarkState() {
    var boardId = $('#board-bookmark-button').data('board-id');

    $.ajax({
        url: '/board/' + boardId + '/bookmark/status',
        type: 'GET',
        dataType: 'json',
        success: function (response) {
            var isBookmarked = response.bookmarked;
            $('#board-bookmark-button').toggleClass('bookmarked', isBookmarked);
        },
        error: function (xhr, status, error) {
            console.log('Error:', error);
        }
    });
}

// 댓글 메뉴 토글
function toggleCommentMenu(icon) {
    const commentMenu = icon.closest('.comment-item').querySelector('.comment-menu');
    commentMenu.style.display = commentMenu.style.display === 'block' ? 'none' : 'block';
}

// 신고 후 리디렉션
function incrementReportCountAndRedirect(boardId) {
    $.post('/board/' + boardId + '/report', function () {
        swal({
            title: "신고 완료",
            text: "신고가 성공적으로 접수되었습니다.",
            icon: "success",
            button: "확인"
        }).then(() => {
            window.location.href = '/board/' + boardId;
        });
    });
}
