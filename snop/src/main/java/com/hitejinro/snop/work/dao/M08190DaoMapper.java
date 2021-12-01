package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 기준정보 > 용기 관리
 * @author 유기후
 *
 */
@Repository
public interface M08190DaoMapper {
    
    /**
     * 데이터 조회
     * @param params
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> search(Map<String, Object> params) throws Exception;
    
    /**
     * 용기 제품마스터 조회
     * @param params {  }
     * @return [{ ITEM_CODE, ITEM_NAME, ITEM_CODE_NAME, ABBR_ITEM_NAME, ITEM_TYPE, ITEM_STATUS, PRIMARY_UOM_CODE, LIQUOR_CODE, VESSEL_CODE, VESSEL_SORT, VOLUME_VALUE, SEGMENT1, SEGMENT2, SEGMENT3, PBOX_PACKING_UNIT, ASSET_BRAND_VOLUME_LIST }]
     * @throws Exception
     */
    public List<Map<String, Object>> selectRiItemList(Map<String, Object> params) throws Exception;
    
    /**
     * 용기관리용 용기 리스트 조회
     * @param params {  }
     * @return [{ CODE, NAME }]
     * @throws Exception
     */
    public List<Map<String, Object>> selectVesselList(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 저장 : 유효성 검증
     * @param params
     * @return [{ ERR_CNT, ERR_MSG }]
     * @throws Exception
     */
    public List<Map<String, Object>> validate(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 저장 : 추가/수정
     * @param params
     * @return
     * @throws Exception
     */
    public int update(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 저장 : 삭제
     * @param params
     * @return
     * @throws Exception
     */
    public int delete(Map<String, Object> params) throws Exception;
    
    
    /**
     * 용기자산 브랜드/볼륨 리스트 조회
     * @param params {  }
     * @return [{ CODE, NAME, ASSET_BRAND_NAME, ASSET_VOLUME_NAME }]
     * @throws Exception
     */
    public List<Map<String, Object>> selectVesselAssetBrandVolumeList(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 저장 : 용기코드의 용기자산 브랜드/볼륨 매핑 추가(전체 삭제 후 추가 방식)
     * @param params { ITEM_CODE, ASSET_BRAND_VOLUME_LIST, userId }
     * @return
     * @throws Exception
     */
    public int insertVesselAssetBrandVolume(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 저장 : 용기코드의 용기자산 브랜드/볼륨 매핑 삭제(전체 삭제 후 추가 방식)
     * @param params { ITEM_CODE }
     * @return
     * @throws Exception
     */
    public int deleteVesselAssetBrandVolume(Map<String, Object> params) throws Exception;
    
    
    
    /**
     * 데이터 조회 : 용기브랜드
     * @param params
     * @return [{ LIQUOR_CODE, VESSEL_BRAND_CODE, VESSEL_BRAND_NAME, VESSEL_BRAND_SEQ, VESSEL_BRAND_USE_YN, BRAND_LIST }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchVesselBrand(Map<String, Object> params) throws Exception;
    
    /**
     * 제품 브랜드 리스트 조회
     * @param params
     * @return [{ LIQUOR_CODE, BRAND_CODE, BRAND_NAME, BRAND_SEQ, USE_YN }]
     * @throws Exception
     */
    public List<Map<String, Object>> selectItemBrandList(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 저장 : 용기브랜드 추가
     * @param params { LIQUOR_CODE, VESSEL_BRAND_CODE, VESSEL_BRAND_NAME, VESSEL_BRAND_SEQ, VESSEL_BRAND_USE_YN, BRAND_LIST, userId }
     * @return
     * @throws Exception
     */
    public int insertVesselBrand(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 저장 : 용기브랜드 수정
     * @param params { LIQUOR_CODE, VESSEL_BRAND_CODE, VESSEL_BRAND_NAME, VESSEL_BRAND_SEQ, VESSEL_BRAND_USE_YN, BRAND_LIST, userId }
     * @return
     * @throws Exception
     */
    public int updateVesselBrand(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 저장 : 용기브랜드 삭제
     * @param params { VESSEL_BRAND_CODE }
     * @return
     * @throws Exception
     */
    public int deleteVesselBrand(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 저장 : 용기브랜드의 상세(제품 브랜드) 추가(전체 삭제 후 추가 방식)
     * @param params { LIQUOR_CODE, VESSEL_BRAND_CODE, VESSEL_BRAND_NAME, VESSEL_BRAND_SEQ, VESSEL_BRAND_USE_YN, userId }
     * @return
     * @throws Exception
     */
    public int insertVesselBrandDtl(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 저장 : 용기브랜드의 상세(제품 브랜드) 삭제(전체 삭제 후 추가 방식)
     * @param params { VESSEL_BRAND_CODE }
     * @return
     * @throws Exception
     */
    public int deleteVesselBrandDtl(Map<String, Object> params) throws Exception;
    
    /**
     * 제품 브랜드 매핑 조회
     * @param params {}
     * @return [{ BRAND_CODE, BRAND_NAME, VOLUME_VALUE, VESSEL_BRAND_CODE }]
     * @throws Exception
     */
    public List<Map<String, Object>> searchItemBrandMap(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 저장 : 제품 브랜드 매핑 삭제
     * @param params { BRAND_CODE, VOLUME_VALUE }
     * @return
     * @throws Exception
     */
    public int deleteItemBrandMap(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 저장 : 제품 브랜드 매핑 추가 및 수정
     * @param params { BRAND_CODE, VOLUME_VALUE, VESSEL_BRAND_CODE }
     * @return
     * @throws Exception
     */
    public int updateItemBrandMap(Map<String, Object> params) throws Exception;
}
