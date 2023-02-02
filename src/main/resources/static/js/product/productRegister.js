function showCategory() {
  window.open("/admin/product/category/popup", "a",
      "width=600, height=700, left=100, top=50");
}

function showAuthor() {
  window.open("/admin/author/maintenance/popup", "a",
      "width=800, height=750, left=100, top=50");
}


document.addEventListener('DOMContentLoaded', () => {
  const Editor = toastui.Editor;

  const editor1 = new Editor({
    el: document.querySelector('#shortEditor'),
    height: '500px',
    initialEditType: 'markdown',
    previewStyle: 'vertical',
    placeholder: '내용을 입력해주세요.',
    autofocus: false
  });
  editor1.getMarkdown();

  const editor2 = new Editor({
    el: document.querySelector('#longEditor'),
    height: '500px',
    initialEditType: 'markdown',
    previewStyle: 'vertical',
    placeholder: '내용을 입력해주세요.',
    autofocus: false
  });
  editor2.getMarkdown();

  const submitButton = document.getElementById('btn-submit');

  submitButton.addEventListener('click', (event) => {
    const shortDescription = editor1.getHTML();
    const longDescription = editor2.getHTML();

    console.log(shortDescription);
    console.log(longDescription);

    document.getElementById('shortDescription').setAttribute('value', shortDescription)
    document.getElementById('longDescription').setAttribute('value', longDescription)
  });
});

var catNo = 1;

var authorNo = 1;

