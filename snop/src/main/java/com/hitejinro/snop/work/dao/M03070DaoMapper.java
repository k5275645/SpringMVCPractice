package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 용기 공급계획 수립
 * @author 남동희
 *
 */
@Repository
public interface M03070DaoMapper {

	/**
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception;

	/**
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int delete(Map<String, Object> params) throws Exception;
	
	/**
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int deleteAllStd(Map<String, Object> params) throws Exception;
	
	/**
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int deleteAllPrdt(Map<String, Object> params) throws Exception;
	
	/**
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int deleteAllResult(Map<String, Object> params) throws Exception;

	/**
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int update(Map<String, Object> params) throws Exception;

	/**
	 * 선택된 버전의 정보 조회
	 * @param params {verCd}
	 * @return [VER_CD, VER_NM, VER_DESC, DALY_SCM_SIMUL_VER_CD, DALY_SCM_SIMUL_YYYY, DALY_SCM_SIMUL_MM}
	 * @throws Exception
	 */
	public Map<String, Object> getVerInfo(Map<String, Object> params) throws Exception;

	/**
	 * 일일 제품수급 시뮬레이션 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchDalyScmSimulVerCd(Map<String, Object> params) throws Exception;
	
	/**
	 * 일일 제품수급 시뮬레이션 저장
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int updateDalyScmSimulVerCd(Map<String, Object> params) throws Exception;
	
	/**
	 * 기준정보 조회
	 * @param params {verCd}
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchStd(Map<String, Object> params) throws Exception;
	
	/**
	 * 기준정보 삭제
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int deleteStd(Map<String, Object> params) throws Exception;
	
	/**
	 * 기준정보 저장
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int updateStd(Map<String, Object> params) throws Exception;
	
	/**
	 * 제병사 생산계획 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchPrdt(Map<String, Object> params) throws Exception;
	
	/**
	 * 제병사 생산계획 저장
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int updatePrdt(Map<String, Object> params) throws Exception;
	
	/**
	 * 용기 시뮬레이션 결과 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchResult(Map<String, Object> params) throws Exception;
	
	/**
	 * 시뮬레이션
	 * @param simulate {verCd}
	 * @return
	 * @throws Exception
	 */
	public void simulate(Map<String, Object> params) throws Exception;
	
	/**
	 * 용기 시뮬레이션 결과 저장
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int updateResult(Map<String, Object> params) throws Exception;
}
