const couponSettingData = []
const paymentData = {
    amount: 0,
    orderId: 't2Yqd0Mp-sADazDVCRoeh',
    orderName: '',
    customerName: '',
    successUrl: 'http://localhost:8080/order/success',
    failUrl: 'http://localhost:8080/order/fail',
}
window.onload = function() {
    //초기화 셋팅
    paymentData.orderName = cartData.length > 0? cartData[0].productName + (cartData.length>1?" 외 " + (cartData.length-1) + "건":""):"";

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
    let destinationRadioList = document.getElementsByClassName("destinationRadio")
    for (let destinationRadio of destinationRadioList){
        destinationRadio.addEventListener("click", destinationClickEvent);
    }
    rewritePage();
    const clientKey = 'test_ck_D5GePWvyJnrK0W0k6q8gLzN97Eoq'
    const tossPayments = TossPayments(clientKey)

    const paymentButton = document.getElementById('toPayment') // 결제하기 버튼

    paymentButton.addEventListener('click', function () {
        if (orderValidCheck()){
            tossPayments.requestPayment('카드', paymentData);
        }
    })
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
    /*
    List<String> couponCodeList;
    List<CartDto> cartDtoList;
    Integer usingPoint;
    Integer giftWrappingPrice;
    Integer paymentAmount;
     */
    let couponCodeList = [];
    couponSettingData.forEach(function (value){
        if (value.coupon != null){
            couponCodeList.push(value.coupon.couponCode);
        }
        if (value.doupon != null){
            couponCodeList.push(value.doupon.couponCode);
        }
    });

    let body = {
        "couponCodeList": couponCodeList,
        "cartDtoList":,
        "usingPoint":,
        "giftWrappingPrice":,
        "paymentAmount":
    };
    httpRequest.open('GET', '/rest/coupons/code/?couponCode='+couponCode);
    httpRequest.responseType = "json";
    httpRequest.send(JSON.stringify(body));
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
    updatePaymentData();
}

function updatePaymentData(){
    let orderData = couponSettingData[couponSettingData.length-1];
    paymentData.amount = parseInt(orderData.orderTotalProductAmount) + parseInt(orderData.orderTotalProductAmount>30000?0:3000)
        - parseInt(orderData.orderTotalDiscount) - parseInt(document.getElementById("usePoint").value);
    paymentData.customerName = "모름";
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

function sample6_execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function (data) {
            let addr = '';
            // let extraAddr = '';

            if (data.userSelectedType === 'R') {
                addr = data.roadAddress;
            } else {
                addr = data.jibunAddress;
            }

            // if(data.userSelectedType === 'R'){
            //     if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
            //         extraAddr += data.bname;
            //     }
            //     if(data.buildingName !== '' && data.apartment === 'Y'){
            //         extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
            //     }
            //     if(extraAddr !== ''){
            //         extraAddr = ' (' + extraAddr + ')';
            //     }
            //     document.getElementById("extraAddress").value = extraAddr;
            //
            // } else {
            //     document.getElementById("extraAddress").value = '';
            // }

            document.getElementById("destination_zipCode").value = data.zonecode;
            document.getElementById("destination_address").value = addr;
            document.getElementById("detailAddress").focus();
        }
    }).open();
}

function destinationClickEvent(e) {
    let targetId = e.target.id;
    let targetNo = parseInt(targetId.toString().slice("destination".length));
    let destinationData = destinationList[targetNo];
    // document.getElementById("destination_name").innerText = destinationData.name;
    document.getElementById("destination_zipCode").value = destinationData.zipCode;
    document.getElementById("destination_address").value = destinationData.address;
    document.getElementById("destination_receiver").value = destinationData.receiver;
    let receiverPhoneNoList = destinationData.receiverPhoneNo.split("-");
    let phoneNoOptions = document.getElementById("destination_receiverPhoneNo1");
    // for(let i=0; i<phoneNoOptions.length; i++){
    //     if (phoneNoOptions.options[i].value  === parseInt(receiverPhoneNoList[0])){
    //         phoneNoOptions.options[i].selected = true;
    //     }
    // }
    for (phoneNo of phoneNoOptions){
        if (phoneNo.value === receiverPhoneNoList[0]){
            phoneNo.selected = true;
        }
    }
    document.getElementById("destination_receiverPhoneNo2").value = parseInt(receiverPhoneNoList[1]);
    document.getElementById("destination_receiverPhoneNo3").value = parseInt(receiverPhoneNoList[2]);

}


function orderValidCheck(){
    let httpResult = null;
    let httpRequest = new XMLHttpRequest();
    httpRequest.onreadystatechange = () => {
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            if (httpRequest.status === 200) {
                httpResult = httpRequest.response;
            } else {
                alert('Request Error!');
            }
        }
    };
    httpRequest.open('POST', '/rest/order/check/');
    httpRequest.responseType = "json";
    httpRequest.send();
    while (true){
        if (httpResult != null){
            return httpResult
        }
    }
}