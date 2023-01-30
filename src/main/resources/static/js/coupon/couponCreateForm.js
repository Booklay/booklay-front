function changeByType() {
    let couponType = document.getElementById("coupon-type");

    // select에서 선택된 option의 text.
    let selectText = couponType.options[couponType.selectedIndex].text;

    let selectDiv = document.getElementById("select-item");

    if(selectText === "포인트") {
        selectDiv.style.display = "none";
    } else {
        selectDiv.style.display = "block";
    }
}

function changeByRange() {
    let couponRange = document.getElementById("coupon-range");
    let rangeValue = couponRange.value;
    let itemLabel = document.getElementById("apply-item-label");
    let searchButton = document.getElementById("item-search-button");

    if(rangeValue === "1") {
        itemLabel.innerText = "적용 카테고리 번호"
        searchButton.setAttribute("onClick", "showCategoryPopup()")
    } else if(rangeValue === "0") {
        itemLabel.innerText = "적용 상품 번호"
        searchButton.setAttribute("onClick", "showProductPopup()")
    }
}
