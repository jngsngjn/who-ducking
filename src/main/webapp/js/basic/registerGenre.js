$(document).ready(function () {
    const maxSelection = 5;
    const genreCheckboxes = $('input[name="genres"]');

    genreCheckboxes.on("change", function () {
        const selectedCheckboxes = genreCheckboxes.filter(":checked");

        if (selectedCheckboxes.length > maxSelection) {
            $(this).prop("checked", false);
            swal({
                title: "선택 제한",
                text: "최대 5개만 선택할 수 있습니다.",
                icon: "warning",
            });
        }
    });
});

$('input[name="genres"]').change(function () {
    let selectedCount = $('input[name="genres"]:checked').length;
    if (selectedCount > 5) {
        swal({
            title: "선택 제한",
            text: "최대 5개의 장르만 선택할 수 있습니다.",
            icon: "warning",
        });
        $(this).prop("checked", false);

        const label = $('label[for="' + $(this).attr("id") + '"]');
        label.removeClass("register_genre_label_click").addClass("register_genre_label");
    } else {
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
            swal({
                title: "장르 선택 필요",
                text: "최소 1개 이상의 장르를 선택해야 합니다.",
                icon: "warning",
            });
        }
    });
});