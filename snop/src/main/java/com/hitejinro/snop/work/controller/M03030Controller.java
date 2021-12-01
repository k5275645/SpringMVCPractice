package com.hitejinro.snop.work.controller;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

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
import com.hitejinro.snop.common.util.UserException;
import com.hitejinro.snop.security.vo.User;
import com.hitejinro.snop.work.service.M03030Service;

/**
 * 일일 용기 현황
 * @author 남동희
 *
 */
@Controller
public class M03030Controller {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M03030Controller.class);

	@Inject
	private M03030Service m03030Service;

	@Inject
	private CommonUtils commonUtils;

	@Inject
	private SessionUtil sessionUtil;

	/**
	 * M03030
	 * @param params
	 * @return /work/M03030
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03030", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		ModelAndView view = new ModelAndView("/work/M03030");

		return view;
	}

	/**
	 * 주차 콤보 데이터 조회
	 * 1주차 ~ 차주 주차
	 * @param params {year : 대상년도}
	 * @return [{CODE, NAME}]
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03030/searchWeek", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> searchWeek(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		return m03030Service.searchWeek(params);
	}

	/**
	 * 조회
	 * @param params {endDate, liquorCode, orgType}
	 * @return {"M03031" : [], "M03032" : [], "M03033" : [], "M03034" : [], Header : [], _RESULT_FLAG}
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03030/search", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> search(@RequestParam Map<String, Object> params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		params.put("liquorCode", String.valueOf(params.get("liquorCode")).split(","));
		params.put("vesselCode", String.valueOf(params.get("vesselCode")).split(","));
		params.put("volumeValue", String.valueOf(params.get("volumeValue")).split(","));
		params.put("brandCode", String.valueOf(params.get("brandCode")).split(","));
		
		Map<String, Object> period = m03030Service.searchPeriod(params);
		if (period == null || period.isEmpty()) {
			throw new UserException("조회된 데이터가 없습니다.");
		}
		params.putAll(period);

		List<Map<String, Object>> header = m03030Service.searchHeader(params);
		params.put("header", header);

		result.put("M03031", m03030Service.searchM03031(params));
		result.put("M03032", m03030Service.searchM03032(params));
		result.put("M03033", m03030Service.searchM03033(params));
		result.put("M03034", m03030Service.searchM03034(params));
		result.put(Const.GRID_HEADER, header);
		result.put(Const.RESULT_FLAG, "S");
		return result;
	}

	/**
	 * 일평균생산량 관리 조회
	 * @param params {week, mfgCode}
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03030/searchM03035", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> searchM03035(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());

		Map<String, Object> result = new HashMap<String, Object>();

		result.put(Const.GRID_BODY, m03030Service.searchM03035(params));
		result.put(Const.RESULT_FLAG, "S");
		return result;
	}

	/**
	 * 일평균생산량 관리 저장
	 * @param params {week, mfgCode, saveDate}
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03030/saveM03035", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveM03035(@RequestBody Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());

		Map<String, Object> result = new HashMap<String, Object>();

		User user = sessionUtil.getUserInfo();
		params.put("userId", user.getUsername());

		result = m03030Service.saveM03035(params);
		return result;
	}

	/**
	 * 시그널 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03030/searchM03036", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> searchM03036(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());

		Map<String, Object> result = new HashMap<String, Object>();

		result.put(Const.GRID_BODY, m03030Service.searchM03036(params));
		result.put(Const.ATTR, m03030Service.searchSign(params));
		result.put(Const.RESULT_FLAG, "S");
		return result;
	}

	/**
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03030/saveM03036", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveM03036(@RequestBody Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());

		Map<String, Object> result = new HashMap<String, Object>();

		User user = sessionUtil.getUserInfo();
		params.put("userId", user.getUsername());

		result = m03030Service.saveM03036(params);
		return result;
	}

	/**
	 * 엑셀 다운로드
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03030/excelDownload", method = RequestMethod.POST)
	@ResponseBody
	public void excelDownload(@RequestBody Map<String, Object> params, HttpServletResponse response) throws Exception {
		commonUtils.debugParam(params.toString());
		
		params.put("liquorCode", String.valueOf(params.get("liquorCode")).split(","));
		params.put("vesselCode", String.valueOf(params.get("vesselCode")).split(","));
		params.put("volumeValue", String.valueOf(params.get("volumeValue")).split(","));
		params.put("brandCode", String.valueOf(params.get("brandCode")).split(","));

		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename=" + "일일 용기 현황");
		response.setHeader("Cache-Control", "max-age=0");
		response.setHeader("Expires:", "0");

		OutputStream output = response.getOutputStream();
		output.write(m03030Service.excelDownload(params));
		output.flush();
	}
}
