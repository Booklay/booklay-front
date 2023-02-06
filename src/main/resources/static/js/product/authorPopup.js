window.addEventListener('DOMContentLoaded', function () {
  const authorButtons = document.querySelectorAll('.author-select-btn')
  console.log(authorButtons)

  authorButtons.forEach(
      button => button.addEventListener('click', function (e) {
        const {id, name} = e.target
        const event = new CustomEvent('authorSelected', {
          detail: {
            author: { id, name }
          }
        })
        opener.document.dispatchEvent(event)
        window.close();
      }))
})

