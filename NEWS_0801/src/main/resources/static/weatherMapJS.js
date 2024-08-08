/*
서울특별시			60	127
부산광역시			98	76
대구광역시			89	90
광주광역시			58	74
대전광역시			67	100
울산광역시			102	84
제주특별자치도			52	38


춘천시		73	134
청주시 69	106
강릉시		92	131
수원시 60	121
안동시		91	106
전주시		63	89
여수시		73	66
목포시		50	67


초단기실황	T1H	기온	℃	10
	RN1	1시간 강수량	mm	8
	UUU	동서바람성분	m/s	12
	VVV	남북바람성분	m/s	12
	REH	습도	%	8
	VEC	풍향	deg	10
	WSD	풍속	m/s	10
	PTY	강수형태
	->(단기) 없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4) 

*/

	/* 초단기 실황 조회 (지역)*/
	var url_UltraSrtNcst = 'http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst'; /*URL*/
	
	var apiKey = 'onln1BmvLIlFVm327173TOIgaWX%2BF5okicZQ6k0NeMNuIBU0yqyXrW3imG38ZZi9sggZ4a%2FsrSpNsdV15%2FnjjQ%3D%3D';
	var queryParams_UltraSrtNcst = '?' + encodeURIComponent('serviceKey') + '='+apiKey; /*Service Key*/
	queryParams_UltraSrtNcst += '&' + encodeURIComponent('pageNo') + '=' + encodeURIComponent('1'); /**/
	queryParams_UltraSrtNcst += '&' + encodeURIComponent('numOfRows') + '=' + encodeURIComponent('100'); /**/
	queryParams_UltraSrtNcst += '&' + encodeURIComponent('dataType') + '=' + encodeURIComponent('JSON'); /**/
	queryParams_UltraSrtNcst += '&' + encodeURIComponent('base_date') + '=' + encodeURIComponent('20240808'); /**/
	queryParams_UltraSrtNcst += '&' + encodeURIComponent('base_time') + '=' + encodeURIComponent('2030'); /**/
	queryParams_UltraSrtNcst += '&' + encodeURIComponent('nx') + '=' + encodeURIComponent('55'); /**/
	queryParams_UltraSrtNcst += '&' + encodeURIComponent('ny') + '=' + encodeURIComponent('127'); /**/
	
	let dataArray = [];
	let dataArrayRe = [];
	
	fetch(url_UltraSrtNcst + queryParams_UltraSrtNcst, {
	}).then((response) => response.json())
	.then((data) =>
		{
			dataArray = [...(data.response.body.items.item)];
			dataArrayRe = [{'x' : dataArray[0].nx}, {'y' : dataArray[0].ny}];
			dataArray.forEach((i)=> 
			 dataArrayRe = [...dataArrayRe, i.category, i.obsrValue]); // 좌표포함 data
				//console.log(dataArrayRe);
		}
	);


