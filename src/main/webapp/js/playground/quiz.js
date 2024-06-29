$(document).ready(function() {
    const startScreen = $('#start-screen');
    const quizScreen = $('#quiz-screen');
    const resultScreen = $('#result-screen');
    const resultImages = $('#result-images');
    const startQuizButton = $('#start-quiz');
    const retryQuizButton = $('#retry-quiz');
    const goHomeButton = $('#go-home');
    const goPlaygroundButton = $('#go-playground');
    const quizImage = $('#quiz-image');
    const quizAnswerInput = $('#quiz-answer-input');
    const quizSubmitAnswer = $('#quiz-submit-answer');
    const quizNext = $('#quiz-next');
    const quizResult = $('#quiz-result');
    const totalQuestions = $('#quiz-total-questions');
    const currentQuestion = $('#quiz-current-question');
    const quizTime = $('#quiz-time');
    const correctCountElement = $('#correct-count');

    let timer;
    let timeLeft;
    let correctCount = 0;
    let quizzes = [];
    let currentQuizIndex = 0;

    function loadQuizzes() {
        $.ajax({
            url: '/api/quizzes',
            method: 'POST',
            dataType: 'json',
            success: function(data) {
                quizzes = data;
                totalQuestions.text(quizzes.length);
            },
            error: function(error) {
                console.error('Error fetching quizzes:', error);
            }
        });
    }

    function showQuiz(index) {
        if (index >= 0 && index < quizzes.length) {
            const quiz = quizzes[index];
            quizImage.attr('src', `/image/ani/${quiz.imageName}`);
            quizAnswerInput.val('');
            quizResult.text('');
            currentQuestion.text(index + 1);

            clearInterval(timer);
            timeLeft = 10;
            quizSubmitAnswer.prop('disabled', false);
            quizAnswerInput.on('keypress', handleKeyPress);
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
        quizTime.text(timeLeft);
    }

    function timeOut() {
        const correctAnswer = quizzes[currentQuizIndex].answer;
        quizResult.text(`시간 초과! 정답은 ${correctAnswer}입니다.`).css('color', '#f44336');
        quizSubmitAnswer.prop('disabled', true);
        quizAnswerInput.off('keypress', handleKeyPress);
    }

    startQuizButton.click(function() {
        startScreen.hide();
        quizScreen.show();
        showQuiz(currentQuizIndex);
    });

    quizSubmitAnswer.click(submitAnswer);
    quizAnswerInput.on('keypress', handleKeyPress);

    function handleKeyPress(e) {
        if (e.key === 'Enter') {
            e.preventDefault();
            submitAnswer();
        }
    }

    function submitAnswer() {
        if (quizSubmitAnswer.prop('disabled')) return;
        clearInterval(timer);
        const userAnswer = quizAnswerInput.val();
        const correctAnswer = quizzes[currentQuizIndex].answer;
        if (userAnswer.toLowerCase() === correctAnswer.toLowerCase()) {
            quizResult.text('정답입니다!').css('color', '#4CAF50');
            correctCount++;
        } else {
            quizResult.text(`틀렸습니다. 정답은 ${correctAnswer}입니다.`).css('color', '#f44336');
        }
        quizSubmitAnswer.prop('disabled', true);
        quizAnswerInput.off('keypress', handleKeyPress);
    }

    quizNext.click(function() {
        if (currentQuizIndex < quizzes.length - 1) {
            currentQuizIndex++;
            showQuiz(currentQuizIndex);
        } else if (currentQuizIndex === quizzes.length - 1) {
            showResults();
        }
    });

    function showResults() {
        quizScreen.hide();
        resultScreen.show();
        resultImages.empty();
        quizzes.forEach((quiz, index) => {
            const img = $('<img>').attr('src', `/image/ani/${quiz.imageName}`).attr('alt', `Quiz ${index + 1}`);
            img.click(() => window.location.href = '/');
            resultImages.append(img);
        });
        correctCountElement.text(`맞춘 문제 수: ${correctCount} / ${quizzes.length}`);
    }

    retryQuizButton.click(function() {
        window.location.href = '/playground/quiz';
    });

    goHomeButton.click(function() {
        window.location.href = '/';
    });

    goPlaygroundButton.click(function() {
        window.location.href = '/playground';
    });

    loadQuizzes();
});