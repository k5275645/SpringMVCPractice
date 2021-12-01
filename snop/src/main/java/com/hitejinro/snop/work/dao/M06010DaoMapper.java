package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 기준정보 > 단종검토 대상 품목 조회
 * @author 이수헌
 *
 */
@Repository
public interface M06010DaoMapper {
	
	/**
	 * 데이터 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception;
		
	/**
	 * 마감년월 조회
	 * @return
	 * @throws Exception
	 */
	public String getMagamYmd() throws Exception;
	
	/**
	 * 폐기량 업로드 엑셀파일 검증
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> checkDisuseExcelList(Map<String, Object> params) throws Exception;
	
	/**
	 * 폐기량 업로드 전 기존 데이터 삭제
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int deleteDisuseData(Map<String, Object> params) throws Exception;

	/**
	 * 폐기량 업로드
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int uploadDisuseData(Map<String, Object> params) throws Exception;
	
	/**
	 * 점유율 업로드 엑셀파일 검증
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> checkMsExcelList(Map<String, Object> params) throws Exception;
	
	/**
	 * 점유율 업로드 전 기존 데이터 삭제
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int deleteMsData(Map<String, Object> params) throws Exception;

	/**
	 * 점유율 업로드
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int uploadMsData(Map<String, Object> params) throws Exception;


}
