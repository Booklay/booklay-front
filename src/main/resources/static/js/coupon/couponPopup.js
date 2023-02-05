function showCouponPopup() {
    let option = "width = 800, height = 600, top = 100, left = 200, scrollbars = yes, location = no"
    let ret = window.open("/admin/coupons/popup/coupon", "couponPopup", option);
}

function setParentText(value, name){
    opener.document.getElementById("couponId").value = value;
    opener.document.getElementById("couponName").value = name;
    window.close();
}

function showMemberPopup() {
    let option = "width = 700, height = 500, top = 100, left = 200, scrollbars = yes, location = no"
    let ret = window.open("/admin/coupons/popup/member", "memberPopup", option);
}

function setMemberText(value, name){
    opener.document.getElementById("memberId").value = value;
    opener.document.getElementById("memberName").value = name;
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

function setApplyItemText(value, name){
    opener.document.getElementById("itemId").value = value;
    opener.document.getElementById("itemName").value = name;
    window.close();
}

function setDate() {
    let origin = document.getElementById("opened-at");
    let target = document.getElementById("closed-at");
    target.value = origin.value;
}