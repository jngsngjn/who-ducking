document.addEventListener("DOMContentLoaded", function () {
    const modal = document.getElementById("worldModal");
    const resultModal = document.getElementById("resultModal");
    const startGameBtn = document.getElementById("startGameBtn");
    const roundInfo = document.getElementById("roundInfo");
    const vsTitle = document.querySelector(".vs-title");
    const worldContainer = document.querySelector(".world-container");
    const worldLeftImage = document.getElementById("world-left-image");
    const worldRightImage = document.getElementById("world-right-image");
    const worldLeftCaption = document.getElementById("world-left-caption");
    const worldRightCaption = document.getElementById("world-right-caption");
    const finalImage = document.getElementById("finalImage");
    const finalCaption = document.getElementById("finalCaption");
    const reviewLink = document.getElementById("reviewLink");
    const jsConfetti = new JSConfetti(); // 폭죽

    let worldImages = [];

    async function fetchData() {
        try {
            const response = await fetch('/api/world-cup');
            worldImages = await response.json();
        } catch (error) {
            console.error('Error fetching world cup data:', error);
        }
    }

    modal.style.display = "block";
    modal.classList.add("show");
    worldContainer.style.display = "none";

    let currentRound = [];
    let nextRound = [];
    let currentIndex = 0;
    let totalRounds;
    let currentRoundNum;
    let currentStage;

    function shuffle(array) {
        for (let i = array.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [array[i], array[j]] = [array[j], array[i]];
        }
    }

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

    function startGame() {
        totalRounds = parseInt(document.getElementById("rounds").value);
        shuffle(worldImages);
        currentRound = worldImages.slice(0, totalRounds);
        nextRound = [];
        currentIndex = 0;
        currentRoundNum = 1;
        currentStage = totalRounds;
        loadImages();
        updateRoundInfo();
        modal.style.display = "none";
        modal.classList.remove("show");
        worldContainer.style.display = "flex";
    }

    function loadImages() {
        if (currentRound.length === 1) {
            showFinalResult(currentRound[0]);
            return;
        }

        if (currentIndex >= currentRound.length) {
            if (nextRound.length === 0) {
                alert("결과 에러");
                return;
            }
            currentRound = nextRound.slice();
            nextRound = [];
            currentIndex = 0;
            currentRoundNum = 1;
            currentStage /= 2;
            updateRoundInfo();
        }

        if (currentIndex < currentRound.length - 1) {
            worldLeftImage.src = `/image/ani/${currentRound[currentIndex].imageName}`;
            worldRightImage.src = `/image/ani/${currentRound[currentIndex + 1].imageName}`;
            worldLeftCaption.innerText = currentRound[currentIndex].name;
            worldRightCaption.innerText = currentRound[currentIndex + 1].name;
        } else {
            worldLeftImage.src = `/image/ani/${currentRound[currentIndex].imageName}`;
            worldLeftCaption.innerText = currentRound[currentIndex].name;
            worldRightImage.src = "";
            worldRightCaption.innerText = "";
            nextRound.push(currentRound[currentIndex]);
            currentIndex++;
            loadImages();
        }
    }

    function showFinalResult(winner) {
        finalImage.src = `/image/ani/${winner.imageName}`;
        finalCaption.textContent = winner.name;
        reviewLink.href = `/animations/${winner.id}`;
        resultModal.style.display = "block";
        resultModal.classList.add("show");
        worldContainer.style.display = "none";

        // 폭죽 효과 추가
        jsConfetti.addConfetti({
            confettiColors: [
                "#ff0a54", // 핑크
                "#ff477e", // 밝은 핑크
                "#ff7096", // 밝은 빨강
                "#ff85a1", // 살구색
                "#fbb1bd", // 밝은 살구색
                "#f9bec7", // 연한 핑크
                "#ff0000", // 빨강
                "#ffcc00", // 노랑
                "#00ff00", // 초록
                "#0000ff", // 파랑
                "#800080", // 보라
                "#ffa500"  // 주황
            ],
            confettiRadius: 5,
            confettiNumber: 1000,
        });
    }

    function restartGame() {
        resultModal.style.display = "none";
        resultModal.classList.remove("show");
        modal.classList.add("show");
        modal.style.display = "block";
        worldContainer.style.display = "none";
    }

    worldLeftImage.addEventListener("click", () => {
        if (currentIndex < currentRound.length) {
            nextRound.push(currentRound[currentIndex]);
            currentIndex += 2;
            currentRoundNum++;
            loadImages();
            updateRoundInfo();
        }
    });

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

    startGameBtn.onclick = function() {
        startGame();
    }

    fetchData();
    window.restartGame = restartGame;
});