$(document).ready(function () {
    const startScreen = $("#start-screen");
    const quizScreen = $("#quiz-screen");
    const resultScreen = $("#result-screen");
    const resultImages = $("#result-images");
    const quizTitle = $(".quiz_main_title");
    const startQuizButton = $("#start-quiz");
    const retryQuizButton = $("#retry-quiz");
    const goHomeButton = $("#go-home");
    const goPlaygroundButton = $("#go-playground");
    const quizImage = $("#quiz-image");
    const quizAnswerInput = $("#quiz-answer-input");
    const quizSubmitAnswer = $("#quiz-submit-answer");
    const quizNext = $("#quiz-next");
    const quizResult = $("#quiz-result");
    const totalQuestions = $("#quiz-total-questions");
    const currentQuestion = $("#quiz-current-question");
    const quizTime = $("#quiz_main_timer");
    const correctCountElement = $("#correct-count");
    const quizTimer = $(".quiz_main_timer_box");
    const quizSubTitle = $(".quiz_start_subTitle");
    const jsConfetti = new JSConfetti();

    // 모달 관련 요소 추가
    const modal = $("#pointModal");
    const closeModal = $("#closeModal");

    let timer;
    let timeLeft;
    let correctCount = 0;
    let quizzes = [];
    let currentQuizIndex = 0;

    function loadQuizzes() {
        $.ajax({
            url: "/api/quizzes",
            method: "GET",
            dataType: "json",
            success: function (data) {
                quizzes = data;
                totalQuestions.text(quizzes.length);
                initSlider();
            },
            error: function (error) {
                console.error("Error fetching quizzes:", error);
            },
        });
    }

    function initSlider() {
        const sliderContainer = $(".quiz_start_slider_container");
        quizzes.forEach((quiz, index) => {
            const img = $("<img>")
                .attr("src", `/image/ani/${quiz.imageName}`)
                .attr("alt", `Quiz ${index + 1}`);
            sliderContainer.append(img);
        });
        // 무한 슬라이드를 위해 컨텐츠 복제
        const clonedContent = sliderContainer.html();
        sliderContainer.append(clonedContent);

        setSlideDuration(100); //100초 설정 css transform: translateX(calc(-1000% )); 값이랑 조정 가능
    }

    function setSlideDuration(duration) {
        document.documentElement.style.setProperty("--slide-duration", `${duration}s`);
    }

    function startQuiz() {
        // 퀴즈 시작 시 5개의 무작위 퀴즈 선택
        quizzes = quizzes.sort(() => 0.5 - Math.random()).slice(0, 5);
        totalQuestions.text(quizzes.length);
        showQuiz(currentQuizIndex);
        quizSubTitle.hide();
    }

    function showQuiz(index) {
        if (index >= 0 && index < quizzes.length) {
            const quiz = quizzes[index];
            quizImage.attr("src", `/image/ani/${quiz.imageName}`);
            quizAnswerInput.val("");
            quizResult.text("");
            currentQuestion.text(index + 1);

            clearInterval(timer);
            timeLeft = 15;
            quizSubmitAnswer.prop("disabled", false);
            quizAnswerInput.on("keypress", handleKeyPress);
            startTimer();
            quizTimer.show();
            quizNext.hide();
        }
    }

    function startTimer() {
        //15초 타이머 설정
        updateTimerDisplay();
        timer = setInterval(() => {
            timeLeft--;
            updateTimerDisplay();
            if (timeLeft <= 0) {
                clearInterval(timer);
                timeOut();
            }
        }, 1000); //1초
    }

    function updateTimerDisplay() {
        quizTime.text(timeLeft);
    }

    function timeOut() {
        //시간 초과 함수
        const correctAnswer = quizzes[currentQuizIndex].answer;
        quizResult
            .html(`시간 초과!<br> 정답은 <span style="color: #FFC60B">"${correctAnswer}"</span>입니다.`)
            .css("color", "#f44336");
        quizSubmitAnswer.prop("disabled", true);
        quizAnswerInput.off("keypress", handleKeyPress);
        quizTimer.hide();
        quizNext.show();
    }

    startQuizButton.click(function () {
        //시작 화면 설정
        startScreen.hide();
        resultScreen.hide();
        quizScreen.show();
        quizTitle.show();
        startQuiz();
    });

    quizSubmitAnswer.click(submitAnswer);
    quizAnswerInput.on("keypress", handleKeyPress);

    function handleKeyPress(e) {
        if (e.key === "Enter") {
            e.preventDefault();
            submitAnswer();
        }
    }

    function submitAnswer() {
        if (quizSubmitAnswer.prop("disabled")) return;
        clearInterval(timer);
        quizTimer.hide();
        const userAnswer = quizAnswerInput.val().replace(/\s+/g, '').toLowerCase();// s+g의 계산식을 이용하여 공백을 제거하고 입력값을 계산함
        const correctAnswer = quizzes[currentQuizIndex].answer.toLowerCase();
        const userInput = quizzes[currentQuizIndex].answer.replace(/\s+/g, '').toLowerCase();
        if (userAnswer === correctAnswer || userAnswer === userInput) {
            quizResult.text('정답입니다!').css('color', '#4CAF50');
            correctCount++;
        } else {
            quizResult
                .html(`틀렸습니다.<br> 정답은 <span style="color: #FFC60B">"${correctAnswer}"</span>입니다.`)
                .css("color", "#f44336");
        }
        quizSubmitAnswer.prop("disabled", true);
        quizAnswerInput.off("keypress", handleKeyPress);
        quizNext.show();
    }

    quizNext.click(function () {
        if (currentQuizIndex < quizzes.length - 1) {
            currentQuizIndex++;
            showQuiz(currentQuizIndex);
        } else if (currentQuizIndex === quizzes.length - 1) {
            showResults();
        }
    });

    function truncateTitle(title) {
        const maxLength = 15; // 15개 글자 넘어가면 ...으로 표시
        return title.length > maxLength ? title.substring(0, maxLength) + "..." : title;
    }

    function showResults() {
        quizScreen.hide();
        quizTitle.hide();
        quizSubTitle.hide();
        resultScreen.show();
        resultImages.empty();
        quizzes.forEach((quiz, index) => {
            const container = $("<div>").addClass("image-container");
            const img = $("<img>") // 이미지 클릭시 id에 대한 애니메이션 리뷰게시판 이동
                .attr("src", `/image/ani/${quiz.imageName}`)
                .attr("alt", `Quiz ${index + 1}`)
                .click(function () {
                    window.location.href = `/animations/${quiz.id}`;
                });
            const title = $("<div>").addClass("anime-title").text(truncateTitle(quiz.answer)); // 제목 표시
            const link = $("<a>").text("리뷰 보기").attr("href", `/animations/${quiz.id}`); // 버튼 클릭 리뷰게시판 이동
            container.append(img, title, link);
            resultImages.append(container);
        });
        correctCountElement.text(`맞춘 문제 수: ${correctCount} / ${quizzes.length}`); // 맞춘 문제 표시
        correctCountElement.addClass("correct-count"); // css 입히기 위해 생성함

        // 맞춘 개수 표시 + 포인트 지급
        if (correctCount > 0) {
            modal.css('display', 'flex');
            let resultText = $('#resultText');
            let pointText = $('#pointText');
            switch (correctCount) {
                case 1 :
                    resultText.text('✨많이 분발하셔야겠어요~✨');
                    pointText.html('💰<span class="quiz_result_modal_text">3포인트</span>💰가 지급되었습니다!');
                    break;
                case 2 :
                    resultText.text('✨아직 만족하기엔 일러요~✨');
                    pointText.html('💰<span class="quiz_result_modal_text">6포인트</span>💰가 지급되었습니다!');
                    break;
                case 3 :
                    resultText.text('✨조금 더 노력해 보세요~✨');
                    pointText.html('💰<span class="quiz_result_modal_text">9포인트</span>💰가 지급되었습니다!');
                    break;
                case 4 :
                    resultText.text('✨와우~ 다음엔 만점에 도전해 보세요~✨');
                    pointText.html('💰<span class="quiz_result_modal_text">12포인트</span>💰가 지급되었습니다!');
                    break;
                default :
                    resultText.text('✨문제를 모두 맞춘 당신,, 대단합니다!✨');
                    pointText.html('💰<span class="quiz_result_modal_text">15포인트</span>💰가 지급되었습니다!');
            }

            $.ajax({
                url: '/playground/quiz/result',
                type: 'POST',
                data: { correctCount: correctCount },
                success: function() {
                    console.log("포인트 지급 성공")
                },
                error: function (xhr, status, error) {
                    console.error("Error occurred:", status, error);
                }
            });
            jsConfetti.addConfetti({
                confettiColors: [
                 "#ff0a54", "#ff477e", "#ff7096", "#ff85a1", "#fbb1bd", "#f9bec7",
                 "#ff0000", "#ffcc00", "#00ff00", "#0000ff", "#800080", "#ffa500"
                ],
                confettiRadius: 5,
                confettiNumber: 800,
            });
        } else {
            modal.css("display", "none");
        }
    }

    // 모달 닫기 버튼 -> 확인 누르면 됨
    closeModal.click(function () {
        modal.css("display", "none");
    });

    //버튼  href 설정
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
