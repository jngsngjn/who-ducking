
    document.addEventListener("DOMContentLoaded", function() {
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
        const jsConfetti = new JSConfetti(); // JSConfetti 초기화
    
        modal.style.display = "block";
        modal.classList.add("show");
        worldContainer.style.display = "none";
    
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
                worldLeftImage.src = currentRound[currentIndex].src;
                worldRightImage.src = currentRound[currentIndex + 1].src;
                worldLeftCaption.innerText = currentRound[currentIndex].caption;
                worldRightCaption.innerText = currentRound[currentIndex + 1].caption;
            } else {
                worldLeftImage.src = currentRound[currentIndex].src;
                worldLeftCaption.innerText = currentRound[currentIndex].caption;
                worldRightImage.src = "";
                worldRightCaption.innerText = "";
                nextRound.push(currentRound[currentIndex]);
                currentIndex++;
                loadImages();
            }
        }
    
        function showFinalResult(winner) {
            finalImage.src = winner.src;
            finalCaption.textContent = winner.caption;
            resultModal.style.display = "block";
            resultModal.classList.add("show");
            worldContainer.style.display = "none";
    
            // 폭죽 효과 추가
            jsConfetti.addConfetti({
                confettiColors: [
                    "#ff0a54",
                    "#ff477e",
                    "#ff7096",
                    "#ff85a1",
                    "#fbb1bd",
                    "#f9bec7",
                ],
                confettiRadius: 5,
                confettiNumber: 500,
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
    
        window.restartGame = restartGame;
    });