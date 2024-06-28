function incrementViewCountAndRedirect(boardId) {
    fetch('/board/' + boardId + '/incrementViewCount',{
        method : 'post',
        headers: {
            'Content-Type' : 'application/json'
        }
    })
        .then(response => {
            if(response.ok){
                window.location.href=`/board/${boardId}`;
            }
            else{
                console.error('조회수 증가 실패');
            }
        })
        .catch(error => {
            console.error('오류 : ' , error);
        })
}
//index.html, freeBoard.html에서 사용됨