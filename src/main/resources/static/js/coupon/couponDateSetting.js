let expiredAt = document.getElementById("expiredAt");

let now = new Date();
let year = now.getFullYear();
let month = now.getMonth()+1;

let nextMonth = new Date(year, month, 0);

if(month < 10) {
    month = "0" + month;
}

let day = nextMonth.getDate();

if(day < 10) {
    day = "0" + day;
}

expiredAt.value = year + "-" + month + "-" + day + "T11:59";
