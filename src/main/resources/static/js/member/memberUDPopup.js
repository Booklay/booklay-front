function showMemberUpdatePopup() {
    let option = "width = 700, height = 750, top = 100, left = 200, scrollbars = yes, location = no"
    let ret = window.open("/member/update", "memberUpdatePopup", option);
}

function showMemberDropPopup() {
    let option = "width = 500, height = 300, top = 100, left = 200, scrollbars = yes, location = no"
    let ret = window.open("/member/drop", "memberDropPopup", option);
}
