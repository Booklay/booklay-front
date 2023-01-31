
function showCouponPopup() {
    let option = "width = 700, height = 500, top = 100, left = 200, scrollbars = yes, location = no"
    let ret = window.open("/admin/coupons/popup/coupon", "couponPopup", option);
}

function setParentText(value){
    opener.document.getElementById("couponId").value = value;
    window.close();
}

function showMemberPopup() {
    let option = "width = 700, height = 500, top = 100, left = 200, scrollbars = yes, location = no"
    let ret = window.open("/admin/coupons/popup/member/pages/0", "memberPopup", option);
}

function setMemberText(value){
    opener.document.getElementById("couponId").value = value;
    window.close();
}

function showCategoryPopup() {
    let option = "width = 700, height = 500, top = 100, left = 200, scrollbars = yes, location = no"
    let ret = window.open("/admin/coupons/popup/category/pages/0", "memberPopup", option);
}

function showProductPopup() {
    let option = "width = 700, height = 500, top = 100, left = 200, scrollbars = yes, location = no"
    let ret = window.open("/admin/coupons/popup/product/pages/0", "memberPopup", option);
}

function setApplyItemText(value){
    opener.document.getElementById("apply-item").value = value;
    window.close();
}
