package com.hitejinro.snop.work.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hitejinro.snop.common.util.Const;
import com.hitejinro.snop.common.util.UserException;
import com.hitejinro.snop.work.dao.M08210DaoMapper;

/**
 * 기준정보 > 모제품 매핑
 * @author 손성은
 *
 */
@Service
public class M08210Service {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(M08210Service.class);

    @Inject
    private M08210DaoMapper m08210DaoMapper;

    /**
     * 데이터 조회
     * @param params { liquorCode }
     * @return [ Body : [{LIQUOR_CODE, FR_ITEM_CODE, FR_ITEM_NAME, FR_CONVERSION_BULK, TO_ITEM_CODE, TO_ITEM_NAME... }]]
     * @throws Exception
     */
    public List<Map<String, Object>> search(Map<String, Object> params) throws Exception {
        return m08210DaoMapper.search(params);
    }

    /**
     * FR 제품마스터 조회
     * @param params { liquorCode }
     * @return [{ ITEM_CODE, ITEM_NAME, ITEM_CODE_NAME, BRAND_NAME, VESSEL_SORT, VOLUME_VALUE, CONVERSION_BULK, LIQUOR_CODE, LIQUOR_CODE_NAME }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchItemList(Map<String, Object> params) throws Exception {
        return m08210DaoMapper.searchItemList(params);
    }

    /**
     * 데이터 저장
     * @param params { saveData }
     * @return { _RESULT_FLAG, _RESULT_MSG }
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
    @SuppressWarnings("unchecked")
    public Map<String, Object> save(Map<String, Object> params) throws Exception {
        Map<String, Object> mResult = new HashMap<String, Object>();

        List<Map<String, Object>> saveList = (List<Map<String, Object>>) params.get("saveData");
        List<Map<String, Object>> deleteList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> updateList = new ArrayList<Map<String, Object>>();

        if (saveList == null || saveList.size() == 0) {
            throw new UserException("유효하지 않은 데이터입니다.");
        }

        Object action;
        for (Map<String, Object> row : saveList) {
            action = row.get(Const.ROW_STATUS);

            if (Const.ROW_STATUS_UPDATE.equals(action) || Const.ROW_STATUS_INSERT.equals(action)) {
                updateList.add(row);
            } else if (Const.ROW_STATUS_DELETE.equals(action)) {
                deleteList.add(row);
            } else {
                throw new UserException("유효하지 않은 데이터입니다.");
            }
        }
        
        // 추가, 수정
        if (updateList.size() > 0) {
            Map<String, Object> updateData = new HashMap<String, Object>();
            updateData.put("updateList", updateList);

            // 유효성 검증
            List<Map<String, Object>> errorList = m08210DaoMapper.validate(updateData);

            if (errorList != null && errorList.size() > 0) {
                StringBuilder error = new StringBuilder();
                error.append("[모제품 매핑]에 오류 \r\n");

                for (Map<String, Object> row : errorList) {
                    error.append(row.get("ERR_MSG"));
                    error.append("\r\n");
                }

                throw new UserException(error.toString());
            }

            // 추가, 수정 및 로그 기록
            for (Map<String, Object> row : updateList) {
                row.put("userId", params.get("userId"));
                m08210DaoMapper.update(row);
                m08210DaoMapper.insertHistory(row);
            }
        }
        
        // 삭제
        if (deleteList.size() > 0) {
            for (Map<String, Object> row : deleteList) {
                row.put("userId", params.get("userId"));
                m08210DaoMapper.delete(row);
                m08210DaoMapper.insertHistory(row);
            }
        }

        // A->B->C 이중변환 매핑처리
        List<Map<String, Object>> conversionList = m08210DaoMapper.searchConversion(params);

        for (Map<String, Object> row : conversionList) {
            row.put("userId", params.get("userId"));
            m08210DaoMapper.updateConversion(row);
            m08210DaoMapper.insertHistory(row);
        }

        mResult.put(Const.RESULT_FLAG, "S");
        mResult.put(Const.RESULT_MSG, "저장이 완료되었습니다.");

        return mResult;
    }
}
