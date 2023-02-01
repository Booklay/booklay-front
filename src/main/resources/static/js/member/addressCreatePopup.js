function showAddressPopup(value) {
    let option = "width = 700, height = 500, top = 100, left = 200, scrollbars = yes, location = no"
    let ret = window.open("/address/register/" + value, "addressRegisterPopup", option);
}
