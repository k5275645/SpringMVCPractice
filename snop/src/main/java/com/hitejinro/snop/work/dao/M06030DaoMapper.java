package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 제품수명관리 > 전사 원부자재 현황(포장자재)
 * @author 이수헌
 *
 */
@Repository
public interface M06030DaoMapper {
	
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
	 * 마감년월 조회
	 * @return
	 * @throws Exception
	 */
	public String getMagamYmd() throws Exception;
	
	/**
	 * 콤보 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getCombo(Map<String, Object> params) throws Exception;
}
