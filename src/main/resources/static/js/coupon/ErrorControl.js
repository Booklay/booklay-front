async function couponDetail(couponId) {

    // TODO 9 : html에 있는 url을 가져옴.
    const url = document.querySelector('#url').value;

    // TODO 10 : 요청 보내려는 url
    const requestUrl = `${url}/admin/coupons/detail/${couponId}`;

    // TODO 11 : 요청 보냄.
    const response = await fetch(requestUrl);

    // TODO 12 : 요청에 따른 처리.
    if (response.ok) { // 응답 코드 200~299 일 때
        location.href = requestUrl;
    }
    else {
        // TODO 7 : 보낸 요청이 에러일 때, message를 보이는 경고창을 띄움.
        const parsedResponse = await response.json(); // 응답 본문을 JSON으로 파싱.
        alert(parsedResponse.message);
    }
}
