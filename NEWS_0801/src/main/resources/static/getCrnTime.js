/**** 오늘 날짜 불러오기 ****/
const fullDate = new Date();
const alertDayLenth = (number) => {
  let dayNumber = JSON.stringify(number);
  if (dayNumber.length === 1) {
    dayNumber = 0 + dayNumber;
  }
  return dayNumber;
}

const year = JSON.stringify(fullDate.getFullYear());
const month = alertDayLenth(fullDate.getMonth() +1);
const day = alertDayLenth(fullDate.getDate());
const current_time = fullDate.getHours();
const full_date = [year, month, day,current_time,'00'].join("");	

console.log(full_date);
