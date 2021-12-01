/***************************
 * 전역 변수 설정
 ***************************/
var cfv_gridLayoutDefault = { Cfg : {}, Actions : {}, Def : [], DefCols : [], Cols : [], Head : [], Toolbar : {} };
var cfv_dataSrcDefault = {Body : [ [] ]}; // 그리드 초기 값 세팅
var cfv_datepickerKr = {
	'closeText' : '닫기',
	'currentText' : '금일',
	'dateFormat' : 'yy-mm-dd',
	'dayNames' : ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'],
	'dayNamesMin' : ['일', '월', '화', '수', '목', '금', '토'],
	'dayNamesShort' : ['일', '월', '화', '수', '목', '금', '토'],
	'firstDay' : 0,
	'isRTL' : false,
	'monthNames' : ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
	'monthNamesShort' : ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
	'nextText' : '다음',
	'prevText' : '전',
	'showMonthAfterYear' : true,
	'weekHeader' : '주',
	'yearSuffix' : ''
};

var cfv_sColorLightGrey     = "242,242,242"; // - 옅은 회색
var cfn_sColorPaleYellow    = "255,255,204"; // - 옅은 노랑색
var cfn_sColorLightGreen    = "175,220,126"; // - 옅은 초록색
var cfn_sColorLightBlue		= "197,217,241"; // - 옅은 파란색
var cfn_sColorWhite			= "255,255,255"; // - 배경 흰색
var cfn_sColorPeriodYYYYMM  = "255,192,0";   // - 월을 표현하는 색상(주황색)
var cfn_sColorPeriodYYYYWW  = "252,228,214"; // - 주를 표현하는 색상(옅은 분홍)

/**********************************
 * chart에서 사용될 공통 색상값 앞부터 순차적으로 사용
 **********************************/
var cfn_dataChartColor01 = ["#ea173f","#ff8b0f","#ffc20a","#16b6b3","#34bcd5","#2e64ad","#453178","#921daf","#da3889","#59aae3"];
var cfn_dataChartColor02 = ["#025ac0","#0881dd","#06b7b3","#01b72e","#36c209","#377b2b","#354747","#53b110","#1651bc","#51389c"];

/*********************************
 * 공통으로 사용하는 펑션 리스트
 ********************************/
/**
 * grid의 기본 layout을 반환
 */
 function cfn_getGridLayoutDefault() {
	
	if( !(this instanceof cfn_getGridLayoutDefault) ) { 
		return new cfn_getGridLayoutDefault(); 
	}
	
	this.Cfg = {};
	this.Actions = {};
	this.Def = [];
	this.DefCols = [];
	this.LeftCols = [];
	this.Cols = [];
	this.RightCols = [];
	this.Head = [];
	this.Toolbar = {};
}

/**
 * grid의 기본 dataSrc를 반환
 */
function cfn_getDataSrcDefault() {
	if( !(this instanceof cfn_getDataSrcDefault) ) { 
		return new cfn_getDataSrcDefault(); 
	}
	
	this.Body = [[]];
}


/**
 * 비동기 Get방식 요청
 *
 * @param av_sUrl : 대상 url
 * @param av_oData : Object 형식의 paramter
 * @param av_bAsync : true -> 비동기, false -> 동기(default = true)
 */
function cfn_ajaxGet(av_sUrl, av_oData, av_bAsync) {

	if (!av_sUrl) {
		throw 'url cannot be null';
	}
	
	if (!av_oData) {
		av_oData = { 'uid' : new Date().getTime() };
	}
	
	var isAsync = true;
	if (av_bAsync === false) {
		isAsync = false;
	}
	
	return $.ajax({
		  url         : av_sUrl
		, type        : 'GET'
		, cache       : false
		, async       : isAsync
		, dataType    : 'json'
		, data        : av_oData
	});
}

/**
 * 비동기 POST방식 요청
 *
 * @param av_sUrl : 대상 url
 * @param av_oData : Object 형식의 paramter
 * @param av_bAsync : true -> 비동기, false -> 동기(default = true)
 */
function cfn_ajaxPost(av_sUrl, av_oData, av_bAsync) {
	
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	
	if (!av_sUrl) {
		throw 'url cannot be null';
	}
	
	if (!token || !header) {
		throw 'csrf token or header is required';
	}
	
	if (!av_oData) {
		av_oData = { 'uid' : new Date().getTime() };
	}
	
	var isAsync = true;
	if (av_bAsync === false) {
		isAsync = false;
	}
	
	return $.ajax({
		  url         : av_sUrl
		, type        : 'POST'
		, cache       : false
		, async       : isAsync
		, dataType    : 'json'
		, contentType : 'application/json'
        , data        : JSON.stringify(av_oData)  // - data 전송은 json string 방식. @RequestBody Map<String, Object> params 로 받을 예정
		, beforeSend  : function(xhr, opts) {
			xhr.setRequestHeader(header, token);
		}
	});
}

function cfn_printError(jqXHR, textStatus, errorThrown) {
	console.log('status : ' + jqXHR['status'] + ', error : ' + jqXHR['responseText']);
	
	var response = jqXHR['responseJSON'];
	if (!response || response['_RESULT_FLAG'] !== 'E') {
		return;
	}
	
	try{
        cfn_hideProgress(); // - Progress Image Hide
    } catch(e) {}

	if (jqXHR['status'] === 401 || jqXHR['status'] === 403) {
	    alert("허가되지 않았거나 세션이 종료되었습니다(" + jqXHR['status'] + ")");
		top.location.href = "/snop/security/login";
	} else {
		alert(response['_RESULT_MSG']);
	}
}

/**
 * 트리그리드의 추가/삭제/변경된 행 정보/데이터를 반환한다.
 * 
 * av_arrIncludeFixed : 포함할 Row의 "Fixed"속성값 리스트. 기본은 [""]. ex) ["", "Foot", "Head", "Solid"]
 *                      "Fixed"속성이 있는 Row : "Foot", "Head", "Solid"와 같은 특수한 Row만 해당. 기본 바디영역의 Row에서는 "Fixed" 값이 없음. 
 * 
 * ROW_INFO :: id           : 트리그리드의 ROW ID
 *             Changed      : 해당 행의 수정 여부. ex) "1", "0"
 *             Deleted      : 해당 행의 삭제 여부. ex) "1", "0"
 *             Added        : 해당 행의 신규 여부. ex) "1", "0"
 *             ChangeStatus : 해당 행의 신규/수정/삭제 상태. ex) insert, update, delete
 *             ChangeColIds : 해당 행의 변경된 컬럼ID 리스트. ex) [ "DESCRIPTION", "EFC_RATE" ]
 * ROW_DATA :: action       : 해당 행의 신규/수정/삭제 상태. Const.java 참고 ex) insert, update, delete
 *             각 컬럼ID    : 트리그리드에 정의된 컬럼ID와 Value. ex) [ MFG_LINE_ALTER, MFG_CODE, MFG_NAME, STR_LINE_CODE, STR_LINE_NAME, END_LINE_CODE, END_LINE_NAME, ALTERNATE, DESCRIPTION, STD_CAPA, EFC_RATE, USE_YN ]
 * 
 * ex) cfn_getGridColInfo(Grids[fv_sGridId])
 *     -> {
 *           ROW_INFO : [ {id, Changed, Deleted, Added, ChangeStatus, ChangeColIds[] }]
 *           ROW_DATA : [ {action, 각 컬럼의 값들} ]
 *        }
 *
 * @param av_oTreeGrid
 * @param av_arrIncludeFixed
 * @return { ROW_INFO : [ {id, Changed, Deleted, Added, ChangeStatus, ChangeColIds[] }]
 *          ,ROW_DATA : [ {action, 각 컬럼의 값들} ] }
 */
function cfn_getGridChangedInfo(av_oTreeGrid, av_arrIncludeFixed) {
    var oGridChangedInfo = { ROW_INFO:[], ROW_DATA:[] };
    if(av_oTreeGrid == undefined) return oGridChangedInfo;
    
    // - 포함할 Fixed 속성 리스트 초기화
    if(av_arrIncludeFixed == undefined) {
        av_arrIncludeFixed = [""];
    }
    
    // - 그리드의 컬럼ID 정보 가져오기
    var arrColList = cfn_getGridColInfo(av_oTreeGrid);
    
    try {
        // - 추가/삭제/변경 행의 정보 및 데이터 수집
        var arrChangedRowInfo = []; // - 변경된 행의 정보 리스트. ex) [ {id, Changed, Deleted, Added, ChangeStatus, ChangeColIds[] }]
        var arrChangedRowData = []; // - 변경된 행의 데이터 리스트. ex) [ {각 컬럼의 값들} ]
        var oTreeGridChangedData = {};
        var sTreeGridChangedData = av_oTreeGrid.GetChanges(); // - "{"IO":{},"Changes":[{"id":"AR8","Changed":1,"DESCRIPTION":"강원공장-생맥주라인222"}]}"
        if(sTreeGridChangedData != "") oTreeGridChangedData = JSON.parse(sTreeGridChangedData);
        for(var i = 0 ; i < oTreeGridChangedData["Changes"].length ; i++) {
            var oTmpInfo = {};
            var arrChangeColIds = [];
            var oTmpData = {};
            var sTmpAction = ""; // - delete, insert, update
            var oRow = oTreeGridChangedData["Changes"][i];
            
            // - Data영역만 체크
            if(av_oTreeGrid.GetRowById( oRow["id"] )["Kind"] != "Data") continue;
            // - Fixed 속성 체크
            var bIncludeByFixed = false;
            for(var k = 0 ; k < av_arrIncludeFixed.length ; k++) {
                if(cfn_defaultValue(av_oTreeGrid.GetRowById( oRow["id"] )["Fixed"]) == av_arrIncludeFixed[k]) {
                    bIncludeByFixed = true;
                    k = av_arrIncludeFixed.length;
                }
            }
            if(!bIncludeByFixed) continue;
            
            //console.log(oRow);
            oTmpInfo["id"] = oRow["id"];
            oTmpInfo["Changed"] = (oRow["Changed"] != undefined ? oRow["Changed"]+"" : "0");
            oTmpInfo["Deleted"] = (oRow["Deleted"] != undefined ? oRow["Deleted"]+"" : "0");
            oTmpInfo["Added"] = (oRow["Added"] != undefined ? oRow["Added"]+"" : "0");
            if(oTmpInfo["Deleted"] == "1") {
                sTmpAction = "delete";
            } else if(oTmpInfo["Added"] == "1") {
                sTmpAction = "insert";
            } else if(oTmpInfo["Changed"] == "1") {
                sTmpAction = "update";
            }
            oTmpInfo["ChangeStatus"] = sTmpAction;
            for(var c = 0 ; c < arrColList.length ; c++) {
                var sColId = arrColList[c];
                if(oRow[sColId] != undefined) arrChangeColIds.push(sColId);
                if(av_oTreeGrid.GetRowById( oRow["id"] )[sColId] == undefined) {
                    oTmpData[sColId] = "";
                } else {
                    if(String(av_oTreeGrid.Cols[sColId]["Type"]).toUpperCase() == String("Enum").toUpperCase()
                       || !cfn_isNull(String(av_oTreeGrid.Cols[sColId]["SuggestType"]))
                      ) {
                        // - Enum, Suggest의 경우에는 그냥 가져와야 제대로 가져온다.
                        oTmpData[sColId] = String( av_oTreeGrid.GetRowById( oRow["id"] )[sColId] );
                        
                    } else if(String(av_oTreeGrid.Cols[sColId]["Type"]).toUpperCase() == String("Date").toUpperCase()) {
                        // - Date의 경우, String 형식으로 변환해서 가져온다. 즉, Format형식으로 가져온다.
                        var sDateFormat = av_oTreeGrid.GetFormat(av_oTreeGrid.GetRowById( oRow["id"] ), sColId);
                        if(sDateFormat == undefined || sDateFormat == null) {
                            oTmpData[sColId] = av_oTreeGrid.GetString(av_oTreeGrid.GetRowById( oRow["id"] ), sColId);
                        } else {
                            oTmpData[sColId] = new Date(av_oTreeGrid.GetRowById( oRow["id"] )[sColId]).format(sDateFormat);
                        }
                        
                    } else {
                        // - 그외의 경우에는 Object 형식으로 가져와서, String으로 변환해서 처리. 단, undefined/null은 공백처리
                        if(av_oTreeGrid.GetRowById( oRow["id"] )[sColId] == null || av_oTreeGrid.GetRowById( oRow["id"] )[sColId] == undefined) {
                            oTmpData[sColId] = "";
                        } else {
                            oTmpData[sColId] = String( av_oTreeGrid.GetRowById( oRow["id"] )[sColId] );
                        }
                    }
                }
            }
            oTmpInfo["ChangeColIds"] = arrChangeColIds;
            oTmpData["id"] = oRow["id"];
            oTmpData["action"] = sTmpAction;
    
            arrChangedRowInfo.push(oTmpInfo);
            arrChangedRowData.push(oTmpData);
        }
        //console.log( arrChangedRowInfo );
        //console.log( arrChangedRowData );
        oGridChangedInfo["ROW_INFO"] = arrChangedRowInfo;
        oGridChangedInfo["ROW_DATA"] = arrChangedRowData;
    } catch(e) {
		console.log(e);
	}
    
    return oGridChangedInfo;
}

/**
 * 트리그리드에서 정의된 컬럼ID를 배열로 반환한다.
 * 
 * ex) cfn_getGridColInfo(Grids[fv_sGridId])
 *     -> [ MFG_LINE_ALTER, MFG_CODE, MFG_NAME, STR_LINE_CODE, STR_LINE_NAME, END_LINE_CODE, END_LINE_NAME, ALTERNATE, DESCRIPTION, STD_CAPA, EFC_RATE, USE_YN ]
 * 
 * @param av_oTreeGrid
 * @return [컬럼ID]
 */
function cfn_getGridColInfo(av_oTreeGrid) {
    // - 컬럼 정보 추출
    var arrColList = av_oTreeGrid.GetCols();
    for(var c = arrColList.length-1 ; c > -1 ; c--) {
        // - 불필요한 정보는 제거
        if(arrColList[c] == "Panel" || arrColList[c] == "_ConstWidth") {
            arrColList.splice(c,1);
        }
    }
    return arrColList;
}



/**
 * 트리그리드의 데이터를 반환한다.
 * 
 * 각 컬럼ID    : 트리그리드에 정의된 컬럼ID와 Value. ex) [ MFG_LINE_ALTER, MFG_CODE, MFG_NAME, STR_LINE_CODE, STR_LINE_NAME, END_LINE_CODE, END_LINE_NAME, ALTERNATE, DESCRIPTION, STD_CAPA, EFC_RATE, USE_YN ]
 * 
 * ex) cfn_getGridData(Grids[fv_sGridId])
 *     -> [ {각 컬럼의 값들} ]
 * 
 * @param av_oTreeGrid
 * @param av_arrIncludeFixed 포함할 Row의 "Fixed"속성값 리스트. 기본은 [""]. ex) ["", "Foot", "Head", "Solid"]
 *                           "Fixed"속성이 있는 Row : "Foot", "Head", "Solid"와 같은 특수한 Row만 해당. 기본 바디영역의 Row에서는 "Fixed" 값이 없음.
 * @return [{컬럼ID : Value}]
 */
function cfn_getGridData(av_oTreeGrid, av_arrIncludeFixed) {
    var arrGridData = [];
    if(av_oTreeGrid == undefined) return arrGridData;
    
    // - 포함할 Fixed 속성 리스트 초기화
    if(av_arrIncludeFixed == undefined) {
        av_arrIncludeFixed = [""];
    }
    
    // - 그리드의 컬럼ID 정보 가져오기
    var arrColList = cfn_getGridColInfo(av_oTreeGrid);
    
    try {
        // - 모든 행의 데이터 수집
        for(var sRowId in av_oTreeGrid.Rows) {
            
            // - Data영역만 체크
            if(sRowId == undefined || sRowId == "" || av_oTreeGrid.GetRowById( sRowId )["Kind"] != "Data") continue;
            // - Fixed 속성 체크
            var bIncludeByFixed = false;
            for(var k = 0 ; k < av_arrIncludeFixed.length ; k++) {
                if(cfn_defaultValue(av_oTreeGrid.GetRowById( sRowId )["Fixed"]) == av_arrIncludeFixed[k]) {
                    bIncludeByFixed = true;
                    k = av_arrIncludeFixed.length;
                }
            }
            if(!bIncludeByFixed) continue;
            
            var oTmpData = {};
            var sTmpAction = ""; // - delete, insert, update
            
            for(var c = 0 ; c < arrColList.length ; c++) {
                var sColId = arrColList[c];
                if(String(av_oTreeGrid.Cols[sColId]["Type"]).toUpperCase() == String("Enum").toUpperCase()
                        || !cfn_isNull(String(av_oTreeGrid.Cols[sColId]["SuggestType"]))
                  ) {
                    // - Enum, Suggest의 경우에는 그냥 가져와야 제대로 가져온다.
                    oTmpData[sColId] = String( av_oTreeGrid.GetRowById( sRowId )[sColId] );

                } else if(String(av_oTreeGrid.Cols[sColId]["Type"]).toUpperCase() == String("Date").toUpperCase()) {
                    // - Date의 경우, String 형식으로 변환해서 가져온다. 즉, Format형식으로 가져온다.
                    var sDateFormat = av_oTreeGrid.GetFormat(av_oTreeGrid.GetRowById( sRowId ), sColId);
                    if(sDateFormat == undefined || sDateFormat == null) {
                        oTmpData[sColId] = av_oTreeGrid.GetString(av_oTreeGrid.GetRowById( sRowId ), sColId);
                    } else {
                        oTmpData[sColId] = new Date(av_oTreeGrid.GetRowById( sRowId )[sColId]).format(sDateFormat);
                    }
                    
                } else {
                    // - 그외의 경우에는 Object 형식으로 가져와서, String으로 변환해서 처리. 단, undefined/null은 공백처리
                    if(av_oTreeGrid.GetRowById( sRowId )[sColId] == null || av_oTreeGrid.GetRowById( sRowId )[sColId] == undefined) {
                        oTmpData[sColId] = "";
                    } else {
                        oTmpData[sColId] = String( av_oTreeGrid.GetRowById( sRowId )[sColId] );
                    }
                }
            }
            
            if (av_oTreeGrid.GetRowById( sRowId )["Added"] == "1") {
                sTmpAction = "insert";
            } else if(av_oTreeGrid.GetRowById( sRowId )["Deleted"] == "1") {
                sTmpAction = "delete";
            } else if(av_oTreeGrid.GetRowById( sRowId )["Changed"] == "1") {
                sTmpAction = "update";
            }
            
            oTmpData["id"] = sRowId;
            oTmpData["action"] = sTmpAction;
            
            arrGridData.push(oTmpData);
        }
    } catch(e) {}
    
    return arrGridData;
}


/**
 * 트리그리드의 특정 RowId 데이터를 반환한다.
 * 각 컬럼ID    : 트리그리드에 정의된 컬럼ID와 Value. ex) [ MFG_LINE_ALTER, MFG_CODE, MFG_NAME, STR_LINE_CODE, STR_LINE_NAME, END_LINE_CODE, END_LINE_NAME, ALTERNATE, DESCRIPTION, STD_CAPA, EFC_RATE, USE_YN ]
 * 
 * ex) cfn_getGridDataByRowId(Grids[fv_sGridId], "AR1")
 *     -> {각 컬럼의 값들}
 * 
 * @param av_oTreeGrid
 * @param av_sRowId 트리그리드의 RowId
 * @return {컬럼ID : Value}
 */
function cfn_getGridDataByRowId(av_oTreeGrid, av_sRowId) {
    var oRowData = {};
    if(av_oTreeGrid == undefined || av_sRowId == undefined || av_sRowId == "") return oRowData;
    
    // - 그리드의 컬럼ID 정보 가져오기
    var arrColList = cfn_getGridColInfo(av_oTreeGrid);
    
    try {
        // - 데이터 수집
        var oRow = av_oTreeGrid.GetRowById( av_sRowId );
        if(oRow == undefined) return oRowData;
        for(var c = 0 ; c < arrColList.length ; c++) {
            var sColId = arrColList[c];
            if(String(av_oTreeGrid.Cols[sColId]["Type"]).toUpperCase() == String("Enum").toUpperCase()
                    || !cfn_isNull(String(av_oTreeGrid.Cols[sColId]["SuggestType"]))
              ) {
                // - Enum, Suggest의 경우에는 그냥 가져와야 제대로 가져온다.
                oRowData[sColId] = String( oRow[sColId] );

            } else if(String(av_oTreeGrid.Cols[sColId]["Type"]).toUpperCase() == String("Date").toUpperCase()) {
                // - Date의 경우, String 형식으로 변환해서 가져온다. 즉, Format형식으로 가져온다.
                var sDateFormat = av_oTreeGrid.GetFormat(oRow, sColId);
                if(sDateFormat == undefined || sDateFormat == null) {
                    oRowData[sColId] = av_oTreeGrid.GetString(oRow, sColId);
                } else {
                    oRowData[sColId] = new Date(oRow[sColId]).format(sDateFormat);
                }
                
            } else {
                // - 그외의 경우에는 Object 형식으로 가져와서, String으로 변환해서 처리. 단, undefined/null은 공백처리
                if(oRow[sColId] == null || oRow[sColId] == undefined) {
                    oRowData[sColId] = "";
                } else {
                    oRowData[sColId] = String( oRow[sColId] );
                }
            }
        }
        oRowData["id"] = av_sRowId;
    } catch(e) {}
    
    return oRowData;
}

/**
 * 트리그리드의 oRow 리스트 데이터를 반환한다.
 * 
 * 각 컬럼ID    : 트리그리드에 정의된 컬럼ID와 Value. ex) [ MFG_LINE_ALTER, MFG_CODE, MFG_NAME, STR_LINE_CODE, STR_LINE_NAME, END_LINE_CODE, END_LINE_NAME, ALTERNATE, DESCRIPTION, STD_CAPA, EFC_RATE, USE_YN ]
 * 
 * ex) cfn_getGridDataByRowList(Grids[fv_sGridId], arrSelectedRow)
 *     -> [{각 컬럼의 값들}]
 * 
 * @param av_oTreeGrid
 * @param av_arrRowList [트리그리드의 Row Object]
 * @return [{컬럼ID : Value}]
 */
function cfn_getGridDataByRowList(av_oTreeGrid, av_arrRowList) {
    var arrRowData = [];
    if(av_oTreeGrid == undefined || av_arrRowList == undefined || av_arrRowList.length < 1) return arrRowData;
    
    // - 그리드의 컬럼ID 정보 가져오기
    var arrColList = cfn_getGridColInfo(av_oTreeGrid);
    
    try {
        // - 데이터 수집
        for(var r = 0 ; r < av_arrRowList.length ; r++) {
            var oRow = av_arrRowList[r];
            var oRowData = {};
            if(oRow == undefined) continue;
            for(var c = 0 ; c < arrColList.length ; c++) {
                var sColId = arrColList[c];
                if(String(av_oTreeGrid.Cols[sColId]["Type"]).toUpperCase() == String("Enum").toUpperCase()
                        || !cfn_isNull(String(av_oTreeGrid.Cols[sColId]["SuggestType"]))
                  ) {
                    // - Enum, Suggest의 경우에는 그냥 가져와야 제대로 가져온다.
                    oRowData[sColId] = String( oRow[sColId] );

                } else if(String(av_oTreeGrid.Cols[sColId]["Type"]).toUpperCase() == String("Date").toUpperCase()) {
                    // - Date의 경우, String 형식으로 변환해서 가져온다. 즉, Format형식으로 가져온다.
                    var sDateFormat = av_oTreeGrid.GetFormat(oRow, sColId);
                    if(sDateFormat == undefined || sDateFormat == null) {
                        oRowData[sColId] = av_oTreeGrid.GetString(oRow, sColId);
                    } else {
                        oRowData[sColId] = new Date(oRow[sColId]).format(sDateFormat);
                    }
                    
                } else {
                    // - 그외의 경우에는 Object 형식으로 가져와서, String으로 변환해서 처리. 단, undefined/null은 공백처리
                    if(oRow[sColId] == null || oRow[sColId] == undefined) {
                        oRowData[sColId] = "";
                    } else {
                        oRowData[sColId] = String( oRow[sColId] );
                    }
                }
            }
            oRowData["id"] = oRow["id"];
            arrRowData.push(oRowData);
        }
    } catch(e) {}
    
    return arrRowData;
}

/**
 * 트리그리드의 타입(Kind)에 따른 ROW ID 리스트를 반환한다.
 * ex) cfn_getGridRowIdList(Grids[fv_sGridId], "Data")
 *     -> [ RowId ]
 *     
 * @param av_sKind           : Header, Data
 * @param av_bVisible        : true, false, all(값이 비어있을경우)
 * @param av_arrIncludeFixed : 포함할 Row의 "Fixed"속성값 리스트. 기본은 [""]. ex) ["", "Foot", "Head", "Solid"]
 *                             "Fixed"속성이 있는 Row : "Foot", "Head", "Solid"와 같은 특수한 Row만 해당. 기본 바디영역의 Row에서는 "Fixed" 값이 없음. 
 * @return [RowId]
 */
function cfn_getGridRowIdList(av_oTreeGrid, av_sKind, av_bVisible, av_arrIncludeFixed) {
    var arrGridRowIdList = [];
    if(av_oTreeGrid == undefined) return oGridChangedInfo;
    
    // - 포함할 Fixed 속성 리스트 초기화
    if(av_arrIncludeFixed == undefined) {
        av_arrIncludeFixed = [""];
    }

    try {
        // - 모든 행의 데이터 수집
        for(var sRowId in av_oTreeGrid.Rows) {
            // - Kind 속성 체크
            if(sRowId == undefined || sRowId == "" || av_oTreeGrid.GetRowById( sRowId )["Kind"] != av_sKind) continue;
            
            // - Fixed 속성 체크
            var bIncludeByFixed = false;
            for(var k = 0 ; k < av_arrIncludeFixed.length ; k++) {
                if(cfn_defaultValue(av_oTreeGrid.GetRowById( sRowId )["Fixed"]) == av_arrIncludeFixed[k]) {
                    bIncludeByFixed = true;
                    k = av_arrIncludeFixed.length;
                }
            }
            if(!bIncludeByFixed) continue;

            // - Visible 속성 체크
            if(av_bVisible != undefined && !cfn_isNull(av_bVisible)) {
                if(av_bVisible == true && av_oTreeGrid.GetRowById( sRowId )["Visible"] != 1) {
                    continue;
                } else if(av_bVisible == false && av_oTreeGrid.GetRowById( sRowId )["Visible"] != 0) {
                    continue;
                }
            }
            arrGridRowIdList.push(sRowId);
        }
    } catch(e) {}
    
    return arrGridRowIdList;
}

/**
 * 트리그리드의 특정 Row/Cell에 포커싱을 한다.
 * 참고) 트리그리드의 Focus() 펑션을 바로 호출하면 제대로 먹히지 않고, setTimeout()안에 넣어야 제대로 처리가 된다. 그래서, 별도의 공통함수로 생성
 * 
 * ex) cfn_focusGrid(Grids[fv_sGridId], oRow, null, false); // - focuses the whole row
 * ex) cfn_focusGrid(Grids[fv_sGridId], null, null, false); // - defocuses grid
 * ex) cfn_focusGrid(Grids[fv_sGridId], oRow, "VER_NM", false); // - focuses cell
 * 
 * @param av_oTreeGrid
 * @param av_oRow          : 트리그리드의 Row Object
 * @param av_sColId        : 활성화할 Column ID. 불필요할 경우 null
 * @param av_bActiveEditor : 포커싱을 한 Cell의 Editor를 활성화 여부. 컬럼이 정의된 경우에만 사용 가능
 */
function cfn_focusGrid(av_oTreeGrid, av_oRow, av_sColId, av_bActiveEditor) {
    if(av_oTreeGrid == undefined) return;
    
    setTimeout(function() {
        try {
            av_oTreeGrid.Focus(cfn_isNull(av_oRow) ? null : av_oRow, cfn_isNull(av_sColId) ? null : av_sColId, null, true); // - (TRow row, string col, int pagepos = null, type[ ] rect = null, int type = 0)
            if(!cfn_isNull(av_oRow) && !cfn_isNull(av_sColId) && av_bActiveEditor == true) {
                av_oTreeGrid.StartEdit(null, null, 0, 0); // - (TRow row = null, string col = null, bool empty = 0, bool test = 0)
            }
        } catch(e) {console.log(e);}
    }, 10);
}


/**
 * 값을 체크해서, 비어있으면 초기값을 반환
 * @param sValue
 * @param sInitValue
 * @return 
 */
function cfn_defaultValue(sValue, sInitValue) {
    if(sInitValue == null || sInitValue == undefined) sInitValue = '';
    if(cfn_isNull(sValue)) {
        return sInitValue;
    }
    return sValue;
}

/**
 * 입력값이 null에 해당하는 경우 모두를 한번에 체크한다.
 * @param {object} sValue object
 * @return Boolean : sValue가 undefined, null, NaN, "", Array.length = 0인 경우 = true, 이외의 경우 = false
 */
function cfn_isNull(sValue) {
    if (new String(sValue).valueOf() == "undefined") {
        return true;
    }
    if (sValue == null) {
        return true;
    }
    if ((sValue == "NaN") && (new String(sValue.length).valueOf() == "undefined")) {
        return true;
    }
    if (sValue.length == 0) {
        return true;
    }
    if (sValue.toString() == "") {
        return true;
    }
    if (new String(sValue).trim() == "") {
        return true;
    }
    return false;
}

/**
 * 날짜 형식인지 체크한다.(ex: 2017-05-23)
 * @returns true / false
 */
function cfn_isDate(av_oVal) {
    if(cfn_isNull(av_oVal)) return false;
    if(av_oVal instanceof Date) return true;
    var sVal = new String(av_oVal);
    sVal = sVal.replace(/-/g, ''); // - "-" 제거. ex) '2017-04-01' -> '20170401'
    // - 숫자로만 구성되어있는지 체크
    var numPattern = /([^0-9\.-])/;
    numPattern = sVal.match(numPattern);
    if(numPattern != null) return false;
    // - 숫자에 따른 체크
    if(sVal.length == 8) {
        if(Number(sVal.substring(0,4)) < 2000 || Number(sVal.substring(0,4)) > 2200) return false;
        if(Number(sVal.substring(4,6)) < 1 || Number(sVal.substring(4,6)) > 12) return false;
        if(Number(sVal.substring(6,8)) < 1 || Number(sVal.substring(6,8)) > 31) return false;
        // - 날짜 체크
        var oTmpDate = new Date(Number(sVal.substring(0,4)), Number(sVal.substring(4,6))-1, 1);
        oTmpDate.addDate(0,1,0);
        oTmpDate.addDate(0,0,-1); // - 해당 월의 마지막날짜로 이동
        if(Number(sVal.substring(6,8)) > oTmpDate.getDate()) return false;
    } else {
        return false;
    }
    return true;
}

/**
 * grid data를 설정한다.
 * @param av_oGridData : Data_Script에 명시된 object
 * @param av_arrData : JSON Array 형식의 조회 대상 데이터
 */
function cfn_setGridData(av_oGridData, av_arrData) {
	
	if (!av_oGridData || !typeof av_oGridData === 'object' || !Array.isArray(av_oGridData['Body'])) {
		throw 'invalid gridData Array';
	}
	
	// 초기화
	av_oGridData['Body'] = [];
	if (!av_arrData || !Array.isArray(av_arrData) || av_arrData.length == 0) {
		av_oGridData['Body'].push([]);
	} else {
		av_oGridData['Body'].push(av_arrData);
	}
}

/**
 * 화면 Grid 사이즈(높이/너비) 지정을 위한 function
 * @param av_sGrdDivId 그리드가 위치하는 DIV ID. Default : 'dvGridLay'
 * 
 * @param av_iHeightExceptGrd 화면에서 그리드를 제외한 영역(타이틀,버튼,조회조건 등)의 높이. Default : 160
 * @param av_iMinGrdHeight 화면에서 그리드의 최소 높이. Default : 0
 * 
 * @param av_iWidthExceptGrd 화면에서 그리드를 제외한 영역의 너비. Default : 20
 * @param av_iMinGrdWidth 그리드의 최소 너비. Default : 0
 */
function cfn_setSize(av_sGrdDivId, av_iHeightExceptGrd, av_iMinGrdHeight, av_iWidthExceptGrd, av_iMinGrdWidth) {
    if(av_sGrdDivId == null || av_sGrdDivId == undefined || av_sGrdDivId == "") av_sGrdDivId = "dvGridLay";
    
	var ySize;
	try {
		ySize = $(window).height();
	} catch(e) {
		ySize = window.innerHeight;
	}
	
    if(av_iHeightExceptGrd == null || av_iHeightExceptGrd == undefined) av_iHeightExceptGrd = 160;
    if(av_iMinGrdHeight == null || av_iMinGrdHeight == undefined) av_iMinGrdHeight = 0;
    if(av_iMinGrdHeight < (ySize - av_iHeightExceptGrd)) {
        document.getElementById(av_sGrdDivId).style.height = (ySize - av_iHeightExceptGrd) + "px";
    } else {
        document.getElementById(av_sGrdDivId).style.height = av_iMinGrdHeight + "px";
    }
    
    var xSize;
    try {
        xSize = $(window).width();
    } catch(e) {
        xSize = window.innerWidth;
    }
    
    if(av_iWidthExceptGrd == null || av_iWidthExceptGrd == undefined) av_iWidthExceptGrd = 20;
    if(av_iMinGrdWidth == null || av_iMinGrdWidth == undefined) av_iMinGrdWidth = 0;
    if(av_iMinGrdWidth < (xSize - av_iWidthExceptGrd)) {
        document.getElementById(av_sGrdDivId).style.width = (xSize - av_iWidthExceptGrd) + "px";
    } else {
        document.getElementById(av_sGrdDivId).style.width = av_iMinGrdWidth + "px";
    }

}

/**
 * jquery를 활용해서, 파일을 다운로드 받는다.
 * ex) $.download('testExcelDownload.do',{find:'commoncode'},'post');
 */
function cfn_fileDownload(av_url, av_data, method){
	// url과 data를 입력받음
	if( av_url && av_data ) {
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
	
		// - 엑셀다운용 프레임 생성
        var iframe;
        if(document.getElementById("iframe_exceldown") == undefined || document.getElementById("iframe_exceldown") == null) {
            iframe = document.createElement("iframe");
            iframe.id = "iframe_exceldown";
            iframe.name = "iframe_exceldown";
            iframe.style.display = "none";
            document.body.appendChild(iframe);
        }

		// - 엑셀다운용 폼 생성
        var f;
        f  = document.createElement("form");
        f.setAttribute("method","post");
        f.setAttribute("action",av_url);
        f.setAttribute("target","iframe_exceldown");

		var hiddenInput = document.createElement("input");
		hiddenInput.setAttribute("type", "hidden")
        hiddenInput.setAttribute("name", "_csrf");
        hiddenInput.setAttribute("value", token);
		f.appendChild(hiddenInput);
		
		hiddenInput = document.createElement("input");
		hiddenInput.setAttribute("type", "hidden")
        hiddenInput.setAttribute("name", "_csrf_header");
        hiddenInput.setAttribute("value", header);
		f.appendChild(hiddenInput);

		// 파라미터를 form의 input으로 만든다.
        if(typeof av_data == "string") {
            jQuery.each(av_data.split("&"), function() {
                var inp = document.createElement("input");
                inp.setAttribute("type","hidden");
                inp.setAttribute("name", this.split("=")[0]);
                inp.setAttribute("value", this.split("=")[1]);
                f.appendChild(inp); 
            });
        } else {
            jQuery.each(av_data, function(sKey, oVal) {
                var inp = document.createElement("input");
                inp.setAttribute("type","hidden"); 
                inp.setAttribute("name", sKey);
                inp.setAttribute("value", oVal);
                f.appendChild(inp);
            });
        }

        // request를 보낸다.
        document.body.appendChild(f);
        f.submit();
	}
}


// Progress Image Show
function cfn_showProgress() {
    if(document.getElementById('dvPg')) return;

    var dvProgress = document.createElement('DIV');
    dvProgress.id = 'dvPg';
    dvProgress.style.position= 'absolute';
    dvProgress.style.top = '0';
    dvProgress.style.width = '100%';
    dvProgress.style.height = '100%';
    dvProgress.style.opacity = '1';
    dvProgress.style.zIndex = '999';

    var img = document.createElement('img');
    img.src = '../resources/image/etc/Reload05.gif';
    img.style.position= 'absolute';
    img.style.display = 'block';
    img.style.width = '200px';
    img.style.height = '200px';
    img.style.top = '40%';
    img.style.marginLeft = '40%';
    
    try {
        document.getElementsByTagName("body")[0].appendChild(dvProgress);
        dvProgress.appendChild(img);
    } catch(e) {}
}

// Progress Image Hide
function cfn_hideProgress() {
    if(document.getElementById('dvPg')) {
        var dvProgress = document.getElementById('dvPg');
        try {
            dvProgress.style.display = "none";
            document.getElementsByTagName("body")[0].removeChild(dvProgress);
        } catch(e) {}
    }
}

/**
 * GET방식으로 값을 넘길 때 처리
 * @param av_sValue         변환할 값
 * @param av_bIsAllToEmpty  "!ALL"값을 ""(공백)으로 변환할지 여부. true(공백으로 변환. Default값), false(변환하지 않고 그대로 넘긴다)
 * @return 
 */
function cfn_setGetValue(av_sValue, av_bIsAllToEmpty) {
    if(av_bIsAllToEmpty == undefined || av_bIsAllToEmpty != false) {
        av_bIsAllToEmpty = true;
    }
    
    if(av_sValue == null) {
        return "";
    }
    if(av_sValue == undefined) {
        return "";
    }
    if(av_sValue == "!ALL" && av_bIsAllToEmpty) {
        return "";
    }
    return encodeURIComponent(av_sValue);
}








/********************************************
아래는 String 관련 함수
*********************************************/

/************
* number_format()
* PHP의 number_format과 똑같은 효과를 낸다.
*
* decimals : 표시할 소수점 자리수(버림으로 처리됨)
* dec_point : 소수점 표시단어
* thousands_sep : 1000자리 표시단어
*
* ex>
* "123456.98765".number_format(4,'.',',');
* String("123456.98765").number_format(4);
* number_format("123456.98765");
* -> 123,456.9876
*
* 123456.98765.number_format(4);
* -> 123,456.9876
* -123456.98765.number_format(4);
* -> NaN
* Number('-123456.98765').number_format(4);
* -> -123,456.9876
****************************************************************/
String.prototype.number_format = function(decimals,dec_point,thousands_sep){
    if(decimals==null){decimals=999;}   
    if(dec_point==null){dec_point='.';}if(thousands_sep==null){thousands_sep=',';}
    var arr = this.toString().replace(/[^-\.\+\d]/g,'').split(dec_point);
    if(arr[1] && arr[1].length>0){arr[1] = arr[1].substr(0,decimals);}
    arr[0] = arr[0].replace(/(\d)(?=(?:\d{3})+(?!\d))/g,'$1'+thousands_sep);
    if(arr[1] && decimals>0 && arr[1].length>0){return arr[0] + dec_point + arr[1];}
    else {return arr[0];}
}

/************
 * 해당 스트링이나 숫자를 반올림한다.
 * @param pov 출력할 소수점 이하 자리수
 * @return 숫자이면 콤마 제거한 숫자, 아니면 0, null이면 0
 ****************************************************************/
String.prototype.round = function (pos) {
    if(this.isNumber()) {
        return Math.round(Number(this) * Math.pow(10, Math.abs(pos)-1))/Math.pow(10, Math.abs(pos)-1);
    }
    return this;
}

/************
 * 해당 스트링를 숫자로 변환하여 리턴
 * @param 
 * @return 숫자이면 콤마제거한 숫자, 아니면 0, null이면 0
 ****************************************************************/
String.prototype.getNumber = function () {
    if(this == null) return 0;
    var srcVal = String(this);
    if(!String(srcVal.delComma()).isNumber()) return 0;
    return srcVal == "" ?  0 : Number(srcVal.delComma());
}

/************
 * 해당 스트링이 숫자로만 구성되었는지 검사
 * @param 
 * @return 숫자이면 true, 아니면 false 
 ****************************************************************/
String.prototype.isNumber = function (){
    if(this == "") return false;
    var numPattern = /([^0-9\.-])/;
    numPattern = this.match(numPattern);
    if(numPattern != null) return false;
    else return true;
} 

/************
 * 숫자에 세자리마다 콤마를 찍어서 리턴
 * @param 
 * @return 숫자이면 true, 아니면 false 
 ****************************************************************/
String.prototype.setComma = function (){
    var srcVal = String(this.strip().getNumber());
    var re0 = /^(-?\d+)(\d{3})($|\..*$)/;
    if (re0.test(srcVal)) return srcVal.replace(re0, function(str,p1,p2,p3) {return p1.setComma() + "," + p2 + p3;});
    else return srcVal;
}

/************
 * 해당 스트링에서 콤마를 제거한 문자를 리턴
 * @param 
 * @return 
 ****************************************************************/
String.prototype.delComma = function (){
    var srcVal = String(this.strip());
    if (srcVal == '') return '';
    else if (srcVal == '-') return '-';
    else if (srcVal == '0-') return '-0';
    srcVal = srcVal.replace(/[^\d\.-]/g,'');
    srcVal = String(srcVal.match(/^-?\d*\.?\d*/));
    srcVal = srcVal.replace(/^(-?)(\d*)(.*)/, function(str,p1,p2,p3) {p2 = (p2>0) ? String(parseInt(p2,10)) : '0'; return p1 + p2 + p3;});
    if(srcVal.isNumber()) return Number(srcVal);
    else return 0;
}

/************
 * 해당 스트링에서 <,>,",'를 특수기호로 변환
 * @param 
 * @return string
 ****************************************************************/
String.prototype.htmlChars = function () {
    var str = ((this.replace('"', '&amp;')).replace('"', '&quot;')).replace('\'', '&#39;');
    return (str.replace('<', '&lt;')).replace('>', '&gt;');
}

/************
 * 좌우 공백없애는 함수
 * @param 
 * @return string
 ****************************************************************/
String.prototype.trim = function () { return this.replace(/(^\s*)|(\s*$)/g, ""); }

/************
 * 왼쪽 공백없애는 함수
 * @param 
 * @return string
 ****************************************************************/
String.prototype.ltrim = function () { return this.replace(/^\s*/g, ""); }

/************
 * 오른쪽 공백없애는 함수
 * @param 
 * @return string
 ****************************************************************/
String.prototype.rtrim = function () { return this.replace(/\s*$/g, ""); }

/************
 * 오른쪽 공백없애는 함수
 * @param 
 * @return string
 ****************************************************************/
String.prototype.strip = function () {  
    return this.replace("&nbsp;", "").trim(); 
}

/************
 * 오른쪽 공백없애는 함수
 * @param 
 * @return string
 ****************************************************************/
String.prototype.count = function (char) { 
    var chars = this.split(char);
    return chars.length;
}

/************
 * HTML Tag제거
 * @param 
 * @return string
 ****************************************************************/
String.prototype.stripTags = function () {
    var str = this;
    var pos1 = str.indexOf('<');

    if (pos1 == -1) return str;
    else {
        var pos2 = str.indexOf('>', pos1);
        if (pos2 == -1) return str;
        return (str.substr(0, pos1) + str.substr(pos2+1)).stripTags();
    }
}

/************
 * 대소문자 구별하지 않고 단어 위치 찾기
 * @param 
 * @return string
 ****************************************************************/
String.prototype.startPos = function (needle, offset) {
    var offset = (typeof offset == "number")?offset:0;
    return this.toLowerCase().indexOf(needle.toLowerCase(), offset);
}

/************
 * 대소문자 구별하지 않고 뒤에서부터 단어위치 찾기
 * @param 
 * @return string
 ****************************************************************/
String.prototype.lastPos = function (needle, offset) {
    var offset = (typeof offset == "number")?offset:0;
    return this.toLowerCase().lastIndexOf(needle.toLowerCase(), offset);
}

/************
 * 문자열을 배열로
 * @param 
 * @return string
 ****************************************************************/
String.prototype.toArray = function () {
    var len = this.length;
    var arr = new Array;
    for (var i=0; i<len; i++) arr[i] = this.charAt(i);
    return arr;
}