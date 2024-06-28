function incrementViewCountAndRedirect(boardId) {
    $.post('/board/' + boardId + '/incrementViewCount', function() {
        window.location.href = '/board/' + boardId;
    });
}
//index.html, freeBoard.html에서 사용됨