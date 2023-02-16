async function issueCoupon(couponId) {
    const couponRequestUrl = `/coupon-zone/${couponId}`;
    const loadingDiv = document.querySelector('#loading');

    const response = await fetch(couponRequestUrl, {
        method: 'POST',
        mode: 'same-origin',
        cache: 'no-cache',
        credentials: 'same-origin',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    });

    if (response.ok) { // 응답 코드 200~299 일 때
        const parsedResponse = await response.json();
        const requestId = parsedResponse.requestId;

        const messageRequestUrl = '/coupon-zone/member/response/' + requestId;

        let timerId = setInterval(async () => {
            const messageResponse = await fetch(messageRequestUrl);

            if (messageResponse.ok) {
                const msgResponse = await messageResponse.json();
                if(msgResponse.message !== "null") {
                    const couponResponse = msgResponse.message;

                    if(couponResponse.includes("이미")) {
                        Swal.fire({
                            title: msgResponse.message,
                            text: '마이페이지 쿠폰함에서 확인할 수 있습니다!',
                            icon: 'info',
                            confirmButtonText: '닫기',
                            footer: '<a href="/mypage/coupon">쿠폰함 바로가기</a>'
                        })
                    } else if(couponResponse.includes("등록되지")) {
                        Swal.fire({
                            title: msgResponse.message,
                            icon: 'question',
                            confirmButtonText: '닫기'
                        })
                    } else if(couponResponse.includes("수량이")) {
                        Swal.fire({
                            title: msgResponse.message,
                            text: '다음번에 도전하세요!',
                            icon: 'warning',
                            confirmButtonText: '닫기'
                        })
                    } else {
                        Swal.fire({
                            title: msgResponse.message,
                            text: '마이페이지 쿠폰함에서 확인하세요!',
                            icon: 'success',
                            confirmButtonText: '닫기',
                            footer: '<a href="/mypage/coupon">쿠폰함 바로가기</a>'
                        })
                    }

                    loadingDiv.style.display = 'none'
                    clearTimeout(timerId);
                    clearTimeout(timeoutId)
                }

            } else {
                Swal.fire({
                    title: '조금 뒤에 다시 시도해주세요..',
                    icon: 'warning',
                    confirmButtonText: '닫기'
                })
            }
        }, 1500);

        let timeoutId = setTimeout(() => {
            clearInterval(timerId);
            Swal.fire({
                title: '요청 시간이 초과되었습니다!',
                text: '다시 시도해주세요.',
                icon: 'warning',
                confirmButtonText: '닫기'
            })
            loadingDiv.style.display = 'none';
        }, 10000);

    } else {
        const parsedResponse = await response.json();
        loadingDiv.style.display = 'none';
        Swal.fire({
            title: parsedResponse.message,
            icon: 'warning',
            confirmButtonText: '닫기'
        })
    }
}