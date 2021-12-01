package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 제품수급 > 일별 제품수급 Simul
 * @author 유기후
 *
 */
@Repository
public interface M02020DaoMapper {
    
    /**
     * 데이터 조회
     * @param params { bssYYYYMM, useYn }
     * @return [{ VER_CD, VER_NM, VER_DESC, STD_YYYYMMDD, STD_YYYYMM, USE_YN, VER_FR_DT, VER_TO_DT, WORK_INFO_10, WORK_INFO_20 }]
     * @throws Exception
     */
    public List<Map<String, Object>> search(Map<String, Object> params) throws Exception;
    
    /**
     * 신규 버전코드 조회
     * @param params { STD_YYYYMMDD }
     * @return [{ NEW_VER_CD }]
     * @throws Exception
     */
    public List<Map<String, Object>> selectNewVerCd(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 저장 : 추가
     * @param params { userId, VER_CD, VER_NM, VER_DESC, STD_YYYYMMDD, USE_YN, VER_FR_DT, VER_TO_DT }
     * @return
     * @throws Exception
     */
    public int insert(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 저장 : 수정
     * @param params { userId, VER_CD, VER_NM, VER_DESC, USE_YN, VER_TO_DT }
     * @return
     * @throws Exception
     */
    public int update(Map<String, Object> params) throws Exception;
    
    /**
     * 판매설정 - 요일별판매비율 데이터 저장 : 버전 생성시, 기준일자(실적) 포함 20일간의 평균으로 계산
     * @param params { userId, NEW_VER_CD }
     * @return
     * @throws Exception
     */
    public int updateSaleSetDowSaleRate(Map<String, Object> params) throws Exception;
    

    
    /**
     * 버전 정보 조회
     * @param params { verCd }
     * @return [{ VER_CD, VER_NM, VER_DESC, STD_YYYYMMDD, STD_YYYYMM, USE_YN, VER_FR_DT, VER_TO_DT, PRDT_VAR_VER_CD, PRDT_VAR_VER_NM, PRDT_VAR_VER_DESC, WORK_INFO_10, WORK_INFO_20, WORK_DT_10, WORK_USER_10, WORK_USER_10_NM, WORK_DT_20, WORK_USER_20, WORK_USER_20_NM }]
     * @throws Exception
     */
    public List<Map<String, Object>> selectVerInfo(Map<String, Object> params) throws Exception;
    
    /**
     * 버전 정보의 작업정보 저장 : 작업에 따라서, liquorCode의 파라메터값은 강제 조정해서 처리
     * @param params { userId, verCd, liquorCode(!ALL, 10, 20) }
     * @return
     * @throws Exception
     */
    public int updateVerWorkInfo(Map<String, Object> params) throws Exception;
    

    
    /**
     * 생산변수 리스트 조회
     * @param params {  }
     * @return [{ PRDT_VAR_VER_CD, PRDT_VAR_VER_NM }]
     * @throws Exception
     */
    public List<Map<String, Object>> selectPrdtVarList(Map<String, Object> params) throws Exception;
    
    /**
     * 생산변수 저장
     * @param params { userId, verCd, prdtVarVerCd }
     * @return
     * @throws Exception
     */
    public int updatePrdtVarVerCd(Map<String, Object> params) throws Exception;


    /**
     * 주간근무일수유형별 근무형태 리스트(주당 생산기준 관리) 조회
     * @param params { verCd }
     * @return [{ WEEK_WORK_DCNT_TP_CODE, WEEK_WORK_DCNT_TP_NAME, SFT_PTRN_DTY_CODE, SFT_PTRN_DTY_NAME, WORK_DCNT, WEEK_WORK_CNT, LIQUOR_CODE }]
     * @throws Exception
     */
    public List<Map<String, Object>> selectSftPtrnDtyByWeekWorkDcntTpList(Map<String, Object> params) throws Exception;

    /**
     * 생산변수의 근무형태 리스트 조회
     * @param params { verCd }
     * @return [{ VER_CD, PRDT_VAR_VER_CD, LIQUOR_CODE, SFT_PTRN_DTY_CODE, SFT_PTRN_DTY_NAME, AVL_HR }]
     * @throws Exception
     */
    public List<Map<String, Object>> selectSftPtrnDtyList(Map<String, Object> params) throws Exception;

    /**
     * 생산설정의 그리드 헤더 조회 : 생산변수의 공장/라인 리스트
     * @param params { verCd, liquorCode }
     * @return [{ VER_CD, STD_YYYYMMDD, VER_FR_DT, VER_TO_DT, PRDT_VAR_VER_CD, PRDT_VAR_VER_NM, PRDT_VAR_VER_DESC, PRDT_VAR_VER_USE_YN, LIQUOR_CODE, ORG_CODE, ORG_NAME, LINE_DEPT_CODE, LINE_DEPT_NAME, NEW_LINE_YN, COL_ID, HEADER_COL_SPAN }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchPrdtSetHeader(Map<String, Object> params) throws Exception;

    /**
     * 생산설정의 그리드 바디 조회 : 버전의 기간 리스트와 헤더의 조합
     * @param params { verCd, liquorCode, TREEGRID_HEADER:[{ ORG_CODE, LINE_DEPT_CODE, COL_ID }] }
     * @return [{ VER_CD, LIQUOR_CODE, LIQUOR_DESC, PERIOD_TYPE, PERIOD_CODE, PERIOD_NAME, BUSINESS_DAY_FLAG, PERIOD_FR_YYYYMMDD, PERIOD_TO_YYYYMMDD, PERIOD_TYPE_SEQ, CALENDAR_WORK_CNT, WORK_CNT, COL_1_SFT_PTRN_DTY_CODE, COL_1_AVL_HR, ... }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchPrdtSetBody(Map<String, Object> params) throws Exception;

    /**
     * 생산설정 데이터 저장 : 일자별 공장/라인의 근무형태/가용시간 삭제(근무형태가 N/A인경우)
     * @param params { updateList:[{ VER_CD, LIQUOR_CODE, ORG_CODE, LINE_DEPT_CODE, PERIOD_TYPE, PERIOD_CODE, SFT_PTRN_DTY_CODE, PERIOD_FR_YYYYMMDD, PERIOD_TO_YYYYMMDD }] }
     * @return
     * @throws Exception
     */
    public int deletePrdtSet(Map<String, Object> params) throws Exception;
    /**
     * 생산설정 데이터 저장 : 일자별 공장/라인의 근무형태/가용시간 저장
     * @param params { userId, updateList:[{ VER_CD, LIQUOR_CODE, ORG_CODE, LINE_DEPT_CODE, PERIOD_TYPE, PERIOD_CODE, SFT_PTRN_DTY_CODE, PERIOD_FR_YYYYMMDD, PERIOD_TO_YYYYMMDD }] }
     * @return
     * @throws Exception
     */
    public int updatePrdtSet(Map<String, Object> params) throws Exception;
    

    /**
     * 생산설정 갱신 초기화 : 허용되지 않는 근무형태와 공장/라인 리스트 삭제
     * @param params { verCd }] }
     * @return
     * @throws Exception
     */
    public int deletePrdtSetRefresh(Map<String, Object> params) throws Exception;
    
    /**
     * 생산설정 갱신 Update : 근무형태에 따른 가용시간과 공장/라인의 명칭 Update
     * @param params { verCd, userId }
     * @return
     * @throws Exception
     */
    public int updatePrdtSetRefresh(Map<String, Object> params) throws Exception;

    

    /**
     * 판매설정의 그리드 헤더 조회 : 선택된 월별 사용판매량으로 계산된 제품 리스트(모제품매핑 처리가 이미 되어있음)
     * @param params { verCd, liquorCode, domExpCode, vesselCode, usageCode }
     * @return [{ VER_CD, LIQUOR_CODE, ITEM_CODE, ITEM_NAME, COL_ID, ITEM_TYPE, ITEM_STATUS, BRAND_CODE, BRAND_NAME, BRAND_SORT_ORDER, VESSEL_CODE, VESSEL_SORT, VESSEL_SORT_ORDER, USAGE_CODE, USAGE_NAME, USAGE_SORT_ORDER, VOLUME_VALUE, UOM_CONVERSION_VALUE }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchSaleSetHeader(Map<String, Object> params) throws Exception;

    /**
     * 판매설정의 그리드 바디 조회 : 버전의 기간 리스트와 헤더의 조합
     * @param params { verCd, liquorCode, TREEGRID_HEADER:[{ ITEM_CODE, COL_ID }] }
     * @return [{ VER_CD, STD_YYYYMMDD, LIQUOR_CODE, LIQUOR_DESC, PERIOD_TYPE, PERIOD_CODE, PERIOD_NAME, PERIOD_DESC, BUSINESS_DAY_FLAG, PERIOD_FR_YYYYMMDD, PERIOD_TO_YYYYMMDD, PERIOD_TYPE_SEQ, CALENDAR_WORK_CNT, WORK_CNT, SALE_TYPE, SALE_TYPE_DESC, COL_1_BF_SALE_VAR_APL_SALE_QTY, COL_1_SALE_QTY, ... }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchSaleSetBody(Map<String, Object> params) throws Exception;
    
    /**
     * 판매설정 데이터 저장 : 일자별 제품의 판매량 변경(수기입력) 저장
     * @param params { userId, updateList:[{ VER_CD, LIQUOR_CODE, PERIOD_TYPE, PERIOD_CODE, ITEM_CODE, SALE_QTY }] }
     * @return
     * @throws Exception
     */
    public int updateSaleSet(Map<String, Object> params) throws Exception;

    
    /**
     * 판매설정 - 사용판매량/실적반영유형 조회
     * @param params { verCd, liquorCode }
     * @return [{ VER_CD, LIQUOR_CODE, LIQUOR_DESC, SALE_SET_TYPE_CODE, SEQNO, USE_SALE_QTY_YYYYMM, USE_SALE_QTY_YYYYMM_DESC, USE_SALE_QTY_VAL, ACTUAL_RFLT_TYPE_VAL }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchUseSaleQtyActualRfltType(Map<String, Object> params) throws Exception;
    
    /**
     * 판매설정 - 사용판매량 저장(추가/수정)
     * @param params { userId, VER_CD, LIQUOR_CODE, SEQNO, USE_SALE_QTY_VAL, USE_SALE_QTY_YYYYMM }
     * @return
     * @throws Exception
     */
    public int updateUseSaleQty(Map<String, Object> params) throws Exception;
    
    /**
     * 판매설정 - 사용판매량 저장(삭제) : 버전이 허용한 기간을 벗어난 것은 삭제
     * @param params { verCd }
     * @return
     * @throws Exception
     */
    public int deleteUseSaleQty(Map<String, Object> params) throws Exception;
    
    /**
     * 판매설정 - 실적반영유형 저장(추가/수정)
     * @param params { userId, VER_CD, LIQUOR_CODE, ACTUAL_RFLT_TYPE_VAL }
     * @return
     * @throws Exception
     */
    public int updateActualRfltType(Map<String, Object> params) throws Exception;

    
    /**
     * 판매설정 - 판매변수 조회
     * @param params { verCd }
     * @return [{ VER_CD, SALE_SET_TYPE_CODE, SALE_SET_TYPE_NAME, SEQNO, SALE_VAR_APL_FR_DT, SALE_VAR_APL_TO_DT, SALE_VAR_DFNT_SEQNO, SALE_VAR_DFNT_SEQNO_NAME, SALE_VAR_VAL, LIQUOR_CODE, LIQUOR_DESC, SALE_VAR_USAGE_NAME, SALE_VAR_ITEM_NAME, SALE_VAR_NAME, SALE_VAR_TYPE, SALE_VAR_USAGE_CODE, SALE_VAR_ITEM_CODE, SALE_VAR_VALID_MSG }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchSaleVar(Map<String, Object> params) throws Exception;
    
    /**
     * 판매설정 - 판매변수 저장(추가/수정)
     * @param params { userId, VER_CD, SEQNO, SALE_VAR_DFNT_SEQNO, SALE_VAR_APL_FR_DT, SALE_VAR_APL_TO_DT }
     * @return
     * @throws Exception
     */
    public int updateSaleVar(Map<String, Object> params) throws Exception;
    
    /**
     * 판매설정 - 판매변수 저장(삭제)
     * @param params { verCd, LIQUOR_CODE, SEQNO }
     * @return
     * @throws Exception
     */
    public int deleteSaleVar(Map<String, Object> params) throws Exception;
    
    /**
     * 판매설정 - 판매변수 저장(동기화 수정) : 판매변수의 상세 속성이 변경되어있으면 반영(화면단에서 변경한 변수는 자동 처리됨)
     * @param params { userId, verCd }
     * @return
     * @throws Exception
     */
    public int updateSaleVarSync(Map<String, Object> params) throws Exception;
    
    /**
     * 판매설정 - 판매변수 저장(동기화 삭제) : 존재하지 않는 변수 삭제
     * @param params { verCd }
     * @return
     * @throws Exception
     */
    public int deleteSaleVarSync(Map<String, Object> params) throws Exception;

    
    /**
     * 판매설정 - 요일별판매비율 조회
     * @param params { verCd }
     * @return [{ VER_CD, LIQUOR_CODE, LIQUOR_DESC, SALE_SET_TYPE_CODE, DOW_TOTAL_VAL, DOW_MON_VAL, DOW_TUE_VAL, DOW_WED_VAL, DOW_THU_VAL, DOW_FRI_VAL }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchDowSaleRate(Map<String, Object> params) throws Exception;
    
    /**
     * 판매설정 - 요일별판매비율 저장(추가/수정) : 사업부문별로 요일이 고정이라, 삭제는 없음
     * @param params { userId, updateList:[{VER_CD, LIQUOR_CODE, DOW_MON_VAL, DOW_TUE_VAL, DOW_WED_VAL, DOW_THU_VAL, DOW_FRI_VAL}] }
     * @return
     * @throws Exception
     */
    public int updateDowSaleRate(Map<String, Object> params) throws Exception;
    
    
    /**
     * 판매설정 - 판매량 계산(생성=추가) : 설정된 사용판매량/실적반영방법/판매변수/요일별판매비율을 이용하여, 일자별 판매량 산출
     * @param params { userId, verCd, liquorCode }
     * @return
     * @throws Exception
     */
    public int updateSale(Map<String, Object> params) throws Exception;
    
    /**
     * 판매설정 - 판매량 초기화 : 삭제
     * @param params { verCd, liquorCode }
     * @return
     * @throws Exception
     */
    public int deleteSale(Map<String, Object> params) throws Exception;



    /**
     * 생산변수의 공장/라인/제품별 단위생산량 조회
     * @param params { verCd }
     * @return [{ PK, VER_CD, PRDT_VAR_VER_CD, ORG_CODE, LINE_DEPT_CODE, ITEM_CODE, QTY_PER_HR }]
     * @throws Exception
     */
    public List<Map<String, Object>> selectPrdtVarDtlList(Map<String, Object> params) throws Exception;

    /**
     * 결과설정의 그리드 헤더 조회 : (시뮬레이션 결과 + 생산변수에 정의된 공장/라인/제품)과 계정의 조합 리스트
     * @param params { verCd, liquorCode, vesselCode }
     * @return [{ COL_GUBUN, COL_ID, COL_VISIBLE, COL_WIDTH, COL_TYPE, COL_FORMAT, COL_ALIGN, COL_CAN_EDIT, COL_SEQ, HEADER1_NM, HEADER1_ROWSPAN, HEADER1_COLSPAN, HEADER2_NM, HEADER2_ROWSPAN, HEADER2_COLSPAN, HEADER3_NM, HEADER3_ROWSPAN, HEADER3_COLSPAN, HEADER4_NM, HEADER4_ROWSPAN, HEADER4_COLSPAN, ITEM_CODE, ITEM_NAME, BRAND_CODE, BRAND_NAME, BRAND_SORT_ORDER, VESSEL_CODE, VESSEL_SORT, VESSEL_SORT_ORDER, USAGE_CODE, USAGE_NAME, USAGE_SORT_ORDER, VOLUME_VALUE, DOM_EXP_CODE, DOM_EXP_FLAG, ORG_CODE, ORG_NAME, LINE_DEPT_CODE, LINE_DEPT_NAME }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchResultHeader(Map<String, Object> params) throws Exception;

    /**
     * 결과설정의 그리드 바디 조회 : 버전의 기간 리스트와 헤더의 조합
     * @param params { verCd, liquorCode, vesselCode, TREEGRID_HEADER:[{ COL_GUBUN, COL_ID, ... }] }
     * @return [{ VER_CD, STD_YYYYMMDD, LIQUOR_CODE, LIQUOR_DESC, PERIOD_TYPE, PERIOD_CODE, PERIOD_NAME, PERIOD_DESC, BUSINESS_DAY_FLAG, PERIOD_FR_YYYYMMDD, PERIOD_TO_YYYYMMDD, PERIOD_TYPE_SEQ, CALENDAR_WORK_CNT, WORK_CNT, COL_1, COL_2, ... }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchResultBody(Map<String, Object> params) throws Exception;
    
    /**
     * 결과설정 데이터 저장 : 제품/공장/라인의 일자별 생산량/사용시간 저장(생산량을 기준으로 사용시간 역산)
     * @param params { userId, updateList:[{ VER_CD, LIQUOR_CODE, PERIOD_TYPE, PERIOD_CODE, ITEM_CODE, ORG_CODE, LINE_DEPT_CODE, PRDT_QTY }] }
     * @return
     * @throws Exception
     */
    public int updateResult(Map<String, Object> params) throws Exception;
    
    /**
     * 시뮬레이션 프로시저 호출 : SCMU.PKG_SOP_DALY_SCM_SIMUL.SP_MAIN_DALY_SCM_SIMUL_F
     * @param params { userId, verCd, liquorCode, O_TOT_ROWCOUNT, O_RETURN_MSG, O_RETURN_STATUS }
     * @return
     * @throws Exception
     */
    public void callDalyScmSimul(Map<String, Object> params) throws Exception;
    
    /**
     * 재고계산 프로시저 호출 : 특정 주차로 계산하기 어려우니, 전체 계획기간으로 재계산 : SCMU.PKG_SOP_DALY_SCM_SIMUL.SP_CALC_STOCK_DALY_SCM_SIMUL_F
     * @param params { userId, verCd, liquorCode, O_TOT_ROWCOUNT, O_RETURN_MSG, O_RETURN_STATUS }
     * @return
     * @throws Exception
     */
    public void callCalcStock(Map<String, Object> params) throws Exception;
    
    
    
}
