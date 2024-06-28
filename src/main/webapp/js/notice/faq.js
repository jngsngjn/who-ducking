// Swiper
$(document).ready(function () {
    // AOS
    AOS.init({
        once: true,
        duration: 2000,
    });

    // MainVisualSwiper
    const mainVisualSwiper = new Swiper(".mv-cnt__faq", {
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
});

// Dropdown
$(document).ready(function () {
    $(".faq_list_item_box_moreBtn").on("click", function () {
        // 모든 드롭다운 닫기 및 버튼 아이콘 +로 변경
        $(".faq_list_item_detail").slideUp();
        $(".faq_list_item_box_moreBtn i").removeClass("fa-minus").addClass("fa-plus");

        // 클릭된 요소의 드롭다운 토글 및 버튼 아이콘 변경
        var dropdown = $(this).closest(".faq_list_item").find(".faq_list_item_detail");
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
