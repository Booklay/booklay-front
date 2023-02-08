function showMemberBlockPopup(value) {
    let option = "width = 700, height = 600, top = 100, left = 200, scrollbars = yes, location = no"
    let ret = window.open("/admin/members/block/" + value, "memberBlockPopup", option);
}

function showMemberBlockHistoryPopup(value) {
    let option = "width = 700, height = 500, top = 100, left = 200, scrollbars = yes, location = no"
    let ret = window.open("/admin/members/block/history/" + value, "memberBlockHistoryPopup", option);
}

function showMemberBlockCancelPopup(value) {
    let option = "width = 500, height = 300, top = 100, left = 200, scrollbars = yes, location = no"
    let ret = window.open("/admin/members/block/cancel/" + value, "memberBlockCancelPopup", option);
}
