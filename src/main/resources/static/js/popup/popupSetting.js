var valueFromParent = {
    productNo:0,
    isDuplicatable:'',
    productTotal:0
};

window.onload = function (){
    let couponTableBody = document.getElementById("couponTableBody");
    let couponCount = couponTableBody.childElementCount;
    let targetId = document.getElementById("targetId").innerText;
    for (let i = 0; i<couponCount;i++){
        let couponCode = couponTableBody.children.item(i).getAttribute("name");
        let couponTdButton = document.getElementById(couponCode);
        let couponId = couponTableBody.children.item(i).getAttribute("id");
        let minimum = parseInt(document.getElementById(couponId+'minimum').innerText);
        if(minimum>valueFromParent.productTotal){
            couponTdButton.classList.add("hidden")
        }else {
            couponTdButton.addEventListener("click", ()=>{
                setParentText(couponCode, targetId);
            });
        }
    }
}

function setParentText(value, targetId){
    opener.setCouponData(value, valueFromParent.productNo, valueFromParent.isDuplicatable);
    window.close();
}