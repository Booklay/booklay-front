async function issueCoupon(couponId) {
    const couponRequestUrl = `http://localhost:6060/coupon-zone/${couponId}`;
    const loadingDiv = document.querySelector('#loading');

    console.log(couponRequestUrl);
    const response = await fetch(couponRequestUrl);

    if (response.ok) { // 응답 코드 200~299 일 때
        const parsedResponse = await response.json();
        const requestId = parsedResponse.requestId;

        const messageRequestUrl = '/coupon-zone/member/response/' + requestId;

        let timerId = setInterval(async () => {
            const messageResponse = await fetch(messageRequestUrl);
            console.log(messageResponse);

            if (messageResponse.ok) {
                const msgResponse = await messageResponse.json();
                console.log(msgResponse.message);
                if(msgResponse.message !== "null") {
                    alert(msgResponse.message)
                    loadingDiv.style.display = 'none'
                    clearTimeout(timerId);
                    clearTimeout(timeoutId)
                }

            } else {
                alert("조금 뒤에 다시 시도해주세요..")
            }
        }, 1500);

        let timeoutId = setTimeout(() => {
            clearInterval(timerId);
            alert('요청 시간이 초과되었습니다.');
            loadingDiv.style.display = 'none';
        }, 10000);

    } else {
        const parsedResponse = await response.json();
        loadingDiv.style.display = 'none';
        alert(parsedResponse.message);
    }
}