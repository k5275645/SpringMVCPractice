package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 주단위 생산 CAPA 설정
 * @author 김남현
 *
 */
@Repository
public interface M08101DaoMapper {
	
	/**
	 * 주단위 생산 CAPA 설정 > 헤더 조회
	 * @param params { liquorCd } 
	 * @return [{CODE, NAME, HEADER1_DESC, HEADER1_SPAN...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchHeader(Map<String, Object> params) throws Exception;
	
	/**
	 * 주단위 생산 CAPA 설정 > 조회
	 * @param params { liquorCd }
	 * @return [{WEEK_WORK_DCNT_TP_CODE, LIQUOR_CODE...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception;
	
	/**
	 * 유효성 검증
	 * @param params { updateList, userId }
	 * @return [{WEEK_WORK_DCNT_TP_CODE, SFT_PTRN_DTY_CODE, IDX번째 행에 오류.. }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> validate(Map<String, Object> params) throws Exception;
	
	/**
	 * 주단위 생산 CAPA 설정 > 저장
	 * @param params { updateData, userId} 
	 * @return 
	 * @throws Exception
	 */
	public int update(Map<String, Object> params) throws Exception;
	
	/**
	 * 주단위 생산 CAPA 설정 > 팝업 > 조회
	 * @param params {} 
	 * @return [{CODE, NAME, DESCRIPTION...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchGroupCd(Map<String, Object> params) throws Exception;
	
	/**
	 * 주단위 생산 CAPA 설정 > 팝업 > 저장
	 * @param params { updateData, userId} 
	 * @return 
	 * @throws Exception
	 */
	public int updateGroupCd(Map<String, Object> params) throws Exception;
	
}
