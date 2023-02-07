const couponSettingData = []
window.onload = function() {
    cartData.forEach(function (value, index){
        couponSettingData.push({
            productNo:value.productNo,
            coupon:null,
            doupon:null,
            totalDiscount:0,
            discountedAmount:0,
            cartDataNo: index
        });
    });
    couponSettingData.push({
        productNo:"order",
        coupon:null,
        doupon:null,
        totalDiscount:0,
        discountedAmount:0,
        cartDataNo: null,
        orderTotalProductAmount:0,
        orderTotalDiscount:0,
    });

    document.getElementById("usePoint").addEventListener("change", updatePoint);
    rewritePage();
};
const couponDataPrefix = {
    id: "쿠폰번호 : ",
    name: "쿠폰이름 : ",
    couponCode: "쿠폰코드 : ",
    typeName: "쿠폰타입 : ",
    amount: "할인 : ",
    minimumUseAmount: "최소사용금액 : ",
    maximumDiscountAmount: "최대할인금액 : ",
    discountAmount: "할인액 : "
};
function showCouponPopup(productNo, isDuplicable) {
    let option = "width = 700, height = 500, top = 100, left = 200, scrollbars = yes, location = no"
    window.open("/admin/coupons/popup/coupon/"+productNo.slice(6)+"?isDuplicable="+isDuplicable, "couponPopup", option);
}

function showOrderCouponPopup(isDuplicable){
    let option = "width = 700, height = 500, top = 100, left = 200, scrollbars = yes, location = no"
    window.open("/admin/coupons/popup/coupon/order?isDuplicable="+isDuplicable, "couponPopup", option);
}
function setCouponData(couponCode, productNo, duplicable){
    let httpResult;
    let httpRequest = new XMLHttpRequest();
    httpRequest.onreadystatechange = () => {
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            if (httpRequest.status === 200) {
                httpResult = httpRequest.response;
                couponSettingData.forEach(function (value){
                    if (value.productNo.toString() === productNo.toString()){
                        if (duplicable === "true"){
                            value.doupon = httpResult;
                        }else if(duplicable === "false"){
                            value.coupon = httpResult;
                        }
                    }
                });
                rewritePage();
            } else {
                alert('Request Error!');
            }
        }
    };
    httpRequest.open('GET', '/rest/coupons/code/?couponCode='+couponCode);
    httpRequest.responseType = "json";
    httpRequest.send();
}

function updatePoint(){
    let usingPoint = document.getElementById("usePoint");
    let usingPointAmount = parseInt(usingPoint.value);
    if (usingPointAmount<0){
        usingPoint.value = 0;
    }
    if (userPointAmount< usingPointAmount){
        usingPoint.value = userPointAmount;
    }
    let orderData = couponSettingData[couponSettingData.length-1];
    let paymentAmount = orderData.discountedAmount + parseInt((orderData.orderTotalProductAmount>30000?0:3000).toString());
    if (paymentAmount<usingPointAmount){
        usingPoint.value = paymentAmount;
    }
    drawOrderSummary();
}

function rewritePage(){
    updateCouponSettingData();
    drawProductCouponData();
    drawOrderCouponData();
    drawOrderSummary();
}

function drawOrderSummary(){
    let orderData = couponSettingData[couponSettingData.length-1];
    document.getElementById("allProductTotal").innerText = orderData.orderTotalProductAmount;
    document.getElementById("shippingFee").innerText = orderData.orderTotalProductAmount>30000?"0":"3000";
    document.getElementById("couponDiscount").innerText = orderData.orderTotalDiscount;
    document.getElementById("pointDiscount").innerText = document.getElementById("usePoint").value;
    let totalPay = parseInt(orderData.orderTotalProductAmount) + parseInt(orderData.orderTotalProductAmount>30000?0:3000)
        - parseInt(orderData.orderTotalDiscount) - parseInt(document.getElementById("usePoint").value);
    document.getElementById("totalPay").innerText = totalPay<0?"0":totalPay;
}

function drawProductCouponData(){
    couponSettingData.filter(value => value.productNo !== "order").forEach(function (value){
        drawTr(value.productNo + "couponTr", value.coupon);
        drawTr(value.productNo + "douponTr", value.doupon);
        document.getElementById(value.productNo + "last").innerText =
            value.discountedAmount;
    });
}

function drawOrderCouponData(){
    let orderData = couponSettingData[couponSettingData.length-1];
    drawTr("orderCouponTr", orderData.coupon);
    drawTr("orderDouponTr", orderData.doupon);

    document.getElementById("orderLast").innerText =
        orderData.discountedAmount;
    document.getElementById("orderLast").innerText =
        orderData.discountedAmount;
}

function drawTr(id, couponData){
    let couponTr = document.getElementById(id);
    couponTr.innerText = "";
    if (couponData !== null){
        couponTr.appendChild(createTrForCouponBody(couponData));
        couponTr.setAttribute("id", id)
        couponTr.removeAttribute("hidden");
    } else {
        couponTr.setAttribute("hidden", "");
    }
}

function updateCouponSettingData(){
    couponSettingData.forEach(function (value) {
        if (value.productNo==="order"){
            let couponDiscount = value.coupon === null?0:calculateOrderCouponDiscountAmount(value.coupon);
            let douponDiscount = value.doupon === null?0:calculateOrderCouponDiscountAmount(value.doupon);
            let orderTotal = 0;
            cartData.forEach(function (value) {
                orderTotal += value.productTotal;
            });
            value.orderTotalProductAmount = orderTotal;
            value.totalDiscount = couponDiscount + douponDiscount;
            if (value.coupon!==null && value.doupon !== null){
                let higherCoupon = value.coupon.categoryNo.toString().length>value.doupon.categoryNo.toString().length?
                                    value.doupon:value.coupon;
                let lowerCoupon = value.coupon.categoryNo.toString().length>value.doupon.categoryNo.toString().length?
                                    value.coupon:value.doupon;
                let discountedCategoryTotalAmount = 0;
                if (isCategoryContains([lowerCoupon.categoryNo], higherCoupon.categoryNo)){
                    let categoryTotalAmount = 0;
                    couponSettingData.filter((value, index) => index !== couponSettingData.length - 1).forEach(function (value){
                        if(isCategoryContains(getCategoryNoList(cartData[value.cartDataNo].categoryNoList), higherCoupon.categoryNo)){
                            categoryTotalAmount += cartData[value.cartDataNo].productTotal;
                            discountedCategoryTotalAmount += value.discountedAmount;
                        }
                    });
                    if (couponDiscount + douponDiscount > categoryTotalAmount){
                        value.totalDiscount = categoryTotalAmount;
                    }
                    value.totalDiscount = value.totalDiscount>discountedCategoryTotalAmount?discountedCategoryTotalAmount:value.totalDiscount;
                }
            }
            let totalDiscount = 0;
            couponSettingData.forEach(function (value){
                totalDiscount += value.totalDiscount;
            });
            value.orderTotalDiscount = totalDiscount;
            value.discountedAmount = orderTotal - totalDiscount < 0 ? 0 : orderTotal - totalDiscount;
        }
        else {
            let couponDiscount = value.coupon === null?0:calculateProductCouponDiscountAmount(value.coupon, value.cartDataNo);
            let douponDiscount = value.doupon === null?0:calculateProductCouponDiscountAmount(value.doupon, value.cartDataNo);
            let productTotal = parseInt(cartData[value.cartDataNo].productTotal);
            value.totalDiscount = couponDiscount + douponDiscount;
            if (couponDiscount + douponDiscount > productTotal){
                value.totalDiscount = productTotal;
            }
            value.discountedAmount = productTotal - value.totalDiscount;
        }
    });
}

function calculateProductCouponDiscountAmount(couponData, cartDataNo){
    let discount = 0;
    let maxDiscount = parseInt(couponData.maximumDiscountAmount);
    let productTotal = parseInt(cartData[cartDataNo].productTotal);
    if (couponData.typeName === "정률쿠폰"){
        discount = Math.floor(productTotal*parseInt(couponData.amount)/100);
    }else if(couponData.typeName === "정액쿠폰"){
        discount = parseInt(couponData.amount);
    }
    discount = discount>maxDiscount?maxDiscount:discount;
    couponData.discountAmount = discount;
    return discount;
}

function calculateOrderCouponDiscountAmount(couponData){
    let discount = 0;
    let categoryNo = parseInt(couponData.categoryNo);
    let categoryTotalAmount = 0;
    let totalAmount = 0;
    let totalDiscount = 0;
    couponSettingData.filter((value, index) => index !== couponSettingData.length - 1).forEach(function (value){
        if(isCategoryContains(getCategoryNoList(cartData[value.cartDataNo].categoryNoList), categoryNo)){
            // categoryTotalAmount += cartData[value.cartDataNo].productTotal;
            categoryTotalAmount += value.discountedAmount;
        }
        // totalAmount += cartData[value.cartDataNo].productTotal;
        totalAmount += value.discountedAmount;
        if (value.coupon !==null){
            totalDiscount += value.coupon.discountAmount;
        }
        if (value.doupon !==null){
            totalDiscount += value.doupon.discountAmount;
        }
    });

    //상품 쿠폰 적용 후 가격과 카테고리 가격합계중 작은 값
    // let maxDiscount = totalAmount-totalDiscount>categoryTotalAmount?categoryTotalAmount:totalAmount-totalDiscount;
    let maxDiscount = totalAmount>categoryTotalAmount?categoryTotalAmount:totalAmount;
    if (couponData.typeName === "정률쿠폰"){
        discount = Math.floor(categoryTotalAmount*parseInt(couponData.amount)/100);
    }else if(couponData.typeName === "정액쿠폰"){
        discount = parseInt(couponData.amount);
    }

    discount = discount>maxDiscount?maxDiscount:discount;
    discount = discount>couponData.maximumDiscountAmount?couponData.maximumDiscountAmount:discount;
    couponData.discountAmount = discount;
    return discount;
}

function getCategoryNoList(categoryNoList) {
    if (!(categoryNoList instanceof Array)) {
        categoryNoList = [categoryNoList];
    }
    let categoryList = [];
    for (let categoryNo of categoryNoList) {
        let categoryString = categoryNo !== null ? categoryNo.toString() : "-1";
        if (categoryString.length > 5) {
            categoryList.push(parseInt(categoryString.slice(0, 3)));
        }
        if (categoryString.length > 2) {
            categoryList.push(parseInt(categoryString.slice(0, 1)));
        }
        categoryList.push(parseInt(categoryString));
    }
    return categoryList;
}

function isCategoryContains(categoryList, categoryNo){
    for(let i of categoryList){
        if(i===categoryNo){
            return true;
        }
    }
    return false;
}

//유효함
function cancelCoupon(couponCode){
    for(let couponSetting of couponSettingData){
        if (couponSetting.coupon !== null && couponSetting.coupon.couponCode === couponCode){
            couponSetting.coupon = null;
        }else if (couponSetting.doupon !== null && couponSetting.doupon.couponCode === couponCode){
            couponSetting.doupon = null;
        }
    }
    rewritePage();
}

function createTrForCouponBody(couponData){
    const beforeEnd = "beforeend";
    let td = document.createElement("td");
    td.setAttribute("colSpan", "7");

    //쿠폰번호, 쿠폰이름, 쿠폰코드
    let div1Data = [
        couponDataPrefix.id + couponData.id,
        couponDataPrefix.name + couponData.name,
        couponDataPrefix.typeName + couponData.typeName,
        couponData.couponCode
    ];
    td.insertAdjacentElement(beforeEnd, createDiv([4, 4, 4, 0], div1Data));

    let div2Data = [
        couponDataPrefix.amount + couponData.amount,
        couponDataPrefix.minimumUseAmount + couponData.minimumUseAmount,
        couponDataPrefix.maximumDiscountAmount + couponData.maximumDiscountAmount
    ]
    td.insertAdjacentElement(beforeEnd, createDiv([4, 4, 4], div2Data));

    let button = document.createElement("button");
    button.setAttribute("id", couponData.couponCode);
    button.setAttribute("onclick", "cancelCoupon(id)");
    button.classList.add("btn", "btn-outline-primary");
    button.innerText = "쿠폰 적용 취소";
    let div3Data = [
        couponDataPrefix.discountAmount + couponData.discountAmount,
        "",
        button
    ]
    td.insertAdjacentElement(beforeEnd, createDiv([4, 4, 4], div3Data));
    return td;
}

function createDiv(size, data){
    let parentDiv = document.createElement("div");
    parentDiv.setAttribute("class", "d-flex")
    for(let i = 0; i<size.length;i++){
        let div = document.createElement("div");
        if (size[i] === 0){
            div.setAttribute("hidden", "");
        }else {
            div.setAttribute("class", "col-"+size[i]);
        }
        if (data[i] instanceof Element){
            div.appendChild(data[i]);
        }else {
            div.innerText = data[i];
        }
        parentDiv.insertAdjacentElement("beforeend", div);
    }
    return parentDiv;
}