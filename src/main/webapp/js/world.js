document.addEventListener("DOMContentLoaded", function() {
    const modal = document.getElementById("worldModal");
    const resultModal = document.getElementById("resultModal");
    const startGameBtn = document.getElementById("startGameBtn");
    const roundInfo = document.getElementById("roundInfo");
    const vsTitle = document.querySelector(".vs-title"); // vs-title 요소 선택
    const worldContainer = document.querySelector(".world-container");
    const worldLeftImage = document.getElementById("world-left-image");
    const worldRightImage = document.getElementById("world-right-image");
    const worldLeftCaption = document.getElementById("world-left-caption");
    const worldRightCaption = document.getElementById("world-right-caption");
    const finalImage = document.getElementById("finalImage");
    const finalCaption = document.getElementById("finalCaption");

    // 모달
    modal.style.display = "block";
    modal.classList.add("show");
    worldContainer.style.display = "none";

    // 이미지 데이터
    const worldImages = [
        { src: "/images/Popup_Image/sakura.png", caption: "사쿠라" },
        { src: "/images/Popup_Image/dora.png", caption: "도라에몽" },
        { src: "/images/Popup_Image/gu.png", caption: "짱구" },
        { src: "/images/Popup_Image/hello.png", caption: "헬로키티" },
        { src: "/images/Popup_Image/ju.png", caption: "주술회전" },
        { src: "/images/Popup_Image/gu-default.png", caption: "짱구" },
        { src: "/images/Popup_Image/one piece-default.png", caption: "원피스" },
        { src: "/images/Lucky/OnePad.png", caption: "원패드" },
        { src: "/images/Lucky/Mit.png", caption: "미츠리" },
        { src: "/images/Lucky/Ren.png", caption: "렌고쿠" }
    ];

    let currentRound = [];
    let nextRound = [];
    let currentIndex = 0;
    let totalRounds;
    let currentRoundNum;
    let currentStage;

    // 배열 섞기
    function shuffle(array) {
        for (let i = array.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1)); // 랜덤값
            [array[i], array[j]] = [array[j], array[i]];
        }
    }

    // 현재 라운드 정보를 업데이트하는 함수
    function updateRoundInfo() {
        const stageMap = {
            32: "32강",
            16: "16강",
            8: "8강",
            4: "4강",
            2: "결승"
        };
        const stageText = stageMap[currentStage] || "결승";
        const roundText = `애니 ${stageText} (${currentRoundNum}/${currentStage / 2})`;
        roundInfo.textContent = roundText;
        vsTitle.textContent = roundText;
    }

    // 게임을 시작하는 함수
    function startGame() {
        totalRounds = parseInt(document.getElementById("rounds").value); // 선택된 라운드 수 가져오기
        shuffle(worldImages); // 이미지 섞기
        currentRound = worldImages.slice(0, totalRounds); // 라운드 수에 맞는 이미지 선택
        nextRound = [];
        currentIndex = 0;
        currentRoundNum = 1;
        currentStage = totalRounds;
        loadImages(); // 첫 라운드 이미지 로드
        updateRoundInfo(); // 라운드 정보 업데이트
        modal.style.display = "none"; // 모달 숨기기
        modal.classList.remove("show");
        worldContainer.style.display = "flex"; // 게임 컨테이너 표시
    }

    // 이미지를 로드하는 함수
    function loadImages() {
        // 마지막 라운드에서 최종 결과 표시
        if (currentRound.length === 1) {
            showFinalResult(currentRound[0]);
            return;
        }

        // 현재 라운드의 이미지가 끝나면 다음 라운드로 전환
        if (currentIndex >= currentRound.length) {
            if (nextRound.length === 0) {
                alert("결과 에러");
                return;
            }
            currentRound = nextRound.slice(); // 다음 라운드로 전환
            nextRound = [];
            currentIndex = 0;
            currentRoundNum = 1;
            currentStage /= 2;
            updateRoundInfo();
        }

        // 이미지와 이미지 이름 로드
        if (currentIndex < currentRound.length - 1) {
            worldLeftImage.src = currentRound[currentIndex].src;
            worldRightImage.src = currentRound[currentIndex + 1].src;
            worldLeftCaption.innerText = currentRound[currentIndex].caption;
            worldRightCaption.innerText = currentRound[currentIndex + 1].caption;
        } else {
            // 이미지가 남았을 때 처리
            worldLeftImage.src = currentRound[currentIndex].src;
            worldLeftCaption.innerText = currentRound[currentIndex].caption;
            worldRightImage.src = "";
            worldRightCaption.innerText = "";
            nextRound.push(currentRound[currentIndex]);
            currentIndex++;
            loadImages();
        }
    }

    // 결과를 표시하는 함수
    function showFinalResult(winner) {
        finalImage.src = winner.src;
        finalCaption.textContent = winner.caption;
        resultModal.style.display = "block";
        resultModal.classList.add("show");
        worldContainer.style.display = "none";
    }

    // 게임을 다시 시작하는 함수
    function restartGame() {
        resultModal.style.display = "none";
        resultModal.classList.remove("show");
        modal.classList.add("show");
        modal.style.display = "block";
        worldContainer.style.display = "none";
    }

    // 좌측 이미지 클릭 이벤트
    worldLeftImage.addEventListener("click", () => {
        if (currentIndex < currentRound.length) {
            nextRound.push(currentRound[currentIndex]);
            currentIndex += 2;
            currentRoundNum++;
            loadImages();
            updateRoundInfo();
        }
    });

    // 우측 이미지 클릭 이벤트
    worldRightImage.addEventListener("click", () => {
        if (currentIndex + 1 < currentRound.length) {
            nextRound.push(currentRound[currentIndex + 1]);
            currentIndex += 2;
            currentRoundNum++;
            loadImages();
            updateRoundInfo();
        } else if (currentIndex < currentRound.length) {
            loadImages();
            updateRoundInfo();
        }
    });

    // 시작 버튼 클릭 이벤트 핸들러
    startGameBtn.onclick = function() {
        startGame();
    }

    window.restartGame = restartGame;
});
