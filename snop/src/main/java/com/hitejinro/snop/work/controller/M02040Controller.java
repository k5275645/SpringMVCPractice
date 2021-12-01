package com.hitejinro.snop.work.controller;

import java.util.HashMap;
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

import com.hitejinro.snop.common.util.CommonUtils;
import com.hitejinro.snop.common.util.Const;
import com.hitejinro.snop.common.util.SessionUtil;
import com.hitejinro.snop.work.service.M02040Service;

/**
 * 제품수급 > 계획대비 실적 차이 분석
 * @author 유기후
 *
 */
@Controller
public class M02040Controller {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(M02040Controller.class);

    @Inject
    private M02040Service m02040Service;
    
    @Inject
    private SessionUtil sessionUtil;
    
    @Inject
    private CommonUtils commonUtils;

    /**
     * 화면 호출
     * @param params
     * @return 
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02040", method = RequestMethod.GET)
    public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);
        
        ModelAndView view = new ModelAndView("/work/M02040");
        
        // - 화면 초기값 설정 : json string형식으로 변환 필요
        Map<String, Object> mSearchOption = new HashMap<String, Object>();
        if(params.get("SEARCH_OPTION") != null && !"".equals((String)params.get("SEARCH_OPTION"))) {
            try {
                mSearchOption = commonUtils.getJsonStrToMap((String)params.get("SEARCH_OPTION"));
            } catch(Exception e) {
                commonUtils.debugError(e);
            }
        }
        view.addObject("SEARCH_OPTION", commonUtils.getMapToJsonStr(mSearchOption));
        
        return view;
    }

    /**
     * 데이터 조회
     * @param params { fromYYYYMM, toYYYYMM, liquorCode, usageCode, vesselCode, volumeValue, acctCd }
     * @return { TREEGRID_HEADER[{  }], TREEGRID_BODY[{  }] }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02040/search", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> search(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 데이터 조회
        mResult = m02040Service.search(params);
        
        return mResult;
    }

    /**
     * 데이터 저장
     * @param params { fromYYYYMM, toYYYYMM, liquorCode, usageCode, vesselCode, volumeValue, acctCd, saveData:[{}], changeRowInfoList:[{}], TREEGRID_HEADER:[{}] }
     * @return { _RESULT_FLAG, _RESULT_MSG }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M02040/save", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> save(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
        
        try {
            // - 저장 처리
            mResult = m02040Service.save(params);

        } catch (Exception e) {
            mResult.put(Const.RESULT_FLAG, "F");
            mResult.put(Const.RESULT_MSG, "저장 중 오류 발생" + " : " + e.getMessage());
            commonUtils.debugError(e);
            return mResult;
        }

        return mResult;
    }
}
