function showCategory() {
  window.open("/admin/categories/", "a",
      "width=600, height=700, left=100, top=50");
}

let catNo = 1;

function addCategory() {
  if (catNo < 4) {
    const categorySpan = document.createElement("span")
    const innerCategorySpan = "카테고리" + catNo + " : "
    categorySpan.innerText = innerCategorySpan;

    const categoryInput = document.createElement("input")
    categoryInput.setAttribute('type', 'number')
    categoryInput.setAttribute('name', 'categoryIds[' + (catNo - 1) + ']')

    const br = document.createElement("br");

    document.getElementById('categories').appendChild(categorySpan);
    document.getElementById('categories').appendChild(categoryInput);
    document.getElementById('categories').appendChild(br);

    catNo = catNo + 1
  }
}

function showAuthor() {
  window.open("/admin/author/maintenance", "a",
      "width=800, height=750, left=100, top=50");
}

let authorNo = 1;

function addAuthor() {
  const authorSpan = document.createElement("span")
  const innerAuthorSpan = "작가" + authorNo + " : "
  authorSpan.innerText = innerAuthorSpan;

  const authorInput = document.createElement("input")
  authorInput.setAttribute('type', 'number')
  authorInput.setAttribute('name', 'authorIds[' + (authorNo - 1) + ']')

  const br = document.createElement("br");

  document.getElementById('authors').appendChild(authorSpan);
  document.getElementById('authors').appendChild(authorInput);
  document.getElementById('authors').appendChild(br);

  authorNo = authorNo + 1
}

function showProduct() {
  window.open("/admin/categories/", "a",
      "width=400, height=600, left=100, top=50");
}

let childProduct = 1;

function addChildProduct() {
    const childProductSpan = document.createElement("span")
    const innerChildProductSpan = "구독 발송 상품" + childProduct + " : "
    childProductSpan.innerText = innerChildProductSpan;

    const childProductInput = document.createElement("input")
    childProductInput.setAttribute('type', 'number')
    childProductInput.setAttribute('name', 'categoryIds[' + (childProduct - 1) + ']')

    const br = document.createElement("br");

    document.getElementById('childProduct').appendChild(childProductSpan);
    document.getElementById('childProduct').appendChild(childProductInput);
    document.getElementById('childProduct').appendChild(br);

  childProduct = childProduct + 1
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