window.addEventListener('DOMContentLoaded', function () {
  const authorButtons = document.querySelectorAll('.author-select-btn')
  console.log(authorButtons)

  authorButtons.forEach(
      button => button.addEventListener('click', function (e) {
        const {id, name} = e.target
          const inputId = this.getAttribute("data-location");
        let customEvent
        if (inputId == null){
            customEvent = new CustomEvent('authorSelected', {
                detail: {
                    author: { id, name }
                }
            })
        }else {
            customEvent = new CustomEvent('authorReselected', {
                detail: {
                    author: { id, name , inputId}
                }
            })
        }
        opener.document.dispatchEvent(customEvent)
        window.close();
      }))
})

