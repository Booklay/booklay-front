function setApplyCategoryText(idValue, nameValue) {
  if (opener.catNo < 4) {
    const categorySpan = opener.document.createElement("span")
    const innerCategorySpan = "카테고리" + opener.catNo + " : "
    categorySpan.innerText = innerCategorySpan;

    const categoryInput = document.createElement("input")
    categoryInput.setAttribute('type', 'number')
    categoryInput.setAttribute('id', 'categoryIds[' + (opener.catNo - 1) + ']')
    categoryInput.setAttribute('value', idValue);
    categoryInput.setAttribute('min', 0);
    categoryInput.hidden=true;

    const categoryDisplay = document.createElement("span")
    categoryDisplay.innerText = nameValue

    const br = opener.document.createElement("br");

    opener.document.getElementById('categories').appendChild(categorySpan);
    opener.document.getElementById('categories').appendChild(categoryInput);
    opener.document.getElementById('categories').appendChild(categoryDisplay);
    opener.document.getElementById('categories').appendChild(br);
    opener.catNo = opener.catNo + 1

    window.close();
  }
}