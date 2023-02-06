function showAuthorEdit(authorNo) {
  window.open("/admin/product/author/edit/" + authorNo, "a",
      "width=600, height=700, left=100, top=50");
}

const updateBtn = document.querySelectorAll('.update-button');
updateBtn.forEach(function (item) {
  item.addEventListener('click', function () { // 클릭 이벤트 발생시,
    var form1 = this.parentNode.parentNode
    console.log(form1)
    form1.querySelector('.update-info').toggleAttribute('disabled')
    form1.querySelector('.submit-btn').toggleAttribute('hidden')
  });
});