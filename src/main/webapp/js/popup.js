$(document).ready(function() {
    initMap();

    // 일정순, 북마크순 버튼 클릭
    $('.schedule-button, .bookmark-button').on('click', function() {
        $('.schedule-button, .bookmark-button').removeClass('active');
        $(this).addClass('active');

        if ($(this).hasClass('bookmark-button')) {
            displayBookmarkedStores(); // 북마크된 정보만 표시
        } else if ($(this).hasClass('schedule-button')) {
            sortPopupStoresByDate(); // 일정순으로 정렬하여 표시
        } else {
            displaySearchResults(areaArr); // 전체 정보 표시
        }
    });

    // pop-title 클릭시 초기화면으로 돌아가기
    $('.popup-title').on('click', function() {
        displaySearchResults(areaArr); // 초기 화면으로 돌아가기 위해 전체 정보 표시
        map.setCenter(new naver.maps.LatLng(37.494676, 127.027633)); // 초기 지도 위치로 이동
        map.setZoom(15); // 초기 줌 레벨 설정
        closeAllInfoWindows(); // 모든 정보창 닫기
    });

    // 북마크 아이콘 클릭
    $(document).on('click', '.bookmark-icon', function() {
        var storeTitle = $(this).closest('li').find('span').text().trim();
        var store = areaArr.find(function(item) {
            return item.title === storeTitle;
        });
        if (store) {
            store.bookmarked = !store.bookmarked; // 북마크 상태 토글
        }

        $(this).toggleClass('fa-solid fa-bookmark');
        $(this).toggleClass('fa-regular fa-bookmark');
    });

    // 검색 버튼 클릭
    $('.search-button').on('click', function() {
        var query = $('.search-input').val();
        if (query) {
            searchPopupStores(query);
        }
        else {
            alert('검색어를 입력해 주세요.');
         }
    });

    // 검색 버튼 엔터키
    $('.search-input').on('keypress', function(e) {
        if (e.which == 13) {
            var query = $(this).val();
            if (query) {
                searchPopupStores(query);
            }
             else {
                alert('검색어를 입력해 주세요.');
             }
        }
    });
});

// 검색 - 쿼리문으로 배열에서 필터링
function searchPopupStores(query) {
    var results = areaArr.filter(function(store) {
        return store.title.includes(query);
    });
    displaySearchResults(results); //검색 결과
    if (results.length > 0) {
        moveToLocation(results[0]); //위치 이동
    }
}

// 검색 결과 표시 함수
function displaySearchResults(results) {
    var resultsContainer = $('.popup-store-left-list ul');
    resultsContainer.empty();

    results.forEach(function(store, index) {
        var bookmarkClass = store.bookmarked ? 'fa-solid' : 'fa-regular';
        var listItem = `
            <li data-index="${index}">
                <div class="popup-store-img-container">
                    <img src="${store.image}" class="popup-store-img">
                </div>
                <div class="popup-info">
                    <span>${store.title} <i class="${bookmarkClass} fa-bookmark icon bookmark-icon"></i></span>
                    <div class="popup-dates">
                        <div class="date-left-date" data-label="기간:">${store.date}</div>
                        <div class="date-left-time" data-label="시간:">${store.time}</div>
                        <div class="date-left-address" data-label="장소:">${store.address}</div>
                    </div>
                </div>
            </li>
        `;
        resultsContainer.append(listItem);
    });
}

// 북마크된 정보만 표시하는 함수
function displayBookmarkedStores() {
    var bookmarkedStores = areaArr.filter(function(store) {
        return store.bookmarked;
    });
    displaySearchResults(bookmarkedStores);
}

// 일정순으로 정렬하여 표시하는 함수
function sortPopupStoresByDate() {
    var sortedStores = areaArr.slice().sort(function(a, b) {
        // "기간 : YY.MM.DD ~ YY.MM.DD" 형식
        var dateA = a.date.split(' ~ ')[0].replace(/기간 : /g, '').split('.');
        var dateB = b.date.split(' ~ ')[0].replace(/기간 : /g, '').split('.');

        // 각 날짜를 연, 월, 일로 분리
        var yearA = parseInt(dateA[0]);
        var yearB = parseInt(dateB[0]);
        var monthA = parseInt(dateA[1]);
        var monthB = parseInt(dateB[1]);
        var dayA = parseInt(dateA[2]);
        var dayB = parseInt(dateB[2]);

        // 연도를 기준으로 비교
        if (yearA !== yearB) {
            return yearA - yearB;
        }
        // 연도가 같으면 월을 기준으로 비교
        else if (monthA !== monthB) {
            return monthA - monthB;
        }
        // 연도와 월이 같으면 일을 기준으로 비교
        else {
            return dayA - dayB;
        }
    });

    // 정렬된 결과를 화면에 표시
    displaySearchResults(sortedStores);
}

// 팝업 데이터 배열
var areaArr = [
    {
        title: "두근두근 도라에몽전 In 부산",
        lat: "35.8548554163402",
        lng: "128.564820995296",
        address: "장소: 대구 이월드 83타워 뮤지엄 2F",
        date: "기간 : 24.05.01 ~ 24.11.03",
        time: "시간: 11:00~19:30",
        image: "/images/Popup_Image/dora.png",
        bookmarked: false
    },
    {
        title: "헬로키티 50주년 특별전 :산리오 캐릭터즈와의 여행",
        lat: "37.5680445876689",
        lng: "127.010890355484",
        address: "장소: DDP 뮤지엄 지하 2층,<br> 전시 1관 동대문 디자인 플라자 뮤지엄",
        date: "기간 : 24.04.13 ~ 24.08.13",
        time: "시간: 10:00~20:00",
        image: "/images/Popup_Image/hello.png",
        bookmarked: false
    },
    {
        title: "원피스 대해적시대展",
        lat: "37.5297718014452",
        lng: "126.964741503485",
        address: "장소: 대원뮤지엄(용산역 아이파크몰 6층)",
        date: "기간 : 24.06.29 ~ 24.11.03",
        time: "시간 : 10:30~20:00 (오후7시 입장마감)",
        image: "/images/Popup_Image/one piece-default.png",
        bookmarked: false
    },
    {
        title: "더현대 서울 2024주술회전 팝업스토어",
        lat: "37.5251913154781",
        lng: "126.929112756574",
        address: "장소: 서울특별시 영등포구 여의대로 108 파크원 더현대 서울",
        date: "기간 : 24.06.27 - 24.07.03",
        time: "시간: 10:30 ~ 20:00",
        image: "/images/Popup_Image/ju.png",
        bookmarked: false
    },
    {
        title: "더현대 대구 2024주술회전 팝업스토어",
        lat: "35.8666296004242",
        lng: "128.590625718585",
        address: "장소: 대구 중구 현대백화점 더현대 대구",
        date: "기간 : 24.08.05 - 24.08.11",
        time: "시간: 10:30 ~ 20:00",
        image: "/images/Popup_Image/ju.png",
        bookmarked: false
    },
    {
        title: "잠실 짱구 팝업스토어 2024",
        lat: "37.5137129859207",
        lng: "127.104301829165",
        address: "장소: 서울 잠실 롯데월드몰",
        date: "기간 : 24.06.05 ~ 24.06.16",
        time: "시간: 10:30 ~22:00",
        image: "/images/Popup_Image/gu.png",
        bookmarked: false
    },
    {
        title: "전주 짱구 팝업스토어 2024",
        lat: "35.8182133310179",
        lng: "127.153608497904",
        address: "장소: 전주 한옥마을",
        date: "기간 : 24.08.02 ~ 24.09.18",
        time: "시간: 10:30 ~22:00",
        image: "/images/Popup_Image/gu-default.png",
        bookmarked: false
    },
    {
        title: "대구 짱구 팝업스토어 2024",
        lat: "35.8666296004242",
        lng: "128.590625718585",
        address: "장소: 대구 현대백화점 더현대",
        date: "기간 : 24.09.27 ~ 24.10.06",
        time: "시간: 10:30 ~22:00",
        image: "/images/Popup_Image/gu-default.png",
        bookmarked: false
    },
    {
        title: "부산 짱구 팝업스토어 2024",
        lat: "35.1688178394069",
        lng: "129.129523260187",
        address: "장소: 부산 신세계백화점 센텀시티",
        date: "기간 : 24.11.15 ~ 24.11.24",
        time: "시간: 10:30 ~22:00",
        image: "/images/Popup_Image/gu-default.png",
        bookmarked: false
    }
];

let markers = [];
let infoWindows = [];
var map;

// 초기 지도 설정 함수
function initMap() {
    map = new naver.maps.Map('map', {
        center: new naver.maps.LatLng(37.494676, 127.027633),
        zoom: 15
    });

    var resultsContainer = $('.popup-store-left-list ul');
    resultsContainer.empty();

    for (var i = 0; i < areaArr.length; i++) {
        var marker = new naver.maps.Marker({
            map: map,
            title: areaArr[i].title,
            position: new naver.maps.LatLng(areaArr[i].lat, areaArr[i].lng)
        });

        var infoWindow = new naver.maps.InfoWindow({
            content: '<div style="width:250px;text-align:center;padding:10px;"><b>' + areaArr[i].title + '</div>'
        });

        markers.push(marker);
        infoWindows.push(infoWindow);

        var listItem = `
            <li data-index="${i}">
                <div class="popup-store-img-container">
                    <img src="${areaArr[i].image}" class="popup-store-img">
                </div>
                <div class="popup-info">
                    <span>${areaArr[i].title} <i class="fa-regular fa-bookmark icon bookmark-icon"></i></span>
                    <div class="popup-dates">
                        <div class="date-left">${areaArr[i].date}</div>
                        <div class="date-left">${areaArr[i].time}</div>
                        <div class="date-left">${areaArr[i].address}</div>
                    </div>
                </div>
            </li>
        `;
        resultsContainer.append(listItem);
    }

    for (var i = 0; i < markers.length; i++) {
        naver.maps.Event.addListener(markers[i], 'click', getClickHandler(i));
    }

    // 제목 클릭 시 해당 위치로 이동
    $(document).on('click', '.popup-info span', function() {
        var storeTitle = $(this).text().trim();
        var store = areaArr.find(function(item) {
            return item.title === storeTitle;
        });
        if (store) {
            moveToLocation(store);
        }
    });
    window.moveToLocation = function(store) {
        for (var i = 0; i < areaArr.length; i++) {
            if (areaArr[i].title === store.title) {
                var latLng = new naver.maps.LatLng(areaArr[i].lat, areaArr[i].lng);
                map.setCenter(latLng);
                infoWindows[i].open(map, markers[i]);
                break;
            }
        }
    };
}

// 마커 클릭 핸들러
function getClickHandler(seq) {
    return function(e) {
        var marker = markers[seq],
            infoWindow = infoWindows[seq];

        if (infoWindow.getMap()) {
            infoWindow.close();
        } else {
            infoWindow.open(map, marker);
        }
    }
}

// 모든 정보창 닫기
function closeAllInfoWindows() {
    for (var i = 0; i < infoWindows.length; i++) {
        infoWindows[i].close();
    }
}
