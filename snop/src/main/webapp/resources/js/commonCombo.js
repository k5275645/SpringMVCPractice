/*************************************
 * 다중선택 콤보를 위한 기본 세팅
 *************************************/
$(function () {
	$('select[multiple].active.common').multiselect({
		columns: 1,
		placeholder: '전체',
		search: true,
		searchOptions: {
			'default': '검색'
		},
        // plugin texts
        texts: {
            selectAll      : '전체선택',     // select all text
            unselectAll    : '전체해제',   // unselect all text
        },
		maxWidth:250,
		maxPlaceholderWidth:250,
		selectAll: true
	});
});

/*************************************
 * 넘겨받은 데이터를 이용하여 콤보 구성
 * 2021-08-09 김태환 av_sDefKey(콤보박스의 초기값) 항목 추가
 *************************************/
function cfn_setCombo(av_oTarget, av_oData, av_bAllFlag, av_sDefKey){
	av_oTarget.find("option").remove();
	
	if(av_oData['RESULT'] == 'NO_DATA') return;
	
	var comboCdList = av_oData['BODY'];
	
	if(comboCdList){
		
		if(av_bAllFlag)	av_oTarget.append("<option value='!ALL'>전체</option>");
		
		for(var i=0; i<comboCdList.length; i++){
			/* 초기값 지정을 위한 로직 추가 */
			if (comboCdList[i]['CODE'] === av_sDefKey) {
				av_oTarget.append("<option value='" + comboCdList[i]['CODE'] + "' selected>" + comboCdList[i]['NAME'] + "</option>");
			} else {
				av_oTarget.append("<option value='" + comboCdList[i]['CODE'] + "'>" + comboCdList[i]['NAME'] + "</option>");
			}
			//av_oTarget.append("<option value='"+comboCdList[i].CODE+"'>"+comboCdList[i].NAME+"</option>");
			/*if((i+1) == comboCdList.length){
				av_oTarget.append("<option value='"+comboCdList[i].CODE+"' selected>"+comboCdList[i].NAME+"</option>");
			}else{
				av_oTarget.append("<option value='"+comboCdList[i].CODE+"'>"+comboCdList[i].NAME+"</option>");
			}*/
		}
	}
	
	
}

/*************************************
 * 공통사용 combo 설정
 *************************************/
/**
 * 공통 콤보 구성
 * 
 * @param av_oTarget       콤보박스를 구성할 대상 Select Object(Select Tag)
 * @param av_sCbId         공통콤보박스 구분 : year, month, liquor, brand, usage, vessel, volume, ...
 * @param av_sDefKey       콤보박스의 초기값
 * @param av_bAllFlag      전체 포함 여부 : true(전체 포함), false(전체 미포함)
 * @param av_oCallbackFn   콤보박스 구성 후, 호출할 펑션. 펑션명이나 펑션Object 둘다 허용.
 * @param av_oOptionParam  추가로 넘길 파라메터 Object : ex) { hasCommon(공통 포함 여부 : Y, N) }
 * @param av_oCallbackParam  콤보박스 구성 후, 호출할 펑션에 넘길 파라메터 Object
 * 
 */
function cfn_getCombo(av_oTarget, av_sCbId, av_sDefKey, av_bAllFlag, av_oCallbackFn, av_oOptionParam, av_oCallbackParam) {
	
	switch(av_sCbId){
		// 년
		case 'year' :
			var nowYear = new Date().getFullYear().toString();
			if (!av_sDefKey) {
				av_sDefKey = nowYear;
			}
			
			return cfn_searchCombo(av_oTarget, '/snop/commonCombo/getYearComboList', av_sDefKey, false, av_oCallbackFn, av_oOptionParam, av_oCallbackParam);
		
		// 월
		case 'month' :
			var nowMonth = (new Date().getMonth() + 1).toString();
			nowMonth = (nowMonth < 10) ? '0' + nowMonth : nowMonth;
			if (!av_sDefKey) {
				av_sDefKey = nowMonth;
			}

			return cfn_searchCombo(av_oTarget, '/snop/commonCombo/getMonthComboList', av_sDefKey, false, av_oCallbackFn, av_oOptionParam, av_oCallbackParam);
		
		// 사업구분
		case 'liquor' :
			return cfn_searchCombo(av_oTarget, '/snop/commonCombo/getLiquorComboList', av_sDefKey, av_bAllFlag, av_oCallbackFn, av_oOptionParam, av_oCallbackParam);
		
		// 브랜드
		case 'brand' :
			return cfn_searchCombo(av_oTarget, '/snop/commonCombo/getBrandComboList', av_sDefKey, av_bAllFlag, av_oCallbackFn, av_oOptionParam, av_oCallbackParam);
		
		// 용도
		case 'usage' :
			return cfn_searchCombo(av_oTarget, '/snop/commonCombo/getUsageComboList', av_sDefKey, av_bAllFlag, av_oCallbackFn, av_oOptionParam, av_oCallbackParam);
		
		// 용기
		case 'vessel' :
			return cfn_searchCombo(av_oTarget, '/snop/commonCombo/getVesselComboList', av_sDefKey, av_bAllFlag, av_oCallbackFn, av_oOptionParam, av_oCallbackParam);
		
		// 용량
		case 'volume' :
			return cfn_searchCombo(av_oTarget, '/snop/commonCombo/getVolumeComboList', av_sDefKey, av_bAllFlag, av_oCallbackFn, av_oOptionParam, av_oCallbackParam);
			
		// 용기관리 품목
		case 'vesselItem' :
			return cfn_searchCombo(av_oTarget, '/snop/common/getVesselItem', av_sDefKey, av_bAllFlag, av_oCallbackFn, av_oOptionParam, av_oCallbackParam);
			
		// 용기관리 용기
		case 'vesselCode' :
			return cfn_searchCombo(av_oTarget, '/snop/common/getVesselCode', av_sDefKey, av_bAllFlag, av_oCallbackFn, av_oOptionParam, av_oCallbackParam);
			
		// 용기관리 용량
		case 'vesselVolume':
			return cfn_searchCombo(av_oTarget, '/snop/common/getVesselVolume', av_sDefKey, av_bAllFlag, av_oCallbackFn, av_oOptionParam, av_oCallbackParam);
			
		// 용기관리 브랜드
		case 'vesselBrand' :
			return cfn_searchCombo(av_oTarget, '/snop/common/getVesselBrand', av_sDefKey, av_bAllFlag, av_oCallbackFn, av_oOptionParam, av_oCallbackParam);

        // 조직
        case 'org' :
            return cfn_searchCombo(av_oTarget, '/snop/common/getOrg', av_sDefKey, av_bAllFlag, av_oCallbackFn, av_oOptionParam, av_oCallbackParam);

        // 기준판매정의
        case 'stdSaleDfn' :
            return cfn_searchCombo(av_oTarget, '/snop/common/getStdSaleDfntList', av_sDefKey, av_bAllFlag, av_oCallbackFn, av_oOptionParam, av_oCallbackParam);
			
		// 공통
		case 'common' :
			return cfn_searchCombo(av_oTarget, '/snop/commonCombo/getComCodeCombo', av_sDefKey, av_bAllFlag, av_oCallbackFn, av_oOptionParam, av_oCallbackParam);

        // 품목 중분류
        case 'itemSegment2' :
            return cfn_searchCombo(av_oTarget, '/snop/common/getItemSegment2List', av_sDefKey, av_bAllFlag, av_oCallbackFn, av_oOptionParam, av_oCallbackParam);
 
        // 품목 소분류
        case 'itemSegment3' :
            return cfn_searchCombo(av_oTarget, '/snop/common/getItemSegment3List', av_sDefKey, av_bAllFlag, av_oCallbackFn, av_oOptionParam, av_oCallbackParam);
	}
}

/**
 * 콤보박스 조회 후 구성
 * 
 * @param av_oTarget  	 	콤보박스를 구성할 대상 DOM jquery object
 * @param av_sUrl   	 	공통콤보박스 구분 : year, month, liquor, brand, usage, vessel, volumne..
 * @param av_sDefKey 	 	선택할 초기값
 * @param av_bAllFlag      	전체 포함 여부 : true(전체 포함), false(전체 미포함), default : false
 * @param av_oCallbackFn 	콤보박스 구성 후 실행할 콜백함수. 함수만 허용
 * @param av_oOptionParam	추가로 넘길 파라메터 Object : ex) { hasCommon(공통 포함 여부 : Y, N) }
 * @param av_oCallbakParam  콜백함수에 추가적으로 전달할 paramter
 * @return $.ajax
 */
function cfn_searchCombo(av_oTarget, av_sUrl, av_sDefKey, av_bAllFlag, av_oCallbackFn, av_oOptionParam, av_oCallbakParam) {

	if (!av_sUrl) {
		throw 'url cannot be null';
	}
	
	var isAll = false;
	if (av_bAllFlag === true) {
		isAll = true;
	}

    // - 추가 파라메터 처리
    if(!av_oOptionParam) {
        av_oOptionParam = { 'uid' : new Date().getTime() };
    } else if(av_oOptionParam["uid"] == undefined || av_oOptionParam["uid"] == "" || av_oOptionParam["uid"] == null) {
        av_oOptionParam["uid"] = new Date().getTime();
    }

	return $.ajax({
		type : 'GET',
		url  : av_sUrl,
		data : av_oOptionParam,
		dataType : 'json',
		async : true,
		success : function(data) {
			
			if (!av_oTarget || !av_oTarget instanceof $ || av_oTarget['length'] === 0) {
				return;
			}
			
			av_oTarget.find("option").remove();
			
        	if (data) {

				if (isAll) {
				    var sAllDesc = "전체";
					av_oTarget.append("<option value='!ALL'>" + sAllDesc + "</option>");
				}

				for (var i in data) {

					if (data[i]['CODE'] === av_sDefKey) {
						av_oTarget.append("<option value='" + data[i]['CODE'] + "' selected>" + data[i]['NAME'] + "</option>");
					} else {
						av_oTarget.append("<option value='" + data[i]['CODE'] + "'>" + data[i]['NAME'] + "</option>");
					}
				}
				
				// - 다중 선택 콤보박스인 경우 처리
            	try {
	                av_oTarget.multiselect("reset");
            	} catch(e) {

				}
				
				if (typeof av_oCallbackFn === 'function') {
					av_oCallbackFn(av_oCallbakParam);
				}
        	}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log('code : ' + jqXHR.status + ', textStatus : ' + textStatus + ', error : ' + errorThrown);
		}
	});
}