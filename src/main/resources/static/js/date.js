/**
 * 家計簿追加画面では当日の、編集画面ではすでに登録されている日のデータを取得する
 */



const date = new Date();

//日付フォーマットを変換
function dateFormat(date, format){
	format = format.replace("YYYY", date.getFullYear());
	format = format.replace("MM", ("0" + (date.getMonth() +1)).slice(-2));
	format = format.replace("DD", ("0" + date.getDate()).slice(-2));
	return format;
}

//optionにセット
const dateData = dateFormat(date, 'YYYY-MM-DD');
const field = document.getElementById("dateSelect");
field.value = dateData;
field.setAttribute("min", dateData);