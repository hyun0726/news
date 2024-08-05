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
const month = alertDayLenth(fullDate.getMonth() + 1);
const day = alertDayLenth(fullDate.getDate());
const full_date = [year, month, day].join("-");


/** base time 구하기 **/

/**** DB 키 ****/
export const apiKey = "onln1BmvLIlFVm327173TOIgaWX%2BF5okicZQ6k0NeMNuIBU0yqyXrW3imG38ZZi9sggZ4a%2FsrSpNsdV15%2FnjjQ%3D%3D";


const movieList = async () => {
  let dataArray = [];
  for (let page = 1; dataArray.length <= 25; page++) {
    const response = await fetch(
      `https://api.themoviedb.org/3/discover/movie?api_key=${dbApiKey}&sort_by=release_date.desc&include_adult=false&include_video=false&page=${page}&vote_average.gte=4.5&vote_average.lte=5.5&with_watch_monetization_types=flatrat&include_video=false`
    );
    const json = await response.json();
    const data: [] = await removeData(json);
    dataArray = [...dataArray, ...data];
  }
  const resultArray = dataArray.slice(0, 25);
  return resultArray;
};
export { movieList };
