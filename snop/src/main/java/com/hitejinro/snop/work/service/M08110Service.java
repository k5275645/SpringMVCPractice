package com.hitejinro.snop.work.service;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.poi.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.hitejinro.snop.common.util.NumberUtil;
import com.hitejinro.snop.work.dao.M08110DaoMapper;

/**
 * 프로그램 :: M08110 : 영업예상판매량
 * 작성일자 :: 2021.05.24
 * 작 성 자 :: 김태환
 */
@Service
public class M08110Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M08110Service.class);

	@Inject
	private M08110DaoMapper m08110DaoMapper;

	/**
	 * 상단 sum 그리드 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<List<Map<String, Object>>> searchSum(Map<String, Object> params) throws Exception {
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		list.add(m08110DaoMapper.searchSum(params));

		return list;
	}

	/**
	 * 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<List<Map<String, Object>>> search(Map<String, Object> params) throws Exception {
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		list.add(m08110DaoMapper.search(params));

		return list;
	}

	/**
	 * 버전삭제
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
	@SuppressWarnings("unchecked")
	public Map<String, Object> deleteVersion(Map<String, Object> params) throws Exception {
		Map<String, Object> mResult = new HashMap<String, Object>();

		try {
			String sResultFlag = "";
			String sResultMsg = "";
			int iDeleteCnt = 0;
			
			// 선택된 버전 데이터 삭제
			iDeleteCnt = m08110DaoMapper.deleteVersionData(params);
			
			sResultFlag = "S";
			sResultMsg = "삭제 성공";
			sResultMsg += "\n" + "==========================";
			sResultMsg += "\n" + "삭제 : " + iDeleteCnt + " 개(셀 단위)";
			
			mResult.put("RETURN_FLAG", sResultFlag);
			mResult.put("RETURN_MSG", sResultMsg);
			mResult.put("DELETE_CNT", iDeleteCnt);

		} catch (Exception e) {
			throw e;
		}

		return mResult;
	}

	/**
	 * version Combo 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> versionCombo(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = m08110DaoMapper.versionCombo(params);

		return list;
	}

	/**
	 * 엑셀업로드
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
	@SuppressWarnings("unchecked")
	public Map<String, Object> uploadData(Map<String, Object> params) throws Exception {
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
			String sVersion = (String)params.get("version");
			String sUploadGb = (String)params.get("uploadGb");
			String sUserId = (String)params.get("userId");
			
			try {
				// 2. 템플릿 파일정보를 바탕으로, 업로드 데이터의 ID 변환 : ex) COL6 -> ITEM_CODE
				// 2.1. 변환 정보 정의
				Map<String,Object> mColInfo = new HashMap<String,Object>();
				mColInfo.put("COL0", "ITEM_CODE");
				mColInfo.put("COL1", "ESPN_SALE_QTY_W0");
				mColInfo.put("COL2", "ESPN_SALE_QTY_W1");
				mColInfo.put("COL3", "ESPN_SALE_QTY_W2");
				mColInfo.put("COL4", "ESPN_SALE_QTY_W3");
				mColInfo.put("COL5", "ESPN_SALE_QTY_W4");
				mColInfo.put("COL6", "ESPN_SALE_QTY_W5");
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
					arrExcelData.get(r).put("sUploadGb", sUploadGb);
					arrExcelData.get(r).put("year", sYear);
					arrExcelData.get(r).put("month", sMonth);
					arrExcelData.get(r).put("version", sVersion);
					arrExcelData.get(r).put("userId", sUserId);
				}
				
				// 3. 정합성 체크
				String sErrMsg = "";
				// 3.1. 조회조건 체크
				if(!"".equals(sErrMsg)) {
					sResultFlag = "F";
					sResultMsg = "업로드 실패 : 조회조건 이상";
					sResultMsg += "\n" + "==========================";
					sResultMsg += "\n" + sErrMsg;
					sResultMsg += "\n" + "※ 창을 닫았다가 다시 열어서 시도해주세요.";
					
					mResult.put("RETURN_FLAG", sResultFlag);
					mResult.put("RETURN_MSG", sResultMsg);
					return mResult;
				}
				
				// - 중복 체크용 변수 선언
				Map<String, Object> mCheckDuplItemCode = new HashMap<String, Object>(); // - ITEM_CODE 기준으로 중복 체크용
				
				// 3.2. 데이터 체크
				for(int r = 0 ; r < arrExcelData.size() ; r++) {
					Map<String, Object> mRow = arrExcelData.get(r);
					String sRowErrMsg = "";
					
					// 3.2.1. ITEM_CODE 체크
					if(mRow.get("ITEM_CODE") == null && StringUtils.isEmpty(mRow.get("ITEM_CODE"))) {
						sRowErrMsg += ("".equals(sRowErrMsg) ? "" : ", ") + "제품코드 오류(빈칸)";
					} else if(mCheckDuplItemCode.containsKey((String)mRow.get("ITEM_CODE"))) {
						sRowErrMsg += ("".equals(sRowErrMsg) ? "" : ", ") + "제품코드 오류(제품코드 중복)";
					}
					
					// 3.2.2. 예상판매수량(SALES_QTY_M01 ~ SALES_QTY_M12) 체크
					String sQtyErrMsg = "";
					for(int m = 0 ; m <= 5 ; m++) {
						String sColId = "ESPN_SALE_QTY_W" + m;
						if(mRow.get(sColId) == null && StringUtils.isEmpty(mRow.get(sColId))) {
							// - 공백은 패스
							mRow.put(sColId, null);
						} else if(!NumberUtil.isNumber((String)mRow.get(sColId), false)) {
							sQtyErrMsg += ("".equals(sQtyErrMsg) ? "" : ", ") + m + "주차";
						} else {
							// - 소수점 10자리까지 반올림 : 환순수량이라서
							mRow.put(sColId, NumberUtil.getNumber((String)mRow.get(sColId), false, 10, RoundingMode.HALF_UP, 1));
						}
					}
					if(!"".equals(sQtyErrMsg)) {
						sRowErrMsg += ("".equals(sRowErrMsg) ? "" : ", ") + "목표수량 오류(숫자형식만 가능 : " + sQtyErrMsg + ")";
					}
					
					// - 에러 메시지 처리
					if(!"".equals(sRowErrMsg)) {
						sErrMsg += "\n- " + (mRow.get("_ROWNUM") == null ? (r+1+iSkipRowCount) : mRow.get("_ROWNUM")) + "행 : " + sRowErrMsg;
					}
				}
				
				// 3.3. 데이터 체크2
				List<Map<String, Object>> inValidList = (List<Map<String, Object>>) m08110DaoMapper.checkExcelList(params);
				
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
				// 4.1. 데이터 삭제 : 조회조건에 해당하는 데이터 삭제
				if(!params.get("version").equals("!new")) {
					iDeleteCnt = m08110DaoMapper.deleteVersionData(params);
				}else {
					List<Map<String, Object>> inpDgrSeqList = (List<Map<String, Object>>) m08110DaoMapper.getInpDgrSeq(params);
					String inpDgr = "1";
					if(inpDgrSeqList.size() > 0){
						inpDgr = (String) inpDgrSeqList.get(0).get("MAX_INP_DGR");
					}
					params.put("version", inpDgr);
				}
				// 4.2 데이터 추가 : 업로드 데이터 넣기
				if(params.get("uploadGb").equals("month")) {
					// 4.2.1 데이터 추가 : 업로드 데이터(월계) 넣기
					iInsertCnt = m08110DaoMapper.uploadMonthVersionData(params);
					iInsertCnt += m08110DaoMapper.uploadMonthAllocWeekVersionData(params);
				}else {
					// 4.2 데이터 추가 : 업로드 데이터(주차별) 넣기
					iInsertCnt = m08110DaoMapper.uploadWeekVersionData(params);
				}
				
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
				//e.printStackTrace();
				throw new RuntimeException(e);
			}
			
			return mResult;

		} catch (Exception e) {
			throw e;
		}
	}

}
