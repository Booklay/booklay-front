async function couponDetail(couponId) {
    const url = document.querySelector('#url').value;
    const requestUrl = `${url}/admin/coupons/detail/${couponId}`;
    const response = await fetch(requestUrl);

    if (response.ok) {
        location.href = requestUrl;
    }
    else {
        const parsedResponse = await response.json();
        alert(parsedResponse.message);
    }
}
