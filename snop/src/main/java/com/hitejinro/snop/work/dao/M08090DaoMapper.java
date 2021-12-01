package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 기준정보 > 안전재고 일수
 * @author 유기후
 *
 */
@Repository
public interface M08090DaoMapper {
    
    /**
     * 데이터 조회
     * @param params { bssYYYYMM, liquorCode, itemIgrdTypeCode, stockCalcWw, itemMapYn }
     * @return [{ YYYYMM, LIQUOR_CODE, ITEM_CODE, ITEM_NAME, CALC_ITEM_IGRD_TYPE_CODE, ITEM_IGRD_TYPE_CODE, BRAND_CODE, BRAND_NAME, BRAND_SORT_ORDER, VESSEL_CODE, VESSEL_SORT, VESSEL_SORT_ORDER, VOLUME_VALUE, USAGE_CODE, USAGE_NAME, USAGE_SORT_ORDER, DOM_EXP_CODE, DOM_EXP_FLAG, ACTUAL_1M_SALE_AVG_CONV_QTY, ACTUAL_3M_SALE_AVG_CONV_QTY, ACTUAL_6M_SALE_AVG_CONV_QTY, ACTUAL_1M_SALE_RATE, ACTUAL_3M_SALE_RATE, ACTUAL_6M_SALE_RATE, MIN_STOCK_DCNT, DLV_LEAD_DCNT, PRDT_LEAD_DCNT, SAFT_STOCK_DCNT, STRG_STOCK_DCNT, STRG_STOCK_CONV_QTY, STRG_SAFT_STOCK_DCNT, STRG_SAFT_MAX_STOCK_DCNT, AVG_STOCK_DAY, MIN_STOCK_DAY, MAX_STOCK_DAY, MIN_PRDT_QTY }]
     * @throws Exception
     */
    public List<Map<String, Object>> search(Map<String, Object> params) throws Exception;

    /**
     * 데이터 저장 : 추가/수정
     * @param params { updateList:[{}], userId }
     * @return
     * @throws Exception
     */
    public int update(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 저장 : 삭제(모든 항목이 null인 경우 삭제)
     * @param params
     * @return
     * @throws Exception
     */
    public int delete(Map<String, Object> params) throws Exception;
    
    

    
    /**
     * 복사 전 Validation
     * @param params { copyFrYYYYMM }
     * @return [{ ERR_MSG, ERR_YN }]
     * @throws Exception
     */
    public List<Map<String, Object>> validateByCopy(Map<String, Object> params) throws Exception;

    /**
     * 복사 전 삭제
     * @param params { copyToYYYYMM, userId }
     * @return
     * @throws Exception
     */
    public int deleteByCopy(Map<String, Object> params) throws Exception;
    
    /**
     * 복사
     * @param params { copyToYYYYMM, copyFrYYYYMM, userId }
     * @return
     * @throws Exception
     */
    public int updateByCopy(Map<String, Object> params) throws Exception;
    
}
