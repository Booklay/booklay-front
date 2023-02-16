async function issueCouponNoLimit(couponId) {
    const couponIssueRequestUrl = `/coupon-zone/no-limit/${couponId}`;
    const loadingDiv = document.querySelector('#loading');

    const response = await fetch(couponIssueRequestUrl, {
        method: 'POST',
        mode: 'same-origin',
        cache: 'no-cache',
        credentials: 'same-origin',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    });

    if (response.ok) {
        const msgResponse = await response.json();
        const couponResponse = msgResponse.message;
        console.log(msgResponse);
        if (couponResponse.includes("이미")) {
            Swal.fire({
                title: msgResponse.message,
                text: '마이페이지 쿠폰함에서 확인할 수 있습니다!',
                icon: 'info',
                confirmButtonText: '닫기',
                footer: '<a href="/mypage/coupon">쿠폰함 바로가기</a>'
            })
        } else if (couponResponse.includes("등록되지")) {
            Swal.fire({
                title: msgResponse.message,
                icon: 'question',
                confirmButtonText: '닫기'
            })
        } else if (couponResponse.includes("수량이")) {
            Swal.fire({
                title: msgResponse.message,
                text: '다음번에 도전하세요!',
                icon: 'warning',
                confirmButtonText: '닫기'
            })
        } else {
            Swal.fire({
                title: '발급 완료되었습니다!',
                text: '마이페이지 쿠폰함에서 확인하세요!',
                icon: 'success',
                confirmButtonText: '닫기',
                footer: '<a href="/mypage/coupon">쿠폰함 바로가기</a>'
            })
        }
        loadingDiv.style.display = 'none'
    } else {
        const msgResponse = await response.json();
        const couponResponse = msgResponse.message;
        loadingDiv.style.display = 'none'
        Swal.fire({
            title: couponResponse,
            icon: 'warning',
            confirmButtonText: '닫기'
        })
    }};