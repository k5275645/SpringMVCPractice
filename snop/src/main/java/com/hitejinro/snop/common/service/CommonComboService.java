package com.hitejinro.snop.common.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hitejinro.snop.common.dao.CommonComboDaoMapper;

/**
 * 콤보공통
 * @author 김태환
 *
 */
@Service
public class CommonComboService {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(CommonService.class);

	@Inject
	private CommonComboDaoMapper commonComboDaoMapper;

	/**
	 * 년도선택 콤보 리스트 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getYearComboList(Map<String, Object> params) throws Exception {
		return commonComboDaoMapper.getYearComboList(params);
	}

	/**
	 * 년도선택 콤보 리스트 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getMonthComboList(Map<String, Object> params) throws Exception {

		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> month = null;
		String code = null;
		String name = null;
		
		for (int i = 1; i < 13; i++) {
			month = new HashMap<String, Object>();
			
			code = (i < 10) ? ("0" + i) : String.valueOf(i);
			//공통적용 요청사항으로 01월 -> 1월 변경
			//name = code + "월";
			name = String.valueOf(i) + "월";
					
			month.put("CODE", code);
			month.put("NAME", name);
			result.add(month);
		}

		return result;
	}

	/**
	 * 사업부문 콤보 리스트 조회
	 * @param params { hasCommon(공통 포함 여부. Y(포함), N(Default. 미포함)) }
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getLiquorComboList(Map<String, Object> params) throws Exception {
		return commonComboDaoMapper.getLiquorComboList(params);
	}

	/**
	 * 브랜드 콤보 리스트 조회
	 * @param params { liquorCode }
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getBrandComboList(Map<String, Object> params) throws Exception {
		return commonComboDaoMapper.getBrandComboList(params);
	}

	/**
	 * 용도 콤보 리스트 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getUsageComboList(Map<String, Object> params) throws Exception {
		return commonComboDaoMapper.getUsageComboList(params);
	}

	/**
	 * 용기 콤보 리스트 조회
	 * @param params { liquorCode }
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getVesselComboList(Map<String, Object> params) throws Exception {
		return commonComboDaoMapper.getVesselComboList(params);
	}

	/**
	 * 용량 콤보 리스트 조회
	 * @param params { liquorCode }
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getVolumeComboList(Map<String, Object> params) throws Exception {
		return commonComboDaoMapper.getVolumeComboList(params);
	}

	/**
	 * 공통코드 콤보 리스트 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getComCodeCombo(Map<String, Object> params) throws Exception {
		return commonComboDaoMapper.getComCodeCombo(params);
	}
}
