$(document).ready(function (){
    let initialFormState = new FormData(document.querySelector('form'));

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

        if (!isFormChanged) {
            // 변경 사항이 없을 경우 알림 및 제출 방지
            event.preventDefault();
            alert('수정한 부분이 없습니다.');
        }
    });
});

$(document).ready(function (){
   const fileInput = document.getElementById('file');
   const imagePreview = document.getElementById('imagePreview');

   fileInput.addEventListener('change', function (e){
      const file = e.target.file[0];
      if(file){
          const reader = new FileReader();
          reader.onload = function (e){
              imagePreview.src = e.target.result;
          }
          reader.readAsDataURL(file);
      }
   });
});