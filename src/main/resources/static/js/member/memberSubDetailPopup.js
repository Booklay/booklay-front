function showMemberGradePopup(value) {
    let option = "width = 500, height = 300, top = 100, left = 200, scrollbars = yes, location = no"
    let ret = window.open("/admin/members/profile/grade/" + value, "memberGradePopup", option);
}
