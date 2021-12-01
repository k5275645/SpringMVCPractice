package com.hitejinro.snop.work.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hitejinro.snop.work.dao.M01030DaoMapper;

/**
 * 판매실적분석(품목)
 * @author 이수헌
 *
 */
@Service
public class M01030Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M01030Service.class);

	@Inject
	private M01030DaoMapper m01030DaoMapper;

	/**
	 * 데이터 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> search(Map<String, Object> params) throws Exception {
		if(params == null) params = new HashMap<String, Object>();
		
		List<Map<String, Object>> arrData = new ArrayList<Map<String, Object>>();
		
		// 1. Header 조회 : 조회기간에 따른 컬럼 구성 리스트 : [{ PERIOD_YYYYMM, PERIOD_MMWW, FR_YYYYMMDD, TO_YYYYMMDD, PERIOD_YYYYMM_DESC, PERIOD_MMWW_DESC, COL_ID, MAX_SALE_MAKE_DT_DESC, MAX_SALE_ESPN_MAKE_DT_DESC, MAX_STOCK_PRDT_MAKE_DT_DESC }]
		params.put("TREEGRID_HEADER", m01030DaoMapper.searchHeader(params));
		
		arrData = m01030DaoMapper.searchBody(params);
		
		if(arrData.size() == 0) {
			// 2. Body 조회 : 헤더를 이용해서, 가변 컬럼 데이터 조회 :
			params.put("TREEGRID_BODY", arrData);
			
			// 3. Chart 조회
			params.put("CHART_DATA",  new HashMap<String, Object>());
			
		}else {
			// 2. Body 조회 : 헤더를 이용해서, 가변 컬럼 데이터 조회 :
			params.put("TREEGRID_BODY", getHierarchyAdjustedData(arrData));
			
			// 3. Chart 조회
			params.put("CHART_DATA", getChartData(arrData, (String)params.get("toYYYYMM")));
		}
		
        
		return params;
	}
	
	public List<Map<String, Object>> getTerritoryCombo(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		
		result = m01030DaoMapper.getTerritoryCombo(params);
		
		return result;
	}
	
	public List<Map<String, Object>> getDepartmentCombo(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		
		result = m01030DaoMapper.getDepartmentCombo(params);
		
		return result;
	}
	
	public List<Map<String, Object>> getPartCombo(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		
		result = m01030DaoMapper.getPartCombo(params);
		
		return result;
	}
	
	public List<Map<String, Object>> getSalesrepCombo(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		
		result = m01030DaoMapper.getSalesrepCombo(params);
		
		return result;
	}
	
	public List<Map<String, Object>> getAccountCombo(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		
		result = m01030DaoMapper.getAccountCombo(params);
		
		return result;
	}
	
	public List<Map<String, Object>> getSegment2Combo(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		
		result = m01030DaoMapper.getSegment2Combo(params);
		
		return result;
	}
	
	public List<Map<String, Object>> getSegment3Combo(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		
		result = m01030DaoMapper.getSegment3Combo(params);
		
		return result;
	}
	
	/**
	 * 차트용 데이터 가공
	 * 
	 * @param arrData
	 * @param toYYYYMM
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getChartData(List<Map<String, Object>> arrData, String toYYYYMM) throws Exception {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		int iToMonth = Integer.parseInt(toYYYYMM.substring(4));
		
		// AS-IS
		// [ { DEPTH : "1", IS_LEAF : "0", ...}]
		// TO-BE
		// { LI10 : [ {SALES_YYYYMM : "202101", PLAN_QTY : "13842176", ...]}
		
		for (int i = 0; i < arrData.size(); i++) {
			Map<String, Object> mRow = arrData.get(i);
			
			String sResultKey = (String) mRow.get("PK_PATH");
			//List<Map<String, Object>> arrResultValue = new ArrayList<Map<String, Object>>(12);
			Map[] arrResultValue = new Map[12];
			
			
			// { 202101 : {PLAN_QTY : "2123124", ...}, ... }
			Map<String, Object> tempMap = new HashMap<String, Object>();
			
			
			mRow.forEach((sKey, oVal)->{

				if("COL".equals(sKey.substring(0, 3)) && !"00".equals(sKey.substring(7, 9))) {
					
					String sYearMonth = sKey.substring(3, 9);
					String sColType = sKey.substring(9);
					Map<String, Object> tempInnerMap = new HashMap<String, Object>();
					
					// MAP 안에 MAP을 넣을 땐 주소값으로 참조
					if(!tempMap.containsKey(sYearMonth)) {
						String sYearMonthText = sYearMonth.substring(0,4) + "년 "+ Integer.parseInt(sYearMonth.substring(4)) + "월";
						tempInnerMap.put("SALES_YYYYMM", sYearMonthText);
						
						tempMap.put(sYearMonth, tempInnerMap);
					}else {
						tempInnerMap = (Map<String, Object>) tempMap.get(sYearMonth);
					}

					switch(sColType) {
						case "01" :	// 경영목표
							tempInnerMap.put("PLAN_QTY", oVal);
							break;
						case "02" : // 판매계획
							tempInnerMap.put("SALES_PLAN_QTY", oVal);
							break;
						case "03" : // 판매실적
							tempInnerMap.put("SALES_QTY_CD", oVal);
							break;
						case "09" : // 목표대비차이
							tempInnerMap.put("DIFF_PER_TARGET", oVal);
							break;
						case "10" : // 목표대비 달성률
							tempInnerMap.put("ACHIEVE_RATE_TARGET", oVal);
							break;
						default : break;
					}
				}
			});
			
			tempMap.forEach((sKey, oVal)->{
				
				// sKey : yearMonth (ex : "202006" ~ toYYYYMM)
				int iMonth = Integer.parseInt(sKey.substring(4));
				
				int iIndex = iMonth > iToMonth ? iMonth - iToMonth - 1 : iMonth - iToMonth + 11 ; 
				
				// arrResultValue.add(iIndex, (Map<String, Object>) oVal);
				arrResultValue[iIndex] = (Map<String, Object>) oVal;
			});
			
			result.put(sResultKey, arrResultValue);
		}
		return result;
	}
	
	/**
	 * 트리 그리드용 구조 생성
	 * 
	 * @param arrData
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getHierarchyAdjustedData(List<Map<String, Object>> arrData) throws Exception {
		long beforeTime = System.currentTimeMillis(); //코드 실행 전에 시간 받아오기
		
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		
		int iMaxDepth = 0;
		
		for (int i = 0; i < arrData.size(); i++) {
			Map<String, Object> mRow = arrData.get(i);
			
			// Declare variables
			int    iDepth  = Integer.parseInt(String.valueOf(mRow.get("DEPTH")));
			int    iIsLeaf = Integer.parseInt(String.valueOf(mRow.get("IS_LEAF")));
            String sPath   = (String) mRow.get("PK_PATH");

            iMaxDepth = iDepth > iMaxDepth ? iDepth : iMaxDepth;
            
            // Empty ArrayList for child items
            ArrayList<Object> arrChildItems = new ArrayList<Object>();
            if(iIsLeaf == 0) mRow.put("Items", arrChildItems);
			
            // Create Map for each Depth
            if(!resultMap.containsKey("Depth_"+iDepth)) {
            	LinkedHashMap<String, Object> mTemp = new LinkedHashMap<String, Object>();
            	mTemp.put(sPath, mRow);
            	resultMap.put("Depth_"+iDepth, mTemp);
            }else {
            	LinkedHashMap<String, Object> mTemp = (LinkedHashMap<String, Object>) resultMap.get("Depth_"+iDepth);
            	mTemp.put(sPath, mRow);
            }
		}
		
		while(iMaxDepth > 1) {
			LinkedHashMap<String, Object> mChilds = (LinkedHashMap<String, Object>)resultMap.get("Depth_"+iMaxDepth);
			LinkedHashMap<String, Object> mParents = (LinkedHashMap)resultMap.get("Depth_"+(iMaxDepth-1));
			
			mChilds.forEach((sKey, mVal)->{
				Map<String, Object> mChildRow = (Map<String, Object>) mVal;
				String sParentKey = (String) mChildRow.get("HRNK_PK_PATH");
				Map<String, Object> mParentRow = (Map<String, Object>)mParents.get(sParentKey);
				List<Map<String, Object>> arrTemp = (List<Map<String, Object>>)mParentRow.get("Items");
				arrTemp.add(mChildRow);
			});
			
			iMaxDepth--;
		}
		
		LinkedHashMap<String, Object> rootMap = (LinkedHashMap<String, Object>) resultMap.get("Depth_1");
		
		rootMap.forEach((sKey, mVal)->{
			result.add((Map<String, Object>) mVal);
		});
		
		long afterTime = System.currentTimeMillis(); 
		System.out.println("{getHierarchyAdjustedData executed in "+ (afterTime - beforeTime)+" msec}");
		
		
		return result;
	}
	
}
