window.addEventListener('DOMContentLoaded', function () {
  const categoryButtons = document.querySelectorAll('.cat-select-btn')
  console.log(categoryButtons)

  categoryButtons.forEach(
      button => button.addEventListener('click', function (e) {
        const {id, name, data} = e.target
        const event = new CustomEvent('categoryReselected', {
          detail: {
            category: { id, name , data}
          }
        })
        opener.document.dispatchEvent(event)
        window.close();
      }))
})
