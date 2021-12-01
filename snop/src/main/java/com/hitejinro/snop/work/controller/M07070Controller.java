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
import com.hitejinro.snop.work.service.M07070Service;

/**
 * KPI > 용기 회수율
 * @author 이수헌
 *
 */
@Controller
public class M07070Controller {

	private static final Logger logger = LoggerFactory.getLogger(M07070Controller.class);

	@Inject
	private M07070Service m07070Service;
	
	@Inject
	private SessionUtil sessionUtil;

    @Inject
    private CommonUtils commonUtils;
    
	/**
	 * UI07070
	 * @param params
	 * @return /common/U107070
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M07070", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		
		// 수익 마감년월  
		String sMagamYmd = "";
		sMagamYmd = m07070Service.getMagamYmd();
		
		ModelAndView view = new ModelAndView("/work/M07070");
		view.addObject("magamYmd", sMagamYmd);
		return view;
	}

	/**
	 * 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M07070/search", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> search(@RequestBody Map<String, Object> params) throws Exception {

		commonUtils.debugParams(params);

		Map<String, Object> mResult = new HashMap<String, Object>();

		// - 데이터 조회
		mResult = m07070Service.search(params);

		return mResult;
	}

}
