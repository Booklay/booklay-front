function showMemberBlockPopup(value) {
    let option = "width = 700, height = 500, top = 100, left = 200, scrollbars = yes, location = no"
    let ret = window.open("/admin/members/block/" + value, "memberBlockPopup", option);
}

function showMemberBlockCancelPopup(value) {
    let option = "width = 700, height = 500, top = 100, left = 200, scrollbars = yes, location = no"
    let ret = window.open("/admin/members/block/cancel/" + value, "memberBlockCancelPopup", option);
}