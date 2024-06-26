// 캔버스와 컨텍스트를 선택
const $c = document.querySelector("canvas");
const ctx = $c.getContext('2d');
const spinButton = document.getElementById('spin-button');

// 상품 배열 및 색상 설정
const product = ["1P", "3P", "5P", "꽝", "1P", "3P", "5P", "꽝"];
const colors = ["#FEFFDB"];
const strokeColor = ["#FF8B00"];

const newMake = () => {
    // 캔버스의 중심 좌표 및 반지름 설정
    const [cw, ch] = [$c.width / 2, $c.height / 2];
    const radius = cw * 0.8; // 반지름을 중심
    const arc = Math.PI / (product.length / 2); // 각 섹션의 각도

    // 캔버스를 초기화
    ctx.clearRect(0, 0, $c.width, $c.height);

    // 상품 배열을 순회하면서 각 섹션을 그림
    for (let i = 0; i < product.length; i++) {
        ctx.fillStyle = colors[0]; // 섹션 배경색
        ctx.strokeStyle = strokeColor[0]; // 섹션 선색
        ctx.beginPath(); // 새로운 경로 시작
        ctx.moveTo(cw, ch); // 중심 좌표로 이동
        ctx.arc(cw, ch, radius, arc * i, arc * (i + 1)); // 섹션을 그림
        ctx.closePath(); // 경로 닫기
        ctx.fill(); // 채우기
        ctx.stroke(); // 선 그리기
    }

    // 텍스트 스타일 설정
    ctx.fillStyle = "#000";
    ctx.font = "bold 20px Pretendard";
    ctx.textAlign = "center";

    // 상품 배열을 순회하면서 텍스트를 그림
    for (let i = 0; i < product.length; i++) {
        const angle = (arc * i) + (arc / 2); // 텍스트 각도 계산
        ctx.save(); // 현재 상태 저장
        ctx.translate(
            cw + Math.cos(angle) * (radius - 50), // 텍스트 위치 계산
            ch + Math.sin(angle) * (radius - 50)
        );
        ctx.rotate(angle + Math.PI / 2); // 텍스트 회전
        product[i].split(" ").forEach((text, j) => {
            ctx.fillText(text, 0, 30 * j); // 텍스트 그리기
        });
        ctx.restore(); // 이전 상태로 복원
    }
    drawArrow(); // 화살표 함수 호출
}

// 화살표
const drawArrow = () => {
    const arrow = document.createElement('div');
    arrow.id = 'arrow';
    document.querySelector('.canvas-container').appendChild(arrow); // canvas-container에 화살표 추가
}

// 돌림판
const rotate = () => {
    $.ajax({
        url: '/roulette/check-points',  // 포인트 확인을 위한 서버 엔드포인트
        type: 'POST',
        success: function(response) {
            if (response) {
                if (confirm("룰렛을 돌리시겠습니까? (2P 차감)")) {
                spinButton.disabled = true;
                $c.style.transform = `initial`;
                $c.style.transition = `initial`;
                const alpha = Math.floor(Math.random() * 100);

                setTimeout(() => {
                    const ran = Math.floor(Math.random() * product.length); // 무작위 인덱스 선택
                    const arc = 360 / product.length; // 각 섹션의 각도
                    const adjustment = Math.random() * (arc - 2) - (arc - 2) / 2; // 각도에 약간의 편차를 추가
                    const rotate = (ran * arc) + 3600 + (arc * 2) - (arc / 3) + alpha + adjustment; // 회전 각도 계산
                    $c.style.transform = `rotate(-${rotate}deg)`; // 회전 적용
                    $c.style.transition = `2s`; // 회전 시간 설정
                    setTimeout(() => {
                        const resultAngle = (rotate % 360 + 360) % 360; // 360으로 나눈 나머지 계산
                        const resultIndex = Math.floor(resultAngle / arc); // 각도를 인덱스로 변환
                        const result = product[(resultIndex + 6) % product.length]; // 결과 계산
                        alert(`포인트 결과: ${result}`); // 결과 알림

                        // 포인트 업데이트
                        if (result !== "꽝") {
                            const points = parseInt(result.replace("P", ""));
                            updatePointsInDB(points); // 포인트를 DB에 바로 업데이트
                        }

                        spinButton.disabled = false; // 버튼 활성화
                    }, 2000);
                    }, 1);
                }
            } else {
                alert('포인트가 부족합니다.');
            }
        },
        error: function() {
            console.log('포인트 확인에 실패했습니다.');
        }
    });
};

// 처음에 돌림판을 만듦
newMake();

// DB에 포인트를 업데이트하는 함수
const updatePointsInDB = (points) => {
    return fetch('/roulette/get-points', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            points: points
        })
    })
        .then(response => response.json());
}