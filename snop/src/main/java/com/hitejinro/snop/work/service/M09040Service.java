package com.hitejinro.snop.work.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hitejinro.snop.work.dao.M09040DaoMapper;

/**
 * 생산관리 > 원부자재/포장자재 사용 실적
 * @author 김남현
 *
 */
@Service
public class M09040Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M09040Service.class);

	@Inject
	private M09040DaoMapper m09040DaoMapper;
	
	/**
	 * 원부자재/포장자재 사용 실적 > 소분류 콤보 조회
	 * @param params {}
	 * @return [{ CODE, NAME }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> getSegment3Combo(Map<String, Object> params) throws Exception {
		return m09040DaoMapper.getSegment3Combo(params);
	}
	
	/**
	 * 원부자재/포장자재 사용 실적 > 원부자재 > 헤더 조회
	 * @param params {yyyymm}
	 * @return [{FR_SCM_YYYYWW, TO_SCM_YYYYWW, STD_PERIOD, CODE, NAME, HEADER1_DESC, HEADER1_SPAN, HEADER2_DESC, SEQ }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchRowSubMatHeader(Map<String, Object> params) throws Exception {
		return m09040DaoMapper.searchRowSubMatHeader(params);
	}
	
	/**
	 * 원부자재/포장자재 사용 실적 > 원부자재 > 바디 조회
	 * @param params { yyyymm, mfgCd, segment3Cd, brandCd, volumeCd, receiptTp, issueTp, header : [{FR_SCM_YYYYWW, TO_SCM_YYYYWW, STD_PERIOD, CODE, NAME, HEADER1_DESC, HEADER1_SPAN, HEADER2_DESC, SEQ }] }
	 * @return [{ ORG_CODE, ORG_NAME, SEGMENT1, ITEM_CODE, DESCRIPTION, COL1...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchRowSubMatBody(Map<String, Object> params) throws Exception {
		return m09040DaoMapper.searchRowSubMatBody(params);
	}
	
	/**
	 * 원부자재/포장자재 사용 실적 > 포장자재 > 헤더 조회
	 * @param params {yyyymm}
	 * @return [{FR_SCM_YYYYWW, TO_SCM_YYYYWW, STD_PERIOD, CODE, NAME, HEADER1_DESC, HEADER1_SPAN, HEADER2_DESC, SEQ }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchPackMatHeader(Map<String, Object> params) throws Exception {
		return m09040DaoMapper.searchPackMatHeader(params);
	}
	
	/**
	 * 원부자재/포장자재 사용 실적 > 포장자재 > 바디 조회
	 * @param params { yyyymm, mfgCd, segment3Cd, brandCd, volumeCd, receiptTp, issueTp, header : [{FR_SCM_YYYYWW, TO_SCM_YYYYWW, STD_PERIOD, CODE, NAME, HEADER1_DESC, HEADER1_SPAN, HEADER2_DESC, SEQ }] }
	 * @return [{ ORG_CODE, ORG_NAME, SEGMENT2, SEGMENT3, PK_SORT, ITEM_CODE, DESCRIPTION, COL1...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchPackMatBody(Map<String, Object> params) throws Exception {
		return m09040DaoMapper.searchPackMatBody(params);
	}
	
	
}
