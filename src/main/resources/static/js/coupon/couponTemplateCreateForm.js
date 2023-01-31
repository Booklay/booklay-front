function changeByType() {
    let couponType = document.getElementById("coupon-type");

    // select에서 선택된 option의 text.
    let selectText = couponType.options[couponType.selectedIndex].text;
    let itemSelectorDiv = document.getElementById("select-item");
    let maximumAmount = document.getElementById("maximumAmountSpan");
    let minimumAmount = document.getElementById("minimumAmountSpan");
    let amountLabel = document.getElementById("amountLabel");
    let amountInput = document.getElementById("coupon-amount");
    let duplicateSpan = document.getElementById("duplicateSpan");
    const pointText = "적립포인트"
    const percentCouponText = "할인율(%)"
    const fixedCouponText = "할인액(원)"

    if(selectText === "포인트") {
        amountLabel.innerText = pointText;
        itemSelectorDiv.classList.add("hidden");
        minimumAmount.classList.add("hidden");
        maximumAmount.classList.add("hidden");
        amountInput.removeAttribute("max");
        duplicateSpan.classList.add("hidden");
    }
    if (selectText === "정액쿠폰"){
        amountLabel.innerText = fixedCouponText;
        itemSelectorDiv.classList.remove("hidden");
        maximumAmount.classList.add("hidden");
        minimumAmount.classList.remove("hidden");
        amountInput.removeAttribute("max");
        duplicateSpan.classList.remove("hidden");
    }
    if (selectText === "정률쿠폰"){
        amountLabel.innerText = percentCouponText;
        itemSelectorDiv.classList.remove("hidden");
        maximumAmount.classList.remove("hidden");
        minimumAmount.classList.remove("hidden");
        duplicateSpan.classList.remove("hidden");
        amountInputMaxCut();
        amountInput.setAttribute("max", "100");
    }
}

function changeByRange() {
    let couponRange = document.getElementById("coupon-range");
    let rangeValue = couponRange.value;
    let itemLabel = document.getElementById("apply-item-label");
    let categoryButton = document.getElementById("category-search-button");
    let productButton = document.getElementById("product-search-button");

    if(rangeValue === "1") {
        itemLabel.innerText = "적용 카테고리 번호";
        categoryButton.classList.remove("hidden");
        productButton.classList.add("hidden");
    } else if(rangeValue === "0") {
        itemLabel.innerText = "적용 상품 번호";
        categoryButton.classList.add("hidden");
        productButton.classList.remove("hidden");
    }
}

function amountInputMaxCut(){
    let couponType = document.getElementById("coupon-type");
    let selectText = couponType.options[couponType.selectedIndex].text;
    let amountInput = document.getElementById("coupon-amount");
    if (selectText==="정률쿠폰") {
        if (amountInput.value > 100) {
            amountInput.value = 100;
        }
    }
}
window.onload = function (){
    document.getElementById("coupon-type").addEventListener("change",changeByType);
    document.getElementById("coupon-range").addEventListener("change",changeByRange);
    document.getElementById("coupon-amount").addEventListener("change",amountInputMaxCut);
    document.getElementById("category-search-button").addEventListener("click", showCategoryPopup);
    document.getElementById("product-search-button").addEventListener("click", showProductPopup);
    document.getElementById("coupon-type").dispatchEvent(new Event("change"));
    document.getElementById("coupon-range").dispatchEvent(new Event("change"));
}