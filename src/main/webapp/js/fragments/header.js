$(document).ready(function () {
    // AOS
    AOS.init({
        once: true,
        duration: 2000,
    });

    // HeaderProfile_DropDownEvent
    const headerProfile_Btn = $(".h-profile_box");
    const headerProfile_Menu = $(".h-profile_menu");

    headerProfile_Menu.hide();

    headerProfile_Btn.click(function () {
        $(this).find(".h-profile").toggleClass("active");
        headerProfile_Menu.slideToggle();

        $.ajax({
            url: '/update-header',
            type: 'POST',
            success: function(response) {
                console.log('요청이 성공적으로 완료되었습니다.');
                console.log(response);

                $('#point').text(response.point + "P");
            },
            error: function() {
                // 요청이 실패했을 때 실행할 코드
                console.log('요청이 실패했습니다.');
            }
        });
    });

    // MobileMenu_OnOffEvent
    const mobileMenu_OnBtn = $(".h-mobile_btn-open");
    const mobileMenu_OffBtn = $(".h-mobile_btn-close");
    const mobileMenu = $(".h-mobile_box");
    const searchBox = $(".h-search_box");
    const mainContents = $("#container");
    const footer = $("#footer");

    mobileMenu.hide();
    mobileMenu_OffBtn.hide();

    mobileMenu_OnBtn.click(function () {
        mobileMenu.fadeIn();
        searchBox.hide();
        mainContents.hide();
        footer.hide();
        mobileMenu_OnBtn.hide();
        mobileMenu_OffBtn.show();
        $.ajax({
            url: '/update-header',
            type: 'POST',
            success: function(response) {
                console.log('요청이 성공적으로 완료되었습니다.');
                console.log(response);

                $('#mobile-point').text(response.point + "P");
            },
            error: function() {
                // 요청이 실패했을 때 실행할 코드
                console.log('요청이 실패했습니다.');
            }
        });
    });

    mobileMenu_OffBtn.click(function () {
        mobileMenu.fadeOut();
        searchBox.show();
        mainContents.show();
        footer.show();
        mobileMenu_OffBtn.hide();
        mobileMenu_OnBtn.show();
    });

    // MobileProfile_DropDownEvent
    const mobileProfile_Btn = $(".h-mobile_profile-box");
    const mobileProfile_Menu = $(".h-mobile_profile-lnb");

    mobileProfile_Menu.hide();

    mobileProfile_Btn.click(function () {
        $(this).toggleClass("active");
        mobileProfile_Menu.slideToggle();
    });

    // HeaderAlarm_DropDownEvent
    const headerAlarm_Btn = $("#alarm_btn");
    const headerAlarm_List = $(".h-alarm_list");

    headerAlarm_List.hide();

    headerAlarm_Btn.click(function () {
        $(this).find(".h-alarm_btn").toggleClass("active");
        headerAlarm_List.slideToggle();
    });

    // 외부 클릭 시 닫기
    $(window).click(function (event) {
        // 만약 클릭한 요소가 h-alarm_btn이나 headerAlarm_List가 아닐 경우에만 실행
        if (!$(event.target).closest('.h-alarm_btn, #h-alarm_list').length) {
            var headerAlarm_List = $('#h-alarm_list'); // 슬라이드 업 할 요소를 선택
            if ($(this).find(".h-alarm_btn").toggleClass("active")) { // h-alarm_btn에 active 클래스가 있는지 확인
                headerAlarm_List.slideUp();
            }
        }
    });

    // MobileAlarm_DropDownEvent
    const mobileAlarm_Btn = $(".h-mobile_alarm");
    const mobileAlarm_List = $(".h-mobile_alarm-list");

    mobileAlarm_List.hide();

    mobileAlarm_Btn.click(function () {
        $(this).toggleClass("active");
        mobileAlarm_List.slideToggle();
    });

    $.ajax({
        url: '/update-alarm',
        type: 'POST',
        success: function(response) {
            let alarmList = $('.h-alarm_list');
            alarmList.empty(); // 기존 알림 리스트 비우기

            if (response.alarmCount === 0) {
                $('#alarm_count').hide();
                alarmList.append('<li><a>알림이 없습니다.</a></li>');
                $('#markAllRead').hide();
            } else if (response.alarms && response.alarms.length > 0) {
                $('#alarm_count').text(response.alarmCount);
                alarmList.append('<button id="markAllRead">모두 확인</button>')
                $('#markAllRead').show();
                response.alarms.forEach(function(alarm) {
                    let link = '<a href="' + alarm.link + '" data-id="' + alarm.id + '">' + alarm.message + '</a>';
                    let listItem = '<li>' + link + ' <button class="delete-alarm-btn" data-id="' + alarm.id + '">X</button> </li>';
                    alarmList.append(listItem);
                });
            }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.error('요청이 실패했습니다.');
            console.error('Status: ' + textStatus);
            console.error('Error: ' + errorThrown);
            console.error('Response Text: ' + jqXHR.responseText);
        }
    });
});

$(document).on('click', '.delete-alarm-btn', function (event) {
    event.preventDefault(); // 버튼의 기본 동작을 막음
    event.stopPropagation(); // 이벤트 전파를 막음
    var alarmId = $(this).data('id');
    if (alarmId) {
        deleteAlarm(alarmId);
    }
});

$(document).on('click', 'a', function() {
    var alarmId = $(this).data('id');
    if (alarmId) {
        deleteAlarm(alarmId);
    }
});

// '모두 확인' 버튼 클릭
$(document).on('click', '#markAllRead', function() {
    $.ajax({
        url: '/delete-all-alarms',
        type: 'POST',
        success: function() {
            $('#alarm_count').text(0);
            $('#alarm_count').hide();
            let alarmList = $('.h-alarm_list');
            alarmList.empty();
            $('#markAllRead').hide();
            var headerAlarm_List = $('#h-alarm_list');
            headerAlarm_List.slideUp();
            alarmList.append('<li><a>알림이 없습니다.</a></li>');
        },
        error: function() {
            console.log('알림을 삭제하는 데 실패했습니다.');
        }
    });
});

function deleteAlarm(alarmId) {
    $.ajax({
        url: '/delete-alarm',
        type: 'POST',
        data: { id: alarmId },
        success: function() {
            $('a[data-id="' + alarmId + '"]').closest('li').remove();
            let alarmCount = parseInt($('#alarm_count').text()) - 1;
            if (alarmCount > 0) {
                $('#alarm_count').text(alarmCount);
            } else {
                $('#alarm_count').hide();
                let alarmList = $('.h-alarm_list');
                alarmList.empty();
                alarmList.append('<li><a>알림이 없습니다.</a></li>');
                $('#markAllRead').hide();
            }
        },
        error: function() {
            console.log('알림을 삭제하는 데 실패했습니다.');
        }
    });
}