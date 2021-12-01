package com.hitejinro.snop.system.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 공통코드 관리
 * @author 김남현
 *
 */
@Repository
public interface M08180DaoMapper {
	
	/**
	 * 공통그룹 조회
	 * @param params { groupCode, useYN }
	 * @return [[{GROUP_CODE, GROUP_NAME...}]]
	 * @throws Exception
	 */
	List<Map<String, Object>> search(Map<String, Object> params) throws Exception;
	
	/**
	 * 공통그룹 유효성 검증
	 * @param params{ updateList, userId }
	 * @return [{GROUP_CODE, IDX번째 행 오류...}]
	 * @throws Exception
	 */
	List<Map<String, Object>> validate(Map<String, Object> params) throws Exception;

	/**
	 * 공통그룹 추가/수정
	 * @param params{ updateList, userId }
	 * @return
	 * @throws Exception
	 */
	public int update(Map<String, Object> params) throws Exception;
	
	/**
	 * 상위 코드 USE_YN = 'N' -> 하위코드 모두 USE_YN ='N'
	 * @param params{ updateList, userId }
	 * @return
	 * @throws Exception
	 */
	public int updateDetailUseYN(Map<String, Object> params) throws Exception;
	
	/**
	 * 공통코드 조회
	 * @param params { groupCode, useYN }
	 * @return [[{GROUP_CODE, CODE, NAME...}]]
	 * @throws Exception
	 */
	List<Map<String, Object>> searchDetail(Map<String, Object> params) throws Exception;

	/**
	 * 공통코드 유효성 검증
	 * @param params { updateList, userId }
	 * @return [{GROUP_CODE, CODE, IDX번째 행 오류...}]
	 * @throws Exception
	 */
	List<Map<String, Object>> validateDetail(Map<String, Object> params) throws Exception;
	
	/**
	 * 공통코드 추가/수정
	 * @param params { updateList, userId }
	 * @return
	 * @throws Exception
	 */
	public int updateDetail(Map<String, Object> params) throws Exception;
	
}
