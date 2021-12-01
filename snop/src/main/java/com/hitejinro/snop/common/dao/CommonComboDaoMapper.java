package com.hitejinro.snop.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface CommonComboDaoMapper {
	
	/**
	 * 년도선택 콤보리스트 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getYearComboList(Map<String, Object> params) throws Exception;

	/**
	 * 사업부문 콤보리스트 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getLiquorComboList(Map<String, Object> params) throws Exception;

	/**
	 * 브랜드 콤보리스트 조회
	 * @param params { liquorCode }
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getBrandComboList(Map<String, Object> params) throws Exception;

	/**
	 * 용도 콤보리스트 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getUsageComboList(Map<String, Object> params) throws Exception;

	/**
	 * 용기 콤보리스트 조회
	 * @param params { liquorCode }
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getVesselComboList(Map<String, Object> params) throws Exception;

	/**
	 * 용량 콤보리스트 조회
	 * @param params { liquorCode }
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getVolumeComboList(Map<String, Object> params) throws Exception;

	/**
	 * 공통코드 콤보리스트 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getComCodeCombo(Map<String, Object> params) throws Exception;
}
