async function issueCoupon(couponId) {
    const couponRequestUrl = `/coupon-zone/${couponId}`;
    const loadingDiv = document.querySelector('#loading');

    const response = await fetch(couponRequestUrl);

    if (response.ok) { // 응답 코드 200~299 일 때
        const parsedResponse = await response.json();
        const requestId = parsedResponse.requestId;

        const messageRequestUrl = '/coupon-zone/member/response/' + requestId;

        let timeoutId = setTimeout(() => {
            clearInterval(timerId);
            alert('요청 시간이 초과되었습니다.');
            loadingDiv.style.display = 'none';
            }, 10000);

        let timerId = setInterval(async () => {
            const messageResponse = await fetch(messageRequestUrl);

            if (messageResponse.ok) {
                const msgResponse = await messageResponse.json();
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
    } else {
        const parsedResponse = await response.json();
        loadingDiv.style.display = 'none';
        alert(parsedResponse.message);
    }
}