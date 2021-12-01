package com.hitejinro.snop.work.controller;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hitejinro.snop.common.service.CommonComboService;
import com.hitejinro.snop.common.util.CommonUtils;
import com.hitejinro.snop.common.util.Const;
import com.hitejinro.snop.common.util.SessionUtil;
import com.hitejinro.snop.work.service.M08130Service;

/**
 * 판매변수 정의(코로나, 명절, 휴가 등)
 * @author 김남현
 *
 */
@Controller
public class M08130Controller {

	@Inject
	private M08130Service m08130Service;
	
	@Inject
	private CommonComboService commonComboService;

	@Inject
	private SessionUtil sessionUtil;
	
	@Inject
	private CommonUtils commonUtils;
	
	/**
	 * /work/M08130
	 * @param params
	 * @return /work/M08130
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08130", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		
		ModelAndView view = new ModelAndView("/work/M08130");
		return view;
	}
	
	/**
	 * 판매변수 정의(코로나, 명절, 휴가 등) 조회
	 * @param params { year }
	 * @return { [{ SALE_VAR_DFNT_SEQNO, VLD_STR_DT, VLD_END_DT... }], SAVE_VAR_LIST, LIQUOR_LIST, USAGE_LIST, ITEM_LIST }
	 * @throws Exception
	 */
	@RequestMapping(value ="/work/M08130/search", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> search(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(Const.GRID_BODY, m08130Service.search(params));
		
		// 판매 변수 리시트
		Map<String, Object> saleVarParam = new HashMap<String, Object>();
		saleVarParam.put("groupCode", "SALE_VAR_TYPE");
		result.put("SALE_VAR_LIST", commonComboService.getComCodeCombo(saleVarParam));
		
		// 사업부문 리스트
		Map<String, Object> liquorParam = new HashMap<String, Object>();
		liquorParam.put("hasCommon", "Y");
		result.put("LIQUOR_LIST", commonComboService.getLiquorComboList(liquorParam));
		
		// 용도 리스트
		result.put("USAGE_LIST", commonComboService.getUsageComboList(params));
		
		result.put(Const.RESULT_FLAG, "S");
		
		return result;
	}
	
	/**
	 * 판매변수 정의(코로나, 명절, 휴가 등) 매핑(사업부문, 용도) 아이템 리스트 조회
	 * @param params { LIQUOR_CODE, USAGE_CODE }
	 * @return [{ ITEM_CODE, ITEM_NAME, ITEM_CODE_NAME }]
	 * @throws Exception
	 */
	@RequestMapping(value ="/work/M08130/getMappingItemList", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getMappingItemList(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		// 아이템 리스트
		result.put("ITEM_LIST", m08130Service.getMappingItemList(params));
		result.put(Const.RESULT_FLAG, "S");
		
		return result;
	}
	
	/**
	 * 저장
	 * @param params { saveData }
	 * @return { _RESULT_FLAG, _RESULT_MSG }
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08130/save", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveMst(@RequestBody Map<String, Object> params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		
		sessionUtil.setUserInfoParam(params);
		
		result = m08130Service.save(params);
		
		return result;
	}
	
	
	
}
