$(document).ready(function() {
    $('.searchAnnouncement_list_item_one').on('click', function(e) {
        e.preventDefault();
        var announcementId = $(this).attr('data-id');
        console.log('Announcement ID:', announcementId); // 로그로 ID 확인
        getAnnouncementPage(announcementId);
    });

    function getAnnouncementPage(id) {
        $.ajax({
            url: '/api/announcements/page-number',
            method: 'GET',
            data: { id: id },
            success: function(response) {
                console.log(response);
                var pageNumber = response;
                window.location.href = '/announcement?page=' + pageNumber + '&id=' + id;
            },
            error: function(error) {
                console.error('Error fetching page number:', error);
            }
        });
    }
});