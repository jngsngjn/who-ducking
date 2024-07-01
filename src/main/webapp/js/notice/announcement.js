$(document).ready(function () {
    // AOS
    AOS.init({
        once: true,
        duration: 2000,
    });

    // MainVisualSwiper
    const mainVisualSwiper = new Swiper(".mv-cnt__announcement", {
        spaceBetween: 40,
        centeredSlides: true,
        loop: true,
        effect: "fade",
        autoplay: {
            delay: 5000,
            disableOnInteraction: false,
        },
        pagination: {
            el: ".swiper-pagination",
            clickable: true,
        },
        navigation: {
            nextEl: ".swiper-button-next",
            prevEl: ".swiper-button-prev",
        },
    });

    // Extract id from URL
    const urlParams = new URLSearchParams(window.location.search);
    const id = urlParams.get('id');

    // If id exists, open the corresponding announcement
    if (id) {
        console.log('쿼리파라미터 포함!');
        const targetAnnouncement = $(`.announcement_list_item_box_title[data-id='${id}']`).closest(".announcement_list_item");
        const dropdown = targetAnnouncement.find(".announcement_list_item_detail");
        const icon = targetAnnouncement.find(".announcement_list_item_box_moreBtn i");

        // Open the detail and change the icon
        dropdown.slideDown();
        icon.removeClass("fa-plus").addClass("fa-minus");
    }

    // Dropdown
    $(".announcement_list_item_box_moreBtn").on("click", function () {
        // 모든 드롭다운 닫기 및 버튼 아이콘 +로 변경
        $(".announcement_list_item_detail").slideUp();
        $(".announcement_list_item_box_moreBtn i").removeClass("fa-minus").addClass("fa-plus");

        // 클릭된 요소의 드롭다운 토글 및 버튼 아이콘 변경
        var dropdown = $(this).closest(".announcement_list_item").find(".announcement_list_item_detail");
        var icon = $(this).find("i");
        if (dropdown.is(":visible")) {
            dropdown.slideUp();
            icon.removeClass("fa-minus").addClass("fa-plus");
        } else {
            dropdown.slideDown();
            icon.removeClass("fa-plus").addClass("fa-minus");
        }
    });
});