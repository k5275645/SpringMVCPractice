package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 기준정보 > 정량적 단종기준
 * @author 김태환
 *
 */
@Repository
public interface M08050DaoMapper {
	
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
	 * 데이터 저장 : 삭제
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int delete(Map<String, Object> params) throws Exception;
	
	/**
	 * 데이터 저장 : 수정
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int update(Map<String, Object> params) throws Exception;
	
	/**
	 * 데이터 저장 : 추가
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int insert(Map<String, Object> params) throws Exception;
	
}
