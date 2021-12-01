package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 제품수급 > 연간 제품수급 Simul
 * @author 유기후
 *
 */
@Repository
public interface M02030DaoMapper {
    
    /**
     * 데이터 조회
     * @param params { bssYYYY, useYn }
     * @return [{ VER_CD, VER_NM, VER_DESC, STD_YYYYMMDD, STD_YYYY, USE_YN, WORK_INFO_10, WORK_INFO_20 }]
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
     * @param params { userId, VER_CD, VER_NM, VER_DESC, STD_YYYYMMDD, STD_YYYY, USE_YN }
     * @return
     * @throws Exception
     */
    public int insert(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 저장 : 수정
     * @param params { userId, VER_CD, VER_NM, VER_DESC, USE_YN }
     * @return
     * @throws Exception
     */
    public int update(Map<String, Object> params) throws Exception;
    

    
    /**
     * 버전 정보 조회
     * @param params { verCd }
     * @return [{ VER_CD, VER_NM, VER_DESC, STD_YYYYMMDD, STD_YYYY, USE_YN, PRDT_VAR_VER_CD, PRDT_VAR_VER_NM, PRDT_VAR_VER_DESC, WORK_INFO_10, WORK_INFO_20, WORK_DT_10, WORK_USER_10, WORK_USER_10_NM, WORK_DT_20, WORK_USER_20, WORK_USER_20_NM }]
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
     * @return [{ LIQUOR_CODE, WEEK_WORK_DCNT_TP_CODE, WEEK_WORK_DCNT_TP_NAME, CALENDAR_WORK_CNT, WEEK_WORK_CNT, SFT_LIST, TOTAL_AVL_HR, SFT_REVERSE_LIST, REVERSE_TOTAL_AVL_HR }]
     * @throws Exception
     */
    public List<Map<String, Object>> selectSftPtrnDtyByWeekWorkDcntTpList(Map<String, Object> params) throws Exception;

    /**
     * 생산설정의 그리드 헤더 조회 : 생산변수의 공장/라인 리스트
     * @param params { verCd, liquorCode }
     * @return [{ VER_CD, STD_YYYYMMDD, PRDT_VAR_VER_CD, PRDT_VAR_VER_NM, PRDT_VAR_VER_DESC, PRDT_VAR_VER_USE_YN, LIQUOR_CODE, ORG_CODE, ORG_NAME, LINE_DEPT_CODE, LINE_DEPT_NAME, NEW_LINE_YN, COL_ID, HEADER_COL_SPAN }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchPrdtSetHeader(Map<String, Object> params) throws Exception;

    /**
     * 생산설정의 그리드 바디 조회 : 버전의 기간 리스트와 헤더의 조합
     * @param params { verCd, liquorCode, TREEGRID_HEADER:[{ ORG_CODE, LINE_DEPT_CODE, COL_ID }] }
     * @return [{ VER_CD, LIQUOR_CODE, LIQUOR_DESC, PERIOD_YYYYMM, PERIOD_TYPE, PERIOD_CODE, PERIOD_NAME, BUSINESS_DAY_FLAG, PERIOD_FR_YYYYMMDD, PERIOD_TO_YYYYMMDD, PERIOD_TYPE_SEQ, CALENDAR_WORK_CNT, WORK_CNT, HAS_SAT_YN, COL_1_SFT_PTRN_DTY_CODE, COL_1_AVL_HR, COL_1_MAX_CONV_QTY_PER_HR, ... }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchPrdtSetBody(Map<String, Object> params) throws Exception;
    
    /**
     * 생산설정 데이터 저장 : 연주차별 공장/라인의 근무형태/가용시간 삭제(근무형태가 N/A인경우)
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
     * 판매설정의 그리드 조회
     * @param params { verCd, liquorCode, vesselCode }
     * @return [{ VER_CD, STD_YYYYMMDD, LIQUOR_CODE, LIQUOR_DESC, PERIOD_YYYYMM, PERIOD_TYPE, PERIOD_CODE, PERIOD_NAME, Def, PERIOD_DESC, PERIOD_FR_YYYYMMDD, PERIOD_TO_YYYYMMDD, PERIOD_TYPE_SEQ, CALENDAR_WORK_CNT, WORK_CNT, USE_YYYY_TRG_SALE_CONV_QTY, USE_ESPN_ACTUAL_SALE_CONV_QTY, RMKS, ACTUAL_SALE_CONV_QTY, YYYY_TRG_SALE_CONV_QTY, ESPN_SALE_CONV_QTY }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchSaleSet(Map<String, Object> params) throws Exception;


    /**
     * 버전의 기간 리스트(년월+연주차) : 판매변수의 적용기간 설정시 사용
     * @param params { verCd }
     * @return [{ CODE, NAME, VER_CD, YYYYMM, SCM_YYYYWW }]
     * @throws Exception
     */
    public List<Map<String, Object>> selectPeriodList(Map<String, Object> params) throws Exception;
    
    /**
     * 판매설정 - 판매변수 조회
     * @param params { verCd }
     * @return [{ VER_CD, SALE_SET_TYPE_CODE, SALE_SET_TYPE_NAME, SEQNO, SALE_VAR_APL_FR_PERIOD, SALE_VAR_APL_TO_PERIOD, SALE_VAR_APL_FR_YYYYMM, SALE_VAR_APL_FR_SCM_YYYYWW, SALE_VAR_APL_TO_YYYYMM, SALE_VAR_APL_TO_SCM_YYYYWW, SALE_VAR_DFNT_SEQNO, SALE_VAR_DFNT_SEQNO_NAME, SALE_VAR_VAL, LIQUOR_CODE, LIQUOR_DESC, SALE_VAR_USAGE_NAME, SALE_VAR_ITEM_NAME, SALE_VAR_NAME, SALE_VAR_TYPE, SALE_VAR_USAGE_CODE, SALE_VAR_ITEM_CODE, SALE_VAR_APL_DIF_TYPE, SALE_VAR_VALID_MSG }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchSaleVar(Map<String, Object> params) throws Exception;
    
    /**
     * 판매설정 - 판매변수 저장(추가/수정)
     * @param params { userId, VER_CD, SEQNO, SALE_VAR_DFNT_SEQNO, SALE_VAR_APL_FR_YYYYMM, SALE_VAR_APL_FR_SCM_YYYYWW, SALE_VAR_APL_TO_YYYYMM, SALE_VAR_APL_TO_SCM_YYYYWW, SALE_VAR_APL_DIF_TYPE }
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
     * 판매설정 - 판매량 계산(생성=추가) : 설정된 판매변수를 이용하여, 연주차별 판매량 산출
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
     * 결과설정의 그리드 조회
     * @param params { verCd, liquorCode, vesselCode }
     * @return [{ VER_CD, STD_YYYYMMDD, LIQUOR_CODE, LIQUOR_DESC, PERIOD_YYYYMM, PERIOD_TYPE, PERIOD_CODE, PERIOD_NAME, Def, PERIOD_DESC, PERIOD_FR_YYYYMMDD, PERIOD_TO_YYYYMMDD, PERIOD_TYPE_SEQ, CALENDAR_WORK_CNT, WORK_CNT, YYYY_TRG_SALE_CONV_QTY, ESPN_ACTUAL_SALE_CONV_QTY, PRDT_PLAN_CONV_QTY, PRDT_ACTUAL_CONV_QTY, STOCK_CONV_QTY, BF_YYYY_STOCK_CONV_QTY, YYYY_TRG_SALE_CONV_AVG_QTY, STOCK_DCNT, AF_STRG_STOCK_DNCT, STRG_STOCK_CONV_QTY }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchResult(Map<String, Object> params) throws Exception;
    
    
    /**
     * 결과설정 - 계산(생성=추가) : 설정된 판매/생산을 이용하여, 전체 산출
     * @param params { userId, verCd, liquorCode }
     * @return
     * @throws Exception
     */
    public int updateResult(Map<String, Object> params) throws Exception;
    
    /**
     * 결과설정 - 초기화 : 삭제
     * @param params { verCd, liquorCode }
     * @return
     * @throws Exception
     */
    public int deleteResult(Map<String, Object> params) throws Exception;
    
    
    
}
