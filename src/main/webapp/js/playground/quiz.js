document.addEventListener("DOMContentLoaded", function () {
    const startScreen = document.getElementById("start-screen");
    const quizScreen = document.getElementById("quiz-screen");
    const resultScreen = document.getElementById("result-screen");
    const resultImages = document.getElementById("result-images");
    const startQuizButton = document.getElementById("start-quiz");
    const retryQuizButton = document.getElementById("retry-quiz");
    const goHomeButton = document.getElementById("go-home");
    const goPlaygroundButton = document.getElementById("go-playground");
    const quizImage = document.getElementById("quiz-image");
    const quizQuestion = document.getElementById("quiz-question");
    const quizAnswerInput = document.getElementById("quiz-answer-input");
    const quizSubmitAnswer = document.getElementById("quiz-submit-answer");
    const quizNext = document.getElementById("quiz-next");
    const quizResult = document.getElementById("quiz-result");
    const totalQuestions = document.getElementById("quiz-total-questions");
    const currentQuestion = document.getElementById("quiz-current-question");
    const quizTime = document.getElementById("quiz-time"); // 타이머
    const correctCountElement = document.getElementById("correct-count"); // 맞춘 문제 수 요소
    let timer;
    let timeLeft;
    let correctCount = 0; // 맞춘 문제 수 저장 변수

    // 퀴즈 데이터 배열
    let quizzes = [
        { imageName: "quiz1.jpg", question: "애니메이션의 이름은?", answer: "주술회전" },
        { imageName: "quiz2.jpg", question: "애니메이션의 이름은?", answer: "귀멸의 칼날" },
        { imageName: "quiz3.jpg", question: "애니메이션의 이름은?", answer: "원피스" },
        { imageName: "quiz4.jpg", question: "애니메이션의 이름은?", answer: "나루토" },
        { imageName: "quiz5.jpg", question: "애니메이션의 이름은?", answer: "도라에몽" }
    ];
    let currentQuizIndex = 0;

    function showQuiz(index) {
        if (index >= 0 && index < quizzes.length) {
            const quiz = quizzes[index];
            quizImage.src = `/image/ani/${quiz.imageName}`;
            quizQuestion.textContent = quiz.question;
            quizAnswerInput.value = '';
            quizResult.textContent = '';
            currentQuestion.textContent = index + 1;
            clearInterval(timer);
            timeLeft = 10;
            quizSubmitAnswer.disabled = false;
            quizAnswerInput.addEventListener('keypress', handleKeyPress);
            startTimer();
        }
    }

    function startTimer() {
        updateTimerDisplay();
        timer = setInterval(() => {
            timeLeft--;
            updateTimerDisplay();
            if (timeLeft <= 0) {
                clearInterval(timer);
                timeOut();
            }
        }, 1000);
    }

    function updateTimerDisplay() {
        quizTime.textContent = timeLeft;
    }

    function timeOut() {
        const correctAnswer = quizzes[currentQuizIndex].answer;
        quizResult.textContent = `시간 초과! 정답은 ${correctAnswer}입니다.`;
        quizResult.style.color = '#f44336';
        quizSubmitAnswer.disabled = true;
        quizAnswerInput.removeEventListener('keypress', handleKeyPress);
    }

    startQuizButton.addEventListener('click', function() {
        startScreen.style.display = 'none';
        quizScreen.style.display = 'block';
        totalQuestions.textContent = quizzes.length;
        showQuiz(currentQuizIndex);
    });

    quizSubmitAnswer.addEventListener('click', submitAnswer);

    function handleKeyPress(e) {
        if (e.key === 'Enter') {
            e.preventDefault();
            submitAnswer();
        }
    }

    function submitAnswer() {
        if (quizSubmitAnswer.disabled) return;
        clearInterval(timer);
        const userAnswer = quizAnswerInput.value;
        const correctAnswer = quizzes[currentQuizIndex].answer;
        if (userAnswer.toLowerCase() === correctAnswer.toLowerCase()) {
            quizResult.textContent = '정답입니다!';
            quizResult.style.color = '#4CAF50';
            correctCount++;
        } else {
            quizResult.textContent = `틀렸습니다. 정답은 ${correctAnswer}입니다.`;
            quizResult.style.color = '#f44336';
        }
        quizSubmitAnswer.disabled = true;
        quizAnswerInput.removeEventListener('keypress', handleKeyPress);
    }

    quizNext.addEventListener('click', function () {
        if (currentQuizIndex < quizzes.length - 1) {
            currentQuizIndex++;
            showQuiz(currentQuizIndex);
        } else if (currentQuizIndex === quizzes.length - 1) {
            showResults();
        }
    });

    function showResults() {
        quizScreen.style.display = 'none';
        resultScreen.style.display = 'block';
        resultImages.innerHTML = '';
        quizzes.forEach((quiz, index) => {
            const img = document.createElement('img');
            img.src = `/image/ani/${quiz.imageName}`;
            img.alt = `Quiz ${index + 1}`;
            img.addEventListener('click', () => {
                window.location.href = '/';
            });
            resultImages.appendChild(img);
        });
        correctCountElement.textContent = `맞춘 문제 수: ${correctCount} / ${quizzes.length}`;
    }

    retryQuizButton.addEventListener('click', function() {
        currentQuizIndex = 0;
        correctCount = 0;
        resultScreen.style.display = 'none';
        quizScreen.style.display = 'block';
        showQuiz(currentQuizIndex);
        window.location.href = '/quiz'; //첫 화면으로 돌아가지만 버그가 있음
    });

    goHomeButton.addEventListener('click', function() {
        window.location.href = '/'; //홈페이지
    });

    goPlaygroundButton.addEventListener('click', function() {
        window.location.href = '/playground';  // 놀이터 페이지
    });
});