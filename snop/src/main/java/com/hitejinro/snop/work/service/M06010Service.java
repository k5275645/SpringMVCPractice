package com.hitejinro.snop.work.service;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.hitejinro.snop.common.util.CommonUtils;
import com.hitejinro.snop.common.util.Const;
import com.hitejinro.snop.common.util.NumberUtil;
import com.hitejinro.snop.work.dao.M06010DaoMapper;

/**
 * 기준정보 > 단종검토 대상 품목 조회
 * @author 이수헌
 *
 */
@Service
public class M06010Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M06010Service.class);

	@Inject
	private M06010DaoMapper m06010DaoMapper;

    @Inject
    private CommonUtils commonUtils;
    
	/**
	 * 데이터 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = m06010DaoMapper.search(params);
		return list;
	}
	
	/**
	 * 마감년월 조회
	 * @return
	 * @throws Exception
	 */
	public String getMagamYmd() throws Exception {
		return m06010DaoMapper.getMagamYmd();
	}
	
	/**
	 * 폐기량 엑셀업로드
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
	@SuppressWarnings("unchecked")
	public Map<String, Object> uploadDisuseData(Map<String, Object> params) throws Exception {
		Map<String, Object> mResult = new HashMap<String, Object>();

		try {
			String sResultFlag = "S";
			String sResultMsg = "";
			int iInsertCnt = 0;
			int iDeleteCnt = 0;
			
			int iSkipRowCount = (Integer)params.get("SKIP_ROW_COUNT"); // - 엑셀에서 데이터가 존재하지 않는 상단 행 개수
			
			// - 엑셀파일을 읽은 데이터 : [{ COL0, COL1, ... COL20 }]
			List<Map<String, Object>> arrExcelData = (List<Map<String, Object>>)params.get("arrUploadData");

			String sYear = (String)params.get("year");
			String sMonth = (String)params.get("month");
			String sUserId = (String)params.get("userId");
			
			try {
				// 2. 템플릿 파일정보를 바탕으로, 업로드 데이터의 ID 변환 : ex) COL6 -> ITEM_CODE

				// 2.1. 변환 정보 정의
				Map<String,Object> mColInfo = new HashMap<String,Object>();
				mColInfo.put("COL0", "ITEM_CODE");
				mColInfo.put("COL1", "DISUSE_QTY");

				// 2.2. 데이터 변환
				for(int r = 0 ; r < arrExcelData.size() ; r++) {
					Set<String> sSet = mColInfo.keySet();
					Iterator<String> itr = sSet.iterator();
					while(itr.hasNext()) {
						String sKey = (String)itr.next(); // - COL0, COL1, ...
						String sChgKey = (String)mColInfo.get(sKey); // - YYYYMM, SCENARIO, ...
						String sValue = ""; // - 입력된 값
						if(arrExcelData.get(r) != null && arrExcelData.get(r).get(sKey) != null) {
							sValue = (String)arrExcelData.get(r).get(sKey);
							sValue = sValue.trim();
						} else {
							sValue = "";
						}
						arrExcelData.get(r).put(sChgKey, sValue);
					}
					// - 조회조건 추가 : year, month, version, 
					arrExcelData.get(r).put("year", sYear);
					arrExcelData.get(r).put("month", sMonth);
					arrExcelData.get(r).put("userId", sUserId);
				}
				
				// 3. 정합성 체크
				String sErrMsg = "";
			
				// - 중복 체크용 변수 선언
				Map<String, Object> mCheckDuplItemCode = new HashMap<String, Object>(); // - ITEM_CODE 기준으로 중복 체크용
				
				// 3.2. 데이터 체크 - Excel 입력값 정합성 
				for(int r = 0 ; r < arrExcelData.size() ; r++) {
					Map<String, Object> mRow = arrExcelData.get(r);
					String sRowErrMsg = "";
					
					// 3.2.1. ITEM_CODE 체크
					if(mRow.get("ITEM_CODE") == null && StringUtils.isEmpty(mRow.get("ITEM_CODE"))) {
						sRowErrMsg += ("".equals(sRowErrMsg) ? "" : ", ") + "제품코드 오류(빈 칸)";
					} else if(mCheckDuplItemCode.containsKey((String)mRow.get("ITEM_CODE"))) {
						sRowErrMsg += ("".equals(sRowErrMsg) ? "" : ", ") + "제품코드 오류(제품코드 중복)";
					}
					
					// 3.2.2. 폐기량(DISUSE_QTY) 체크
					String sQtyErrMsg = "";
					String sColId = "DISUSE_QTY";
					
					if(mRow.get(sColId) == null && StringUtils.isEmpty(mRow.get(sColId))) {
						// - 폐기량은 Null 값을 허용하지 않는다. 
						sQtyErrMsg += ("".equals(sQtyErrMsg) ? "" : ", ") + "폐기량 오류(빈 칸)";
					} else if(!NumberUtil.isNumber((String)mRow.get(sColId), false)) {
						sQtyErrMsg += ("".equals(sQtyErrMsg) ? "" : ", ") + "폐기량 오류(숫자형식만 가능)";
					} else {
						// TBD : 반올림 여부  
						// 소수점 10자리까지 반올림  
						// mRow.put(sColId, NumberUtil.getNumber((String)mRow.get(sColId), false, 10, RoundingMode.HALF_UP, 1));
					}
					
					if(!"".equals(sQtyErrMsg)) {
						sRowErrMsg += ("".equals(sRowErrMsg) ? "" : ", ") + sQtyErrMsg ;
					}
					
					// - 에러 메시지 처리
					if(!"".equals(sRowErrMsg)) {
						sErrMsg += "\n- " + (mRow.get("_ROWNUM") == null ? (r+1+iSkipRowCount) : mRow.get("_ROWNUM")) + "행 : " + sRowErrMsg;
					}
				}
				
				// 3.3. 데이터 체크 - Database 교차 검증
				List<Map<String, Object>> inValidList = (List<Map<String, Object>>) m06010DaoMapper.checkDisuseExcelList(params);
				
				for(int i=0; i<inValidList.size(); i++) {
					sErrMsg += "\n"+(String)inValidList.get(i).get("MESSAGE");
				}
				
				if(!"".equals(sErrMsg)) {
					sResultFlag = "F";
					sResultMsg = "업로드 실패 : 데이터 이상";
					sResultMsg += "\n" + "==========================";
					sResultMsg += "\n" + sErrMsg;
					
					mResult.put("RETURN_FLAG", sResultFlag);
					mResult.put("RETURN_MSG", sResultMsg);
					return mResult;
				}
				
				// 4. 데이터 저장
				// 4.1. 데이터 삭제 : 조회조건에 해당하는 데이터 무조건 삭제
				iDeleteCnt = m06010DaoMapper.deleteDisuseData(params);
				
				iInsertCnt = m06010DaoMapper.uploadDisuseData(params);
				
				sResultFlag = "S";
				sResultMsg = "업로드 성공";
				sResultMsg += "\n" + "==========================";
				sResultMsg += "\n" + "엑셀 : " + arrExcelData.size() + " 행";
				sResultMsg += "\n" + "삭제 : " + iDeleteCnt + " 개(셀 단위)";
				sResultMsg += "\n" + "추가 : " + iInsertCnt + " 개(셀 단위)";
				
				mResult.put("RETURN_FLAG", sResultFlag);
				mResult.put("RETURN_MSG", sResultMsg);
				mResult.put("INSERT_CNT", iInsertCnt);
				mResult.put("DELETE_CNT", iDeleteCnt);
				
			} catch (Exception e) {
				logger.info("[ E99 ] " + e.getMessage());
				throw new RuntimeException(e);
			}
			
			return mResult;

		} catch (Exception e) {
			throw e;
		}
	}

	
	/**
	 * Market Share 엑셀업로드
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
	@SuppressWarnings("unchecked")
	public Map<String, Object> uploadMsData(Map<String, Object> params) throws Exception {
		Map<String, Object> mResult = new HashMap<String, Object>();

		try {
			String sResultFlag = "S";
			String sResultMsg = "";
			int iInsertCnt = 0;
			int iDeleteCnt = 0;
			
			int iSkipRowCount = (Integer)params.get("SKIP_ROW_COUNT"); // - 엑셀에서 데이터가 존재하지 않는 상단 행 개수
			
			// - 엑셀파일을 읽은 데이터 : [{ COL0, COL1, ... COL20 }]
			List<Map<String, Object>> arrExcelData = (List<Map<String, Object>>)params.get("arrUploadData");

			String sYear = (String)params.get("year");
			String sMonth = (String)params.get("month");
			String sUserId = (String)params.get("userId");
			
			try {
				// 2. 템플릿 파일정보를 바탕으로, 업로드 데이터의 ID 변환 : ex) COL6 -> ITEM_CODE

				// 2.1. 변환 정보 정의
				Map<String,Object> mColInfo = new HashMap<String,Object>();
				mColInfo.put("COL0", "ITEM_CODE");
				mColInfo.put("COL1", "MARKET_SHARE_VALUE");

				// 2.2. 데이터 변환
				for(int r = 0 ; r < arrExcelData.size() ; r++) {
					Set<String> sSet = mColInfo.keySet();
					Iterator<String> itr = sSet.iterator();
					while(itr.hasNext()) {
						String sKey = (String)itr.next(); // - COL0, COL1, ...
						String sChgKey = (String)mColInfo.get(sKey); // - YYYYMM, SCENARIO, ...
						String sValue = ""; // - 입력된 값
						if(arrExcelData.get(r) != null && arrExcelData.get(r).get(sKey) != null) {
							sValue = (String)arrExcelData.get(r).get(sKey);
							sValue = sValue.trim();
						} else {
							sValue = "";
						}
						arrExcelData.get(r).put(sChgKey, sValue);
					}
					// - 조회조건 추가 : year, month, version, 
					arrExcelData.get(r).put("year", sYear);
					arrExcelData.get(r).put("month", sMonth);
					arrExcelData.get(r).put("userId", sUserId);
				}
				
				// 3. 정합성 체크
				String sErrMsg = "";
			
				// - 중복 체크용 변수 선언
				Map<String, Object> mCheckDuplItemCode = new HashMap<String, Object>(); // - ITEM_CODE 기준으로 중복 체크용
				
				// 3.2. 데이터 체크 - Excel 입력값 정합성 
				for(int r = 0 ; r < arrExcelData.size() ; r++) {
					Map<String, Object> mRow = arrExcelData.get(r);
					String sRowErrMsg = "";
					
					// 3.2.1. ITEM_CODE 체크
					if(mRow.get("ITEM_CODE") == null && StringUtils.isEmpty(mRow.get("ITEM_CODE"))) {
						sRowErrMsg += ("".equals(sRowErrMsg) ? "" : ", ") + "제품코드 오류(빈 칸)";
					} else if(mCheckDuplItemCode.containsKey((String)mRow.get("ITEM_CODE"))) {
						sRowErrMsg += ("".equals(sRowErrMsg) ? "" : ", ") + "제품코드 오류(제품코드 중복)";
					}
					
					// 3.2.2. 폐기량(DISUSE_QTY) 체크
					String sQtyErrMsg = "";
					String sColId = "MARKET_SHARE_VALUE";
					
					if(mRow.get(sColId) == null && StringUtils.isEmpty(mRow.get(sColId))) {
						// - 폐기량은 Null 값을 허용하지 않는다. 
						sQtyErrMsg += ("".equals(sQtyErrMsg) ? "" : ", ") + "점유율 오류(빈 칸)";
					} else if(!NumberUtil.isNumber((String)mRow.get(sColId), false)) {
						sQtyErrMsg += ("".equals(sQtyErrMsg) ? "" : ", ") + "점유율 오류(숫자형식만 가능)";
					} else {
						// TBD : 반올림 여부  
						// 소수점 10자리까지 반올림  
						// mRow.put(sColId, NumberUtil.getNumber((String)mRow.get(sColId), false, 10, RoundingMode.HALF_UP, 1));
					}
					
					if(!"".equals(sQtyErrMsg)) {
						sRowErrMsg += ("".equals(sRowErrMsg) ? "" : ", ") + sQtyErrMsg ;
					}
					
					// - 에러 메시지 처리
					if(!"".equals(sRowErrMsg)) {
						sErrMsg += "\n- " + (mRow.get("_ROWNUM") == null ? (r+1+iSkipRowCount) : mRow.get("_ROWNUM")) + "행 : " + sRowErrMsg;
					}
				}
				
				// 3.3. 데이터 체크 - Database 교차 검증
				List<Map<String, Object>> inValidList = (List<Map<String, Object>>) m06010DaoMapper.checkMsExcelList(params);
				
				for(int i=0; i<inValidList.size(); i++) {
					sErrMsg += "\n"+(String)inValidList.get(i).get("MESSAGE");
				}
				
				if(!"".equals(sErrMsg)) {
					sResultFlag = "F";
					sResultMsg = "업로드 실패 : 데이터 이상";
					sResultMsg += "\n" + "==========================";
					sResultMsg += "\n" + sErrMsg;
					
					mResult.put("RETURN_FLAG", sResultFlag);
					mResult.put("RETURN_MSG", sResultMsg);
					return mResult;
				}
				
				// 4. 데이터 저장
				// 4.1. 데이터 삭제 : 조회조건에 해당하는 데이터 무조건 삭제
				iDeleteCnt = m06010DaoMapper.deleteMsData(params);
				
				iInsertCnt = m06010DaoMapper.uploadMsData(params);
				
				sResultFlag = "S";
				sResultMsg = "업로드 성공";
				sResultMsg += "\n" + "==========================";
				sResultMsg += "\n" + "엑셀 : " + arrExcelData.size() + " 행";
				sResultMsg += "\n" + "삭제 : " + iDeleteCnt + " 개(셀 단위)";
				sResultMsg += "\n" + "추가 : " + iInsertCnt + " 개(셀 단위)";
				
				mResult.put("RETURN_FLAG", sResultFlag);
				mResult.put("RETURN_MSG", sResultMsg);
				mResult.put("INSERT_CNT", iInsertCnt);
				mResult.put("DELETE_CNT", iDeleteCnt);
				
			} catch (Exception e) {
				logger.info("[ E99 ] " + e.getMessage());
				throw new RuntimeException(e);
			}
			
			return mResult;

		} catch (Exception e) {
			throw e;
		}
	}

}
