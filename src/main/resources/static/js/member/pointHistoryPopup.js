function showPointHistoryPopup(value) {
    let option = "width = 700, height = 700, top = 100, left = 200, scrollbars = yes, location = no"
    let ret = window.open("/member/profile/point/" + value, "pointHistoryViewPopup", option);
}
