$(document).ready(function() {
    initMap();

    $('.schedule-button, .bookmark-button').on('click', function() {
        $('.schedule-button, .bookmark-button').removeClass('active');
        $(this).addClass('active');
    });

    // 이벤트 위임을 사용하여 동적으로 추가된 북마크 아이콘에 이벤트 바인딩
    $(document).on('click', '.bookmark-icon', function() {
        $(this).toggleClass('fa-solid fa-bookmark');
        $(this).toggleClass('fa-regular fa-bookmark');
    });

    $('.search-button').on('click', function() {
        var query = $('.search-input').val();
        if (query) {
            searchPopupStores(query);
        }
    });

    $('.search-input').on('keypress', function(e) {
        if (e.which == 13) {
            var query = $(this).val();
            if (query) {
                searchPopupStores(query);
            }
        }
    });
});

function searchPopupStores(query) {
    $.ajax({
        url: 'https://api.example.com/search',
        type: 'GET',
        data: { query: query },
        success: function(response) {
            displaySearchResults(response.data);
        },
        error: function(error) {
            console.error("Error fetching search results:", error);
        }
    });
}

function displaySearchResults(results) {
    var resultsContainer = $('.popup-store-left-list ul');
    resultsContainer.empty();

    results.forEach(function(store) {
        var listItem = `
            <li>
                <div class="popup-store-img-container">
                    <img src="${store.image}" class="popup-store-img">
                </div>
                <div class="popup-info">
                    <span>${store.title} <i class="fa-regular fa-bookmark icon bookmark-icon"></i></span>
                    <div class="popup-dates">
                        <div class="date-left">${store.location}</div>
                        <div class="date-right">${store.date}</div>
                    </div>
                </div>
            </li>
        `;
        resultsContainer.append(listItem);
    });
}

function initMap() {
    var areaArr = [
        { location: "부산/ 대구 달서구 두류공원로 200 이월드<br>두근두근 도라에몽전 in 부산", lat: "35.8548554163402", lng: "128.564820995296" },
        { location: "서울특별시 중구 을지로 281 동대문디자인플라자뮤지엄<br>헬로키티 50주년 특별전 : 산리오 캐릭터즈와의 여행", lat: "37.5680445876689", lng: "127.010890355484" },
        { location: "용산 아이파크몰 팝콘D스퀘어<br>원피스 대해적시대展", lat: "37.5297718014452", lng: "126.964741503485" },
        { location: "더현대 서울<br>2024주술회전 팝업스토어", lat: "37.5251913154781", lng: "126.929112756574" },
        { location: "더현대 대구<br>2024주술회전 팝업스토어", lat: "35.8666296004242", lng: "128.590625718585" },
        { location: "서울(잠실) 롯데월드몰<br>짱구 팝업스토어 2024", lat: "37.5137129859207", lng: "127.104301829165" },
        { location: "전주 한옥마을<br>짱구 팝업스토어 2024", lat: "35.8182133310179", lng: "127.153608497904" },
        { location: "대구 현대백화점 더현대<br>짱구 팝업스토어 2024", lat: "35.8666296004242", lng: "128.590625718585" },
        { location: "부산 신세계백화점 센텀시티<br>짱구 팝업스토어 2024", lat: "35.1688178394069", lng: "129.129523260187" }
    ];

    let markers = [];
    let infoWindows = [];
    var map = new naver.maps.Map('map', {
        center: new naver.maps.LatLng(37.552758094502494, 126.98732600494576),
        zoom: 15
    });

    var resultsContainer = $('.popup-store-left-list ul');
    resultsContainer.empty();

    for (var i = 0; i < areaArr.length; i++) {
        var marker = new naver.maps.Marker({
            map: map,
            title: areaArr[i].location,
            position: new naver.maps.LatLng(areaArr[i].lat, areaArr[i].lng)
        });

        var infoWindow = new naver.maps.InfoWindow({
            content: '<div style="width:250px;text-align:center;padding:10px;"><b>' + areaArr[i].location + '</b><br> - 네이버 지도 - </div>'
        });

        markers.push(marker);
        infoWindows.push(infoWindow);

        var listItem = `
            <li>
                <div class="popup-store-img-container">
                    <img src="/images/Level/10duck.png" class="popup-store-img">
                </div>
                <div class="popup-info">
                    <span>${areaArr[i].location} <i class="fa-regular fa-bookmark icon bookmark-icon"></i></span>
                    <div class="popup-dates">
                        <div class="date-left">${areaArr[i].lat}</div>
                        <div class="date-right">${areaArr[i].lng}</div>
                    </div>
                </div>
            </li>
        `;
        resultsContainer.append(listItem);
    }

    for (var i = 0; i < markers.length; i++) {
        naver.maps.Event.addListener(markers[i], 'click', getClickHandler(i));
    }
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
