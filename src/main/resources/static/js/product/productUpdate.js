var catNo = 1;

var authorNo = 1;

function showCategory() {
  window.open("/admin/product/category/popup", "a",
      "width=600, height=700, left=100, top=50");
}

function showAuthor() {
  window.open("/admin/author/maintenance/popup", "a",
      "width=800, height=750, left=100, top=50");
}

function editButtonActive(idValue){
  console.log(idValue)
  const targetNo = parseInt(idValue)
  const targetId = 'categoryIds[' + (targetNo-1) + ']'
  document.getElementById(targetId).toggleAttribute('hidden')
}