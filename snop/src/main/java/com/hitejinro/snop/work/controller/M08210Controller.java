package com.hitejinro.snop.work.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hitejinro.snop.common.util.CommonUtils;
import com.hitejinro.snop.common.util.Const;
import com.hitejinro.snop.common.util.SessionUtil;
import com.hitejinro.snop.work.service.M08210Service;

/**
 * 기준정보 > 모제품 매핑
 * 
 * @author 손성은
 *
 */
@Controller
public class M08210Controller {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M08210Controller.class);

	@Inject
	private M08210Service m08210Service;

	@Inject
	private SessionUtil sessionUtil;

	@Inject
	private CommonUtils commonUtils;

	/**
	 * 화면 호출
	 * @param params
	 * @return /system/M08210
	 * @throws Exception
	 */

	@RequestMapping(value = "/work/M08210", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		ModelAndView view = new ModelAndView("/work/M08210");
		return view;
	}

	/**
	 * 데이터 조회
	 * @param params { liquorCode }
	 * @return { Body: [{LIQUOR_CODE, FR_ITEM_CODE, TO_ITEM_CODE, CONVERSION_VALUE... }], _RESULT_FLAG, FR_ITEM_LIST : [{ITEM_CODE, DESCRIPTION, CONVERSION_BULK... }] }
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08210/search", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> search(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		Map<String, Object> mResult = new HashMap<String, Object>();

		// - 데이터 조회
		List<Map<String, Object>> arrDataList = m08210Service.search(params);
		mResult.put(Const.RESULT_FLAG, "S");
		mResult.put(Const.GRID_BODY, arrDataList);

		// - 추가 데이터 조회해서 담기 : suggest용 제품 마스터, 브랜드, 용기, 용량
		mResult.put("FR_ITEM_LIST", m08210Service.searchItemList(params));
		return mResult;
	}

	/**
	 * 데이터 저장
	 * @param params { liquorCode, saveData:[{FR_ITEM_CODE, TO_ITEM_CODE, CONVERSION_VALUE, RMKS }] }
	 * @return { _RESULT_FLAG, _RESULT_MSG }
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08210/save", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> save(@RequestBody Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		Map<String, Object> mResult = new HashMap<String, Object>();

		// - 사용자 정보 담기
		sessionUtil.setUserInfoParam(params);
		mResult = m08210Service.save(params);

		return mResult;
	}

}
