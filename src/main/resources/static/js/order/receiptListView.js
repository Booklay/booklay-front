window.onload = () => {
    let buttons = document.getElementsByClassName("confirm-button");
    for (let button of buttons){
        button.addEventListener("click", async function () {
            let productNo = this.getAttribute("id").slice(8);
            const response = await fetch("/rest/order/confirm/"+productNo, {
                method: 'GET',
                mode: 'same-origin',
                cache: 'no-cache',
                credentials: 'same-origin',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            });

            if (response.ok){
                location.reload();
            }else {
                alert("구매확정 실패");
            }
            
        })
    }
}

