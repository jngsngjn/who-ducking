$(document).ready(function () {
    // 초기 폼 상태 저장
    let initialFormState = new FormData(document.querySelector('form'));

    // 폼 제출 이벤트 리스너 추가
    document.querySelector('form').addEventListener('submit', function(event) {
        let currentFormState = new FormData(this);

        // 폼 변경 여부 확인
        let isFormChanged = false;
        for (let [key, value] of initialFormState.entries()) {
            if (currentFormState.get(key) !== value) {
                isFormChanged = true;
                break;
            }
        }

        // 초기 폼 상태에 없는 새로운 필드가 추가된 경우
        if (!isFormChanged) {
            for (let [key, value] of currentFormState.entries()) {
                if (!initialFormState.has(key)) {
                    isFormChanged = true;
                    break;
                }
            }
        }

        // 변경 사항이 없을 경우 알림 및 제출 방지3
        // 애초에 alert도 안먹음
        if (!isFormChanged) {
            event.preventDefault();
            swal({
                title: "수정된 부분이 없습니다.",
                icon: "warning",
                button: "확인"
            });
        }
    });
});
