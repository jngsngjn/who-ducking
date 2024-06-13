$(document).ready(function() {

    let profileImagePreview = $('#profileImagePreview');
    let levelImagePreview = $('#levelImagePreview');
    let useDefaultImageButton = $('#useDefaultImage');

    // 초기 상태 설정
    // 사용 중인 프사가 없을 때
    if (!profileImagePreview.attr('src') || profileImagePreview.attr('src') === '') {
        profileImagePreview.hide();
        levelImagePreview.show();
        useDefaultImageButton.hide();
    } else {
        // 사용 중인 프사가 있을 때
        profileImagePreview.show();
        levelImagePreview.hide();
        useDefaultImageButton.show();
    }

    // 사진을 업로드했을 때
    $('#profileImageInput').on('change', function(event) {
        let file = event.target.files[0];
        if (file) {
            let reader = new FileReader();
            reader.onload = function(e) {
                $('#profileImagePreview').attr('src', e.target.result).show();
                $('#levelImagePreview').hide();
                $('#useDefaultImageFlag').val('false');
                useDefaultImageButton.show();
            }
            reader.readAsDataURL(file);
        }
    });

    // 기본 이미지 버튼을 클릭했을 때
    $('#useDefaultImage').on('click', function() {
        let useDefaultImage = $('#useDefaultImageFlag').val();

        // 이미 기본 이미지를 사용 중이라면 AJAX 요청을 보내지 않음
        if (useDefaultImage !== 'true') {
        $.ajax({
            url: '/myPage/user/level-image',
            type: 'POST',
            success: function(data) {
                let defaultImagePath = '/image/level/' + data;
                $('#profileImagePreview').attr('src', defaultImagePath).show();
                $('#levelImagePreview').hide();
                $('#profileImageInput').val('');
                $('#useDefaultImageFlag').val('true');
                useDefaultImageButton.hide();
            },
            error: function(xhr, status, error) {
                alert('기본 이미지 로드에 실패했습니다.');
            }
        });
        }
    });
});