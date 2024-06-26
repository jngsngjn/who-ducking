$(document).ready(function () {
    // AOS
    AOS.init({
        once: true,
        duration: 2000,
    });

    // MainVisualSwiper
    const mainVisualSwiper = new Swiper(".mv-cnt__luckyDraw", {
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

document.addEventListener("DOMContentLoaded", function () {
    const tabs = document.querySelectorAll(".luckyDraw_grade");
    const contents = document.querySelectorAll(".luckyDraw_product");

    tabs.forEach((tab, index) => {
        tab.addEventListener("click", () => {
            tabs.forEach((tab) => tab.classList.remove("active"));
            contents.forEach((content) => content.classList.remove("active"));

            tab.classList.add("active");
            contents[index].classList.add("active");
        });
    });
});
