document.addEventListener('DOMContentLoaded', (event) => {
    loadLikesDislikes();
});

function toggleLike(button) {
    let commentId = button.parentElement.dataset.commentId;

    if (button.classList.contains('liked')) {
        // 좋아요 취소
        button.classList.remove('liked');
    } else {
        // 좋아요 추가
        button.classList.add('liked');
        // 싫어요 버튼이 눌려있는 경우 취소
        let dislikeButton = button.nextElementSibling;
        if (dislikeButton.classList.contains('disliked')) {
            toggleDislike(dislikeButton);
        }
    }
    saveLikeDislikeState(commentId, 'like', button.classList.contains('liked'));
}

function toggleDislike(button) {
    let commentId = button.parentElement.dataset.commentId;

    if (button.classList.contains('disliked')) {
        // 싫어요 취소
        button.classList.remove('disliked');
    } else {
        // 싫어요 추가
        button.classList.add('disliked');
        // 좋아요 버튼이 눌려있는 경우 취소
        let likeButton = button.previousElementSibling;
        if (likeButton.classList.contains('liked')) {
            toggleLike(likeButton);
        }
    }
    saveLikeDislikeState(commentId, 'dislike', button.classList.contains('disliked'));
}

function saveLikeDislikeState(commentId, type, state) {
    let stateData = JSON.parse(localStorage.getItem('likeDislikeState')) || {};
    console.log('Saving state:', commentId, type, state); // 디버깅용 로그
    if (!stateData[commentId]) {
        stateData[commentId] = {};
    }
    stateData[commentId][type] = state;
    localStorage.setItem('likeDislikeState', JSON.stringify(stateData));
}

function loadLikesDislikes() {
    let stateData = JSON.parse(localStorage.getItem('likeDislikeState')) || {};
    console.log('Loaded state:', stateData); // 디버깅용 로그
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