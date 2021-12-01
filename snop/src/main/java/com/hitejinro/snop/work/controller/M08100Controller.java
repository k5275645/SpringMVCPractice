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

import com.hitejinro.snop.common.service.CommonComboService;
import com.hitejinro.snop.common.util.CommonUtils;
import com.hitejinro.snop.common.util.Const;
import com.hitejinro.snop.common.util.SessionUtil;
import com.hitejinro.snop.common.util.UserException;
import com.hitejinro.snop.work.service.M08100Service;

/**
 * 생산 라인별 CAPA 설정
 * @author 김남현
 *
 */
@Controller
public class M08100Controller {

	@Inject
	private M08100Service m08100Service;
	
	@Inject
	private CommonComboService commonComboService;

	@Inject
	private SessionUtil sessionUtil;
	
	@Inject
	private CommonUtils commonUtils;
	
	/**
	 * /work/M08100
	 * @param params
	 * @return /work/M08100
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08100", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		
		ModelAndView view = new ModelAndView("/work/M08100");
		view.addObject("searchWord", params.get("searchWord"));
		return view;
	}
	
	/**
	 * 생산 라인별 CAPA 설정 > 조회
	 * @param params { verNm, useYn }
	 * @return	{ Body : [{PRDT_VAR_VER_CD, PRDT_VAR_VER_NM, PRDT_VAR_VER_DESC...}], _RESULT_FLAG }
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08100/search", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> search(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put(Const.GRID_BODY, m08100Service.search(params));
		result.put(Const.RESULT_FLAG, "S");
		
		return result;
	}
	
	/**
	 * 생산 라인별 CAPA 설정 > 저장
	 * @param params { saveData }
	 * @return { _RESULT_MSG, _RESULT_FLAG }
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08100/saveMst", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveMst(@RequestBody Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());

		Map<String, Object> result = new HashMap<String, Object>();

		sessionUtil.setUserInfoParam(params);

		result = m08100Service.save(params);

		return result;
	}
	
	/**
	 * 생산 라인별 CAPA 설정 > 갱신
	 * @param params { prdtVarVerCd }
	 * @return { _RESULT_MSG, _RESULT_FLAG }
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08100/renew", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> renew(@RequestBody Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());

		Map<String, Object> result = new HashMap<String, Object>();

		sessionUtil.setUserInfoParam(params);

		result = m08100Service.renew(params);

		return result;
	}
	
	/**
	 * 생산 라인별 CAPA 설정 > 버전복사
	 * @param params { fromPrdtVarVerCd, toPrdtVarVerCd }
	 * @return { _RESULT_MSG, _RESULT_FLAG }
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08100/verCopy", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> verCopy(@RequestBody Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());

		Map<String, Object> result = new HashMap<String, Object>();

		sessionUtil.setUserInfoParam(params);

		result = m08100Service.verCopy(params);

		return result;
	}
	
	/**
	 * /work/M08100Dtl
	 * @param params
	 * @return /work/M08100Dtl
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08100Dtl", method = RequestMethod.GET)
	public ModelAndView getDetailView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		
		ModelAndView view = new ModelAndView("/work/M08100Dtl");
		view.addObject("prdtVarVerCd", params.get("PRDT_VAR_VER_CD"));
		view.addObject("prdtVarVerNm", params.get("PRDT_VAR_VER_NM"));
		view.addObject("useYn", params.get("USE_YN"));
		view.addObject("prdtInfoUpdateDate", params.get("PRDT_INFO_UPDATE_DATE"));
		view.addObject("searchWord", params.get("searchWord"));
		return view;
	}
	
	/**
	 * 생산 라인별 CAPA 설정 > 상세 > 공장 콤보 조회
	 * @param params {}
	 * @return [{ CODE, NAME }]
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08100/getMfgList", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getMfgList(@RequestParam Map<String, Object> params) throws Exception {
	    commonUtils.debugParams(params);

		return m08100Service.getMfgList(params);
	}
	
	/**
	 * 생산 라인별 CAPA 설정 > 상세 > 조회
	 * @param params { prdtVarVerCd, liquorCd, orgCd, expCd }
	 * @return { Header1 : [{}], Header2 : [{}], Body : [{PRDT_VAR_VER_CD, ORG_CODE, LINE_DEPT_CODE, ITEM_CODE..}], MFG_LIST, VESSEL_LIST, _RESULT_FLAG}
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08100/searchDetail", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> searchDetail(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("MFG_LIST", m08100Service.getMfgList(params));
		result.put("VESSEL_LIST", commonComboService.getVesselComboList(params));

		List<Map<String, Object>> header1 = m08100Service.searchDetailHeader1(params);
		List<Map<String, Object>> header2 = m08100Service.searchDetailHeader2(params);

		if (header1 == null || header1.size() == 0 || header2 == null || header2.size() == 0) {
			throw new UserException("조회된 데이터가 없습니다.");
		}
		
		params.put("header1", header1);
		params.put("header2", header2);
		
		List<Map<String, Object>> body = m08100Service.searchDetail(params);
		
		result.put("Header1", header1);
		result.put("Header2", header2);
		result.put(Const.GRID_BODY, body);
		result.put(Const.RESULT_FLAG, "S");

		return result;
	}
    
    /**
     * 생산 라인별 CAPA 설정 > 상세 > 제품라우팅 조회
     * @param params { liquorCd, prdtVarVerCd, orgCd, expCd, searchItem }
     * @return { Body : [{PRDT_VAR_VER_CD, ORG_CODE, LINE_DEPT_CODE, ITEM_CODE..}], MFG_LIST, VESSEL_LIST, _RESULT_FLAG}
     * @throws Exception
     */
    @RequestMapping(value = "/work/M08100/selectItemRouting", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> selectItemRouting(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 데이터 조회
        mResult.put("TREEGRID_ITEM_ROUTING", m08100Service.selectItemRouting(params));
        
        return mResult;
    }
	
	/**
	 * 생산 라인별 CAPA 설정 > 상세 > 저장
	 * @param params { saveData }
	 * @return { _RESULT_MSG, _RESULT_FLAG }
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08100/saveDetailMst", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveDetailMst(@RequestBody Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());

		Map<String, Object> result = new HashMap<String, Object>();

		sessionUtil.setUserInfoParam(params);

		result = m08100Service.saveDetailMst(params);

		return result;
	}
	
	/**
	 * 생산 라인별 CAPA 설정 > 상세 > 일일 할당시간관리 팝업 > 조회
	 * @param params { prdtVarVerCd }
	 * @return [{ PRDT_VAR_VER_CD, AVL_HR.. }]
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08100/searchSftHr", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> searchSftHr(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		
		Map<String, Object> result = new HashMap<String, Object>();

		result.put(Const.GRID_BODY, m08100Service.searchSftHr(params));
		result.put(Const.RESULT_FLAG, "S");

		return result;
	}
	
	/**
	 * 생산 라인별 CAPA 설정 > 상세 > 일일 할당시간관리 팝업 > 저장
	 * @param params { saveData }
	 * @return { _RESULT_MSG, _RESULT_FLAG }
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08100/saveSftHr", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveSftHr(@RequestBody Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());

		Map<String, Object> result = new HashMap<String, Object>();

		sessionUtil.setUserInfoParam(params);

		result = m08100Service.saveSftHr(params);

		return result;
	}
	
	/**
	 * 생산 라인별 CAPA 설정 > 상세 > 라인관리 팝업 > 조회 
	 * @param params { prdtVarVerCd }
	 * @return [{ PRDT_VAR_VER_CD, ORG_CODE, LINE_DEPT_CODE.. }]
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08100/searchNewLine", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> searchNewLine(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put(Const.GRID_BODY, m08100Service.searchNewLine(params));
		result.put(Const.RESULT_FLAG, "S");

		return result;
	}
	
	/**
	 * 생산 라인별 CAPA 설정 > 상세 > 라인관리 팝업 > 저장
	 * @param params { saveData }
	 * @return { _RESULT_MSG, _RESULT_FLAG }
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08100/saveNewLine", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveNewLine(@RequestBody Map<String, Object> params) throws Exception {
		commonUtils.debugParam(params.toString());

		Map<String, Object> result = new HashMap<String, Object>();

		sessionUtil.setUserInfoParam(params);

		result = m08100Service.saveNewLine(params);

		return result;
	}
	
}
