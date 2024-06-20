   const images = [
        "/images/Popup_Image/sakura.png",
        "/images/Popup_Image/dora.png",
        "/images/Popup_Image/gu.png",
        "/images/Popup_Image/hello.png",
        "/images/Popup_Image/ju.png",
        "/images/Popup_Image/gu-default.png"
    ];

    let currentRound = [];
    let nextRound = [];
    let currentIndex = 0;

    function shuffle(array) {
        for (let i = array.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [array[i], array[j]] = [array[j], array[i]];
        }
    }

    function startGame() {
        shuffle(images);
        currentRound = images.slice();
        nextRound = [];
        currentIndex = 0;
        loadImages();
    }

    function loadImages() {
        if (currentRound.length === 1) {
            alert(`Winner is: ${currentRound[0]}`);
            return;
        }

        if (currentIndex >= currentRound.length) {
            currentRound = nextRound.slice();
            nextRound = [];
            currentIndex = 0;
        }

        document.getElementById("left-image").src = currentRound[currentIndex];
        document.getElementById("right-image").src = currentRound[currentIndex + 1];
    }

    document.getElementById("left-image").addEventListener("click", () => {
        nextRound.push(currentRound[currentIndex]);
        currentIndex += 2;
        loadImages();
    });

    document.getElementById("right-image").addEventListener("click", () => {
        nextRound.push(currentRound[currentIndex + 1]);
        currentIndex += 2;
        loadImages();
    });

    startGame();