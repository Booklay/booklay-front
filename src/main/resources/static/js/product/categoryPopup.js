window.addEventListener('DOMContentLoaded', function () {
  const categoryButtons = document.querySelectorAll('.cat-select-btn')
  console.log(categoryButtons)

  categoryButtons.forEach(
      button => button.addEventListener('click', function (e) {
        const {id, name} = e.target
        const event = new CustomEvent('categorySelected', {
          detail: {
            category: { id, name }
          }
        })
        opener.document.dispatchEvent(event)
        window.close();
      }))
})
