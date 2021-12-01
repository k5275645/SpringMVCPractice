//
// 월을 2자리숫자 타입으로 반환
//
Date.prototype.getFullMonth = function () {
    return this.getMonth() + 1 > 9 ? this.getMonth() + 1 : "0" + (this.getMonth() + 1);
}

//
// 일을 2자리숫자 타입으로 반환
//
Date.prototype.getFullDay = function () {
    return (this.getDate() > 9 ? this.getDate()  : "0" + this.getDate());
}

//
// 시간을 2자리숫자 타입으로 반환
//
Date.prototype.getFullHour = function () {
    return (this.getHours() > 9 ? this.getHours()  : "0" + this.getHours());
}

//
// 분을 2자리숫자 타입으로 반환
//
Date.prototype.getFullMinute = function () {
    return (this.getMinutes() > 9 ? this.getMinutes()  : "0" + this.getMinutes());
}

//
// 초를 2자리숫자 타입으로 반환
//
Date.prototype.getFullSecond = function () {
    return (this.getSeconds() > 9 ? this.getSeconds()  : "0" + this.getSeconds());
}


//
//  오늘날짜를 반환 년월일 사이의 구분자로 delimiter를 사용
// ex) delimiter가 "-"이면 YYYY-MM-DD타입으로 반환
//
Date.prototype.getFullDate = function (delimiter) {
    if(delimiter == null) delimiter = "";
    return this.getFullYear() + delimiter + this.getFullMonth() + delimiter + this.getFullDay();
}

//
//  일시를 반환.
// ex) YYYY-MM-DD HH24:MI:SS : 2016-08-26 08:45:00
//
Date.prototype.getFullDateTime = function () {
    return this.getFullYear() + '-' + this.getFullMonth() + '-' + this.getFullDay() + ' ' + this.getFullHour() + ':' + this.getFullMinute() + ':' + this.getFullSecond();
}


/**
 * 이번주 첫번째 요일(일요일)을 Date 형식으로 반환
 * 참고) 주의 시작은 일요일. 주의 마지막은 토요일이다.
 */
Date.prototype.getDateWithWeekFirstDay = function () {
    var dstTime = new Date();
    dstTime.addDate(0, 0, -1 * dstTime.getDay());
    return dstTime;
}
/**
 * 이번주 첫번째 요일(일요일)을 String 형식으로 반환 : 구분자로 delimiter를 사용
 * 참고) 주의 시작은 일요일. 주의 마지막은 토요일이다.
 * ex) delimiter가 "-"이면 YYYY-MM-DD타입으로 반환
 */
Date.prototype.getFullDateWithWeekFirstDay = function (delimiter) {
    var dstTime = this.getDateWithWeekFirstDay();
    return dstTime.getFullDate(delimiter);
}

/**
 * 당월 1일을 Date 형식으로 반환
 */
Date.prototype.getDateWithFirstDay = function () {
    var dstTime = new Date(Number(this.getFullYear()), Number(this.getFullMonth())-1, "01");
    return dstTime;
}
/**
 * 당월 1일을 String 형식으로 반환 : 구분자로 delimiter를 사용
 * ex) delimiter가 "-"이면 YYYY-MM-DD타입으로 반환
 */
Date.prototype.getFullDateWithFirstDay = function (delimiter) {
    var dstTime = this.getDateWithFirstDay();
    return dstTime.getFullDate(delimiter);
}

/**
 * 당월 말일을 Date 형식으로 반환
 */
Date.prototype.getDateWithLastDay = function () {
    var dstTime = new Date(Number(this.getFullYear()), Number(this.getFullMonth())-1, "01");
    dstTime.addDate(0,1,0);
    dstTime.addDate(0,0,-1);
    return dstTime;
}
/**
 * 당월 말일을 String 형식으로 반환 : 구분자로 delimiter를 사용
 * ex) delimiter가 "-"이면 YYYY-MM-DD타입으로 반환
 */
Date.prototype.getFullDateWithLastDay = function (delimiter) {
    var dstTime = this.getDateWithLastDay();
    return dstTime.getFullDate(delimiter);
}


/**
 * 다음달의 1일을 Date 형식으로 반환
 */
Date.prototype.getDateWithNextMonthFirstDay = function () {
    var dstTime = new Date(Number(this.getFullYear()), Number(this.getFullMonth())-1, "01");
    dstTime.addDate(0,1,0);
    return dstTime;
}
/**
 * 다음달의 1일을 String 형식으로 반환 : 구분자로 delimiter를 사용
 * ex) delimiter가 "-"이면 YYYY-MM-DD타입으로 반환
 */
Date.prototype.getFullDateWithNextMonthFirstDay = function (delimiter) {
    var dstTime = this.getDateWithNextMonthFirstDay();
    return dstTime.getFullDate(delimiter);
}

/**
 * 다음달의 말일을 Date 형식으로 반환
 */
Date.prototype.getDateWithNextMonthLastDay = function () {
    var dstTime = new Date(Number(this.getFullYear()), Number(this.getFullMonth())-1, "01");
    dstTime.addDate(0,2,0);
    dstTime.addDate(0,0,-1);
    return dstTime;
}
/**
 * 다음달의 말일을 String 형식으로 반환 : 구분자로 delimiter를 사용
 * ex) delimiter가 "-"이면 YYYY-MM-DD타입으로 반환
 */
Date.prototype.getFullDateWithNextMonthLastDay = function (delimiter) {
    var dstTime = this.getDateWithNextMonthLastDay();
    return dstTime.getFullDate(delimiter);
}


//
// Time(YYYYMMDD, YYYY-MM-DD) 스트링을 자바스크립트 Date 객체로 변환. 시간은 초기화
// parameter time: Time 형식의 String
//
Date.prototype.setFormatDate = function (time) {
	time = time.replace("-", "").replace("-", "");

    var year  = Number(time.substr(0,4));
    var month = Number(time.substr(4,2)) - 1; // 1월=0,12월=11
    var day = Number(time.substr(6,2));
    
    var date = new Date(year, month, day);
    
    return date;
}


//
// Data객체에서 지정된 년월일 만큼 더한다.
// parameter time: Time 형식의 String
//
Date.prototype.addDate = function (y,m,d) { 
    var date = this;

    date.setFullYear(date.getFullYear() + parseInt(y));
    date.setMonth(date.getMonth() + parseInt(m));
    date.setDate(date.getDate() + parseInt(d));

    return date;
}


//
// Date객체에 설정된날짜와 dstDate날짜사이의 일수차를 구한다.
// parameter time: Time 형식의 String(YYYYMMDD, YYYY-MM-DD)
//
Date.prototype.diffDay =  function(dstDate) {
	dstDate = dstDate.replace("-", "").replace("-", "");

    var sYear = Number(dstDate.substr(0,4));
    var sMon = Number(dstDate.substr(4,2));
    var sDay  = Number(dstDate.substr(6,2)); 

    var dstTime=new Date(sYear, sMon-1, sDay); 
    
    return Math.ceil((dstTime.getTime()-this.getTime())/(24*60*60*1000));
}


//
// Date객체에 설정된날짜와 dstDate날짜사이의 월수차를 구한다.
// parameter time: Time 형식의 String(YYYYMMDD, YYYY-MM-DD)
//
Date.prototype.diffMonth =  function(dstDate) { 
	dstDate = dstDate.replace("-", "").replace("-", "");
    var dYear = Number(dstDate.substr(0,4));
    var dMon = Number(dstDate.substr(4,2));
	var nYear = this.getFullYear();
    var nMon = this.getMonth();

    return eval((nYear - dYear)*12 + nMon - dMon);
}


Date.prototype.format = function(f) {
    if (!this.valueOf()) return " ";
 
    var weekName = ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];
    var d = this;
     
    return f.replace(/(yyyy|yy|MM|dd|E|hh|mm|ss|a\/p)/gi, function($1) {
        switch ($1) {
            case "yyyy": return d.getFullYear();
            case "yy": return (d.getFullYear() % 1000).zf(2);
            case "MM": return (d.getMonth() + 1).zf(2);
            case "dd": return d.getDate().zf(2);
            case "E": return weekName[d.getDay()];
            case "HH": return d.getHours().zf(2);
            case "hh": return ((h = d.getHours() % 12) ? h : 12).zf(2);
            case "mm": return d.getMinutes().zf(2);
            case "ss": return d.getSeconds().zf(2);
            case "a/p": return d.getHours() < 12 ? "오전" : "오후";
            default: return $1;
        }
    });
};

String.prototype.string = function(len) {var s = '', i = 0; while (i++ < len) { s += this; } return s;};
String.prototype.zf = function(len) {return "0".string(len - this.length) + this;};
Number.prototype.zf = function(len) {return this.toString().zf(len);};