function showCategory() {
  window.open("/admin/product/category/popup", "a",
      "width=600, height=700, left=100, top=50");
}

function showAuthor() {
  window.open("/admin/product/author/popup", "a",
      "width=1000, height=750, left=100, top=50");
}

function validCheck(){
  let categoryCount = document.getElementsByClassName("categoryInput").length;
  if (categoryCount === 0){
    alert("카테고리를 하나이상 지정해주세요")
    return false;
  }
  let authorCount = document.getElementsByClassName("authorInput").length;
  if (authorCount === 0){
    alert("작가를 한명 이상 지정해주세요")
    return false;
  }
  return true;
}


document.addEventListener('DOMContentLoaded', () => {
  const Editor = toastui.Editor;

  const editor1 = new Editor({
    el: document.querySelector('#shortEditor'),
    height: '500px',
    initialEditType: 'markdown',
    previewStyle: 'vertical',
    autofocus: false
  });
  editor1.getMarkdown();

  const editor2 = new Editor({
    el: document.querySelector('#longEditor'),
    height: '500px',
    initialEditType: 'markdown',
    previewStyle: 'vertical',
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
