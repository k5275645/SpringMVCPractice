package com.hitejinro.snop.work.controller;

import java.util.HashMap;
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
import com.hitejinro.snop.work.service.M07080Service;

/**
 * KPI > 신병 사용률
 * @author 이수헌
 *
 */
@Controller
public class M07080Controller {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M07080Controller.class);

	@Inject
	private M07080Service m07080Service;

    @Inject
    private CommonUtils commonUtils;
    
	/**
	 * UI07080
	 * @param params
	 * @return /common/U107080
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M07080", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		
		// 수익 마감년월  
		String sMagamYmd = "";
		sMagamYmd = m07080Service.getMagamYmd();
		
		ModelAndView view = new ModelAndView("/work/M07080");
		view.addObject("magamYmd", sMagamYmd);
		return view;
	}

	/**
	 * 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M07080/search", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> search(@RequestBody Map<String, Object> params) throws Exception {

		commonUtils.debugParams(params);

		Map<String, Object> mResult = new HashMap<String, Object>();

		// - 데이터 조회
		mResult = m07080Service.search(params);

		return mResult;
	}

}
