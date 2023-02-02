function setApplyAuthorText(idValue, nameValue) {

  const authorSpan = opener.document.createElement("span")
  const innerAuthorSpan = "작가" + opener.authorNo + " : "
  authorSpan.innerText = innerAuthorSpan;

  const authorInput = opener.document.createElement("input")
  authorInput.setAttribute('type', 'number')
  authorInput.setAttribute('id', 'authorIds[' + (opener.authorNo - 1) + ']')
  authorInput.setAttribute('value', idValue)
  authorInput.hidden=true;

  const authorDisplay = opener.document.createElement("span")
  authorDisplay.innerText = nameValue

  const br = opener.document.createElement("br");

  opener.document.getElementById('authors').appendChild(authorSpan);
  opener.document.getElementById('authors').appendChild(authorInput);
  opener.document.getElementById('authors').appendChild(authorDisplay);
  opener.document.getElementById('authors').appendChild(br);
  opener.authorNo = opener.authorNo + 1;

  window.close();
}
