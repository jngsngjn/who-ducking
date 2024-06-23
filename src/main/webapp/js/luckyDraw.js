$(document).ready(function () {
    // AOS
    AOS.init({
        once: true,
        duration: 2000,
    });

    // MainVisualSwiper
    const mainVisualSwiper = new Swiper(".mv-cnt-lucky", {
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
    const tabs = document.querySelectorAll(".lucky_tab");
    const contents = document.querySelectorAll(".lucky_tab__content");

    tabs.forEach((tab, index) => {
        tab.addEventListener("click", () => {
            tabs.forEach(tab => tab.classList.remove("active"));
            contents.forEach(content => content.classList.remove("active"));

            tab.classList.add("active");
            contents[index].classList.add("active");
        });
    });
});

// const tabItem = document.querySelectorAll(".lucky_tab");
// const tabContent = document.querySelectorAll(".lucky_tab__content");
//
// // 탭 버튼들을 forEach 문을 통해 한번씩 순회한다.
// // 이때 index도 같이 가져온다.
// tabItem.forEach((item, index) => {
//     // 탭 버튼에 클릭 이벤트를 준다.
//     item.addEventListener("click", (e) => {
//         // 탭 버튼들을 forEach 문을 통해 한번씩 순회한다.
//         tabItem.forEach((item) => {
//             // 탭 버튼들의 active 클래스를 제거한다.
//             item.classList.remove("active");
//         });
//         // 클릭한 index의 탭 버튼에 active 클래스를 추가한다.
//         tabItem[index].classList.add("active");
//
//         // 탭 버튼의 id값을 string으로 가져온다.
//         const tabItemId = String(item.id);
//         // 탭 내용 부분들을 forEach 문을 통해 한번씩 순회한다.
//         tabContent.forEach((item, index) => {
//             // 탭 내용 부분들 전부 active 클래스를 제거한다.
//             item.classList.remove("active");
//
//             // 탭 내용의 id값을 string으로 가져온다.
//             const tabContentId = String(item.id);
//             // 만약 탭 버튼의 id 값과 탭 내용의 id값이 같다면,
//             // 해당 탭 내용에 active 클래스를 추가한다.
//             if (tabContentId === tabItemId) {
//                 tabContent[index].classList.add("active");
//             }
//         });
//     });
// });