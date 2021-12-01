package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 제품수명관리 > 기 단종품목 조회
 * @author 이수헌
 *
 */
@Repository
public interface M06020DaoMapper {
	
	/**
	 * 데이터 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception;
	
	/**
	 * 데이터 저장 : 유효성 검증
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> validate(Map<String, Object> params) throws Exception;
	
	
	/**
	 * 데이터 저장 : 추가/수정
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int update(Map<String, Object> params) throws Exception;
	
	
	/**
	 * 버전 콤보 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> itemStatusCombo(Map<String, Object> params) throws Exception;
	

}
