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

    const editBtn = document.createElement("input")
    editBtn.setAttribute('type', 'button')
    editBtn.setAttribute('value', '수정')
    editBtn.setAttribute('id', opener.catNo);

    opener.document.getElementById('categories').appendChild(categoryInput);

    editBtn.addEventListener('click', () => {
      console.log(opener.catNo)
      const targetNo = parseInt(opener.catNo)
      const targetId = 'categoryIds[' + (opener.catNo-1) + ']'
      document.getElementById(targetId).toggleAttribute('hidden')
    }, false)

    const br = opener.document.createElement("br");

    opener.document.getElementById('categories').appendChild(categorySpan);
    opener.document.getElementById('categories').appendChild(categoryDisplay);
    opener.document.getElementById('categories').appendChild(editBtn);
    opener.document.getElementById('categories').appendChild(br);

    // opener.document.getElementById(opener.catNo).addEventListener('click', opener.editButtonActive(opener.catNo))
    // opener.document.getElementById(opener.catNo).setAttribute('onclick', opener.editButtonActive(opener.catNo))

    opener.catNo = opener.catNo + 1
    window.close();
  }
}
