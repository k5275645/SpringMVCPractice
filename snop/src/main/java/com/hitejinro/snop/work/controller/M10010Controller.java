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

import com.hitejinro.snop.common.util.CommonUtils;
import com.hitejinro.snop.common.util.Const;
import com.hitejinro.snop.common.util.SessionUtil;
import com.hitejinro.snop.work.service.M10010Service;

/**
 * SnOP회의 > 주간S&OP회의 화면준비
 * @author 유기후
 *
 */
@Controller
public class M10010Controller {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(M10010Controller.class);

    @Inject
    private M10010Service m10010Service;

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
    @RequestMapping(value = "/work/M10010", method = RequestMethod.GET)
    public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);
        
        ModelAndView view = new ModelAndView("/work/M10010");
        
        return view;
    }

    /**
     * 데이터 조회
     * @param params {  }
     * @return { TREEGRID_DATA:[{}], LEAF_MENU_LIST:[{}] }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M10010/search", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> search(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 데이터 조회 : [{ MENU_CD, MENU_NM, MENU_DESC, MENU_NM_PATH, SEQ, RMKS, MENU_VAR }]
        List<Map<String, Object>> arrDataList = m10010Service.search(params);
        mResult.put("TREEGRID_DATA", arrDataList);
        
        // - 최하위 메뉴 리스트 조회 : [{ MENU_CD, MEMU_NM, MENU_DESC, MENU_NM_PATH, LVL, ... }]
        mResult.put("LEAF_MENU_LIST", m10010Service.selectLeafMenuList(params));

        return mResult;
    }

    /**
     * 데이터 저장
     * @param params { saveData:[{}] }
     * @return { _RESULT_FLAG, _RESULT_MSG }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M10010/save", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> save(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
        
        try {
            // - 저장 처리
            mResult = m10010Service.save(params);

        } catch (Exception e) {
            mResult.put(Const.RESULT_FLAG, "F");
            mResult.put(Const.RESULT_MSG, "저장 중 오류 발생" + " : " + e.getMessage());
            commonUtils.debugError(e);
            return mResult;
        }

        return mResult;
    }
}
