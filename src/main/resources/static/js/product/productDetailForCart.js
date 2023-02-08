window.onload = function () {
  document.getElementById("addCart").addEventListener('click',
      function () {
        let productCount = document.getElementById("productCount").value;
        let productNo = document.getElementById("productNo").value;
        let httpRequest = new XMLHttpRequest();
        let body = {
          "productNo": parseInt(productNo),
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

  document.getElementById("toOrder").addEventListener("click",
      function () {
        let cartDtoArray = [];

        const productNo = document.getElementById("productNo").value;

        cartDtoArray.push({
          "productNo": parseInt(productNo),
          "count": parseInt(
              document.getElementById("count").value)
        });

        let formElement = document.createElement('form');
        formElement.setAttribute('method', 'post');
        formElement.setAttribute('action', '/order/page');
        for (let i = 0; i < cartDtoArray.length; i++) {
          let inputElementProductNo = document.createElement("input");
          inputElementProductNo.setAttribute("value",
              cartDtoArray[i].productNo);
          inputElementProductNo.setAttribute("name", "productNo[" + i + "]");
          formElement.appendChild(inputElementProductNo);
          let inputElementCount = document.createElement("input");
          inputElementCount.setAttribute("value", cartDtoArray[i].count);
          inputElementCount.setAttribute("name", "count[" + i + "]");
          formElement.appendChild(inputElementCount);
        }
        document.body.appendChild(formElement);
        formElement.submit();
      });
};

