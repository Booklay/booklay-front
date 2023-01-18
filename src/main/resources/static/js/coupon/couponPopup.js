
function showCouponPopup() {
    let option = "width = 700, height = 500, top = 100, left = 200, scrollbars = yes, location = no"
    let ret = window.open("/admin/coupon/popup/pages/0", "couponPopup", option);
}

function setParentText(value){
    opener.document.getElementById("couponId").value = value;
    window.close();
}