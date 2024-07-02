// levelUpModal.js
$(document).ready(function () {
    var modal = $('#levelUp_modal');
    var closeBtn = modal.find('.levelUp_modal_close');
    var bgm1 = document.getElementById('bgm1');
    var bgm2 = document.getElementById('bgm2');


    // 모달 창 열기 함수
    function openModal() {
        modal.show();
        bgm1.play(); // 첫 번째 BGM 재생
    }

    // 모달 창 닫기 함수
    function closeModal() {
        modal.hide();
        bgm1.pause(); // 첫 번째 BGM 정지
        bgm1.currentTime = 0; // 첫 번째 BGM 재생 위치 초기화
        deleteSession();
    }

    // 모달 닫기 버튼에 클릭 이벤트 추가
    closeBtn.on('click', function () {
        closeModal();
    });

    // 모달 외부 클릭 시 닫기
    $(window).on('click', function (event) {
        if (event.target === modal[0]) {
            closeModal();
        }
    });

    // 세션 삭제 요청 함수
    function deleteSession() {
        $.ajax({
            url: '/delete-levelUp-session',
            type: 'POST',
            success: function (response) {
                console.log('세션 삭제 완료');
            },
            error: function (xhr, status, error) {
                console.error('AJAX 오류 발생:', status, error);
            }
        });
    }

    // 서버에서 세션 상태를 확인하는 함수
    function checkSessionStatus() {
        console.log('checkSessionStatus');
        $.ajax({
            url: '/check-levelUp-session',
            type: 'POST',
            success: function (response) {
                if (response) {
                    console.log('openModal()');
                    openModal();
                } else {
                    console.log('세션이 유효하지 않습니다.');
                }
            },
            error: function (xhr, status, error) {
                console.error('AJAX 오류 발생:', status, error);
            }
        });
    }

    checkSessionStatus();
});

// ----------------------------------------------------------------------------

let particles = [];
const colors = ["#FFC60B", "#FF8B00", "#FEFFDB", "#ffffff"];

function pop() {
    for (let i = 0; i < 150; i++) {
        const p = document.createElement('particule');
        p.x = window.innerWidth * 0.5;
        p.y = window.innerHeight + (Math.random() * window.innerHeight * 0.3);
        p.vel = {
            x: (Math.random() - 0.5) * 15,
            y: Math.random() * -20 - 15
        };
        p.mass = Math.random() * 0.2 + 0.8;
        particles.push(p);
        p.style.transform = `translate(${p.x}px, ${p.y}px)`;
        const size = Math.random() * 15 + 5;
        p.style.width = size + 'px';
        p.style.height = size + 'px';
        p.style.background = colors[Math.floor(Math.random() * colors.length)];
        document.body.appendChild(p);
    }
}

function render() {
    for (let i = particles.length - 1; i--; i > -1) {
        const p = particles[i];
        p.style.transform = `translate3d(${p.x}px, ${p.y}px, 1px)`;

        p.x += p.vel.x;
        p.y += p.vel.y;

        p.vel.y += (0.5 * p.mass);
        if (p.y > (window.innerHeight * 2)) {
            p.remove();
            particles.splice(i, 1);
        }
    }
    requestAnimationFrame(render);
}

function updateModalContent() {
    const modalContent = document.getElementById('modal_content');
    modalContent.innerHTML = `
                <div id="modal_content" class="modal_content">
                    <div class="image-container">
                        <img src="/gif/levelupGIF.gif" class="level_img_star">
                        <img src="/image/${afterLevelImage}" class="level_img">
                    </div>
                    <p>축하합니다! 후덕이는</p>
                    <p>${afterLevel}레벨로 진화했습니다!</p>
                </div>
            `;
}

// 기존 'next' 버튼에 이벤트 리스너 추가
document.getElementById('next').addEventListener('click', () => {
    updateModalContent();
    pop();
    window.setTimeout(render, 200);

    bgm1.pause(); // 첫 번째 BGM 정지
    bgm1.currentTime = 0; // 첫 번째 BGM 재생 위치 초기화
    bgm2.play(); // 두 번째 BGM 재생
});