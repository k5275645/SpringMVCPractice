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

import com.hitejinro.snop.common.service.CommonComboService;
import com.hitejinro.snop.common.util.CommonUtils;
import com.hitejinro.snop.common.util.Const;
import com.hitejinro.snop.common.util.SessionUtil;
import com.hitejinro.snop.work.service.M08200Service;

/**
 * 기준정보 > Alert 기능 종합
 * @author 유기후
 *
 */
@Controller
public class M08200Controller {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(M08200Controller.class);

    @Inject
    private M08200Service m08200Service;

    @Inject
    private CommonComboService commonComboService;
    
    @Inject
    private SessionUtil sessionUtil;
    
    @Inject
    private CommonUtils commonUtils;

    /**
     * 화면 호출 : Alert 마스터
     * @param params
     * @return 
     * @throws Exception
     */
    @RequestMapping(value = "/work/M08200", method = RequestMethod.GET)
    public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        ModelAndView view = new ModelAndView("/work/M08200");
        
        // - 화면에 데이터 설정 : 사용자 리스트(USER_LIST), 사용자의 부서 리스트(USER_DEPARTMENT_LIST), 지점 리스트(DEPT_LIST). json string형식으로 변환 필요.
        view.addObject("USER_LIST", commonUtils.getListToJsonStr(m08200Service.selectUserList(params)));
        view.addObject("USER_DEPARTMENT_LIST", commonUtils.getListToJsonStr(m08200Service.selectUserDepartmentList(params)));
        view.addObject("DEPT_LIST", commonUtils.getListToJsonStr(m08200Service.selectDeptList(params)));
        
        return view;
    }

    
    
    /**
     * 데이터 조회 : Alert 리스트
     * @param params {  }
     * @return { TREEGRID_DATA:[{}], SEND_PERIOD_CODE_LIST:[{}], SEND_DEPT_TYPE_LIST:[{}] }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M08200/search", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> search(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 데이터 조회 : [{ ALERT_CODE, ALERT_NAME, ALERT_DESC, ALERT_TYPE, SEND_PERIOD_CODE, SEND_HR, SEND_TYPE, USE_YN, SEND_DEPT_TYPE, PROGRAM_NAME, MSG, RMKS, TARGET_CNT, CODE, NAME }]
        List<Map<String, Object>> arrDataList = m08200Service.search(params);
        mResult.put("TREEGRID_DATA", arrDataList);

        // - 추가 데이터 조회해서 담기 : 발송주기코드(공통그룹코드=SEND_PERIOD_CODE), 발송조직유형(공통그룹코드=SEND_DEPT_TYPE)
        Map<String, Object> mSendPeriodCodeListParam = new HashMap<String, Object>();
        mSendPeriodCodeListParam.put("groupCode", "SEND_PERIOD_CODE");
        mSendPeriodCodeListParam.put("useYn", "Y");
        mResult.put("SEND_PERIOD_CODE_LIST", commonComboService.getComCodeCombo(mSendPeriodCodeListParam)); // - 공통그룹코드=SEND_PERIOD_CODE : [{ CODE, NAME }]
        Map<String, Object> mSendDeptTypeListParam = new HashMap<String, Object>();
        mSendDeptTypeListParam.put("groupCode", "SEND_DEPT_TYPE");
        mSendDeptTypeListParam.put("useYn", "Y");
        mResult.put("SEND_DEPT_TYPE_LIST", commonComboService.getComCodeCombo(mSendDeptTypeListParam)); // - 공통그룹코드=SEND_DEPT_TYPE : [{ CODE, NAME }]
        
        return mResult;
    }

    /**
     * 데이터 저장 : Alert 리스트
     * @param params { saveData:[{}] }
     * @return { _RESULT_FLAG, _RESULT_MSG }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M08200/save", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> save(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
        
        try {
            // - 저장 처리
            mResult = m08200Service.save(params);

        } catch (Exception e) {
            mResult.put(Const.RESULT_FLAG, "F");
            mResult.put(Const.RESULT_MSG, "저장 중 오류 발생" + " : " + e.getMessage());
            commonUtils.debugError(e);
            return mResult;
        }

        return mResult;
    }

    /**
     * Alert 발송 호출 : 자동발송이 아닌 수동으로, 해당 Alert을 강제 발송
     * @param params { alertCode, programName }
     * @return { _RESULT_FLAG, _RESULT_MSG }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M08200/callAlertSend", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> callAlertSend(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
        
        try {
            // - 호출 처리
            mResult = m08200Service.callAlertSend(params);

        } catch (Exception e) {
            mResult.put(Const.RESULT_FLAG, "F");
            mResult.put(Const.RESULT_MSG, "호출 중 오류 발생" + " : " + e.getMessage());
            commonUtils.debugError(e);
            return mResult;
        }

        return mResult;
    }


    /**
     * Alert 대상자 데이터 조회
     * @param params { alertCode }
     * @return { TREEGRID_DATA:[{}] }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M08200/searchAlertTarget", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> searchAlertTarget(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 데이터 조회 : [{ ALERT_CODE, TARGET_TYPE, TARGET_VAL, SEND_DEPT_TYPE, SEND_DEPT_VAL, USE_YN }]
        List<Map<String, Object>> arrDataList = m08200Service.searchAlertTarget(params);
        mResult.put("TREEGRID_DATA", arrDataList);
        
        return mResult;
    }

    /**
     * Alert 대상자 데이터 저장
     * @param params { saveData:[{}] }
     * @return { _RESULT_FLAG, _RESULT_MSG }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M08200/saveAlertTarget", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> saveAlertTarget(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
        
        try {
            // - 저장 처리
            mResult = m08200Service.saveAlertTarget(params);

        } catch (Exception e) {
            mResult.put(Const.RESULT_FLAG, "F");
            mResult.put(Const.RESULT_MSG, "저장 중 오류 발생" + " : " + e.getMessage());
            commonUtils.debugError(e);
            return mResult;
        }

        return mResult;
    }


    
    /**
     * 수동 Alert 발송
     * @param params { msg, saveData:[{}] }
     * @return { _RESULT_FLAG, _RESULT_MSG }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M08200/sendManualAlert", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> sendManualAlert(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
        
        try {
            // - 저장 처리
            mResult = m08200Service.sendManualAlert(params);

        } catch (Exception e) {
            mResult.put(Const.RESULT_FLAG, "F");
            mResult.put(Const.RESULT_MSG, "발송 중 오류 발생" + " : " + e.getMessage());
            commonUtils.debugError(e);
            return mResult;
        }

        return mResult;
    }
    
}
