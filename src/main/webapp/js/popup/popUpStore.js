var areaArr = []; // 전역 변수로 선언
var markers = [];
var infoWindows = [];
var map;

$(document).ready(function() {
    loadPopupStores();

    // 일정순, 북마크순 버튼 클릭
    $('.popUpStore_schedule_btn, .popUpStore_bookmark_btn').on('click', function() {
        $('.popUpStore_schedule_btn, .popUpStore_bookmark_btn').removeClass('active');
        $(this).addClass('active');

        if ($(this).hasClass('popUpStore_bookmark_btn')) {
            displayBookmarkedStores(); // 북마크된 정보만 표시
        } else if ($(this).hasClass('popUpStore_schedule_btn')) {
            location.reload(); // 페이지 새로고침
        } else {
            displaySearchResults(areaArr); // 전체 정보 표시
        }
    });

    // 검색 버튼 클릭
    $('.popUpStore_search-button').on('click', function() {
        var query = $('.popUpStore_search-input').val();
        if (query) {
            searchPopupStores(query);
        } else {
            swal({
                text: "검색어를 입력해 주세요.",
                icon: "info"
            });
        }
    });

    // 검색 버튼 엔터키
    $('.popUpStore_search-input').on('keypress', function(e) {
        if (e.which == 13) {
            var query = $(this).val();
            if (query) {
                searchPopupStores(query);
            } else {
                swal({
                    text: "검색어를 입력해 주세요.",
                    icon: "info"
                });
            }
        }
    });

    // 북마크 버튼 클릭
    //굳이 일지도?
    $(document).on('click', '.bookmark-icon', function() {
        if (!isAuthenticated) {
            swal({
                title: "로그인 필요",
                text: "로그인이 필요합니다.",
                icon: "warning"
            });
            return;
        }

        var $icon = $(this);
        var storeTitle = $icon.closest('li').find('span.popUpStore_info_title').text().trim();
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
        url: '/api/popup-stores',
        method: 'GET',
        success: function(data) {
            console.log('Server response:', data);  // 응답 데이터 형식 출력

            if (!Array.isArray(data)) {
                console.error('Invalid data format:', data);
                return;
            }

            areaArr = data.map(function(store) {
                return {
                    id: store.id,
                    title: store.name,
                    lat: store.latitude,
                    lng: store.longitude,
                    address: store.address,
                    date: formatDate(store.startDate) + " - " + formatDate(store.endDate),
                    time: store.openTime + " - " + store.closeTime,
                    image: "/image/popup/" + store.imageName,
                    bookmarked: store.bookmarked || false // 서버에서 북마크 상태를 반환, 인증되지 않은 경우 기본값으로 false 사용
                };
            });
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
    var resultsContainer = $('.popUpStore_Left ul');
    resultsContainer.empty();

    // 마커와 인포윈도우 초기화
    markers = [];
    infoWindows = [];

    for (var i = 0; i < areaArr.length; i++) {
        var marker = new naver.maps.Marker({
            map: map,
            title: areaArr[i].title,
            position: new naver.maps.LatLng(areaArr[i].lat, areaArr[i].lng),
            icon: {
                content: `
                    <div style="background-color: var(--mainColor); width: 24px; height: 24px; border-radius: 50%; border: 2px solid white;"></div>
                `,
                anchor: new naver.maps.Point(12, 12),
                zIndex: 100
            }
        });

        var infoWindow = new naver.maps.InfoWindow ({
           content: '<div style="width:250px;text-align:center;padding:10px;"><b>' + areaArr[i].title + '</div>'
        });

        markers.push(marker);
        infoWindows.push(infoWindow);

        var listItem = `
            <li data-index="${i}">
                <div class="popUpStore_img_box">
                    <img src="${areaArr[i].image}" class="popUpStore_img">
                </div>
                <div class="popUpStore_info">
                    <div class="popUpStore_info_title_box">
                        <span class="popUpStore_info_title">${areaArr[i].title}</span>
                        ${isAuthenticated ? `<i class="${areaArr[i].bookmarked ? 'fa-solid' : 'fa-regular'} fa-bookmark icon bookmark-icon"></i>` : ''}
                    </div>
                    <div class="popUpStore_dates">
                        <div class="popUpStore_date-left">
                            <span class="popUpStore_subTitle">기간</span>${areaArr[i].date}
                        </div>
                        <div class="popUpStore_date-left">
                            <span class="popUpStore_subTitle">시간</span>${areaArr[i].time}
                        </div>
                        <div class="popUpStore_date-left">
                            <span class="popUpStore_subTitle">주소</span>${areaArr[i].address}
                        </div>
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

    $(document).on('click', '.popUpStore_info span.popUpStore_info_title', function() {
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
    displaySearchResults(results); // 검색 결과
    if (results.length > 0) {
        moveToLocation(results[0]); // 위치 이동
    }
}

// 검색 결과 표시 함수
function displaySearchResults(results) {
    var resultsContainer = $('.popUpStore_Left ul');
    resultsContainer.empty();

    if (results.length === 0) {
        resultsContainer.append('<li>검색 결과가 없습니다.</li>');
    } else {
        results.forEach(function(store, index) {
            var bookmarkClass = store.bookmarked ? 'fa-solid' : 'fa-regular';
            var listItem = `
                <li data-index="${index}">
                    <div class="popUpStore_img_box">
                        <img src="${store.image}" class="popUpStore_img">
                    </div>
                    <div class="popUpStore_info">
                        <div class="popUpStore_info_title_box">
                            <span class="popUpStore_info_title">${store.title}</span>
                            ${isAuthenticated ? `<i class="${bookmarkClass} fa-bookmark icon bookmark-icon"></i>` : ''}
                        </div>
                        <div class="popUpStore_dates">
                            <div class="popUpStore_date-left-date" data-label="기간">
                                <span class="popUpStore_subTitle">기간</span>${store.date}
                            </div>
                            <div class="popUpStore_date-left-time" data-label="시간">
                                <span class="popUpStore_subTitle">시간</span>${store.time}
                            </div>
                            <div class="popUpStore_date-left-address" data-label="장소">
                                <span class="popUpStore_subTitle">장소</span>${store.address}
                            </div>
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

    var resultsContainer = $('.popUpStore_Left ul');
    resultsContainer.empty();

    if (bookmarkedStores.length === 0) {
        resultsContainer.append('<li>북마크된 팝업스토어가 없습니다.</li>');
    } else {
        displaySearchResults(bookmarkedStores);
    }
}

function toggleBookmark(store, bookmarked, $icon) {
    var url = '/api/bookmark/' + store.id;
    var method = bookmarked ? 'POST' : 'DELETE';

    $.ajax({
        url: url,
        method: method,
        success: function() {
            $icon.toggleClass(bookmarked ? 'fa-regular' : 'fa-solid');
            $icon.toggleClass(bookmarked ? 'fa-solid' : 'fa-regular');
        },
        error: function(error) {
            swal({
                title: "오류",
                text: "북마크 상태를 업데이트하는 중 오류가 발생했습니다.",
                icon: "error"
            });
            console.error('Error updating bookmark:', error);
        }
    });
}
