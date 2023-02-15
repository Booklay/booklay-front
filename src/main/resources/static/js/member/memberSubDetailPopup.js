function showMemberGradePopup(value) {
    let option = "width = 500, height = 300, top = 100, left = 200, scrollbars = yes, location = no"
    let ret = window.open("/admin/members/grade/" + value, "memberGradePopup", option);
}

function showMemberAuthorityPopup(value) {
    let option = "width = 500, height = 300, top = 100, left = 200, scrollbars = yes, location = no"
    let ret = window.open("/admin/members/authority/" + value, "memberAuthorityPopup", option);
}