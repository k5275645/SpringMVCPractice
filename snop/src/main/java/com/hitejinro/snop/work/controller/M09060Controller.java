package com.hitejinro.snop.work.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hitejinro.snop.common.util.CommonUtils;
import com.hitejinro.snop.common.util.SessionUtil;
import com.hitejinro.snop.work.service.M09060Service;

/**
 * 생산관리 > 제조원가
 * @author 유기후
 *
 */
@Controller
public class M09060Controller {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(M09060Controller.class);

    @Inject
    private M09060Service m09060Service;
    
    @Inject
    private CommonUtils commonUtils;
    
    @Inject
    private SessionUtil sessionUtil;

    /**
     * 화면 호출
     * @param params
     * @return 
     * @throws Exception
     */
    @RequestMapping(value = "/work/M09060", method = RequestMethod.GET)
    public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);
        
        ModelAndView view = new ModelAndView("/work/M09060");
        
        // - 화면 초기값 설정 : json string형식으로 변환 필요
        Map<String, Object> mSearchOption = new HashMap<String, Object>();
        if(params.get("SEARCH_OPTION") != null && !"".equals((String)params.get("SEARCH_OPTION"))) {
            try {
                mSearchOption = commonUtils.getJsonStrToMap((String)params.get("SEARCH_OPTION"));
            } catch(Exception e) {
                commonUtils.debugError(e);
            }
        }
        // - 최신 마감년월 설정
        if(StringUtils.isEmpty(mSearchOption.get("toYYYYMM"))) {
            List<Map<String, Object>> arrMaxPeriod = m09060Service.selectMaxPeriod(params);
            if(arrMaxPeriod != null && arrMaxPeriod.size() == 1 && !StringUtils.isEmpty(arrMaxPeriod.get(0).get("MAX_YYYYMM"))) {
                mSearchOption.put("frYYYYMM", ((String)arrMaxPeriod.get(0).get("MAX_YYYYMM")).substring(0, 4) + "01");
                mSearchOption.put("toYYYYMM", arrMaxPeriod.get(0).get("MAX_YYYYMM"));
            }
        }
        
        view.addObject("SEARCH_OPTION", commonUtils.getMapToJsonStr(mSearchOption));
        
        return view;
    }

    /**
     * 데이터 조회
     * @param params { frYYYYMM, toYYYYMM, liquorCode, orgCode, itemSegment2, itemSegment3, brandCode, usageCode, vesselCode, domExpCode, mainFlag, eventItemFlag }
     * @return { TREEGRID_HEADER[{  }], TREEGRID_BODY[{  }], CHART_DATA[{  }] }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M09060/search", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> search(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);

        // - 데이터 조회
        mResult = m09060Service.search(params);
        
        return mResult;
    }
    
}
