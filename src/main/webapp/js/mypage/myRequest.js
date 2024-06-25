// 페이지가 로드된 후 실행
$(document).ready(function () {
    // 모든 버튼 요소를 선택
    $(".myRequest_list_state").each(function () {
        // 버튼의 id에 따라 스타일 적용 및 텍스트 변경
        switch ($(this).attr("id")) {
            case "APPROVED":
                $(this).css("background-color", "rgba(16, 255, 11, 0.5)").text("승인"); // 파란색
                break;
            case "REJECTED":
                $(this).css("background-color", "rgba(255, 0, 0, 0.5)").text("반려"); // 빨간색
                break;
            default:
                $(this).css("background-color", "#ffc60b").text("접수"); // 기본 색상
                break;
        }
    });

    var modal = $("#myModal");
    var closeModalBtn = $(".myRequest_modal-close");
    var modalTitle = $("#modalTitle");
    var modalContent = $("#modalContent");
    var writeDate = $("#writeDate");
    var deleteForm = $("#deleteForm");
    var requestIdInput = $("#requestId");
    var responseDate = $("#responseDate");
    var responseDateValue = $("#responseDateValue");
    var responseText = $("#responseText");
    var response = $("#response");

    $(".myRequest_list_title").on("click", function () {
        var title = $(this).text();
        var content = $(this).attr("data-content");
        var status = $(this).attr("data-status");
        var requestId = $(this).attr("data-request-id");
        var responseDateText = $(this).attr("data-response-date");
        var writeDateText = $(this).attr("data-write-date");
        var responseContent = $(this).attr("data-response");

        modalTitle.text(title);
        modalContent.text(content);
        writeDate.text(writeDateText);
        requestIdInput.val(requestId);

        if (status === "RECEIVED") {
            deleteForm.show();
            responseDate.hide();
            responseText.hide();
        } else if (status === "APPROVED") {
            deleteForm.hide();
            responseDateValue.text(responseDateText);
            responseDate.show();
            responseText.hide();
        } else if (status === "REJECTED") {
            deleteForm.hide();
            responseDateValue.text(responseDateText);
            responseDate.show();
            response.text(responseContent);
            responseText.show();
        } else {
            deleteForm.hide();
            responseDate.hide();
            responseText.hide();
        }

        // Get current scroll position
        var scrollTop = $(window).scrollTop();

        // Set the modal position based on current scroll position
        modal.css({
            top: scrollTop + 50 + "px",
            left: "50%",
            transform: "translate(-50%, 0)",
        });

        modal.show();
    });

    closeModalBtn.on("click", function () {
        modal.hide();
    });

    $(window).on("click", function (event) {
        if ($(event.target).is(modal)) {
            modal.hide();
        }
    });

    $("#openModal").click(function () {
        $("#myModal2").show();
    });

    // 모달 닫기
    $(".myRequest_modal-close").click(function () {
        $(this).closest(".myRequest_modal").hide();
    });

    // 모달 외부 클릭 시 닫기
    $(window).click(function (event) {
        if ($(event.target).hasClass("myRequest_modal")) {
            $(".myRequest_modal").hide();
        }
    });
});

// 제목과 내용 글자 수 제한
document.addEventListener("DOMContentLoaded", function () {
    const titleInput = document.getElementById("title2");
    const contentTextarea = document.getElementById("content");
    const titleCount = document.getElementById("title_count");
    const textareaCount = document.getElementById("textarea_count");

    const maxTitleLength = 30;
    const maxContentLength = 500;

    // 제목 글자 수 업데이트 함수
    function updateTitleCount() {
        let currentLength = titleInput.value.length;
        if (currentLength > maxTitleLength) {
            titleInput.value = titleInput.value.substring(0, maxTitleLength);
            currentLength = maxTitleLength; // 초과하지 않도록 설정
        }
        titleCount.textContent = `${currentLength} / ${maxTitleLength}`;
    }

    // 내용 글자 수 업데이트 함수
    function updateTextareaCount() {
        let currentLength = contentTextarea.value.length;
        if (currentLength > maxContentLength) {
            contentTextarea.value = contentTextarea.value.substring(0, maxContentLength);
            currentLength = maxContentLength; // 초과하지 않도록 설정
        }
        textareaCount.textContent = `${currentLength} / ${maxContentLength}`;
    }

    // 제목 입력 필드에 이벤트 리스너 추가
    titleInput.addEventListener("input", updateTitleCount);

    // 내용 텍스트 영역에 이벤트 리스너 추가
    contentTextarea.addEventListener("input", updateTextareaCount);

    // 제목 입력 필드에서 최대 글자 수를 초과하지 않도록 막기
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

    // 내용 텍스트 영역에서 최대 글자 수를 초과하지 않도록 막기
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
});

// 페이지가 로드된 후 실행
$(document).ready(function () {
    // 모든 버튼 요소를 선택
    $(".myRequest_list_state").each(function () {
        // 버튼의 id에 따라 스타일 적용 및 텍스트 변경
        switch ($(this).attr("id")) {
            case "APPROVED":
                $(this).css("background-color", "rgba(16, 255, 11, 0.5)").text("승인"); // 파란색
                break;
            case "REJECTED":
                $(this).css("background-color", "rgba(255, 0, 0, 0.5)").text("반려"); // 빨간색
                break;
            default:
                $(this).css("background-color", "#ffc60b").text("접수"); // 기본 색상
                break;
        }
    });

    var modal = $("#myModal");
    var closeModalBtn = $(".myRequest_modal-close");
    var modalTitle = $("#modalTitle");
    var modalContent = $("#modalContent");
    var writeDate = $("#writeDate");
    var deleteForm = $("#deleteForm");
    var requestIdInput = $("#requestId");
    var responseDate = $("#responseDate");
    var responseDateValue = $("#responseDateValue");
    var responseText = $("#responseText");
    var response = $("#response");

    $(".myRequest_list_title").on("click", function () {
        var title = $(this).text();
        var content = $(this).attr("data-content");
        var status = $(this).attr("data-status");
        var requestId = $(this).attr("data-request-id");
        var responseDateText = $(this).attr("data-response-date");
        var writeDateText = $(this).attr("data-write-date");
        var responseContent = $(this).attr("data-response");

        modalTitle.text(title);
        modalContent.text(content);
        writeDate.text(writeDateText);
        requestIdInput.val(requestId);

        if (status === "RECEIVED") {
            deleteForm.show();
            responseDate.hide();
            responseText.hide();
        } else if (status === "APPROVED") {
            deleteForm.hide();
            responseDateValue.text(responseDateText);
            responseDate.show();
            responseText.hide();
        } else if (status === "REJECTED") {
            deleteForm.hide();
            responseDateValue.text(responseDateText);
            responseDate.show();
            response.text(responseContent);
            responseText.show();
        } else {
            deleteForm.hide();
            responseDate.hide();
            responseText.hide();
        }

        // Get current scroll position
        var scrollTop = $(window).scrollTop();

        // Set the modal position based on current scroll position
        modal.css({
            top: scrollTop + 50 + "px",
            left: "50%",
            transform: "translate(-50%, 0)",
        });

        modal.show();
    });

    closeModalBtn.on("click", function () {
        modal.hide();
    });

    $(window).on("click", function (event) {
        if ($(event.target).is(modal)) {
            modal.hide();
        }
    });

    $("#openModal").click(function () {
        $("#myModal2").show();
    });

    // 모달 닫기
    $(".myRequest_modal-close").click(function () {
        $(this).closest(".myRequest_modal").hide();
    });

    // 모달 외부 클릭 시 닫기
    $(window).click(function (event) {
        if ($(event.target).hasClass("myRequest_modal")) {
            $(".myRequest_modal").hide();
        }
    });
});

// 제목과 내용 글자 수 제한
document.addEventListener("DOMContentLoaded", function () {
    const titleInput = document.getElementById("title2");
    const contentTextarea = document.getElementById("content");
    const titleCount = document.getElementById("title_count");
    const textareaCount = document.getElementById("textarea_count");

    const maxTitleLength = 30;
    const maxContentLength = 500;

    // 제목 글자 수 업데이트 함수
    function updateTitleCount() {
        let currentLength = titleInput.value.length;
        if (currentLength > maxTitleLength) {
            titleInput.value = titleInput.value.substring(0, maxTitleLength);
            currentLength = maxTitleLength; // 초과하지 않도록 설정
        }
        titleCount.textContent = `${currentLength} / ${maxTitleLength}`;
    }

    // 내용 글자 수 업데이트 함수
    function updateTextareaCount() {
        let currentLength = contentTextarea.value.length;
        if (currentLength > maxContentLength) {
            contentTextarea.value = contentTextarea.value.substring(0, maxContentLength);
            currentLength = maxContentLength; // 초과하지 않도록 설정
        }
        textareaCount.textContent = `${currentLength} / ${maxContentLength}`;
    }

    // 제목 입력 필드에 이벤트 리스너 추가
    titleInput.addEventListener("input", updateTitleCount);

    // 내용 텍스트 영역에 이벤트 리스너 추가
    contentTextarea.addEventListener("input", updateTextareaCount);

    // 제목 입력 필드에서 최대 글자 수를 초과하지 않도록 막기
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

    // 내용 텍스트 영역에서 최대 글자 수를 초과하지 않도록 막기
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
});