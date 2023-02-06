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
    let slicedProductNo = productNo.slice(6);
    let ret = window.open("/admin/coupons/popup/coupon/"+slicedProductNo+"?isDuplicable="+isDuplicable, "couponPopup", option);
    ret.addEventListener("load", ()=>{
        ret.valueFromParent.productNo = parseInt(slicedProductNo);
        ret.valueFromParent.isDuplicatable = isDuplicable;
        ret.valueFromParent.productTotal = parseInt(document.getElementById(slicedProductNo+"total").innerText)
    });
}

function showOrderCouponPopup(isDuplicable){
    let option = "width = 700, height = 500, top = 100, left = 200, scrollbars = yes, location = no"
    let ret = window.open("/admin/coupons/popup/coupon/order?isDuplicable="+isDuplicable, "couponPopup", option);
    ret.addEventListener("load", ()=>{
        ret.valueFromParent.productNo = "order";
        ret.valueFromParent.isDuplicatable = isDuplicable;
        ret.valueFromParent.productTotal = parseInt(document.getElementById("allProductTotal").innerText)
    });
}
function setCouponData(couponCode, productNo, duplicable){
    let httpResult;
    let httpRequest = new XMLHttpRequest();
    httpRequest.onreadystatechange = () => {
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            if (httpRequest.status === 200) {
                httpResult = httpRequest.response;

                couponSettingData.forEach(function (value){
                    if (value.productNo.toString() === productNo){
                        if (duplicable === "true"){
                            value.doupon = httpResult;
                        }else if(duplicable === "false"){
                            value.coupon = httpResult;
                        }
                    }
                });
                rewritePage();
                //
                //
                // let targetDiv = writeCouponData(httpResult, productNo, duplicable);
                // if (productNo === 'order'){
                //     setOrderDiscountAmount(duplicable);
                // }else {
                //     setProductDiscountAmount(productNo, targetDiv, duplicable);
                // }
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
    let paymentAmount = orderData.orderTotalProductAmount + orderData.orderTotalProductAmount>30000?0:3000
        - orderData.orderTotalDiscount;
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
    let totalPay = orderData.orderTotalProductAmount + orderData.orderTotalProductAmount>30000?0:3000
        - orderData.orderTotalDiscount - document.getElementById("usePoint").value;
    document.getElementById("totalPay").innerText = totalPay<0?"0":totalPay;
}

function drawProductCouponData(){
    couponSettingData.filter(value => value.productNo !== "order").forEach(function (value){
        drawTr(value.productNo + "couponTr", value.coupon);
        drawTr(value.productNo + "douponTr", value.doupon);
        let couponDiscount = value.coupon != null?value.coupon.discountAmount:0;
        let douponDiscount = value.doupon != null?value.doupon.discountAmount:0;
        document.getElementById(value.productNo + "last").innerText =  couponDiscount+douponDiscount;
    });
}

function drawOrderCouponData(){
    let orderData = couponSettingData[couponSettingData.length-1];
    drawTr("orderCouponTr", orderData.coupon);
    drawTr("orderDouponTr", orderData.doupon);
}

function drawTr(id, couponData){
    let couponTr = document.getElementById(id);
    if (couponData !== null){
        couponTr.innerText = "";
        couponTr.appendChild(createTrForCouponBody(couponData));
        couponTr.setAttribute("id", id)
        couponTr.classList.remove("hidden");
    } else {
        couponTr.classList.add("hidden");
    }
}

function updateCouponSettingData(){
    couponSettingData.forEach(function (value, index, array) {
        if (value.productNo==="order"){
            let couponDiscount = value.coupon === null?0:calculateOrderCouponDiscountAmount(value.coupon);
            let douponDiscount = value.doupon === null?0:calculateOrderCouponDiscountAmount(value.doupon);
            let orderTotal = 0;
            cartData.forEach(function (value) {
                orderTotal += value.productTotal;
            });
            value.orderTotalProductAmount = orderTotal;
            value.totalDiscount = couponDiscount + douponDiscount;
            if (value.coupon!==null && value.doupon !== null && value.coupon.categoryNo === value.doupon.categoryNo){
                let categoryTotalAmount = 0;
                couponSettingData.filter((value, index) => index !== couponSettingData.length - 1).forEach(function (value){
                    if(isCategoryContains(getCategoryNoList(value.categoryNo), categoryNo)){
                        categoryTotalAmount += cartData[value.cartDataNo].productTotal;
                    }
                });
                if (couponDiscount + douponDiscount > categoryTotalAmount){
                    value.totalDiscount = categoryTotalAmount;
                }
            }
            let totalDiscount = 0;
            couponSettingData.forEach(function (value){
                if (value.coupon !==null){
                    totalDiscount += value.coupon.discountAmount;
                }
                if (value.doupon !==null){
                    totalDiscount += value.doupon.discountAmount;
                }
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
    if (couponData.typeName === "정율쿠폰"){
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
        if(isCategoryContains(getCategoryNoList(cartData[value.cartDataNo].categoryNo), categoryNo)){
            categoryTotalAmount += cartData[value.cartDataNo].productTotal;
        }
        totalAmount += cartData[value.cartDataNo].productTotal;
        if (value.coupon !==null){
            totalDiscount += value.coupon.discountAmount;
        }
        if (value.doupon !==null){
            totalDiscount += value.doupon.discountAmount;
        }
    });

    //상품 쿠폰 적용 후 가격과 카테고리 가격합계중 작은 값
    let maxDiscount = totalAmount-totalDiscount>categoryTotalAmount?categoryTotalAmount:totalAmount-totalDiscount;
    if (couponData.typeName === "정율쿠폰"){
        discount = Math.floor(categoryTotalAmount*parseInt(couponData.amount)/100);
    }else if(couponData.typeName === "정액쿠폰"){
        discount = parseInt(couponData.amount);
    }
    discount = discount>maxDiscount?maxDiscount:discount;
    return discount;
}

function getCategoryNoList(categoryNo){
    let categoryString = categoryNo !== null?categoryNo.toString():"-1";
    let categoryList = [];
    if (categoryString.length>5){
        categoryList.push(parseInt(categoryString.slice(0, 3)));
    }
    if (categoryString.length>2){
        categoryList.push(parseInt(categoryString.slice(0, 1)));
    }
    categoryList.push(parseInt(categoryString));
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

// function writeCouponData(result, productNo, duplicable){
//
//     let targetNamePost = duplicable==='true'?"doupon":"coupon";
//     let targetTr = document.getElementById(productNo + targetNamePost+"Tr");
//     let targetDiv = {
//         div1 : document.getElementById(productNo + targetNamePost + "Div1"),
//         div2 : document.getElementById(productNo + targetNamePost + "Div2"),
//         div3 : document.getElementById(productNo + targetNamePost + "Div3")
//     }
//     targetTr.classList.remove("hidden");
//     targetDiv.div1.children.item(0).innerHTML = couponDataPrefix.id + result.id;
//     targetDiv.div1.children.item(1).innerHTML = couponDataPrefix.name + result.name;
//     targetDiv.div1.children.item(2).innerHTML = couponDataPrefix.couponCode + result.couponCode;
//     targetDiv.div1.children.item(3).innerHTML = result.typeName;
//     targetDiv.div2.children.item(0).innerHTML = couponDataPrefix.amount + result.amount;
//     targetDiv.div2.children.item(1).innerHTML = couponDataPrefix.minimumUseAmount + result.minimumUseAmount;
//     targetDiv.div2.children.item(2).innerHTML = couponDataPrefix.maximumDiscountAmount + result.maximumDiscountAmount;
//     return targetDiv;
// }
// function updateProductLastMoney(productNo){
//     let couponDiscount = parseInt(document.getElementById(productNo+"couponDiv3").lastElementChild.innerHTML);
//     let douponDiscount = parseInt(document.getElementById(productNo+"douponDiv3").lastElementChild.innerHTML);
//     document.getElementById(productNo+"last").innerText =
//         parseInt(document.getElementById(productNo+"total").innerText) - couponDiscount - douponDiscount;
//     updateOrderLastMoney();
// }
function updateLastMoney(){
    //set 상품 가격 합계
    let productTotalHtmlCollection = document.getElementsByClassName("productTotal");
    let totalAmount = 0;
    for(let item of productTotalHtmlCollection){
        totalAmount += parseInt(item.innerHTML);
    }
    document.getElementById("allProductTotal").innerText = totalAmount;
}
function updateOrderLastMoney(){
    let couponDiscount = parseInt(document.getElementById("ordercouponDiv3").lastElementChild.innerHTML);
    let douponDiscount = parseInt(document.getElementById("orderdouponDiv3").lastElementChild.innerHTML);

    let productLastAmountSum = 0;
    let productLastAmountCollection = document.getElementsByClassName("productLastAmount");
    for(let item of productLastAmountCollection){
        productLastAmountSum += parseInt(item.innerHTML);
    }
    document.getElementById("orderLast").innerText =
        productLastAmountSum - couponDiscount - douponDiscount;
    updateLastMoney();
}
// function setOrderDiscountAmount(duplicable){
//     let targetDiv = getTargetDiv(duplicable==='true'?"orderdouponDiv":"ordercouponDiv");
//     let couponData= getCouponData(targetDiv);
//     createTrForCouponBody(couponData);/////////////////////////////////////////////////////////////////////////////////////////////////
//     let discount = 0;
//     let writeTarget = document.getElementById(productNo+(duplicable==='true'?"doupon":"coupon")+"Div3").lastElementChild;
//     writeTarget.innerHTML = discount;
//     updateOrderLastMoney();
//
// }
// function setProductDiscountAmount(productNo, targetDiv, duplicable){
//     let couponData= getCouponData(targetDiv);
//     let discount = 0;
//     let writeTarget = document.getElementById(productNo+(duplicable==='true'?"doupon":"coupon")+"Div3").lastElementChild;
//     writeTarget.innerHTML = discount;
//     updateProductLastMoney(productNo);
//     let targetTr = document.getElementById(productNo + (duplicable==='true'?"doupon":"coupon") +"Tr");
//     if (targetTr.classList.contains("hidden")){
//         return;
//     }
//     let maxDiscount = parseInt(document.getElementById(productNo+"last").innerText);
//     let productTotal = parseInt(document.getElementById(productNo+"total").innerText);
//     if (couponData.typeName === "정율쿠폰"){
//         discount = productTotal*parseFloat(couponData.amount);
//         discount = discount>maxDiscount?maxDiscount:discount;
//     }else if(couponData.typeName === "정액쿠폰"){
//         discount = parseInt(couponData.amount)>maxDiscount?maxDiscount:couponData.amount;
//     }
//     writeTarget.innerHTML = discount;
//     updateProductLastMoney(productNo);
// }

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
// function cancelProductCoupon(buttonId){
//     let productNo = buttonId.slice(12);
//     let couponType = buttonId.slice(0, 6);
//     let writeTarget = document.getElementById(productNo + couponType + "Div3").lastElementChild;
//     writeTarget.innerHTML = "0";
//     let duplicable = couponType === 'coupon';
//     let targetDiv = {
//         div1 : document.getElementById(productNo + (duplicable?'d':'c') + couponType.slice(1) + "Div1"),
//         div2 : document.getElementById(productNo + (duplicable?'d':'c') + couponType.slice(1) + "Div2"),
//         div3 : document.getElementById(productNo + (duplicable?'d':'c') + couponType.slice(1) + "Div3")
//     }
//     setProductDiscountAmount(productNo, targetDiv, duplicable.toString());
//     document.getElementById(productNo + couponType+"Tr").classList.add("hidden");
// }
//
// function cancelOrderCoupon(buttonId){
//
// }
// function getCouponData(targetTd){
//     return {
//         id :                    targetTd.div1.children.item(0).innerHTML.slice(couponDataPrefix.id.length),
//         name :                  targetTd.div1.children.item(1).innerHTML.slice(couponDataPrefix.name.length),
//         couponCode :            targetTd.div1.children.item(2).innerHTML.slice(couponDataPrefix.couponCode.length),
//         typeName :              targetTd.div1.children.item(3).innerHTML,
//         amount :                targetTd.div2.children.item(0).innerHTML.slice(couponDataPrefix.amount.length),
//         minimumUseAmount :      targetTd.div2.children.item(1).innerHTML.slice(couponDataPrefix.minimumUseAmount.length),
//         maximumDiscountAmount : targetTd.div2.children.item(2).innerHTML.slice(couponDataPrefix.maximumDiscountAmount.length)
//     };
// }
// function getTargetDiv(divId){
//     return {
//         div1 :document.getElementById(divId+"1"),
//         div2 :document.getElementById(divId+"2"),
//         div3 :document.getElementById(divId+"3")
//     }
// }

function createTrForCouponBody(couponData){
    const beforeEnd = "beforeend";
    let td = document.createElement("td");
    td.setAttribute("colSpan", "6");

    //쿠폰번호, 쿠폰이름, 쿠폰코드
    let div1Data = [
        couponDataPrefix.id + couponData.id,
        couponDataPrefix.name + couponData.name,
        couponDataPrefix.typeName + couponData.typeName,
        couponData.couponCode
    ];
    td.insertAdjacentElement(beforeEnd, createDiv([2, 5, 5, 0], div1Data));

    let div2Data = [
        couponDataPrefix.amount + couponData.amount,
        couponDataPrefix.minimumUseAmount + couponData.minimumUseAmount,
        couponDataPrefix.maximumDiscountAmount + couponData.maximumDiscountAmount
    ]
    td.insertAdjacentElement(beforeEnd, createDiv([4, 4, 4], div2Data));

    let button = document.createElement("button");
    button.setAttribute("id", couponData.couponCode);
    button.setAttribute("onclick", "cancelCoupon(id)");
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
    for(let i = 0; i<size.length;i++){
        let div = document.createElement("div");
        if (size[i] === 0){
            div.setAttribute("class", "hidden");
        }else {
            div.setAttribute("class", "col-lg-"+size[i]);
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