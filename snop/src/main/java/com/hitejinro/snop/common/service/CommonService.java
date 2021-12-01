package com.hitejinro.snop.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hitejinro.snop.common.dao.CommonDaoMapper;

/**
 * 공통
 * @author 남동희
 *
 */
@Service
public class CommonService {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(CommonService.class);

	@Inject
	private CommonDaoMapper commonDaoMapper;

	/**
	 * 메뉴 조회 : 권한처리
	 * @param params { userId }
	 * @return [{LEVEL, MENU_CD, MENU_NM, URL, HRNK_MENU_CD...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> getMenu(Map<String, Object> params) throws Exception {
		return commonDaoMapper.getMenu(params);
	}

	/**
	 * 용기관리 품목 조회
	 * @param params {liquorCode, botlType, companyType, volumeValue}
	 * @return [{CODE, NAME}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> getVesselItem(Map<String, Object> params) throws Exception {
		return commonDaoMapper.getVesselItem(params);
	}

	/**
	 * 용기관리 용기 조회
	 * @param params {liquorCode, botlType, companyType}
	 * @return [{CODE, NAME}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> getVesselCode(Map<String, Object> params) throws Exception {
		return commonDaoMapper.getVesselCode(params);
	}

	/**
	 * 용기관리 용량 조회
	 * @param params {liquorCode, botlType, companyType, vesselCode}
	 * @return [{CODE, NAME}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> getVesselVolume(Map<String, Object> params) throws Exception {
		return commonDaoMapper.getVesselVolume(params);
	}

	/**
	 * 용기관리 브랜드 조회
	 * @param params {liquorCode, botlType, companyType, vesselCode, brandCode}
	 * @return [{CODE, NAME}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> getVesselBrand(Map<String, Object> params) throws Exception {
		return commonDaoMapper.getVesselBrand(params);
	}

	/**
	 * 조직 조회
	 * @param params { orgType("!ALL":전체, "MFG":공장, "LOGISTICS":물류센터), liquorCode("!ALL":전체, "10":맥주, "20":소주) }
	 * @return [{ CODE, NAME }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> getOrg(Map<String, Object> params) throws Exception {
		return commonDaoMapper.getOrg(params);
	}

    /**
     * 품목 중분류 조회
     * @param params { liquorCode }
     * @return [{ CODE, NAME }]
     * @throws Exception
     */
    public List<Map<String, Object>> getItemSegment2List(Map<String, Object> params) throws Exception {
        return commonDaoMapper.getItemSegment2List(params);
    }

    /**
     * 품목 소분류 조회
     * @param params { liquorCode, segment2 }
     * @return [{ CODE, NAME }]
     * @throws Exception
     */
    public List<Map<String, Object>> getItemSegment3List(Map<String, Object> params) throws Exception {
        return commonDaoMapper.getItemSegment3List(params);
    }

	/**
	 * 오늘의 정보 조회
	 * @param params {  }
	 * @return { YYYYMMDD, BUSINESS_DAY_FLAG, YYYY, YYYYMM, YYYYWW, SCM_YYYYWW, SCM_YYYYMMWW, DAY_OF_WEEK, DAY_OF_WEEK_DESC, SCM_YYYYWW_WORK_CNT, YYYYMM_WORK_CNT, SCM_YYYYMMWW_WORK_CNT, BF_YYYYMMDD, BF_5D_YYYYMMDD, BF_10D_YYYYMMDD, BF_20D_YYYYMMDD, BF_60D_YYYYMMDD }
	 * @throws Exception
	 */
	public Map<String, Object> getTodayInfo(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> arrResultList = commonDaoMapper.getTodayInfo(params);
		return (arrResultList == null || arrResultList.size() < 1 ? new HashMap<String, Object>() : arrResultList.get(0));
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
    public List<Map<String, Object>> getStdSaleDfntList(Map<String, Object> params) throws Exception {
        return commonDaoMapper.getStdSaleDfntList(params);
    }

    /**
     * 판매변수정의 콤보박스 리스트<p>
     * <li>판매변수유형 조건절(saleVarType) : 다중선택값(콤마로 구분)도 가능 : EVENT(이벤트), ISSUE(이슈), DISASTER(재해), ETC(기타)
     * <li>사업부문 조건절(liquorCode) : 다중선택값(콤마로 구분)도 가능 : 10(맥주), 20(소주)
     * @param params { saleVarType, liquorCode }
     * @return [{ CODE, NAME, SALE_VAR_DFNT_SEQNO, SALE_VAR_DFNT_SEQNO_NAME, VLD_STR_DT, VLD_END_DT, SALE_VAR_TYPE, SALE_VAR_TYPE_DESC, SALE_VAR_NAME, LIQUOR_CODE, LIQUOR_DESC, USAGE_CODE, USAGE_NAME, ITEM_CODE, ITEM_NAME, VAR_VAL, CALC_STR_DT, CALC_END_DT }]
     * @throws Exception
     */
    public List<Map<String, Object>> getSaleVarDfntList(Map<String, Object> params) throws Exception {
        return commonDaoMapper.getSaleVarDfntList(params);
    }
    
    
    
}
