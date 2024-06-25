function loadScript(url) {
    return new Promise((resolve, reject) => {
        let script = document.createElement("script");
        script.type = "text/javascript";
        script.src = url;

        script.onload = () => resolve(url);
        script.onerror = () => reject(new Error(`Failed to load ${url}`));

        document.head.appendChild(script);
    });
}

/* 새로운 JavaScript 파일 생성 시 loadScript 추가. [Ex. loadScript("/경로/header.js"),] */
Promise.all([
    loadScript("/js/fragments/header.js"),
    loadScript("/js/main.js"),
    loadScript("/js/basic/registerBasic.js"),
    loadScript("/js/basic/registerGenre.js"),

    loadScript("/js/board/boardWritePage.js"),
    loadScript("/js/board/showPage.js"),
    loadScript("/js/notice/noticePage.js"),
])
    .then(() => {
        console.log("All scripts loaded.");
    })
    .catch((error) => {
        console.error(error);
    });