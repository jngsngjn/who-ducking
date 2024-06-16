$(document).ready(function () {
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
        slidesPerview: 5,
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

    // Update Animation Swiper
    const updateAnimationSwiper = document.querySelectorAll(".mainThirdSection_update_animation_box");

    updateAnimationSwiper.forEach(function (element, index) {
        element.classList.add("swiper-" + index);

        // 슬라이드 요소 동적 생성
        const swiperWrapper = element.querySelector(".mainThirdSection_update_animation_items");
        const swiperSlides = swiperWrapper.querySelectorAll(".mainThirdSection_update_animation_item");
        const swiperSlideCount = swiperSlides.length;

        // 무한 슬라이드 생성
        function createInfiniteSlides() {
            swiperSlides.forEach((slide) => {
                const cloneSlide = slide.cloneNode(true);
                swiperWrapper.appendChild(cloneSlide);
            });
            swiper.update();
        }

        // 일정 시간마다 무한 생성
        const addSlidesInterval = setInterval(() => {
            const currentSlides = swiperWrapper.querySelectorAll(".mainThirdSection_update_animation_item");
            createInfiniteSlides();
        }, 100);

        // 슬라이드 제거
        const removeAllSlidesInterval = setInterval(() => {
            const slides = swiperWrapper.querySelectorAll(".mainThirdSection_update_animation_item");
            if (slides.length > swiperSlideCount * 3) {
                // 화면에 보이지 않는 슬라이드 개수 계산
                const excessSlides = slides.length - swiperSlideCount * 3;
                for (let i = 0; i < excessSlides; i++) {
                    swiperWrapper.removeChild(slides[i]);
                }
                swiper.update();
            }
        }, 30000);

        let swiper = new Swiper(".swiper-" + index, {
            autoplay: {
                delay: 1,
                desableOnInteraction: false,
            },
            speed: 30000,
            loop: true,
            loopAdditionalSlides: swiperSlideCount,
            slidePerView: "auto",
            spaceBetween: 20,
            freemode: false,
            slidesOffsetBefore: 0,
            slidesOffsetAfter: 0,
            allowTouchMove: false,
            touchRatio: 0,
        });
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
    });
});
