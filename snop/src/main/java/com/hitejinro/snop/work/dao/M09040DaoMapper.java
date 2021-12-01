package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 생산관리 > 원부자재/포장자재 사용 실적
 * @author 김남현
 *
 */
@Repository
public interface M09040DaoMapper {
	
	/**
	 * 원부자재/포장자재 사용 실적 > 소분류 콤보 조회
	 * @param params {}
	 * @return [{ CODE, NAME }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> getSegment3Combo(Map<String, Object> params) throws Exception;
	
	/**
	 * 원부자재/포장자재 사용 실적 > 원부자재 > 헤더 조회
	 * @param params {yyyymm}
	 * @return [{FR_SCM_YYYYWW, TO_SCM_YYYYWW, STD_PERIOD, CODE, NAME, HEADER1_DESC, HEADER1_SPAN, HEADER2_DESC, SEQ }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchRowSubMatHeader(Map<String, Object> params) throws Exception;
	
	/**
	 * 원부자재/포장자재 사용 실적 > 원부자재 > 바디 조회
	 * @param params { yyyymm, mfgCd, segment3Cd, brandCd, volumeCd, receiptTp, issueTp, header : [{FR_SCM_YYYYWW, TO_SCM_YYYYWW, STD_PERIOD, CODE, NAME, HEADER1_DESC, HEADER1_SPAN, HEADER2_DESC, SEQ }] }
	 * @return [{ ORG_CODE, ORG_NAME, SEGMENT1, ITEM_CODE, DESCRIPTION, COL1...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchRowSubMatBody(Map<String, Object> params) throws Exception;
	
	/**
	 * 원부자재/포장자재 사용 실적 > 포장자재 > 헤더 조회
	 * @param params {yyyymm}
	 * @return [{FR_SCM_YYYYWW, TO_SCM_YYYYWW, STD_PERIOD, CODE, NAME, HEADER1_DESC, HEADER1_SPAN, HEADER2_DESC, SEQ }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchPackMatHeader(Map<String, Object> params) throws Exception;
	
	/**
	 * 원부자재/포장자재 사용 실적 > 포장자재 > 바디 조회
	 * @param params { yyyymm, mfgCd, segment3Cd, brandCd, volumeCd, receiptTp, issueTp, header : [{FR_SCM_YYYYWW, TO_SCM_YYYYWW, STD_PERIOD, CODE, NAME, HEADER1_DESC, HEADER1_SPAN, HEADER2_DESC, SEQ }] }
	 * @return [{ ORG_CODE, ORG_NAME, SEGMENT2, SEGMENT3, PK_SORT, ITEM_CODE, DESCRIPTION, COL1...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchPackMatBody(Map<String, Object> params) throws Exception;
	
}
