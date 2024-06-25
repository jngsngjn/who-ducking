$(document).ready(function () {
    const maxSelection = 5;
    const genreCheckboxes = $('input[name="genres"]');

    genreCheckboxes.on("change", function () {
        const selectedCheckboxes = genreCheckboxes.filter(":checked");

        if (selectedCheckboxes.length > maxSelection) {
            $(this).prop("checked", false);
            alert("최대 5개만 선택할 수 있습니다.");
        }
    });
});

$('input[name="genres"]').change(function () {
    let selectedCount = $('input[name="genres"]:checked').length; // 체크된 체크박스 개수 확인
    if (selectedCount > 5) {
        alert("최대 5개의 장르만 선택할 수 있습니다."); // 경고 메시지 표시
        $(this).prop("checked", false); // 체크 해제

        // 체크 해제 시 클래스 변경
        const label = $('label[for="' + $(this).attr("id") + '"]');
        label.removeClass("register_genre_label_click").addClass("register_genre_label");
    } else {
        // 체크 시 클래스 변경
        const label = $('label[for="' + $(this).attr("id") + '"]');
        if ($(this).is(":checked")) {
            label.removeClass("register_genre_label").addClass("register_genre_label_click");
        } else {
            label.removeClass("register_genre_label_click").addClass("register_genre_label");
        }
    }
});

$(document).ready(function () {
    $("#register_form").on("submit", function (event) {
        const selectedGenres = $('input[name="genres"]:checked');

        if (selectedGenres.length === 0) {
            event.preventDefault();
            alert("최소 1개 이상의 장르를 선택해야 합니다.");
        }
    });
});
