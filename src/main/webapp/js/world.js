document.addEventListener("DOMContentLoaded", function() {
    const modal = document.getElementById("myModal");
    const btn = document.getElementById("startGameBtn");
    const span = document.getElementsByClassName("close")[0];

    // 모달을 열기
    modal.style.display = "block";

    // 닫기 버튼을 클릭하면 모달을 닫기
    span.onclick = function() {
        modal.style.display = "none";
    }

    // 모달 외부를 클릭하면 모달을 닫기
    window.onclick = function(event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }

    const worldImages = [
        "/images/Popup_Image/sakura.png",
        "/images/Popup_Image/dora.png",
        "/images/Popup_Image/gu.png",
        "/images/Popup_Image/hello.png",
        "/images/Popup_Image/ju.png",
        "/images/Popup_Image/gu-default.png",
        "/images/Popup_Image/one piece-default.png",
        "/images/Lucky/OnePad.png",
        "/images/Lucky/Mit.png",
        "/images/Lucky/Ren.png"
    ];

    let worldCurrentRound = [];
    let worldNextRound = [];
    let worldCurrentIndex = 0;

    function worldShuffle(array) {
        for (let i = array.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [array[i], array[j]] = [array[j], array[i]];
        }
    }

    function worldStartGame() {
        const rounds = document.getElementById("rounds").value;
        const totalImages = parseInt(rounds);
        worldShuffle(worldImages);
        worldCurrentRound = worldImages.slice(0, totalImages);
        worldNextRound = [];
        worldCurrentIndex = 0;
        worldLoadImages();
        modal.style.display = "none";
    }

    function worldLoadImages() {
        if (worldCurrentRound.length === 1) {
            alert(`Winner is: ${worldCurrentRound[0]}`);
            return;
        }

        if (worldCurrentIndex >= worldCurrentRound.length) {
            if (worldNextRound.length === 0) {
                alert("Error: No more images to load.");
                return;
            }
            worldCurrentRound = worldNextRound.slice();
            worldNextRound = [];
            worldCurrentIndex = 0;
        }

        if (worldCurrentIndex < worldCurrentRound.length - 1) {
            document.getElementById("world-left-image").src = worldCurrentRound[worldCurrentIndex];
            document.getElementById("world-right-image").src = worldCurrentRound[worldCurrentIndex + 1];
        } else {
            alert("Error: Insufficient images to load.");
        }
    }

    document.getElementById("world-left-image").addEventListener("click", () => {
        if (worldCurrentIndex < worldCurrentRound.length) {
            worldNextRound.push(worldCurrentRound[worldCurrentIndex]);
            worldCurrentIndex += 2;
            worldLoadImages();
        }
    });

    document.getElementById("world-right-image").addEventListener("click", () => {
        if (worldCurrentIndex + 1 < worldCurrentRound.length) {
            worldNextRound.push(worldCurrentRound[worldCurrentIndex + 1]);
            worldCurrentIndex += 2;
            worldLoadImages();
        }
    });

    btn.onclick = function() {
        worldStartGame();
    }
});
