package com.hitejinro.snop.work.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hitejinro.snop.common.util.CommonUtils;
import com.hitejinro.snop.common.util.Const;
import com.hitejinro.snop.common.util.SessionUtil;
import com.hitejinro.snop.common.util.UserException;
import com.hitejinro.snop.work.service.M09040Service;

/**
 * 생산관리 > 원부자재/포장자재 사용 실적
 * @author 김남현
 *
 */
@Controller
public class M09040Controller {

	@Inject
	private M09040Service m09040Service;

	@Inject
	private SessionUtil sessionUtil;
	
	@Inject
	private CommonUtils commonUtils;
	
	/**
	 * M09040
	 * @param params
	 * @return /work/M09040
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M09040", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		
		ModelAndView view = new ModelAndView("/work/M09040");
		return view;
	}
	
	/**
	 * 원부자재/포장자재 사용 실적 > 소분류 Combo 조회
	 * @param params {}
	 * @return { BODY : [{CODE, NAME}], _RESULT_FLAG }
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M09040/getSegment3Combo", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getSegment3Combo(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<Map<String, Object>> segment3Combo = m09040Service.getSegment3Combo(params);
		
		if (segment3Combo == null || segment3Combo.size() == 0) {
			throw new UserException("조회된 데이터가 없습니다.");
		}
		
		result.put("BODY", segment3Combo);
		result.put(Const.RESULT_FLAG, "S");
		
		return result;
	}
	
	/**
	 * 원부자재/포장자재 사용 실적 > 원부자재 > 조회
	 * @param { yyyymm, mfgCd, segment3Cd, brandCd, volumeCd, receiptTp, issueTp }
	 * @return { Header : [{FR_SCM_YYYYWW, TO_SCM_YYYYWW, STD_PERIOD, CODE, NAME, HEADER1_DESC, HEADER1_SPAN, HEADER2_DESC, SEQ }], Body : [{ ORG_CODE, ORG_NAME, SEGMENT1, ITEM_CODE, DESCRIPTION, COL1...}], _RESULT_FLAG }
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M09040/searchRowSubMat", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> searchRowSubMat(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		// 헤더 조회
		List<Map<String, Object>> header = m09040Service.searchRowSubMatHeader(params);
		
		if (header == null || header.size() == 0) {
			throw new UserException("조회된 데이터가 없습니다.");
		}
		params.put("header", header);
		
		List<Map<String, Object>> body = m09040Service.searchRowSubMatBody(params);
		
		result.put(Const.GRID_HEADER, header);
		result.put(Const.GRID_BODY, body);
		result.put(Const.RESULT_FLAG, "S");

		return result;
	}
	
	/**
	 * 원부자재/포장자재 사용 실적 > 포장자재 > 조회
	 * @param { yyyymm, mfgCd, segment3Cd, brandCd, volumeCd, receiptTp, issueTp }
	 * @return { Header : [{FR_SCM_YYYYWW, TO_SCM_YYYYWW, STD_PERIOD, CODE, NAME, HEADER1_DESC, HEADER1_SPAN, HEADER2_DESC, SEQ }], Body : [{ ORG_CODE, ORG_NAME, SEGMENT2, SEGMENT3, PK_SORT, ITEM_CODE, DESCRIPTION, COL1...}], _RESULT_FLAG }
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M09040/searchPackMat", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> searchPackMat(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		params.put("receiptTp", ((String) params.get("receiptTp")).split(","));
		params.put("issueTp", ((String) params.get("issueTp")).split(","));
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		// 헤더 조회
		List<Map<String, Object>> header = m09040Service.searchPackMatHeader(params);
		
		if (header == null || header.size() == 0) {
			throw new UserException("조회된 데이터가 없습니다.");
		}
		params.put("header", header);
		
		
		
		List<Map<String, Object>> body = m09040Service.searchPackMatBody(params);
		
		result.put(Const.GRID_HEADER, header);
		result.put(Const.GRID_BODY, body);
		result.put(Const.RESULT_FLAG, "S");

		return result;
	}
	
}
