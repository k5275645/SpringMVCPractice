package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 제품적재규모(공장, 물류센터)
 * @author 김남현
 *
 */
@Repository
public interface M08170DaoMapper {
	
	/**
	 * 제품적재 규모 버전리스트 콤보 조회
	 * @param parmas { verCd }
	 * @return	[{ ORG_CAPA_VER_CD, ORG_CAPA_VER_NM, CREATION_DATE }, ]
	 * @throws Exception
	 */
	public List<Map<String, Object>> getVersion(Map<String, Object> parmas) throws Exception;
	
	/**
	 * 제품적재규모(공장, 물류센터) 조회
	 * @param params { verCd, useYn }
	 * @return { Body : [[{ORG_CODE, ORG_NAME, ORG_WH_OPT_CAPA...}]], _RESULT_FLAG, _RESULT_MSG }
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception;
	
	/**
	 * 저장 유효성 검증
	 * @param params { updateList, verCd, userId }
	 * @return [{ ORG_CAPA_VER_CD, ORG_CODE, IDX번째 행 오류...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> validateUpdate(Map<String, Object> params) throws Exception;
	
	/**
	 * 제품적재규모(공장, 물류센터) 저장
	 * @param params { updateList, verCd, userId }
	 * @return
	 * @throws Exception
	 */
	public int update(Map<String, Object> params) throws Exception;
	
	/**
	 * 제품적재규모(공장, 물류센터) 최신 코드 조회
	 * @param params 
	 * @return { LAST_VER_CD, NEW_VER_CD }
	 * @throws Exception
	 */
	public Map<String, Object> getNewVersion(Map<String, Object> params) throws Exception;
	
	/**
	 * 버전추가 유효성 검증
	 * @param params { newVerCd }
	 * @return { ...ERR_MSG}
	 * @throws Exception
	 */
	public Map<String, Object> validateNewVersion(Map<String, Object> params) throws Exception;
	
	/**
	 * 제품적재규모(공장, 물류센터) 버전추가
	 * @param params { verNm, YYYYMM, lastVerCd }
	 * @return
	 * @throws Exception
	 */
	public int insertNewVersion(Map<String, Object> params) throws Exception;
	
	/**
	 * 제품적재규모(공장, 물류센터) 버전상세추가
	 * @param params { lastVerCd, newVerCd }
	 * @return	
	 * @throws Exception
	 */
	public int insertNewVersionDetail(Map<String, Object> params) throws Exception;
	
}
