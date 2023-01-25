
function showCouponPopup() {
    let option = "width = 700, height = 500, top = 100, left = 200, scrollbars = yes, location = no"
    let ret = window.open("/admin/coupons/popup/pages/0", "couponPopup", option);
}

function setParentText(value){
    opener.document.getElementById("couponId").value = value;
    window.close();
}

function showMemberPopup() {
    let option = "width = 700, height = 500, top = 100, left = 200, scrollbars = yes, location = no"
    let ret = window.open("/admin/coupons/member/popup/pages/0", "memberPopup", option);
}

function setMemberText(value){
    opener.document.getElementById("couponId").value = value;
    window.close();
}