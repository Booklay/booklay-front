function showPointCouponConvertPopup(value) {
    let option = "width = 400, height = 250, top = 100, left = 200, scrollbars = yes, location = no"
    let ret = window.open("/member/profile/point/coupon/view/" + value, "pointCouponConvertPopup", option);
}
