let totalPay;
let allProductTotal;
let shippingFee;
window.onload = function() {
    totalPay = document.getElementById("totalPay");
    allProductTotal = document.getElementById("allProductTotal");
    shippingFee = document.getElementById("shippingFee");
    result.forEach(function (value) {
        document.getElementById(value.productNo+"update").addEventListener('click',
            function (){
                let productCount = document.getElementById(value.productNo+"count").value;
                let httpRequest = new XMLHttpRequest();
                let body = {
                    "productNo":value.productNo,
                    "count":parseInt(productCount)
                };
                httpRequest.onreadystatechange = () => {
                    if (httpRequest.readyState === XMLHttpRequest.DONE) {
                        if (httpRequest.status === 200) {
                            alert("수정 성공");
                        } else {
                            alert("수정 실패");
                        }
                    }
                };
                httpRequest.open('POST', '/cart');
                httpRequest.responseType = "json";
                httpRequest.setRequestHeader("Content-Type", "application/json");
                httpRequest.send(JSON.stringify(body));
            });



        document.getElementById(value.productNo+"count").addEventListener('change',
            function (){
                let productCount = this.value;
                let productTotal = document.getElementById(value.productNo+"total");
                productTotal.innerText= productCount * value.productPrice;
                redrawSummary();
            });
    });

    document.getElementById("toOrder").addEventListener("click",
        function (){
            let cartDtoArray = [];
            result.forEach(value => {
                if(document.getElementById(value.productNo+"check").checked){
                    cartDtoArray.push({
                        "productNo":value.productNo,
                        "count": parseInt(document.getElementById(value.productNo+"count").value)
                    });
                }
            });

            let formElement = document.createElement('form');
            formElement.setAttribute('method', 'get');
            formElement.setAttribute('action', '/order/page');
            for (let i=0; i< cartDtoArray.length;i++){
                let inputElementProductNo = document.createElement("input");
                inputElementProductNo.setAttribute("value", cartDtoArray[i].productNo);
                inputElementProductNo.setAttribute("name", "productNo["+i+"]");
                formElement.appendChild(inputElementProductNo);
                let inputElementCount = document.createElement("input");
                inputElementCount.setAttribute("value", cartDtoArray[i].count);
                inputElementCount.setAttribute("name", "count["+i+"]");
                formElement.appendChild(inputElementCount);
            }
            document.body.appendChild(formElement);
            formElement.submit();
        });
    $('.quantity button').on('click', function () {
        let newVal;
        let button = $(this);
        let oldValue = button.parent().parent().find('input').val();
        if (button.hasClass('btn-plus')) {
            newVal = parseFloat(oldValue) + 1;
        } else {
            if (oldValue > 0) {
                newVal = parseFloat(oldValue) - 1;
            } else {
                newVal = 0;
            }
        }

        button.parent().parent().find('input').val(newVal);
        button.parent().parent().find('input').get(0).dispatchEvent(new Event("change"));
    });

    let allProductCheck = document.getElementById("allProductCheck");
    allProductCheck.addEventListener("change",
        function (){
            let allProductCheck = document.getElementById("allProductCheck");
            result.forEach(value => {
                document.getElementById(value.productNo+"check").checked = allProductCheck.checked;
            });
            redrawSummary();
        });
    result.forEach(value => {
        document.getElementById(value.productNo+"check").addEventListener("change", redrawSummary)
    });
    allProductCheck.checked = true;
    allProductCheck.dispatchEvent(new Event("change"));
    if (result.length>0){
        document.getElementById(result[0].productNo+"count").dispatchEvent(new Event("change"));
    }


};

function redrawSummary() {
    let sum = 0;
    result.forEach(function (value) {
        if (document.getElementById(value.productNo + 'check').checked) {
            sum += parseInt(document.getElementById(value.productNo + "total").innerText);
        }
    });
    allProductTotal.innerText = sum;
    if (sum > 30000) {
        shippingFee.innerText = 0;
    } else {
        shippingFee.innerText = 3000;
    }
    totalPay.innerText = sum + parseInt(shippingFee.innerText);
}