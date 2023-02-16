function showAddressCreatePopup() {
    let option = "width = 700, height = 850, top = 100, left = 200, scrollbars = yes, location = no"
    let ret = window.open("/member/profile/address/register", "addressRegisterPopup", option);
}

function showAddressUpdatePopup(value) {
    let option = "width = 700, height = 900, top = 100, left = 200, scrollbars = yes, location = no"
    let ret = window.open("/member/profile/address/update/" + value, "addressUpdatePopup", option);
}
