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
import com.hitejinro.snop.work.service.M07050Service;

/**
 * KPI > 제품재고일수
 * @author 이수헌
 *
 */
@Controller
public class M07050Controller {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M07050Controller.class);

	@Inject
	private M07050Service m07050Service;
	
	@Inject
	private SessionUtil sessionUtil;

    @Inject
    private CommonUtils commonUtils;
    
	/**
	 * 화면 호출
	 * @param params
	 * @return /common/U107050
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M07050", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		
		// 수익 마감년월  
		String sMagamYmd = "";
		sMagamYmd = m07050Service.getMagamYmd();
		
		ModelAndView view = new ModelAndView("/work/M07050");
		view.addObject("magamYmd", sMagamYmd);
		return view;
	}

	/**
	 * 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M07050/search", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> search(@RequestBody Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		Map<String, Object> mResult = new HashMap<String, Object>();

		// - 데이터 조회
		mResult = m07050Service.search(params);

        return mResult;
	}
	
	/**
	 * 팝업 그리드 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M07050/searchPop", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> searchPop(@RequestBody Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		Map<String, Object> mResult = new HashMap<String, Object>();

		// - 데이터 조회
		mResult = m07050Service.searchPop(params);

        return mResult;
	}
	
	/**
	 * Combo 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M07050/getCombo", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getCombo(@RequestParam Map<String, Object> params) throws Exception {
		logger.info("/work/M07050/getCombo");
		commonUtils.debugParams(params);

		return m07050Service.getCombo(params);
	}
}
