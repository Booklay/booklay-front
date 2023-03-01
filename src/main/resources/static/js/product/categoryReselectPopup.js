window.addEventListener('DOMContentLoaded', function () {
  const categoryButtons = document.querySelectorAll('.cat-select-btn')
  console.log(categoryButtons)

  categoryButtons.forEach(
      button => button.addEventListener('click', function (e) {
          const {id, name} = e.target
          let data = document.getElementById(id).getAttribute("data-location");
          const event = new CustomEvent('categoryReselected', {
          detail: {
            category: { id, name , data}
          }
        })
        opener.document.dispatchEvent(event)
        window.close();
      }))
})
