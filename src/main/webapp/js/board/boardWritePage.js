$(document).ready(function() {
    $('#create-file').on('change', function(event) {
        let file = event.target.files[0];
        if (file) {
            let reader = new FileReader();
            reader.onload = function(e) {
                $('#create-imagePreview').attr('src', e.target.result).show();
                $('.create-delete-image-button').show();
                $('#create-fileName').text(file.name).show();
            }
            reader.readAsDataURL(file);
        }
    });
});

function createRemoveImage() {
    $('#create-imagePreview').attr('src', '').hide();
    $('#create-file').val('');
    $('.create-delete-image-button').hide();
    $('#create-fileName').hide();
}

function formValidate() {
    const title = document.getElementById('create-title').value;
    const content = document.getElementById('create-content').value;

    if (!title) {
        swal('제목을 입력해 주세요.');
        return false;
    }
    if (!content) {
        swal("내용을 입력해 주세요");
        return false;
    }
    return true;
}


