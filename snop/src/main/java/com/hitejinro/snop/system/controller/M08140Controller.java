package com.hitejinro.snop.system.controller;

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
import com.hitejinro.snop.system.service.M08140Service;

/**
 * 사용자 관리
 * @author 김남현
 *
 */
@Controller
public class M08140Controller {

	@Inject
	private M08140Service m08140Service;

    @Inject
    private CommonComboService commonComboService;

	@Inject
	private SessionUtil sessionUtil;
	
	@Inject
	private CommonUtils commonUtils;
	
	/**
	 * M08140
	 * @param params
	 * @return /system/M08140
	 * @throws Exception
	 */
	@RequestMapping(value = "/system/M08140", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);
		
		ModelAndView view = new ModelAndView("/system/M08140");
		return view;
	}
	
	/**
	 * 사용자관리 조회
	 * @param params { searchType, searchWord, useYN }
	 * @return { TREEGRID_DATA:[{}], AUTH_LIST:[{}], ORG_LIST:[{}] }
	 * @throws Exception
	 */
	@RequestMapping(value = "/system/M08140/search" , produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> search(@RequestParam Map<String, Object> params) throws Exception {
		commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();
        
        mResult.putAll(m08140Service.search(params));
        
        // - 
        Map<String, Object> mAuthOrgLevelParam = new HashMap<String, Object>();
        mAuthOrgLevelParam.put("groupCode", "AUTH_ORG_LEVEL");
        mAuthOrgLevelParam.put("useYn", "Y");
        mResult.put("AUTH_ORG_LEVEL_LIST", commonComboService.getComCodeCombo(mAuthOrgLevelParam)); // - 공통그룹코드=AUTH_ORG_LEVEL(조직레벨권한) : [{ CODE, NAME }]
		
		return mResult;
	}
	
	/**
	 * 사용자관리 저장
	 * @param params { saveData, changeRowInfoList }
	 * @return { _RESULT_FLAG, _RESULT_MSG }
	 * @throws Exception
	 */
	@RequestMapping(value = "/system/M08140/save", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveMst(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
        
        try {
            // - 저장 처리
            mResult = m08140Service.save(params);

        } catch (Exception e) {
            mResult.put(Const.RESULT_FLAG, "F");
            mResult.put(Const.RESULT_MSG, "저장 중 오류 발생" + " : " + e.getMessage());
            commonUtils.debugError(e);
            return mResult;
        }

        return mResult;
	}

}
