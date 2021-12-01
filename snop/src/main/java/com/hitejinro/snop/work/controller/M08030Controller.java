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
import com.hitejinro.snop.work.service.M08030Service;

/**
 * 체화재고 기준정보
 * @author 김태환
 *
 */
@Controller
public class M08030Controller {

	private static final Logger logger = LoggerFactory.getLogger(M08030Controller.class);

	@Inject
	private M08030Service m08010Service;
	
	@Inject
	private SessionUtil sessionUtil;

    @Inject
    private CommonUtils commonUtils;
    
	/**
	 * UI08010
	 * @param params
	 * @return /common/U108010
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08030", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		
		ModelAndView view = new ModelAndView("/work/M08030");
		return view;
	}

	/**
	 * 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08030/search", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> search(@RequestBody Map<String, Object> params) throws Exception {

		commonUtils.debugParams(params);

		Map<String, Object> mResult = new HashMap<String, Object>();

		// - 데이터 조회
		List<Map<String, Object>> arrDataList = m08010Service.search(params);

		// - 트리그리드 형식에 맞게 변형해서 담기
        commonUtils.setTreeGridData(mResult, "TREEGRID_DATA", arrDataList);

        return mResult;
	}

	/**
	 * 저장
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08030/save", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> save(@RequestBody Map<String, Object> params) throws Exception {

		commonUtils.debugParams(params);

		Map<String, Object> mResult = new HashMap<String, Object>();

		// - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
		
		try {
			// - 저장 처리
			mResult = m08010Service.save(params);
		} catch (Exception e) {
			mResult.put(Const.RESULT_FLAG, "F");
            mResult.put(Const.RESULT_MSG, "저장 중 오류 발생" + " : " + e.getMessage());
            commonUtils.debugError(e);
            return mResult;
		}

		return mResult;
	}
	
	/**
	 * 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08030/searchPop", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> searchPop(@RequestBody Map<String, Object> params) throws Exception {
		logger.info("/work/M08030/searchPop");
		logger.info(params.toString());

		Map<String, Object> result = new HashMap<String, Object>();

		List<List<Map<String, Object>>> body = m08010Service.searchPop(params);

		if (body.get(0).size() < 1) {
			result.put("MEESSAGE", "조회된 데이터가 없습니다.");
			result.put("RESULT", "NO_DATA");
			return result;
		}

		result.put("BODY", body);
		result.put("RESULT", "SUCCESS");

		return result;
	}
}
