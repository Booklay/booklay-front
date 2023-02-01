window.onload = function() {
    let totalPay = document.getElementById("totalPay");
    let allProductTotal = document.getElementById("allProductTotal");
    let shippingFee = document.getElementById("shippingFee");
    result.forEach(function (value) {
        document.getElementById(value.productNo+"update").addEventListener('click',
            function (){
                let productCount = document.getElementById(value.productNo+"count").value;
                let httpRequest = new XMLHttpRequest();
                let body = {
                    "productNo":value.productNo,
                    "count":parseInt(productCount)
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
                let sum = 0;
                result.forEach(function (value) {
                    sum += parseInt(document.getElementById(value.productNo+"total").innerText);
                });
                allProductTotal.innerText = sum;
                if (sum >= 20000){
                    shippingFee.innerText = 0;
                }else {
                    shippingFee.innerText = 3000;
                }
                totalPay.innerText = sum + parseInt(shippingFee.innerText);
            });
    });
    document.getElementById("toOrder").addEventListener('click',
        function (){
            let productCount = document.getElementById(value.productNo+"count").value;
            let httpRequest = new XMLHttpRequest();
            let body = {
                "productNo":value.productNo,
                "count":parseInt(productCount)
            };
            httpRequest.open('POST', '/order/page');
            httpRequest.responseType = "json";
            httpRequest.setRequestHeader("Content-Type", "application/json");
            httpRequest.send(JSON.stringify(body));

        });
    if (result.length>0){
        document.getElementById(result[0].productNo+"count").dispatchEvent(new Event("change"));
    }

    document.getElementById("allProductCheck").addEventListener("change",
        function (){
        let allProductCheck = document.getElementById("allProductCheck");
        result.forEach(value => {
            document.getElementById(value.productNo+"check").checked = allProductCheck.checked;
        });
    });

    document.getElementById("toOrder").addEventListener("click",
        function (){
        let cartDtoArray = [];
        result.forEach(value => {
            if(document.getElementById(value.productNo+"check").checked){
                cartDtoArray.add({
                    "productNo":value.productNo,
                    "count": parseInt(document.getElementById(value.productNo+"count").value)
                });

            }
        });
        console.log(cartDtoArray);
            // let productCount = document.getElementById(value.productNo+"count").value;
            // let httpRequest = new XMLHttpRequest();
            // let body = {
            //     "productNo":value.productNo,
            //     "count":parseInt(productCount)
            // };
            // httpRequest.open('POST', '/order/page');
            // httpRequest.responseType = "json";
            // httpRequest.setRequestHeader("Content-Type", "application/json");
            // httpRequest.send(JSON.stringify(body));
    });

};

