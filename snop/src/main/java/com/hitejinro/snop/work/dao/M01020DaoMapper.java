package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 프로그램 :: M01020 : 일일 판매현황 분석
 * 작성일자 :: 2021.7.13
 * 작 성 자 :: 김태환
 */
@Repository
public interface M01020DaoMapper {
	
	/**
	 * 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception;
	
	/**
	 * 영본 판매예상랑 입력 차수 구하기
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchEspnSaleInpDgr(Map<String, Object> params) throws Exception;
	
    /**
     * 데이터 조회 : 판매현황 관리 그리드
     * @param params {  }
     * @return [{ CODE, NAME }]
     * @throws Exception
     */
    public List<Map<String, Object>> getStdSaleDfnList(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 조회 : 판매현황 관리 그리드
     * @param params {  }
     * @return [{ DAILY_SALES_MNG_SEQNO, LIQUOR_CODE, GUBUN1_TXT, GUBUN2_TXT, GUBUN3_TXT, GUBUN4_TXT, SEQ, BRAND_CODE, USAGE_CODE, VESSEL_CODE, VOLUME_VALUE }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchSaleCurstMng(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 저장 : 판매현황 관리의 생성
     * @param params { userId, LIQUOR_CODE, GUBUN1_TXT, GUBUN2_TXT, GUBUN3_TXT, GUBUN4_TXT, SEQ, BRAND_CODE, USAGE_CODE, VESSEL_CODE, VOLUME_VALUE }
     * @return
     * @throws Exception
     */
    public int insertSaleCurstMng(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 저장 : 판매현황 관리의 수정
     * @param params { userId, DAILY_SALES_MNG_SEQNO, LIQUOR_CODE, GUBUN1_TXT, GUBUN2_TXT, GUBUN3_TXT, GUBUN4_TXT, SEQ, BRAND_CODE, USAGE_CODE, VESSEL_CODE, VOLUME_VALUE }
     * @return
     * @throws Exception
     */
    public int updateSaleCurstMng(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 저장 : 판매현황 관리의 삭제
     * @param params { userId, DAILY_SALES_MNG_SEQNO }
     * @return
     * @throws Exception
     */
    public int deleteSaleCurstMng(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 조회 : 현재일자 기준 진척률 조회
     * @param params {  }
     * @return [{ NOW_DAY_CNT, COMP_RATE, MAGAM_DAY_CNT, TOTAL_DAY_CNT }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchDayMagamCnt(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 조회 : 판매량 항목 관리 그리드
     * @param params {  }
     * @return [{ STD_SALE_DFNT_CODE, SALE_DFNT_NAME, VIEW_TXT, ORDER_SEQ }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchStdSaleDfntMng1(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 조회 : 판매량 항목 관리 그리드
     * @param params {  }
     * @return [{ STD_SALE_DFNT, STD_SALE_DFNT_CODE, VIEW_TXT, DIF_SEQ_1, DIF_SEQ_2 }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchStdSaleDfntMng2(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 저장 : 판매량 항목 관리의 생성
     * @param params { userId, STD_SALE_DFNT_CODE, SALE_DFNT_NAME, VIEW_TXT, ORDER_SEQ }
     * @return
     * @throws Exception
     */
    public int insertStdSaleDfntMng(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 저장 : 판매량 항목 관리의 수정
     * @param params { userId, STD_SALE_DFNT_CODE, SALE_DFNT_NAME, VIEW_TXT, ORDER_SEQ }
     * @return
     * @throws Exception
     */
    public int updateStdSaleDfntMng(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 저장 : 판매량 항목 관리의 삭제
     * @param params { userId, STD_SALE_DFNT_CODE }
     * @return
     * @throws Exception
     */
    public int deleteStdSaleDfntMng(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 저장 : 차감 항목 관리의 수정
     * @param params { userId, STD_SALE_DFNT_CODE, SALE_DFNT_NAME, VIEW_TXT, DIF_SEQ_1, DIF_SEQ_2 }
     * @return
     * @throws Exception
     */
    public int updateStdSaleDfntMng2(Map<String, Object> params) throws Exception;
    
}
