package com.hitejinro.snop.work.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

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
import com.hitejinro.snop.work.service.M03070Service;

/**
 * 용기 공급계획 수립
 * @author 남동희
 *
 */
@Controller
public class M03070Controller {

	@Inject
	private M03070Service m03070Service;

	@Inject
	private SessionUtil sessionUtil;

	@Inject
	private CommonUtils commonUtils;

	/**
	 * M03070
	 * @param params
	 * @return /work/M03070
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03070", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		ModelAndView view = new ModelAndView("/work/M03070");
		view.addObject("SEARCH_OPTION", params.get("SEARCH_OPTION"));

		return view;
	}

	/**
	 * M03070Std
	 * @param params {verCd}
	 * @return /work/M03070Std
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03070Std", method = RequestMethod.GET)
	public ModelAndView getViewStd(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		ModelAndView view = new ModelAndView("/work/M03070Std");
		view.addObject("SEARCH_OPTION", params.get("SEARCH_OPTION"));

		return view;
	}

	/**
	 * M03070Prdt
	 * @param params {verCd}
	 * @return /work/M03070Prdt
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03070Prdt", method = RequestMethod.GET)
	public ModelAndView getViewPrdt(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		ModelAndView view = new ModelAndView("/work/M03070Prdt");
		view.addObject("SEARCH_OPTION", params.get("SEARCH_OPTION"));

		return view;
	}

	/**
	 * M03070Result
	 * @param params {verCd}
	 * @return /work/M03070Result
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03070Result", method = RequestMethod.GET)
	public ModelAndView getViewResult(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		ModelAndView view = new ModelAndView("/work/M03070Result");
		view.addObject("SEARCH_OPTION", params.get("SEARCH_OPTION"));

		return view;
	}

	/**
	 * 조회 
	 * @param params {YYYYMM, brandCode, useYN}
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03070/search", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> search(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		Map<String, Object> result = new HashMap<String, Object>();

		result.put(Const.ATTR, m03070Service.searchBrand(params));
		result.put(Const.GRID_BODY, m03070Service.search(params));
		result.put(Const.RESULT_FLAG, "S");
		return result;
	}

	/**
	 * 저장
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03070/save", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> save(@RequestBody Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());

		Map<String, Object> result = new HashMap<String, Object>();

		User user = sessionUtil.getUserInfo();
		params.put("userId", user.getUsername());

		result = m03070Service.save(params);

		return result;
	}

	/**
	 * 선택된 버전의 정보 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03070/getVerInfo", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getVerInfo(@RequestParam Map<String, Object> params) throws Exception {
		return m03070Service.getVerInfo(params);
	}

	/**
	 * 일일 제품수급 시뮬레이션 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03070/searchDalyScmSimulVerCd", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> searchDalyScmSimulVerCd(@RequestParam Map<String, Object> params) throws Exception {
		return m03070Service.searchDalyScmSimulVerCd(params);
	}

	/**
	 * 일일 제품수급 시뮬레이션 저장
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03070/saveDalyScmSimulVerCd", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveDalyScmSimulVerCd(@RequestBody Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());

		Map<String, Object> result = new HashMap<String, Object>();

		User user = sessionUtil.getUserInfo();
		params.put("userId", user.getUsername());

		result = m03070Service.saveDalyScmSimulVerCd(params);

		return result;
	}

	/**
	 * 기준정보 조회 
	 * @param params {verCd}
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03070/searchStd", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> searchStd(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		Map<String, Object> result = new HashMap<String, Object>();

		result.put("Std", m03070Service.searchSimulStd(params));
		result.put("Val", m03070Service.searchStdValCrtType(params));
		result.put("Calc", m03070Service.searchActualCalcType(params));
		result.put("Rflt", m03070Service.searchActualRfltType(params));
		result.put(Const.GRID_BODY, m03070Service.searchStd(params));
		result.put(Const.RESULT_FLAG, "S");
		return result;
	}

	/**
	 * 기준정보 저장
	 * @param params {verCd, saveData : [{VER_CD, ...}]
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03070/saveStd", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveStd(@RequestBody Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());

		Map<String, Object> result = new HashMap<String, Object>();

		User user = sessionUtil.getUserInfo();
		params.put("userId", user.getUsername());

		result = m03070Service.saveStd(params);

		return result;
	}

	/**
	 * 제병사 생산계획 조회
	 * @param params {verCd}
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03070/searchPrdt", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> searchPrdt(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> header = m03070Service.searchPrdtHeader(params);

		if (header == null || header.size() == 0) {
			throw new UserException("조회된 데이터가 없습니다.");
		}
		params.put("header", header);

		result.put(Const.GRID_HEADER, header);
		result.put(Const.GRID_BODY, m03070Service.searchPrdt(params));
		result.put(Const.RESULT_FLAG, "S");
		return result;
	}

	/**
	 * 제병사 생산계획 저장
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03070/savePrdt", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> savePrdt(@RequestBody Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());

		Map<String, Object> result = new HashMap<String, Object>();

		User user = sessionUtil.getUserInfo();
		params.put("userId", user.getUsername());

		result = m03070Service.savePrdt(params);

		return result;
	}

	/**
	 * 용기 시뮬레이션 결과 조회
	 * @param params {verCd}
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03070/searchResult", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> searchResult(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		Map<String, Object> result = new HashMap<String, Object>();

		result.put(Const.GRID_BODY, m03070Service.searchResult(params));
		result.put(Const.RESULT_FLAG, "S");
		return result;
	}

	/**
	 * 용기 시뮬레이션 실행
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03070/simulate", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> simulate(@RequestBody Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());

		Map<String, Object> result = new HashMap<String, Object>();

		User user = sessionUtil.getUserInfo();
		params.put("userId", user.getUsername());

		result = m03070Service.simulate(params);
		return result;
	}
	
	/**
	 * 용기 시뮬레이션 결과 저장
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M03070/saveResult", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveResult(@RequestBody Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());

		Map<String, Object> result = new HashMap<String, Object>();

		User user = sessionUtil.getUserInfo();
		params.put("userId", user.getUsername());

		result = m03070Service.saveResult(params);
		return result;
	}
}
