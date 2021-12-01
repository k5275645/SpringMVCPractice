package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 생산 라인별 CAPA 설정
 * @author 김남현
 *
 */
@Repository
public interface M08100DaoMapper {
	
	/**
	 * 생산 라인별 CAPA 설정 > 조회
	 * @param params { verNm, useYn }
	 * @return [{ PRDT_VAR_VER_CD, PRDT_VAR_VER_NM, PRDT_VAR_VER_DESC... }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception;
	
	/**
	 * 생산 라인별 CAPA 설정 > VERSION 채번
	 * @param params {}
	 * @return { NEW_VER_CD }
	 * @throws Exception
	 */
	public Map<String, Object> getNewVersion(Map<String, Object> params) throws Exception;
	
	/**
	 * 생산 라인별 CAPA 설정 > 저장 > 추가/수정
	 * @param params { prdtVarVerCd.., userId }
	 * @return
	 * @throws Exception
	 */
	public int update(Map<String, Object> params) throws Exception;
	
	/**
	 * 생산 라인별 CAPA 설정 > 저장 > 추가 > 일일 할당시간 기본값 입력
	 * @param params { prdtVarVerCd.., userId }
	 * @return
	 * @throws Exception
	 */
	public int insertSfthr(Map<String, Object> params) throws Exception;

	/**
	 * 생산 라인별 CAPA 설정 > 갱신 > 추가/수정
	 * @param params { prdtVarVerCd.., userId }
	 * @return
	 * @throws Exception
	 */
	public int renew(Map<String, Object> params) throws Exception;
	
	/**
	 * 생산 라인별 CAPA 설정 > 갱신 > 삭제
	 * @param params { prdtVarVerCd.., userId }
	 * @return
	 * @throws Exception
	 */
	public int deleteNotExistsRenew(Map<String, Object> params) throws Exception;
	
	/**
	 * 생산 라인별 CAPA 설정 > 갱신날짜 수정
	 * @param params { prdtVarVerCd.., userId }
	 * @return
	 * @throws Exception
	 */
	public int updateRenewDate(Map<String, Object> params) throws Exception;
	
	/**
	 * 생산 라인별 CAPA 설정 > 버전복사 > TO버전 상세 테이블 데이터 삭제
	 * @param params { fromPrdtVarVerCd, toPrdtVarVerCd }
	 * @return
	 * @throws Exception
	 */
	public int deleteToVerDtl(Map<String, Object> params) throws Exception;
	
	/**
	 * 생산 라인별 CAPA 설정 > 버전복사 > TO버전 신규라인 테이블 데이터 삭제
	 * @param params { fromPrdtVarVerCd, toPrdtVarVerCd }
	 * @return
	 * @throws Exception
	 */
	public int deleteToVerNewLine(Map<String, Object> params) throws Exception;
	
	/**
	 * 생산 라인별 CAPA 설정 > 버전복사 > TO버전 일일할당시간 테이블 데이터 삭제
	 * @param params { fromPrdtVarVerCd, toPrdtVarVerCd }
	 * @return
	 * @throws Exception
	 */
	public int deleteToVerSftHr(Map<String, Object> params) throws Exception;
	
	/**
	 * 생산 라인별 CAPA 설정 > 버전복사 > FROM버전 상세 테이블 데이터 입력
	 * @param params { fromPrdtVarVerCd, toPrdtVarVerCd }
	 * @return
	 * @throws Exception
	 */
	public int insertFromVerDtl(Map<String, Object> params) throws Exception;
	
	/**
	 * 생산 라인별 CAPA 설정 > 버전복사 > FROM버전 신규라인 테이블 데이터 입력
	 * @param params { fromPrdtVarVerCd, toPrdtVarVerCd }
	 * @return
	 * @throws Exception
	 */
	public int insertFromVerNewLine(Map<String, Object> params) throws Exception;
	
	/**
	 * 생산 라인별 CAPA 설정 > 버전복사 > FROM버전 일일할당시간 데이터 입력
	 * @param params { fromPrdtVarVerCd, toPrdtVarVerCd }
	 * @return
	 * @throws Exception
	 */
	public int insertFromVerSftHr(Map<String, Object> params) throws Exception;
	
	/**
	 * 생산 라인별 CAPA 설정 > 상세 > 공장 콤보 조회
	 * @param params {}
	 * @return [{ CODE, NAME }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> getMfgList(Map<String, Object> params) throws Exception;
	
	/**
	 * 생산 라인별 CAPA 설정 > 상세 > Header1 조회
	 * @param params { liquorCd }
	 * @return [{ CODE, HEADER1_DESC, HEADER1_SPAN.. }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchDetailHeader1(Map<String, Object> params) throws Exception;
	
	/**
	 * 생산 라인별 CAPA 설정 > 상세 > Header2 조회
	 * @param params { liquorCd }
	 * @return [{ CODE, HEADER1_DESC, HEADER1_SPAN.. }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchDetailHeader2(Map<String, Object> params) throws Exception;
	
	/**
	 * 생산 라인별 CAPA 설정 > 상세 > 조회
	 * @param params { header1, header2, liquorCd, prdtVarVerCd, orgCd, expCd }
	 * @return [{ PRDT_VAR_VER_CD, ORG_CODE, LINE_DEPT_CODE, ITEM_CODE.. }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchDetail(Map<String, Object> params) throws Exception;
    
    /**
     * 생산 라인별 CAPA 설정 > 상세 > 제품라우팅 조회
     * @param params { liquorCd, prdtVarVerCd, orgCd, expCd, searchItem }
     * @return [{ LIQUOR_CODE, LIQUOR_DESC, ORG_CODE, ORG_NAME, LINE_DEPT_CODE, LINE_DEPT_NAME, ITEM_CODE, ITEM_NAME, VESSEL_CODE, VESSEL_NAME, VOLUME_VALUE, QTY_PER_HOUR, QTY_PER_MIN, ACTUAL_60D_SALE_QTY, UOM_CONVERSION_VALUE, PRDT_RANK }]
     * @throws Exception
     */
    public List<Map<String, Object>> selectItemRouting(Map<String, Object> params) throws Exception;
	
	/**
	 * 생산 라인별 CAPA 설정 > 상세 > 저장
	 * @param params { PRDT_VAR_VER_CD, ORG_CODE, LINE_DEPT_CODE, ITEM_CODE.. , userId }
	 * @return
	 * @throws Exception
	 */
	public int updateDetail(Map<String, Object> params) throws Exception;
	
	/**
	 * 생산 라인별 CAPA 설정 > 상세 > 일일 할당시간관리 팝업 > 조회
	 * @param params { prdtVarVerCd }
	 * @return [{ PRDT_VAR_VER_CD, AVL_HR.. }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchSftHr(Map<String, Object> params) throws Exception;
	
	/**
	 * 생산 라인별 CAPA 설정 > 상세 > 일일 할당시간관리 팝업 > 저장
	 * @param params { PRDT_VAR_VER_CD, SFT_PTRN_DTY_CODE, userId }
	 * @return 
	 * @throws Exception
	 */
	public int updateSftHr(Map<String, Object> params) throws Exception;
	
	/**
	 * 생산 라인별 CAPA 설정 > 상세 > 라인관리 팝업 > 조회 
	 * @param params { prdtVarVerCd }
	 * @return [{ PRDT_VAR_VER_CD, ORG_CODE, LINE_DEPT_CODE.. }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchNewLine(Map<String, Object> params) throws Exception;
	
	/**
	 * 생산 라인별 CAPA 설정 > 상세 > 라인관리 팝업 > 라인 채번 
	 * @param params {}
	 * @return { NEW_LINE_CODE }
	 * @throws Exception
	 */
	public Map<String, Object> getNewLineCd(Map<String, Object> params) throws Exception;
	
	/**
	 * 생산 라인별 CAPA 설정 > 상세 > 라인관리 팝업 > 삭제
	 * @param params { PRDT_VAR_VER_CD, ORG_CODE, LINE_DEPT_CODE, VESSEL_CODE }
	 * @return
	 * @throws Exception
	 */
	public int deleteNewLine(Map<String, Object> params) throws Exception;
	
	/**
	 * 생산 라인별 CAPA 설정 > 상세 > 라인관리 팝업 > 상세 테이블 삭제
	 * @param params { PRDT_VAR_VER_CD, ORG_CODE, LINE_DEPT_CODE, VESSEL_CODE }
	 * @return
	 * @throws Exception
	 */
	public int deleteDetailNewLine(Map<String, Object> params) throws Exception;
	
	/**
	 * 생산 라인별 CAPA 설정 > 상세 > 라인관리 팝업 > 추가/수정
	 * @param params { PRDT_VAR_VER_CD, ORG_CODE, LINE_DEPT_CODE, LINE_DEPT_NAME, VESSEL_CODE.. }
	 * @return
	 * @throws Exception
	 */
	public int updateNewLine(Map<String, Object> params) throws Exception;
	
	/**
	 * 생산 라인별 CAPA 설정 > 상세 > 라인관리 팝업 > 상세 테이블 추가/수정
	 * @param params { PRDT_VAR_VER_CD, ORG_CODE, LINE_DEPT_CODE, LINE_DEPT_NAME, VESSEL_CODE.. }
	 * @return
	 * @throws Exception
	 */
	public int updateDetailNewLine(Map<String, Object> params) throws Exception;
	
}
