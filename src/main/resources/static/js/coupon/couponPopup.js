
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
    let ret = window.open("/admin/coupons/popup/member", "memberPopup", option);
}

function setMemberText(value){
    opener.document.getElementById("memberId").value = value;
    window.close();
}

function showCategoryPopup() {
    let option = "width = 700, height = 500, top = 100, left = 200, scrollbars = yes, location = no"
    let ret = window.open("/admin/coupons/popup/category", "categoryPopup", option);
}

function showProductPopup() {
    let option = "width = 1000, height = 500, top = 100, left = 200, scrollbars = yes, location = no"
    let ret = window.open("/admin/coupons/popup/product", "productPopup", option);
}

function setApplyItemText(value){
    opener.document.getElementById("apply-item").value = value;
    window.close();
}
