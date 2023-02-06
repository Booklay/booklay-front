
let catNo = 1;

let authorNo = 1;

document.addEventListener('categorySelected', function (e) {
  if (catNo < 4) {
    const {id, name} = e.detail.category

    const categorySpan = document.createElement("span")
    const innerCategorySpan = "카테고리" + catNo + " : "
    categorySpan.innerText = innerCategorySpan;

    const categoryInput = document.createElement("input")
    categoryInput.setAttribute('type', 'number')
    categoryInput.setAttribute('name', 'categoryIds[' + (catNo - 1) + ']')
    categoryInput.setAttribute('value', id);
    categoryInput.setAttribute('min', 0);
    categoryInput.hidden = true;

    const categoryDisplay = document.createElement("span")
    categoryDisplay.innerText = name
    categoryDisplay.setAttribute('id', "spanNo"+(catNo-1))

    const editBtn = document.createElement("button")
    editBtn.setAttribute('value', '수정')
    editBtn.innerText = "카테고리 수정"
    editBtn.setAttribute('id', catNo);

    const br = document.createElement("br");

    const categoryDiv = document.getElementById('categories');

    categoryDiv.appendChild(categorySpan);
    categoryDiv.appendChild(categoryInput);
    categoryDiv.appendChild(categoryDisplay);
    categoryDiv.appendChild(editBtn);
    categoryDiv.appendChild(br);

    document.getElementById(catNo).addEventListener('click', function (e) {
      e.preventDefault()
      const inputId = catNo
      window.open("/admin/product/category/popup/" + inputId, "a",
          "width=600, height=700, left=100, top=50");
    })
    catNo = catNo + 1;
  }
})

document.addEventListener('categoryReselected', function (e) {
  if (catNo < 4) {
    const {id, name, data} = e.detail.category
    console.log(id, name, data)

    document.getElementById('categoryIds[' + (data - 1) + ']').setAttribute(
        'value', id)
    document.getElementById("spanNo"+(data-1)).innerText=name
  }
})

document.addEventListener('authorSelected', function (e) {
  if (catNo < 4) {
    const {id, name} = e.detail.author

    const authorSpan = document.createElement("span")
    const innerAuthorSpan = "작가" + authorNo + " : "
    authorSpan.innerText = innerAuthorSpan;

    const authorInput = document.createElement("input")
    authorInput.setAttribute('type', 'number')
    authorInput.setAttribute('name', 'authorIds[' + (authorNo - 1) + ']')
    authorInput.setAttribute('value', id)
    authorInput.hidden=true;

    const authorDisplay = document.createElement("span")
    authorDisplay.innerText = name

    const br = document.createElement("br");

    const authorDiv = document.getElementById('authors')

    authorDiv.appendChild(authorSpan);
    authorDiv.appendChild(authorInput);
    authorDiv.appendChild(authorDisplay);
    authorDiv.appendChild(br);
    authorNo = authorNo + 1;
  }
})