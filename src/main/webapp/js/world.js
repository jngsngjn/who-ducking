document.addEventListener("DOMContentLoaded", function() {
    const modal = document.getElementById("worldModal");
    const resultModal = document.getElementById("resultModal");
    const btn = document.getElementById("startGameBtn");
    const span = document.getElementsByClassName("world-close")[0];
    const roundInfo = document.getElementById("roundInfo");
    const worldContainer = document.querySelector(".world-container");

    // 초기 상태 설정
    modal.style.display = "block";
    modal.classList.add("show");
    worldContainer.style.display = "none";

    // 모달 닫기 버튼 제거 (옵션)
    if (span) {
        span.style.display = "none";
    }

    const worldImages = [
        { src: "/images/Popup_Image/sakura.png", caption: "사쿠라" },
        { src: "/images/Popup_Image/dora.png", caption: "도라에몽" },
        { src: "/images/Popup_Image/gu.png", caption: "짱구" },
        { src: "/images/Popup_Image/hello.png", caption: "헬로키티" },
        { src: "/images/Popup_Image/ju.png", caption: "주술회전" },
        { src: "/images/Popup_Image/gu-default.png", caption: "구구" },
        { src: "/images/Popup_Image/one piece-default.png", caption: "원피스" },
        { src: "/images/Lucky/OnePad.png", caption: "원패드" },
        { src: "/images/Lucky/Mit.png", caption: "미츠리" },
        { src: "/images/Lucky/Ren.png", caption: "렌고쿠" }
    ];

    let worldCurrentRound = [];
    let worldNextRound = [];
    let worldCurrentIndex = 0;
    let totalRounds;
    let currentRound;

    function worldShuffle(array) {
        for (let i = array.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [array[i], array[j]] = [array[j], array[i]];
        }
    }

    function updateRoundInfo() {
        roundInfo.textContent = `애니 ${totalRounds}강 ${currentRound}/${totalRounds / 2}`;
    }

    function worldStartGame() {
        totalRounds = parseInt(document.getElementById("rounds").value);
        worldShuffle(worldImages);
        worldCurrentRound = worldImages.slice(0, totalRounds);
        worldNextRound = [];
        worldCurrentIndex = 0;
        currentRound = 1;
        worldLoadImages();
        updateRoundInfo();
        modal.style.display = "none";
        modal.classList.remove("show");
        worldContainer.style.display = "flex";
    }

    function worldLoadImages() {
        if (worldCurrentRound.length === 1) {
            showFinalResult(worldCurrentRound[0]);
            return;
        }

        if (worldCurrentIndex >= worldCurrentRound.length) {
            if (worldNextRound.length === 0) {
                alert("결과 에러");
                return;
            }
            worldCurrentRound = worldNextRound.slice();
            worldNextRound = [];
            worldCurrentIndex = 0;
            currentRound++;
            updateRoundInfo();
        }

        if (worldCurrentIndex < worldCurrentRound.length - 1) {
            document.getElementById("world-left-image").src = worldCurrentRound[worldCurrentIndex].src;
            document.getElementById("world-right-image").src = worldCurrentRound[worldCurrentIndex + 1].src;
            document.getElementById("world-left-caption").innerText = worldCurrentRound[worldCurrentIndex].caption;
            document.getElementById("world-right-caption").innerText = worldCurrentRound[worldCurrentIndex + 1].caption;
        } else if (worldCurrentIndex === worldCurrentRound.length - 1) {
            document.getElementById("world-left-image").src = worldCurrentRound[worldCurrentIndex].src;
            document.getElementById("world-left-caption").innerText = worldCurrentRound[worldCurrentIndex].caption;
            document.getElementById("world-right-image").src = "";
            document.getElementById("world-right-caption").innerText = "";
            worldNextRound.push(worldCurrentRound[worldCurrentIndex]);
            worldCurrentIndex++;
            worldLoadImages();
        } else {
            console.log("Unexpected state in worldLoadImages");
            console.log("worldCurrentIndex:", worldCurrentIndex);
            console.log("worldCurrentRound.length:", worldCurrentRound.length);
            console.log("worldNextRound:", worldNextRound);
        }
    }

    function showFinalResult(winner) {
        const finalImage = document.getElementById("finalImage");
        const finalCaption = document.getElementById("finalCaption");

        finalImage.src = winner.src;
        finalCaption.textContent = winner.caption;
        resultModal.style.display = "block";
        resultModal.classList.add("show");
        worldContainer.style.display = "none";
    }

    function restartGame() {
        resultModal.style.display = "none";
        resultModal.classList.remove("show");
        modal.classList.add("show");
        modal.style.display = "block";
        worldContainer.style.display = "none";
    }

    document.getElementById("world-left-image").addEventListener("click", () => {
        if (worldCurrentIndex < worldCurrentRound.length) {
            worldNextRound.push(worldCurrentRound[worldCurrentIndex]);
            worldCurrentIndex += 2;
            worldLoadImages();
            updateRoundInfo();
        }
    });

    document.getElementById("world-right-image").addEventListener("click", () => {
        if (worldCurrentIndex + 1 < worldCurrentRound.length) {
            worldNextRound.push(worldCurrentRound[worldCurrentIndex + 1]);
            worldCurrentIndex += 2;
            worldLoadImages();
            updateRoundInfo();
        } else if (worldCurrentIndex < worldCurrentRound.length) {
            worldLoadImages();
            updateRoundInfo();
        }
    });

    btn.onclick = function() {
        worldStartGame();
    }

    // 전역 스코프에 함수 추가
    window.restartGame = restartGame;
});
