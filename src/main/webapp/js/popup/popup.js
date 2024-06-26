var areaArr = []; // 전역 변수로 선언
var markers = [];
var infoWindows = [];
var map;

$(document).ready(function() {
    initMap();
    loadPopupStores();

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

    // 검색 버튼 클릭
    $('.search-button').on('click', function() {
        var query = $('.search-input').val();
        if (query) {
            searchPopupStores(query);
        } else {
            alert('검색어를 입력해 주세요.');
        }
    });

    // 검색 버튼 엔터키
    $('.search-input').on('keypress', function(e) {
        if (e.which == 13) {
            var query = $(this).val();
            if (query) {
                searchPopupStores(query);
            } else {
                alert('검색어를 입력해 주세요.');
            }
        }
    });
});

function loadPopupStores() {
    $.ajax({
        url: '/api/popupstores',
        method: 'GET',
        success: function(data) {
            areaArr = data.map(function(store) {
                return {
                    title: store.name,
                    lat: store.latitude,
                    lng: store.longitude,
                    address: "장소: " + store.address.address + " " + store.address.detailedAddress,
                    date: "기간 : " + formatDate(store.startDate) + " ~ " + formatDate(store.endDate),
                    time: "시간: " + store.openTime + " ~ " + store.closeTime,
                    image: "/image/popup/" + store.imageName,
                    bookmarked: false // 기본값으로 설정
                };
            });
            displaySearchResults(areaArr); // 초기 화면에 표시
        },
        error: function(error) {
            console.error('Error loading popup stores:', error);
        }
    });
}

function formatDate(date) {
    var d = new Date(date);
    var year = d.getFullYear().toString().substr(2,2);
    var month = ('0' + (d.getMonth() + 1)).slice(-2);
    var day = ('0' + d.getDate()).slice(-2);
    return year + '.' + month + '.' + day;
}

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
        var dateA = a.date.split(' ~ ')[0].replace(/기간 : /g, '').split('.');
        var dateB = b.date.split(' ~ ')[0].replace(/기간 : /g, '').split('.');

        var yearA = parseInt(dateA[0]);
        var yearB = parseInt(dateB[0]);
        var monthA = parseInt(dateA[1]);
        var monthB = parseInt(dateB[1]);
        var dayA = parseInt(dateA[2]);
        var dayB = parseInt(dateB[2]);

        if (yearA !== yearB) {
            return yearA - yearB;
        } else if (monthA !== monthB) {
            return monthA - monthB;
        } else {
            return dayA - dayB;
        }
    });

    displaySearchResults(sortedStores);
}

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

function closeAllInfoWindows() {
    for (var i = 0; i < infoWindows.length; i++) {
        infoWindows[i].close();
    }
}