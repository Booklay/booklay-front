function showMemberUpdatePopup(value) {
    let option = "width = 700, height = 500, top = 100, left = 200, scrollbars = yes, location = no"
    let ret = window.open("/member/update/" + value, "memberUpdatePopup", option);
}

function showMemberDropPopup(value) {
    let option = "width = 500, height = 300, top = 100, left = 200, scrollbars = yes, location = no"
    let ret = window.open("/member/drop/" + value, "memberDropPopup", option);
}
