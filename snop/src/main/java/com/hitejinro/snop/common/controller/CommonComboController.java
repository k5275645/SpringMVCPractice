package com.hitejinro.snop.common.controller;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hitejinro.snop.common.service.CommonComboService;
import com.hitejinro.snop.common.util.CommonUtils;

/**
 * 콤보생성 공통
 * @author 김태환
 *
 */
@Controller
public class CommonComboController {

	@SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(CommonComboController.class);

	@Inject
	private CommonComboService commonComboService;
    
    @Inject
    private CommonUtils commonUtils;

	/**
	 * 년도 콤보리스트 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/commonCombo/getYearComboList", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getYearComboList(@RequestParam Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

		return commonComboService.getYearComboList(params);
	}
	
	/**
	 * 월 콤보리스트 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/commonCombo/getMonthComboList", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getMonthComboList(@RequestParam Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

		return commonComboService.getMonthComboList(params);
	}

	/**
	 * 사업부문 콤보리스트 조회
	 * @param params { hasCommon(공통 포함 여부. Y(포함), N(Default. 미포함)) }
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/commonCombo/getLiquorComboList", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getLiquorComboList(@RequestParam Map<String, Object> params) throws Exception {
	    commonUtils.debugParams(params);

		return commonComboService.getLiquorComboList(params);
	}

	/**
	 * 브랜드 콤보리스트 조회
	 * @param params { liquorCode }
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/commonCombo/getBrandComboList", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getBrandComboList(@RequestParam Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

		return commonComboService.getBrandComboList(params);
	}

	/**
	 * 용도 콤보리스트 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/commonCombo/getUsageComboList", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getUsageComboList(@RequestParam Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

		return commonComboService.getUsageComboList(params);
	}

	/**
	 * 용기 콤보리스트 조회
	 * @param params { liquorCode }
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/commonCombo/getVesselComboList", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getVesselComboList(@RequestParam Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

		return commonComboService.getVesselComboList(params);
	}

	/**
	 * 용량 콤보리스트 조회
	 * @param params { liquorCode }
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/commonCombo/getVolumeComboList", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getVolumeComboList(@RequestParam Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

		return commonComboService.getVolumeComboList(params);
	}

	/**
	 * 공통코드 콤보리스트 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/commonCombo/getComCodeCombo", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getComCodeCombo(@RequestParam Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

		return commonComboService.getComCodeCombo(params);
	}
}
