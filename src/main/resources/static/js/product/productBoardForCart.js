window.onload = function() {
    productList.forEach(function (value) {
        if (value.isSelling) {
            document.getElementById(value.productId + "cart").addEventListener('click',
                function () {
                    let productCount = document.getElementById(value.productId + "count").value;
                    let httpRequest = new XMLHttpRequest();
                    let body = {
                        "productNo": value.productId,
                        "count": parseInt(productCount)
                    };
                    httpRequest.onreadystatechange = () => {
                        if (httpRequest.readyState === XMLHttpRequest.DONE) {
                            if (httpRequest.status === 200) {
                                alert('장바구니 담기 완료!');
                            } else {
                                alert('담기 실패');
                            }
                        }
                    };
                    httpRequest.open('POST', '/cart');
                    httpRequest.responseType = "json";
                    httpRequest.setRequestHeader("Content-Type", "application/json");
                    httpRequest.send(JSON.stringify(body));
                });

            document.getElementById(value.productId + "order").addEventListener('click',
                function () {
                    let productCount = document.getElementById(value.productId + "count").value;

                });
        }
    });
};
