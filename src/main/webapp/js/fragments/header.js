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
    const headerAlarm_Btn = $(".h-alarm");
    const headerAlarm_List = $(".h-alarm_list");

    headerAlarm_List.hide();

    headerAlarm_Btn.click(function () {
        $(this).find(".h-alarm_btn").toggleClass("active");
        headerAlarm_List.slideToggle();
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
                alarmList.append('<li><a href="#none">알림이 없습니다.</a></li>');
            } else if (response.alarms && response.alarms.length > 0) {
                $('#alarm_count').text(response.alarmCount);
                response.alarms.forEach(function(alarm) {
                    let link = '<a href="' + alarm.link + '">' + alarm.message + '</a>';
                    alarmList.append('<li onclick="deleteAlarm()">' + link + '</li>');
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