document.addEventListener('DOMContentLoaded', () => {
    function createParagraph() {
        console.log("event");
        let categorySelectorLabel = document.getElementById("categorySelectorLabel")
        let categorySelector = document.getElementById("categorySelector")
        let itemSelectorLabel = document.getElementById("itemSelectorLabel")
        let itemSelector = document.getElementById("itemSelector")
        let isOrderCoupon = document.getElementById("isOrderCoupon")

        if (isOrderCoupon.checked){
            itemSelector.setAttribute("name", "disable");
            itemSelectorLabel.classList.add("hidden");
            categorySelector.setAttribute("name", "applyItemId");
            categorySelectorLabel.classList.remove("hidden");
        }else {
            categorySelector.setAttribute("name", "disable");
            categorySelectorLabel.classList.add("hidden");
            itemSelector.setAttribute("name", "applyItemId");
            itemSelectorLabel.classList.remove("hidden");
        }
    }
    let button = document.getElementById("isOrderCoupon");
    button.addEventListener("change", createParagraph);
    let button2 = document.getElementById("isProductCoupon");
    button2.addEventListener("change", createParagraph);
});