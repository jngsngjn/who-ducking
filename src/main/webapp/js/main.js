$(document).ready(function () {
    function setSlideDuration(duration) {
        document.documentElement.style.setProperty("--slide-duration", `${duration}s`);
    }

    setSlideDuration(100); // 100초 설정

    function cloneImages() {
        const container = $(".mainThirdSection_update_slider_container");
        $.ajax({
            url: "/api/animations",
            method: "GET",
            dataType: "json",
            success: function (data) {
                data.forEach(function(animation) {
                    const imgContainer = $("<div>").addClass("animation-item");
                    const img = $("<img>")
                        .attr("src", `/image/ani/${animation.imageName}`)
                        .attr("alt", animation.title)
                        .css("cursor", "pointer")
                        .on("click", function() {
                             window.location.href = `/animations/${animation.id}`;
                         });
                    // 제목 요소 생성
                    const title = $("<div>")
                        .addClass("animation-title")
                        .text(animation.title);
                    // 이미지와 을 imgContainer에 추가
                    imgContainer.append(img);
                    // imgContainer를 메인 컨테이너에 추가
                    container.append(imgContainer);
                });
                //이미지 반복
                const images = container.children().clone();
                container.append(images);
            },
            error: function (error) {
                console.error("Error fetching animations:", error);
            }
        });
    }

    cloneImages(); // 이미지를 반복하도록 클론

    // AOS
    AOS.init({
        once: true,
        duration: 2000,
    });

    // MainVisualSwiper
    const mainVisualSwiper = new Swiper(".mv-cnt", {
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

    // Popularity Animation Swiper
    const popularityAnimationSwiper = new Swiper(".mainSecondSection_popularity_animation_box", {
        slidesPerView: 5,
        slidesPerGroup: 5,
        speed: 1000,
        autoplay: {
            delay: 5000,
            disableOnInteraction: false,
        },
        loop: true,
        breakpoints: {
            500: {
                slidesPerView: 2,
                slidesPerGroup: 2,
                spaceBetween: 160,
            },
            768: {
                slidesPerView: 3,
                slidesPerGroup: 3,
                spaceBetween: 200,
            },
            1024: {
                slidesPerView: 4,
                slidesPerGroup: 4,
                spaceBetween: 260,
            },
            1320: {
                slidesPerView: 5,
                slidesPerGroup: 5,
                spaceBetween: 260,
            },
            1690: {
                slidesPerView: 5,
                slidesPerGroup: 5,
                spaceBetween: 30,
            },
            1920: {
                slidesPerView: 5,
                slidesPerGroup: 5,
                spaceBetween: 0,
            },
        },
        navigation: {
            nextEl: ".swiper-button-next",
            prevEl: ".swiper-button-prev",
        },
    });

    // LuckyDrawSwiper
    const LuckyDrawSwiper = new Swiper(".mainFourthSection_luckydraw_box", {
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
