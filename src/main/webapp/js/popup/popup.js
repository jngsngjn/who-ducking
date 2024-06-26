var areaArr = []; // 전역 변수로 선언
var markers = [];
var infoWindows = [];
var map;

$(document).ready(function() {
    loadPopupStores();

    // 일정순, 북마크순 버튼 클릭
    $('.schedule-button, .bookmark-button').on('click', function() {
        $('.schedule-button, .bookmark-button').removeClass('active');
        $(this).addClass('active');

        if ($(this).hasClass('bookmark-button')) {
            displayBookmarkedStores(); // 북마크된 정보만 표시
        } else if ($(this).hasClass('schedule-button')) {
            displaySearchResults(areaArr); // 전체 정보 표시 (이미 정렬되어 있음)
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

    // 북마크 버튼 클릭
    $(document).on('click', '.bookmark-icon', function() {
        var $icon = $(this);
        var storeTitle = $icon.closest('li').find('span').text().trim();
        var store = areaArr.find(function(item) {
            return item.title === storeTitle;
        });

        if (store) {
            var bookmarked = !store.bookmarked;
            store.bookmarked = bookmarked;
            toggleBookmark(store, bookmarked, $icon);
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
                    id: store.id,
                    title: store.name,
                    lat: store.latitude,
                    lng: store.longitude,
                    address: "장소: " + store.address.address + " " + store.address.detailedAddress,
                    date: "기간 : " + formatDate(store.startDate) + " ~ " + formatDate(store.endDate),
                    time: "시간: " + store.openTime + " ~ " + store.closeTime,
                    image: "/image/popup/" + store.imageName,
                    bookmarked: store.bookmarked // 서버에서 북마크 상태를 반환하도록 수정
                };
            });
            sortPopupStoresByDate(); // 데이터를 일정순으로 정렬
            initMap();
            setMarkers(); // 마커 설정 함수 호출
            displaySearchResults(areaArr); // 초기 화면에 표시
        },
        error: function(error) {
            console.error('Error loading popup stores:', error);
        }
    });
}

function initMap() {
    map = new naver.maps.Map('map', {
        center: new naver.maps.LatLng(37.494676, 127.027633),
        zoom: 15
    });
}

function setMarkers() {
    var resultsContainer = $('.popup-store-left-list ul');
    resultsContainer.empty();

    // 마커와 인포윈도우 초기화
    markers = [];
    infoWindows = [];

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
                    <span>${areaArr[i].title} <i class="${areaArr[i].bookmarked ? 'fa-solid' : 'fa-regular'} fa-bookmark icon bookmark-icon"></i></span>
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

    // 첫 번째 팝업 스토어 마커 및 인포윈도우 열기
    if (markers.length > 0 && infoWindows.length > 0) {
        var firstLatLng = new naver.maps.LatLng(areaArr[0].lat, areaArr[0].lng);
        map.setCenter(firstLatLng);
        infoWindows[0].open(map, markers[0]);
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

function moveToLocation(store) {
    var index = areaArr.findIndex(function(item) {
        return item.title === store.title;
    });

    if (index !== -1) {
        var latLng = new naver.maps.LatLng(areaArr[index].lat, areaArr[index].lng);
        map.setCenter(latLng);
        infoWindows[index].open(map, markers[index]);
    }
}

function closeAllInfoWindows() {
    for (var i = 0; i < infoWindows.length; i++) {
        infoWindows[i].close();
    }
}

function formatDate(date) {
    var d = new Date(date);
    var year = d.getFullYear().toString().substr(2, 2);
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

    if (results.length === 0) {
        resultsContainer.append('<li>검색 결과가 없습니다.</li>');
    } else {
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
    areaArr.sort(function(a, b) {
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
}

function toggleBookmark(store, bookmarked, $icon) {
    var url = '/api/bookmark/' + store.id;
    var method = bookmarked ? 'POST' : 'DELETE';

    $.ajax({
        url: url,
        method: method,
        success: function() {
            $icon.toggleClass('fa-regular', !bookmarked);
            $icon.toggleClass('fa-solid', bookmarked);
        },
        error: function(error) {
            console.error('Error updating bookmark:', error);
        }
    });
}