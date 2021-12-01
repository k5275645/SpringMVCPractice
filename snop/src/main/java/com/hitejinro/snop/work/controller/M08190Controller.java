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
import com.hitejinro.snop.work.service.M08190Service;

/**
 * 기준정보 > 용기 관리
 * @author 유기후
 *
 */
@Controller
public class M08190Controller {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(M08190Controller.class);

    @Inject
    private M08190Service m08190Service;

    @Inject
    private CommonComboService commonComboService;
    
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
    @RequestMapping(value = "/work/M08190", method = RequestMethod.GET)
    public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);
        
        ModelAndView view = new ModelAndView("/work/M08190");
        return view;
    }

    /**
     * 데이터 조회
     * @param params { liquorCode }
     * @return { TREEGRID_DATA:[{}] }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M08190/search", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> search(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 데이터 조회
        List<Map<String, Object>> arrDataList = m08190Service.search(params);
        mResult.put("TREEGRID_DATA", arrDataList);
        
        // - 추가 데이터 조회해서 담기 : 용기제품마스터, 브랜드, 용량, 용기, 병구분(공통그룹코드=BOTL_TYPE), 생통구분(공통그룹코드=KEG_TYPE), 회사구분(공통그룹코드=COMPANY_TYPE), 용기자산 브랜드/볼륨 리스트
        mResult.put("RI_ITEM_LIST", m08190Service.selectRiItemList(params)); // - [{ ITEM_CODE, ITEM_NAME, ITEM_CODE_NAME, ITEM_TYPE, ITEM_STATUS, PRIMARY_UOM_CODE, LIQUOR_CODE, VESSEL_CODE, VESSEL_SORT, VOLUME_VALUE, SEGMENT1, SEGMENT2, SEGMENT3 }]
        Map<String, Object> mBrandParam = new HashMap<String, Object>();
        mBrandParam.put("groupCode", "BOTL_BRAND_LIST");
        mBrandParam.put("useYn", "Y");
        mResult.put("BRAND_LIST", commonComboService.getComCodeCombo(mBrandParam)); // - 공통그룹코드=BOTL_BRAND_LIST(별도로 정의된 브랜드) : [{ CODE, NAME }]
        mResult.put("VOLUME_LIST", commonComboService.getVolumeComboList(params)); // - [{ CODE, NAME }]
        mResult.put("VESSEL_LIST", m08190Service.selectVesselList(params)); // - 실제 용기 리스트 + 공통그룹코드=OTHER_VESSEL_LIST : [{ CODE, NAME }]
        Map<String, Object> mBotlTypeParam = new HashMap<String, Object>();
        mBotlTypeParam.put("groupCode", "BOTL_TYPE");
        mBotlTypeParam.put("useYn", "Y");
        mResult.put("BOTL_TYPE_LIST", commonComboService.getComCodeCombo(mBotlTypeParam)); // - [{ CODE, NAME }]
        Map<String, Object> mKegTypeParam = new HashMap<String, Object>();
        mKegTypeParam.put("groupCode", "KEG_TYPE");
        mKegTypeParam.put("useYn", "Y");
        mResult.put("KEG_TYPE_LIST", commonComboService.getComCodeCombo(mKegTypeParam)); // - [{ CODE, NAME }]
        Map<String, Object> mCompanyTypeParam = new HashMap<String, Object>();
        mCompanyTypeParam.put("groupCode", "COMPANY_TYPE");
        mCompanyTypeParam.put("useYn", "Y");
        mResult.put("COMPANY_TYPE_LIST", commonComboService.getComCodeCombo(mCompanyTypeParam)); // - [{ CODE, NAME }]
        mResult.put("VESSEL_ASSET_BRAND_VOLUME_LIST", m08190Service.selectVesselAssetBrandVolumeList(params)); // - 용기자산 브랜드/볼륨 리스트 : [{ CODE, NAME, ASSET_BRAND_NAME, ASSET_VOLUME_NAME }]

        return mResult;
    }

    /**
     * 데이터 저장
     * @param params { liquorCode, saveData:[{}] }
     * @return { _RESULT_FLAG, _RESULT_MSG }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M08190/save", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> save(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
        
        try {
            // - 저장 처리
            mResult = m08190Service.save(params);

        } catch (Exception e) {
            mResult.put(Const.RESULT_FLAG, "F");
            mResult.put(Const.RESULT_MSG, "저장 중 오류 발생" + " : " + e.getMessage());
            commonUtils.debugError(e);
            return mResult;
        }

        return mResult;
    }

    /**
     * 데이터 조회 : 용기브랜드. 팝업 그리드 조회용
     * @param params {  }
     * @return { TREEGRID_VESSEL_BRAND:[{}], BRAND_LIST:[{}] }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M08190/searchVesselBrand", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> searchVesselBrand(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 데이터 조회
        mResult.put("TREEGRID_VESSEL_BRAND", m08190Service.searchVesselBrand(params)); // - 용기브랜드 : [{ LIQUOR_CODE, VESSEL_BRAND_CODE, VESSEL_BRAND_NAME, VESSEL_BRAND_SEQ, VESSEL_BRAND_USE_YN, BRAND_LIST }]
        mResult.put("ITEM_BRAND_LIST", m08190Service.selectItemBrandList(params)); // - 제품 브랜드 리스트 : [{ LIQUOR_CODE, BRAND_CODE, BRAND_NAME, BRAND_SEQ, USE_YN }]
        
        return mResult;
    }

    /**
     * 데이터 저장 : 용기브랜드와 매핑되는 제품 브랜드를 저장
     * @param params { saveData:[{}] }
     * @return { _RESULT_FLAG, _RESULT_MSG }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M08190/saveVesselBrand", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> saveVesselBrand(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
        
        try {
            // - 저장 처리
            mResult = m08190Service.saveVesselBrand(params);

        } catch (Exception e) {
            mResult.put(Const.RESULT_FLAG, "F");
            mResult.put(Const.RESULT_MSG, "저장 중 오류 발생" + " : " + e.getMessage());
            commonUtils.debugError(e);
            return mResult;
        }

        return mResult;
    }
    
    /**
     * 데이터 조회 : 제품 브랜드 매핑 관리. 팝업 그리드 조회용
     * @param params {  }
     * @return { TREEGRID_ITEM_BRAND_MAP:[{}], BRAND_LIST:[{}] }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M08190/searchItemBrandMap", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> searchItemBrandMap(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 데이터 조회
        mResult.put("TREEGRID_ITEM_BRAND_MAP", m08190Service.searchItemBrandMap(params));
        Map<String, Object> mBrandParam = new HashMap<String, Object>();
        mBrandParam.put("groupCode", "BOTL_BRAND_LIST");
        mBrandParam.put("useYn", "Y");
        mResult.put("BRAND_LIST", commonComboService.getComCodeCombo(mBrandParam));
        
        return mResult;
    }
    
    /**
     * 데이터 저장 : 제품 브랜드 매핑 저장
     * @param params { saveData:[{}] }
     * @return { _RESULT_FLAG, _RESULT_MSG }
     * @throws Exception
     */
    @RequestMapping(value = "/work/M08190/saveItemBrandMap", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> saveItemBrandMap(@RequestBody Map<String, Object> params) throws Exception {
        commonUtils.debugParams(params);

        Map<String, Object> mResult = new HashMap<String, Object>();

        // - 사용자 정보 담기
        sessionUtil.setUserInfoParam(params);
        
        try {
            // - 저장 처리
            mResult = m08190Service.saveItemBrandMap(params);

        } catch (Exception e) {
            mResult.put(Const.RESULT_FLAG, "F");
            mResult.put(Const.RESULT_MSG, "저장 중 오류 발생" + " : " + e.getMessage());
            commonUtils.debugError(e);
            return mResult;
        }

        return mResult;
    }
}
