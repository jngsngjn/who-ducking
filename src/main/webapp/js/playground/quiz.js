$(document).ready(function() {
    const startScreen = $('#start-screen');
    const quizScreen = $('#quiz-screen');
    const resultScreen = $('#result-screen');
    const resultImages = $('#result-images');
    const quizTitle = $('.quiz-title');
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
    const quizTimer = $('.quiz-timer'); // 타이머와 모래시계 컨테이너 선택자 추가
    const jsConfetti = new JSConfetti(); // 폭죽

    let timer;
    let timeLeft;
    let correctCount = 0;
    let quizzes = [];
    let currentQuizIndex = 0;

    function loadQuizzes() {
        $.ajax({
            url: '/api/quizzes',  // 서버의 API 엔드포인트
            method: 'GET',  // GET 요청으로 변경
            dataType: 'json',
            success: function(data) {
                quizzes = data;
                totalQuestions.text(quizzes.length);
                initSlider();
            },
            error: function(error) {
                console.error('Error fetching quizzes:', error);
            }
        });
    }

    function initSlider() {
        const sliderContainer = $('.slider-container');
        quizzes.forEach((quiz, index) => {
            const img = $('<img>')
                .attr('src', `/image/ani/${quiz.imageName}`)
                .attr('alt', `Quiz ${index + 1}`);
            sliderContainer.append(img);
        });

        // 무한 슬라이드
        const clonedContent = sliderContainer.html();
        sliderContainer.append(clonedContent);

        let position = 0;
        const slideWidth = sliderContainer.find('img').first().width() + 20; // 이미지 너비 + 마진

        setInterval(() => {
            position -= slideWidth;
            if (Math.abs(position) >= sliderContainer.width() / 2) {
                position = 0;
            }
            sliderContainer.css('transform', `translateX(${position}px)`);
        }, 2000);
    }

    function startQuiz() {
        // 퀴즈 시작 시 5개의 무작위 퀴즈 선택
        quizzes = quizzes.sort(() => 0.5 - Math.random()).slice(0, 5);
        totalQuestions.text(quizzes.length);
        showQuiz(currentQuizIndex);
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
            quizTimer.show(); // 타이머와 모래시계 표시
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
        quizResult.html(`시간 초과!<br> 정답은 <span style="color: #FFC60B">"${correctAnswer}"</span>입니다.`).css('color', '#f44336');
        quizSubmitAnswer.prop('disabled', true);
        quizAnswerInput.off('keypress', handleKeyPress);
        quizTimer.hide(); // 타이머와 모래시계 숨기기
    }

    startQuizButton.click(function() {
        startScreen.hide();
        resultScreen.hide();
        quizScreen.show();
        quizTitle.show();
        startQuiz(); // 퀴즈 시작 함수 호출
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
        quizTimer.hide(); // 타이머와 모래시계 숨기기
        const userAnswer = quizAnswerInput.val().replace(/\s+/g, '').toLowerCase(); //띄어쓰기 비교
        const correctAnswer = quizzes[currentQuizIndex].answer.replace(/\s+/g, '').toLowerCase(); //띄어쓰기 비교
        if (userAnswer === correctAnswer) {
            quizResult.text('정답입니다!').css('color', '#4CAF50');
            correctCount++;
        } else {
            quizResult.html(`틀렸습니다.<br> 정답은 <span style="color: #FFC60B">"${correctAnswer}"</span>입니다.`).css('color', '#f44336');
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
        quizTimer.show(); // 다음 문제로 넘어갈 때 타이머와 모래시계 표시
    });

    function truncateTitle(title) {
        const maxLength = 15;
        if (title.length > maxLength) {
            return title.substring(0, maxLength) + '...';
        } else {
            return title;
        }
    }

    function showResults() {
        quizScreen.hide();
        quizTitle.hide();
        resultScreen.show();
        resultImages.empty();
        quizzes.forEach((quiz, index) => {
            const container = $('<div>').addClass('image-container');
            const img = $('<img>').attr('src', `/image/ani/${quiz.imageName}`).attr('alt', `Quiz ${index + 1}`);
            const title = $('<div>').addClass('anime-title').text(truncateTitle(quiz.answer)); // 애니메이션 제목 추가
            const link = $('<a>').text('리뷰 보기').attr('href', `/animations/${quiz.id}`); // 리뷰 링크 설정
            container.append(img, title, link);
            resultImages.append(container);
        });
        correctCountElement.text(`맞춘 문제 수: ${correctCount} / ${quizzes.length}`);
        correctCountElement.addClass('correct-count');
        jsConfetti.addConfetti({
            confettiColors: [
                "#ff0a54", "#ff477e", "#ff7096", "#ff85a1", "#fbb1bd", "#f9bec7",
                "#ff0000", "#ffcc00", "#00ff00", "#0000ff", "#800080", "#ffa500"
            ],
            confettiRadius: 5,
            confettiNumber: 1000,
        });
    }

    $('#quiz-prev').click(function() {
        window.location.href = '/playground/quiz';
    });

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
