$(document).ready(function() {
    // 모달 열기
    $("#openApproveModal").click(function() {
        $("#approveModal").show();
    });
    $("#openRejectModal").click(function() {
        $("#rejectModal").show();
    });

    // 모달 닫기
    $("#closeApproveModal, #closeApproveModalButton").click(function() {
        $("#approveModal").hide();
    });
    $("#closeRejectModal, #closeRejectModalButton").click(function() {
        $("#rejectModal").hide();
    });

    // 모달 외부 클릭 시 닫기
    $(window).click(function(event) {
        if ($(event.target).is("#approveModal")) {
            $("#approveModal").hide();
        }
        if ($(event.target).is("#rejectModal")) {
            $("#rejectModal").hide();
        }
    });
});