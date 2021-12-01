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
import com.hitejinro.snop.work.service.M08230Service;

/**
 * 기준정보 > 용기 관리
 * @author 유기후
 *
 */
@Controller
public class M08230Controller {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(M08230Controller.class);

    @Inject
    private M08230Service m08230Service;
    
    @Inject
    private CommonUtils commonUtils;

    /**
     * 화면 호출
     * @param params
     * @return 
     * @throws Exception
     */
    @RequestMapping(value = "/system/M08230", method = RequestMethod.GET)
    public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);
        
        ModelAndView view = new ModelAndView("/system/M08230");
        return view;
    }

    /**
     * 데이터 조회
     * @param params { frYYYYMMDD, toYYYYMMDD }
     * @return { TREEGRID_DATA:[{}] }
     * @throws Exception
     */
    @RequestMapping(value = "/system/M08230/search", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> search(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 데이터 조회
        List<Map<String, Object>> arrDataList = m08230Service.search(params);
        mResult.put("TREEGRID_DATA", arrDataList);

        return mResult;
    }

}
