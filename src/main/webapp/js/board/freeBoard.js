function incrementViewCountAndRedirect(boardId) {
    $.post('/board/' + boardId + '/incrementViewCount', function() {
        window.location.href = '/board/' + boardId;
    });
}