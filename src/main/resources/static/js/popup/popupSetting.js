const valueFromParent = {
    productNo:0,
    isDuplicable:'',
    productTotal:0
};

window.onload = function (){
    let couponTableBody = document.getElementById("couponTableBody");
    let couponCount = couponTableBody.childElementCount;

    let productNo = document.getElementById("productNo").innerText;
    valueFromParent.productNo = productNo;
    valueFromParent.isDuplicable = document.getElementById("isDuplicable").innerText;
    valueFromParent.productTotal = parseInt(opener.document.getElementById(productNo+"total").innerText)
    for (let i = 0; i<couponCount;i++){
        let couponCode = couponTableBody.children.item(i).getAttribute("name");
        let couponTdButton = document.getElementById(couponCode);
        let couponId = couponTableBody.children.item(i).getAttribute("id");
        let minimum = parseInt(document.getElementById(couponId+'minimum').innerText);
        if(minimum>valueFromParent.productTotal){
            console.log("hidden")
            couponTdButton.classList.add("hidden")
        }else {
            console.log("click")
            couponTdButton.addEventListener("click", ()=>{
                setParentText(couponCode);
            });
        }
    }
}

function setParentText(value){
    opener.setCouponData(value, valueFromParent.productNo, valueFromParent.isDuplicable.toString());
    window.close();
}
