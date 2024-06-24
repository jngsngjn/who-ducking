//좋아요, 싫어요
// document.addEventListener('DOMContentLoaded', (event) => {
//     console.log("이벤트 발생");
//     loadLikesDislikes();
//     console.log("컨텐트 로드 성공");
// });
$(document).ready(function (){
    console.log("이벤트 발생");
    loadLikesDislikes();
});

function toggleLike(button) {
    let commentId = button.parentElement.dataset.commentId;

    if (button.classList.contains('liked')) {
        // 좋아요 취소
        button.classList.remove('liked');
        saveLikeDislikeState(commentId, 'like', false);
    } else {
        // 좋아요 추가
        button.classList.add('liked');
        saveLikeDislikeState(commentId, 'like', true);

        // 싫어요 버튼이 눌려있는 경우 취소
        let dislikeButton = button.nextElementSibling;
        if (dislikeButton.classList.contains('disliked')) {
            dislikeButton.classList.remove('disliked');
            saveLikeDislikeState(commentId, 'dislike', false);
        }
    }
}

function toggleDislike(button) {
    let commentId = button.parentElement.dataset.commentId;

    if (button.classList.contains('disliked')) {
        // 싫어요 취소
        button.classList.remove('disliked');
        saveLikeDislikeState(commentId, 'dislike', false);

    } else {
        // 싫어요 추가
        button.classList.add('disliked');
        saveLikeDislikeState(commentId, 'dislike', true);

        // 좋아요 버튼이 눌려있는 경우 취소
        let likeButton = button.previousElementSibling;
        if (likeButton.classList.contains('liked')) {
            likeButton.classList.remove('liked');
            saveLikeDislikeState(commentId, 'like', false);
        }
    }
}

function saveLikeDislikeState(commentId, type, state) {
    let stateData = JSON.parse(localStorage.getItem('likeDislikeState')) || {};
    if (!stateData[commentId]) {
        stateData[commentId] = {};
    }
    stateData[commentId][type] = state;
    console.log('Save state :', stateData);
    localStorage.setItem('likeDislikeState', JSON.stringify(stateData));
}

function loadLikesDislikes() {
    let stateData = JSON.parse(localStorage.getItem('likeDislikeState')) || {};
    console.log('Loaded state:', stateData); // 디버깅용 로그
    document.querySelectorAll('.comment-meta').forEach(meta => {
        let commentId = meta.dataset.commentId;
        console.log('Processing commentId:', commentId);
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


//북마크
$(document).ready(function() {

    loadBookmarkState();

    var isBookmarked = $('#board-bookmark-button').hasClass('bookmarked');

    $('#board-bookmark-button').click(function() {
        var boardId = $(this).data('board-id');
        var button = $(this);

        $.ajax({
            url: '/board/' + boardId + '/bookmark',
            type: 'POST',
            dataType: 'json',
            success: function(response) {
                isBookmarked = response.bookmarked;
                button.toggleClass('bookmarked', isBookmarked);
                button.text(isBookmarked ? '북마크됨' : '북마크');

                saveBookmarkState(boardId, isBookmarked);
            },
            error: function(xhr, status, error) {
                console.log('Error:', error);
                alert('북마크 처리 중 오류가 발생했습니다.');
            }
        });
    });
});

function saveBookmarkState(boardId, isBookmarked){
    let bookmarkState = JSON.parse(localStorage.getItem('bookmarkState')) || {};
    bookmarkState[boardId] = isBookmarked;
    localStorage.setItem('bookmarkState', JSON.stringify(bookmarkState));
}

function loadBookmarkState(){
    let bookmarkState = JSON.parse(localStorage.getItem('bookmarkState')) || {};
    var boardId = $('#board-bookmark-button').data('board-id');

    if(bookmarkState[boardId]){
        $('#board-bookmark-button').addClass('bookmarked').text('북마크됨');
    }
    else{
        $('#board-bookmark-button').removeClass('bookmarked').text('북마크');
    }
}