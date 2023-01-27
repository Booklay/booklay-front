function showCategory() {
  window.open("/admin/categories/", "a",
      "width=400, height=600, left=100, top=50");
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
      "width=725, height=750, left=100, top=50");
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
  childProductInput.setAttribute('name', 'productIds[' + (childProduct - 1) + ']')

  const br = document.createElement("br");

  document.getElementById('childProduct').appendChild(childProductSpan);
  document.getElementById('childProduct').appendChild(childProductInput);
  document.getElementById('childProduct').appendChild(br);

  childProduct = childProduct + 1
}
