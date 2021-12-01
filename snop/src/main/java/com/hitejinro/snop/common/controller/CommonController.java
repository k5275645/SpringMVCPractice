package com.hitejinro.snop.common.controller;

import java.util.HashMap;
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
import org.springframework.web.servlet.ModelAndView;

import com.hitejinro.snop.common.service.CommonService;
import com.hitejinro.snop.common.util.CommonUtils;
import com.hitejinro.snop.common.util.Const;
import com.hitejinro.snop.common.util.SessionUtil;
import com.hitejinro.snop.security.service.LoginUrlAuthenticationEntryPointImpl;

/**
 * 공통
 * @author 남동희
 *
 */
@Controller
public class CommonController {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

	@Inject
	private CommonService commonService;

	@Inject
	private CommonUtils commonUtils;

	@Inject
	private SessionUtil sessionUtil;

	/**
	 * 메인화면
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView main(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		ModelAndView view = new ModelAndView("/common/main");
		view.addObject("userNm", sessionUtil.getUserNm());
		return view;
	}

	/**
	 * 간단한 세션 체크(ex : iframe에 src 설정 시 세션 체크)
	 * 해당 메서드를 호출하지 않고 직접 src를 지정한 경우
	 * 세션이 유효하지 않으면 로그인 페이지가 iframe안으로 전송됨
	 * 
	 * srcdoc을 사용한다면, 해당 메서드 대신 각 화면의 url을 호출
	 * 단 srcdoc은 ie11에서 사용불가
	 * CORS를 허용하지 않음
	 * 추가적으로 모든 요청 header에 X-Requested-With = XMLHttpRequest를 지정해야함(cors policy)
	 * 
	 * 만약 세션이 유효하지 않은 상태라면, {@link LoginUrlAuthenticationEntryPointImpl}에 의해 401에러 반환
	 * @return {_RESULT_FLAG : "S"}
	 * @throws Exception
	 */
	@RequestMapping(value = "/common/isSessionValid", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> isSessionValid(@RequestParam Map<String, Object> params) throws Exception {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put(Const.RESULT_FLAG, "S");

		return result;
	}

	/**
	 * 메뉴 조회 : 권한처리
	 * @param params
	 * @return [{LEVEL, MENU_CD, MENU_NM, URL, HRNK_MENU_CD...}]
	 * @throws Exception
	 */
	@RequestMapping(value = "/common/getMenu", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getMenu(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
        
		return commonService.getMenu(params);
	}

	/**
	 * 용기관리 품목 조회
	 * @param params {liquorCode, botlType, companyType, volumeValue}
	 * @return [{CODE, NAME}]
	 * @throws Exception
	 */
	@RequestMapping(value = "/common/getVesselItem", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getVesselItem(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		return commonService.getVesselItem(params);
	}

	/**
	 * 용기관리 용기 조회
	 * @param params {liquorCode, botlType, companyType}
	 * @return [{CODE, NAME}]
	 * @throws Exception
	 */
	@RequestMapping(value = "/common/getVesselCode", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getVesselCode(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		
		params.put("liquorCode", String.valueOf(params.get("liquorCode")).split(","));

		return commonService.getVesselCode(params);
	}

	/**
	 * 용기관리 용량 조회
	 * @param params {liquorCode, botlType, companyType, vesselCode}
	 * @return [{CODE, NAME}]
	 * @throws Exception
	 */
	@RequestMapping(value = "/common/getVesselVolume", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getVesselVolume(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		params.put("liquorCode", String.valueOf(params.get("liquorCode")).split(","));
		params.put("vesselCode", String.valueOf(params.get("vesselCode")).split(","));
		
		return commonService.getVesselVolume(params);
	}

	/**
	 * 용기관리 브랜드 조회
	 * @param params {liquorCode, botlType, companyType, vesselCode, volumeValue, brandCode}
	 * @return [{CODE, NAME}]
	 * @throws Exception
	 */
	@RequestMapping(value = "/common/getVesselBrand", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getVesselBrand(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		
		params.put("liquorCode", String.valueOf(params.get("liquorCode")).split(","));
		params.put("vesselCode", String.valueOf(params.get("vesselCode")).split(","));
		params.put("volumeValue", String.valueOf(params.get("volumeValue")).split(","));

		return commonService.getVesselBrand(params);
	}

	/**
	 * 조직 조회
	 * @param params { orgType("!ALL":전체, "MFG":공장, "LOGISTICS":물류센터), liquorCode("!ALL":전체, "10":맥주, "20":소주) }
	 * @return [{ CODE, NAME }]
	 * @throws Exception
	 */
	@RequestMapping(value = "/common/getOrg", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getOrg(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		return commonService.getOrg(params);
	}

    /**
     * 품목 중분류 조회
     * @param params { liquorCode("!ALL":전체, "10":맥주, "20":소주) }
     * @return [{ CODE, NAME }]
     * @throws Exception
     */
    @RequestMapping(value = "/common/getItemSegment2List", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getItemSegment2List(@RequestParam Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        return commonService.getItemSegment2List(params);
    }

    /**
     * 품목 소분류 조회
     * @param params { liquorCode("!ALL":전체, "10":맥주, "20":소주), segment2 }
     * @return [{ CODE, NAME }]
     * @throws Exception
     */
    @RequestMapping(value = "/common/getItemSegment3List", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getItemSegment3List(@RequestParam Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        return commonService.getItemSegment3List(params);
    }

	/**
	 * 오늘의 정보 조회
	 * @param params {  }
	 * @return { YYYYMMDD, BUSINESS_DAY_FLAG, YYYY, YYYYMM, YYYYWW, SCM_YYYYWW, SCM_YYYYMMWW, DAY_OF_WEEK, DAY_OF_WEEK_DESC, SCM_YYYYWW_WORK_CNT, YYYYMM_WORK_CNT, SCM_YYYYMMWW_WORK_CNT, BF_YYYYMMDD, BF_5D_YYYYMMDD, BF_10D_YYYYMMDD, BF_20D_YYYYMMDD, BF_60D_YYYYMMDD }
	 * @throws Exception
	 */
	@RequestMapping(value = "/common/getTodayInfo", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getTodayInfo(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		return commonService.getTodayInfo(params);
	}

	/**
	 * 기준판매정의 콤보박스 리스트<p>
	 * <li>기간구분 조건절(periodGbCode) : 다중선택값(콤마로 구분)도 가능 : PG010(년), PG020(월), PG030(주), PG040(일)
	 * <li>집계유형구분 조건절(aggrTypeGbCode) : 다중선택값(콤마로 구분)도 가능 : ATG010(평균), ATG020(누계), ATG030(최대판매), ATG040(최소판매)
	 * <li>집계기간구분 조건절(aggrPrdGbCode) : 다중선택값(콤마로 구분)도 가능 : APG010(직전), APG020(전년동기), APG030(구간)
	 * <li>사용여부 조건절(useYn) : ''(전체), !ALL(전체), Y, N
	 * @param params { periodGbCode, aggrTypeGbCode, aggrPrdGbCode, useYn }
	 * @return [{ CODE, NAME, STD_SALE_DFNT_CODE, PERIOD_GB_CODE, AGGR_TYPE_GB_CODE, AGGR_PRD_GB_CODE, SALE_DFNT_NAME, APL_PRD_VAL, APL_STR_DT, APL_END_DT, USE_YN }]
	 * @throws Exception
	 */
	@RequestMapping(value = "/common/getStdSaleDfntList", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getStdSaleDfntList(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		return commonService.getStdSaleDfntList(params);
	}

	/**
	 * 판매변수정의 콤보박스 리스트<p>
	 * <li>판매변수유형 조건절(saleVarType) : 다중선택값(콤마로 구분)도 가능 : EVENT(이벤트), ISSUE(이슈), DISASTER(재해), ETC(기타)
	 * <li>사업부문 조건절(liquorCode) : 다중선택값(콤마로 구분)도 가능 : 10(맥주), 20(소주)
	 * @param params { saleVarType, liquorCode }
	 * @return [{ CODE, NAME, SALE_VAR_DFNT_SEQNO, SALE_VAR_DFNT_SEQNO_NAME, VLD_STR_DT, VLD_END_DT, SALE_VAR_TYPE, SALE_VAR_TYPE_DESC, SALE_VAR_NAME, LIQUOR_CODE, LIQUOR_DESC, USAGE_CODE, USAGE_NAME, ITEM_CODE, ITEM_NAME, VAR_VAL, CALC_STR_DT, CALC_END_DT }]
	 * @throws Exception
	 */
	@RequestMapping(value = "/common/getSaleVarDfntList", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getSaleVarDfntList(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

		return commonService.getSaleVarDfntList(params);
	}
}
