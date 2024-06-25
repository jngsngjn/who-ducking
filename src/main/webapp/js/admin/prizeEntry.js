/** 응모하기 버튼을 눌렀을 때
1. 오늘 응모했는지 확인
2. 레벨에 맞는 경품인지 확인
3. 포인트 있는지 확인
4. 주소 입력되어 있는지 확인
*/

$(document).ready(function() {
    // 응모하기 버튼 클릭 시
    $('#entry-button').click(function() {
        $.ajax({
            url: '/check-user/' + prizeId,
            type: 'POST',
            success: function(response) {
                if (response.alreadyEntry) {
                    alert('이미 오늘 경품에 응모하셨습니다.');
                } else if (!response.validGradeAndLevel) {
                    alert('해당 경품에 응모할 수 있는 레벨이 아닙니다.');
                } else if (!response.hasEnoughPoints) {
                    alert('포인트가 부족합니다.');
                } else if (response.addressEmpty) {
                    alert('경품에 응모하기 전, 마이페이지에서 주소를 입력해 주세요.\n마이페이지로 이동합니다.');
                    window.location.href = '/myPage';
                } else {
                    if (confirm('해당 경품에 응모하시겠습니까? (30P 차감)\n응모는 하루에 한 번만 가능합니다.')) {
                        $.ajax({
                            url: '/entry-prize/' + prizeId,
                            type: 'POST',
                            success: function(response) {
                                alert(response);
                            },
                            error: function(error) {
                                alert('응모 처리 중 오류가 발생했습니다. 나중에 다시 시도해주세요.');
                            }
                        });
                    }
                }
            },
            error: function(error) {
                alert('서버 오류가 발생했습니다. 나중에 다시 시도해주세요.');
            }
        });
    });
});