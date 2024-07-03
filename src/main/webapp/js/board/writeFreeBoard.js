$(document).ready(function () {
    $("#create-image").on("change", function (event) {
        let file = event.target.files[0];
        if (file) {
            let reader = new FileReader();
            reader.onload = function (e) {
                $("#create-imagePreview").attr("src", e.target.result).show();
                $(".writeFreeBoard_img_delete_btn").show();
            };
            reader.readAsDataURL(file);
        }
    });

    $(".writeFreeBoard_img_input_btn").on("click", function () {
        $("#create-image").click();
    });

    // Title and Content character count
    const titleInput = document.getElementById("create-title");
    const contentTextarea = document.getElementById("create-content");
    const titleCount = document.getElementById("title_count");
    const textareaCount = document.getElementById("textarea_count");

    const maxTitleLength = 30;
    const maxContentLength = 500;

    // Update title character count
    function updateTitleCount() {
        let currentLength = titleInput.value.length;
        if (currentLength > maxTitleLength) {
            titleInput.value = titleInput.value.substring(0, maxTitleLength);
            currentLength = maxTitleLength; // 초과하지 않도록 설정
        }
        titleCount.textContent = `${currentLength} / ${maxTitleLength}`;
    }

    // Update content character count
    function updateTextareaCount() {
        let currentLength = contentTextarea.value.length;
        if (currentLength > maxContentLength) {
            contentTextarea.value = contentTextarea.value.substring(0, maxContentLength);
            currentLength = maxContentLength; // 초과하지 않도록 설정
        }
        textareaCount.textContent = `${currentLength} / ${maxContentLength}`;
    }

    // Add event listeners to title input
    titleInput.addEventListener("input", updateTitleCount);

    // Add event listeners to content textarea
    contentTextarea.addEventListener("input", updateTextareaCount);

    // Prevent exceeding max length in title input
    titleInput.addEventListener("keydown", function (event) {
        if (
            titleInput.value.length >= maxTitleLength &&
            event.key !== "Backspace" &&
            event.key !== "Delete" &&
            !event.ctrlKey &&
            !event.metaKey &&
            !event.altKey
        ) {
            event.preventDefault();
        }
    });

    // Prevent exceeding max length in content textarea
    contentTextarea.addEventListener("keydown", function (event) {
        if (
            contentTextarea.value.length >= maxContentLength &&
            event.key !== "Backspace" &&
            event.key !== "Delete" &&
            !event.ctrlKey &&
            !event.metaKey &&
            !event.altKey
        ) {
            event.preventDefault();
        }
    });

    // Initial update of character counts
    updateTitleCount();
    updateTextareaCount();
});

function createRemoveImage() {
    $("#create-imagePreview").attr("src", "").hide();
    $("#create-image").val("");
    $(".writeFreeBoard_img_delete_btn").hide();
}

function formValidate() {
    const title = document.getElementById("create-title").value;
    const content = document.getElementById("create-content").value;

    if (!title) {
        swal({
            title: "제목을 입력해 주세요.",
            buttons: "확인",
        });
        return false;
    }
    if (!content) {
        swal({
            title: "내용을 입력해 주세요.",
            buttons: "확인",
        });
        return false;
    }
    return true;
}
