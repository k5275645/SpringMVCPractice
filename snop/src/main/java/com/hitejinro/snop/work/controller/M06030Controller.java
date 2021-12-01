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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hitejinro.snop.common.util.CommonUtils;
import com.hitejinro.snop.common.util.Const;
import com.hitejinro.snop.common.util.SessionUtil;
import com.hitejinro.snop.security.vo.User;
import com.hitejinro.snop.work.service.M06030Service;

/**
 * 전사 원부자재 현황(포장자재)
 * @author 이수헌
 *
 */
@Controller
public class M06030Controller {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M06030Controller.class);

	@Inject
	private M06030Service m06030Service;
	
	@Inject
	private SessionUtil sessionUtil;

    @Inject
    private CommonUtils commonUtils;
    
	/**
	 * UI06030
	 * @param params
	 * @return /common/U106030
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M06030", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		ModelAndView view = new ModelAndView("/work/M06030");
		
		return view;
	}

	/**
	 * 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M06030/search", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> search(@RequestBody Map<String, Object> params) throws Exception {

		commonUtils.debugParams(params);

		Map<String, Object> mResult = new HashMap<String, Object>();

		// - 데이터 조회
		List<Map<String, Object>> arrDataList = m06030Service.search(params);

		// - 트리그리드 형식에 맞게 변형해서 담기
        commonUtils.setTreeGridData(mResult, "TREEGRID_DATA", arrDataList);

        return mResult;
	}
	
	/**
	 * Combo 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M06030/getCombo", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getCombo(@RequestParam Map<String, Object> params) throws Exception {
		logger.info("/work/M06030/getCombo");
		commonUtils.debugParams(params);

		return m06030Service.getCombo(params);
	}
}
