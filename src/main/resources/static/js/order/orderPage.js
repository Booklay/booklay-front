// window.onload = function() {
//
//     // window.open()
// };
const couponDataPrefix = {
    id: "쿠폰번호 : ",
    name: "쿠폰이름 : ",
    couponCode: "쿠폰코드 : ",
    amount: "할인 : ",
    minimumUseAmount: "최소사용금액 : ",
    maximumDiscountAmount: "최대할인금액 : "
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
                let targetDiv = writeCouponData(httpResult, productNo, duplicable);
                if (productNo === 'order'){
                    setOrderDiscountAmount(duplicable);
                }else {
                    setProductDiscountAmount(productNo, targetDiv, duplicable);
                }
            } else {
                alert('Request Error!');
            }
        }
    };
    httpRequest.open('GET', '/rest/coupons/code/?couponCode='+couponCode);
    httpRequest.responseType = "json";
    httpRequest.send();
}

function updateProductLastMoney(productNo){
    let couponDiscount = parseInt(document.getElementById(productNo+"couponDiv3").lastElementChild.innerHTML);
    let douponDiscount = parseInt(document.getElementById(productNo+"douponDiv3").lastElementChild.innerHTML);
    document.getElementById(productNo+"last").innerText =
        parseInt(document.getElementById(productNo+"total").innerText) - couponDiscount - douponDiscount;
    updateOrderLastMoney();
}

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
function setOrderDiscountAmount(duplicable){
    let targetDiv = getTargetDiv(duplicable==='true'?"orderdouponDiv":"ordercouponDiv");
    let couponData= getCouponData(targetDiv);
    let discount = 0;
    let writeTarget = document.getElementById(productNo+(duplicable==='true'?"doupon":"coupon")+"Div3").lastElementChild;
    writeTarget.innerHTML = discount;
    updateOrderLastMoney();

}
function setProductDiscountAmount(productNo, targetDiv, duplicable){
    let couponData= getCouponData(targetDiv);
    let discount = 0;
    let writeTarget = document.getElementById(productNo+(duplicable==='true'?"doupon":"coupon")+"Div3").lastElementChild;
    writeTarget.innerHTML = discount;
    updateProductLastMoney(productNo);
    let targetTr = document.getElementById(productNo + (duplicable==='true'?"doupon":"coupon") +"Tr");
    if (targetTr.classList.contains("hidden")){
        return;
    }
    let maxDiscount = parseInt(document.getElementById(productNo+"last").innerText);
    let productTotal = parseInt(document.getElementById(productNo+"total").innerText);
    if (couponData.typeName === "정율쿠폰"){
        discount = productTotal*parseFloat(couponData.amount);
        discount = discount>maxDiscount?maxDiscount:discount;
    }else if(couponData.typeName === "정액쿠폰"){
        discount = parseInt(couponData.amount)>maxDiscount?maxDiscount:couponData.amount;
    }
    writeTarget.innerHTML = discount;
    updateProductLastMoney(productNo);
}

function writeCouponData(result, productNo, duplicable){

    let targetNamePost = duplicable==='true'?"doupon":"coupon";
    let targetTr = document.getElementById(productNo + targetNamePost+"Tr");
    let targetDiv = {
        div1 : document.getElementById(productNo + targetNamePost + "Div1"),
        div2 : document.getElementById(productNo + targetNamePost + "Div2"),
        div3 : document.getElementById(productNo + targetNamePost + "Div3")
    }
    targetTr.classList.remove("hidden");
    targetDiv.div1.children.item(0).innerHTML = couponDataPrefix.id + result.id;
    targetDiv.div1.children.item(1).innerHTML = couponDataPrefix.name + result.name;
    targetDiv.div1.children.item(2).innerHTML = couponDataPrefix.couponCode + result.couponCode;
    targetDiv.div1.children.item(3).innerHTML = result.typeName;
    targetDiv.div2.children.item(0).innerHTML = couponDataPrefix.amount + result.amount;
    targetDiv.div2.children.item(1).innerHTML = couponDataPrefix.minimumUseAmount + result.minimumUseAmount;
    targetDiv.div2.children.item(2).innerHTML = couponDataPrefix.maximumDiscountAmount + result.maximumDiscountAmount;
    return targetDiv;
}

function cancelProductCoupon(buttonId){
    let productNo = buttonId.slice(12);
    let couponType = buttonId.slice(0, 6);
    let writeTarget = document.getElementById(productNo + couponType + "Div3").lastElementChild;
    writeTarget.innerHTML = "0";
    let duplicable = couponType === 'coupon';
    let targetDiv = {
        div1 : document.getElementById(productNo + (duplicable?'d':'c') + couponType.slice(1) + "Div1"),
        div2 : document.getElementById(productNo + (duplicable?'d':'c') + couponType.slice(1) + "Div2"),
        div3 : document.getElementById(productNo + (duplicable?'d':'c') + couponType.slice(1) + "Div3")
    }
    setProductDiscountAmount(productNo, targetDiv, duplicable.toString());
    document.getElementById(productNo + couponType+"Tr").classList.add("hidden");
}

function cancelOrderCoupon(buttonId){

}

function getCouponData(targetTd){
    return {
        id :                    targetTd.div1.children.item(0).innerHTML.slice(couponDataPrefix.id.length),
        name :                  targetTd.div1.children.item(1).innerHTML.slice(couponDataPrefix.name.length),
        couponCode :            targetTd.div1.children.item(2).innerHTML.slice(couponDataPrefix.couponCode.length),
        typeName :              targetTd.div1.children.item(3).innerHTML,
        amount :                targetTd.div2.children.item(0).innerHTML.slice(couponDataPrefix.amount.length),
        minimumUseAmount :      targetTd.div2.children.item(1).innerHTML.slice(couponDataPrefix.minimumUseAmount.length),
        maximumDiscountAmount : targetTd.div2.children.item(2).innerHTML.slice(couponDataPrefix.maximumDiscountAmount.length)
    };
}
function getTargetDiv(divId){
    return {
        div1 :document.getElementById(divId+"1"),
        div2 :document.getElementById(divId+"2"),
        div3 :document.getElementById(divId+"3")
    }
}