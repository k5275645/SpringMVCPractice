package com.hitejinro.snop.work.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hitejinro.snop.common.util.Const;
import com.hitejinro.snop.common.util.UserException;
import com.hitejinro.snop.work.dao.M04020DaoMapper;

/**
 * 재고 조회 > 전사 제품 적재 현황
 * @author 김남현
 *
 */
@Service
public class M04020Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M04020Service.class);

	@Inject
	private M04020DaoMapper m04020DaoMapper;
	
	/**
	 * 전사 제품 적재 현황 > QC, 수출주 조회
	 * @param params { yyyymmdd, unitVal, capaVal, capaExceptVal, highestStockVal, periodStockVal, highestAvgStockVal }
	 * @return { QC_STOCK, EXP_STOCK, L10_EXP_STOCK, L20_EXP_STOCK }
	 * @throws Exception
	 */
	public Map<String, Object> searchExpQcStock(Map<String, Object> params) throws Exception {
		return m04020DaoMapper.searchExpQcStock(params);
	}
	
	/**
	 * 전사 제품 적재 현황 > 기간 조회
	 * @param params { yyyymmdd, unitVal, capaVal, capaExceptVal, highestStockVal, periodStockVal, highestAvgStockVal }
	 * @return { MIN_YYYYMMDD, MAX_YYYYMMDD}
	 * @throws Exception
	 */
	public Map<String, Object> searchPeriod(Map<String, Object> params) throws Exception {
		return m04020DaoMapper.searchPeriod(params);
	}
	
	/**
	 * 전사 제품 적재 현황 > 차트 데이터 조회
	 * @param params { yyyymmdd, unitVal, capaVal, capaExceptVal, highestStockVal, periodStockVal, highestAvgStockVal, MIN_YYYYMMDD, MAX_YYYYMMDD }
	 * @return [{ STD_PERIOD, TOTAL_QTY, QC_QTY, EXP_QTY, WH_LOAD_RATE, OPNSTOR_LOAD_RATE }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchChart(Map<String, Object> params) throws Exception {
		return m04020DaoMapper.searchChart(params);
	}
	
	/**
	 * 전사 제품 적재 현황 > 조회
	 * @param params { yyyymmdd, unitVal, capaVal, capaExceptVal, highestStockVal, periodStockVal, highestAvgStockVal, MIN_YYYYMMDD, MAX_YYYYMMDD }
	 * @return [{ ORG_CODE, ORG_NAME, A.ORG_OPT_CAPA, A.ORG_OPT_PT_CAPA, A.ORG_MAX_CAPA, A.ORG_MAX_PT_CAPA.. }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception {
		return m04020DaoMapper.search(params);
	}
	
	/**
	 * 전사 제품 적재 현황 > 저장
	 * @param params { saveData, yyyymmdd, unitVal, capaVal }
	 * @return { _RESULT_FLAG, _RESULT_MSG }
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
	@SuppressWarnings("unchecked")
	public Map<String, Object> save(Map<String, Object> params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<Map<String, Object>> saveList = (List<Map<String, Object>>) params.get("saveData");
		List<Map<String, Object>> updateList = new ArrayList<Map<String, Object>>();
		
		if (saveList == null || saveList.size() == 0) {
			throw new UserException("유효하지 않은 데이터입니다.");
		}
		
		Object action;
		for (Map<String, Object> row : saveList) {
			action = row.get(Const.ROW_STATUS);
			
			if (Const.ROW_STATUS_UPDATE.equals(action)) {
				updateList.add(row);
			} else {
				throw new UserException("유효하지 않은 데이터입니다.");
			}
		}
		
		// 추가/수정
		for (Map<String, Object> row : updateList) {
			row.put("userId", params.get("userId"));
			row.put("yyyymmdd", params.get("yyyymmdd"));
			row.put("unitVal", params.get("unitVal"));
			row.put("capaVal", params.get("capaVal"));
			
			m04020DaoMapper.update(row);
		}
				
		result.put(Const.RESULT_FLAG, "S");
		result.put(Const.RESULT_MSG, "저장이 완료되었습니다.");
		
		return result;
	}

	
}
