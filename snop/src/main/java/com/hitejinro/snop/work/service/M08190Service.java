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

import com.hitejinro.snop.common.util.CommonUtils;
import com.hitejinro.snop.common.util.Const;
import com.hitejinro.snop.work.dao.M08190DaoMapper;

/**
 * 기준정보 > 용기 관리
 * @author 유기후
 *
 */
@Service
public class M08190Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M08190Service.class);

	@Inject
	private M08190DaoMapper m08190DaoMapper;

	@Inject
	private CommonUtils commonUtils;

	/**
	 * 데이터 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = m08190DaoMapper.search(params);
		return list;
	}

	/**
	 * 용기 제품마스터 조회
	 * @param params {  }
	 * @return [{ ITEM_CODE, ITEM_NAME, ITEM_CODE_NAME, ABBR_ITEM_NAME, ITEM_TYPE, ITEM_STATUS, PRIMARY_UOM_CODE, LIQUOR_CODE, VESSEL_CODE, VESSEL_SORT, VOLUME_VALUE, SEGMENT1, SEGMENT2, SEGMENT3 }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectRiItemList(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = m08190DaoMapper.selectRiItemList(params);
		return list;
	}

	/**
	 * 용기관리용 용기 리스트 조회
	 * @param params {  }
	 * @return [{ CODE, NAME }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectVesselList(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = m08190DaoMapper.selectVesselList(params);
		return list;
	}

	/**
	 * 용기자산 브랜드/볼륨 리스트 조회
	 * @param params {  }
	 * @return [{ CODE, NAME, ASSET_BRAND_NAME, ASSET_VOLUME_NAME }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectVesselAssetBrandVolumeList(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = m08190DaoMapper.selectVesselAssetBrandVolumeList(params);
		return list;
	}

	/**
	 * 데이터 저장
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
	@SuppressWarnings("unchecked")
	public Map<String, Object> save(Map<String, Object> params) throws Exception {
		Map<String, Object> mResult = new HashMap<String, Object>();

		try {
			List<Map<String, Object>> saveList = (List<Map<String, Object>>) params.get("saveData");
			List<Map<String, Object>> deleteList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> updateList = new ArrayList<Map<String, Object>>();

			int iDeleteCnt = 0;
			int iUpdateCnt = 0;

			// 1. 데이터 체크
			if (saveList.size() == 0) {
				mResult.put(Const.RESULT_FLAG, "F");
				mResult.put(Const.RESULT_MSG, "저장할 데이터가 없습니다.");
				commonUtils.debugParams(mResult);
				return mResult;
			}

			// 2. 저장 데이터 분리 : 삭제와 추가/수정으로 분리
			for (Map<String, Object> mRow : saveList) {
				String sAction = (mRow == null ? "" : (String) mRow.get(Const.ROW_STATUS));
				mRow.put("userId", params.get("userId"));

				if (Const.ROW_STATUS_INSERT.equals(sAction) || Const.ROW_STATUS_UPDATE.equals(sAction)) {
					updateList.add(mRow);
				} else if (Const.ROW_STATUS_DELETE.equals(sAction)) {
					deleteList.add(mRow);
				} else {
					mResult.put(Const.RESULT_FLAG, "F");
					mResult.put(Const.RESULT_MSG, "정상적인 데이터가 아닙니다(상태값 오류).");
					mResult.put("ROW_DATA", mRow);
					commonUtils.debugParams(mResult);
					return mResult;
				}
			}

			// - 별도의 Map에 담기 : 화면단에서 넘어온 것과 분리
			Map<String, Object> oSaveParam = new HashMap<String, Object>();
			oSaveParam.putAll(params);
			oSaveParam.put("deleteList", deleteList);
			oSaveParam.put("updateList", updateList);

			// 3. 정합성 체크
			List<Map<String, Object>> arrValidList = m08190DaoMapper.validate(oSaveParam);
			if (arrValidList != null && arrValidList.size() > 0 && !"0".equals(arrValidList.get(0).get("ERR_CNT").toString())) {
				mResult.put(Const.RESULT_FLAG, "F");
				mResult.put(Const.RESULT_MSG, "정합성 체크 중 오류 발생" + "\r\n" + arrValidList.get(0).get("ERR_MSG"));
				commonUtils.debugParams(mResult);
				return mResult;
			}

			// 4. 저장 처리
			// 4.1. 삭제
			if (deleteList.size() > 0) {
				// - 용기코드 삭제
				iDeleteCnt = m08190DaoMapper.delete(oSaveParam);
				// - 용기코드에 매핑된 용기자산의 브랜드/볼륨 삭제
				for (int r = 0; r < deleteList.size(); r++) {
					m08190DaoMapper.deleteVesselAssetBrandVolume(deleteList.get(r));
				}
				if (deleteList.size() != iDeleteCnt) {
					throw new Exception("삭제 중 오류 발생 : 삭제할 대상(" + deleteList.size() + "건)과 처리된 대상(" + iDeleteCnt + "건) 건수가 다릅니다");
				}
			}
			// 4.2. 추가/수정
			if (updateList.size() > 0) {
				// - 용기코드 추가/수정
				iUpdateCnt = m08190DaoMapper.update(oSaveParam);
				// - 용기코드에 매핑된 용기자산의 브랜드/볼륨 추가/수정 : 일괄삭제 후 처리하는 방식
				for (int r = 0; r < updateList.size(); r++) {
					if (Const.ROW_STATUS_UPDATE.equals((String) updateList.get(r).get(Const.ROW_STATUS))) {
						// - 수정은 일괄삭제 후 추가
						m08190DaoMapper.deleteVesselAssetBrandVolume(updateList.get(r));
					}
					m08190DaoMapper.insertVesselAssetBrandVolume(updateList.get(r));
				}
				if (updateList.size() != iUpdateCnt) {
					throw new Exception("추가/수정 중 오류 발생 : 추가/수정 대상(" + updateList.size() + "건)과 처리된 대상(" + iUpdateCnt + "건) 건수가 다릅니다");
				}
			}

			// - 최종 처리
			mResult.put(Const.RESULT_FLAG, "S");
			mResult.put(Const.RESULT_MSG, "저장이 성공하였습니다.");
			mResult.put("DELETE_CNT", iDeleteCnt);
			mResult.put("UPDATE_CNT", iUpdateCnt);

		} catch (Exception e) {
			throw e;
		}

		return mResult;
	}

	/**
	 * 데이터 조회 : 용기브랜드
	 * @param params
	 * @return [{ LIQUOR_CODE, VESSEL_BRAND_CODE, VESSEL_BRAND_NAME, VESSEL_BRAND_SEQ, VESSEL_BRAND_USE_YN, BRAND_LIST }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchVesselBrand(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = m08190DaoMapper.searchVesselBrand(params);
		return list;
	}

	/**
	 * 제품 브랜드 리스트 조회
	 * @param params
	 * @return [{ LIQUOR_CODE, BRAND_CODE, BRAND_NAME, BRAND_SEQ, USE_YN }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectItemBrandList(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = m08190DaoMapper.selectItemBrandList(params);
		return list;
	}

	/**
	 * 데이터 저장 : 용기브랜드와 매핑되는 제품 브랜드를 저장
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
	@SuppressWarnings("unchecked")
	public Map<String, Object> saveVesselBrand(Map<String, Object> params) throws Exception {
		Map<String, Object> mResult = new HashMap<String, Object>();

		try {
			List<Map<String, Object>> arrSaveList = (List<Map<String, Object>>) params.get("saveData");
			List<Map<String, Object>> arrDeleteList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> arrUpdateList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> arrInsertList = new ArrayList<Map<String, Object>>();

			int iDeleteCnt = 0;
			int iUpdateCnt = 0;
			int iInsertCnt = 0;

			// 1. 데이터 체크
			if (arrSaveList.size() == 0) {
				mResult.put(Const.RESULT_FLAG, "F");
				mResult.put(Const.RESULT_MSG, "저장할 데이터가 없습니다.");
				commonUtils.debugParams(mResult);
				return mResult;
			}

			// 2. 저장 데이터 분리 : 삭제, 추가, 수정으로 분리
			for (Map<String, Object> mRow : arrSaveList) {
				String sAction = (mRow == null ? "" : (String) mRow.get(Const.ROW_STATUS));
				mRow.putAll(params); // - 사용자정보(userId) 등 추가

				if (Const.ROW_STATUS_INSERT.equals(sAction)) {
					arrInsertList.add(mRow);
				} else if (Const.ROW_STATUS_UPDATE.equals(sAction)) {
					arrUpdateList.add(mRow);
				} else if (Const.ROW_STATUS_DELETE.equals(sAction)) {
					arrDeleteList.add(mRow);
				} else {
					mResult.put(Const.RESULT_FLAG, "F");
					mResult.put(Const.RESULT_MSG, "정상적인 데이터가 아닙니다(상태값 오류).");
					mResult.put("ROW_DATA", mRow);
					commonUtils.debugParams(mResult);
					return mResult;
				}
			}

			// 3. 저장 처리
			// 3.1. 삭제
			if (arrDeleteList.size() > 0) {
				for (int r = 0; r < arrDeleteList.size(); r++) {
					// - 용기브랜드 삭제
					iDeleteCnt += m08190DaoMapper.deleteVesselBrand(arrDeleteList.get(r));
					// - 용기브랜드에 연결된 제품 브랜드 삭제
					m08190DaoMapper.deleteVesselBrandDtl(arrDeleteList.get(r));
				}
				if (arrDeleteList.size() != iDeleteCnt) {
					throw new Exception("삭제 중 오류 발생 : 삭제할 대상(" + arrDeleteList.size() + "건)과 처리된 대상(" + iDeleteCnt + "건) 건수가 다릅니다");
				}
			}
			// 3.2. 추가
			if (arrInsertList.size() > 0) {
				for (int r = 0; r < arrInsertList.size(); r++) {
					// - 용기브랜드 추가
					iInsertCnt += m08190DaoMapper.insertVesselBrand(arrInsertList.get(r));
					// - 용기브랜드에 연결된 제품 브랜드 추가
					m08190DaoMapper.insertVesselBrandDtl(arrInsertList.get(r));
				}
				if (arrInsertList.size() != iInsertCnt) {
					throw new Exception("추가 중 오류 발생 : 추가할 대상(" + arrInsertList.size() + "건)과 처리된 대상(" + iInsertCnt + "건) 건수가 다릅니다");
				}
			}
			// 3.3. 수정
			if (arrUpdateList.size() > 0) {
				for (int r = 0; r < arrUpdateList.size(); r++) {
					// - 용기브랜드 추가
					iUpdateCnt += m08190DaoMapper.updateVesselBrand(arrUpdateList.get(r));
					// - 용기브랜드에 연결된 제품 브랜드 삭제 : 일괄 삭제 후 처리하는 방식
					m08190DaoMapper.deleteVesselBrandDtl(arrUpdateList.get(r));
					// - 용기브랜드에 연결된 제품 브랜드 추가
					m08190DaoMapper.insertVesselBrandDtl(arrUpdateList.get(r));
				}
				if (arrUpdateList.size() != iUpdateCnt) {
					throw new Exception("수정 중 오류 발생 : 추가할 대상(" + arrUpdateList.size() + "건)과 처리된 대상(" + iUpdateCnt + "건) 건수가 다릅니다");
				}
			}

			// - 최종 처리
			mResult.put(Const.RESULT_FLAG, "S");
			mResult.put(Const.RESULT_MSG, "저장이 성공하였습니다.");
			mResult.put("DELETE_CNT", iDeleteCnt);
			mResult.put("UPDATE_CNT", iUpdateCnt);
			mResult.put("INSERT_CNT", iInsertCnt);

		} catch (Exception e) {
			throw e;
		}

		return mResult;
	}

	/**
	 * 데이터 조회 : 제품 브랜드 매핑
	 * @param params
	 * @return [{ BRAND_CODE, BRAND_NAME, VOLUME_VALUE, VESSEL_BRAND_CODE }]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchItemBrandMap(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = m08190DaoMapper.searchItemBrandMap(params);
		return list;
	}

	/**
	 * 데이터 저장 : 용기브랜드와 매핑되는 제품 브랜드를 저장
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
	@SuppressWarnings("unchecked")
	public Map<String, Object> saveItemBrandMap(Map<String, Object> params) throws Exception {
		Map<String, Object> mResult = new HashMap<String, Object>();

		try {
			List<Map<String, Object>> arrSaveList = (List<Map<String, Object>>) params.get("saveData");
			List<Map<String, Object>> arrDeleteList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> arrUpdateList = new ArrayList<Map<String, Object>>();

			int iDeleteCnt = 0;
			int iUpdateCnt = 0;

			// 1. 데이터 체크
			if (arrSaveList == null || arrSaveList.size() == 0) {
				mResult.put(Const.RESULT_FLAG, "F");
				mResult.put(Const.RESULT_MSG, "저장할 데이터가 없습니다.");
				commonUtils.debugParams(mResult);
				return mResult;
			}

			// 2. 저장 데이터 분리 : 삭제, 추가/수정으로 분리
			// 삭제 대상은 용기 브랜드 코드 = NA
			for (Map<String, Object> mRow : arrSaveList) {
				String sAction = (mRow == null ? "" : (String) mRow.get(Const.ROW_STATUS));
				mRow.putAll(params);

				if ("NA".equals(mRow.get("VESSEL_BRAND_CODE"))) {
					arrDeleteList.add(mRow);
				} else if (Const.ROW_STATUS_INSERT.equals(sAction) || Const.ROW_STATUS_UPDATE.equals(sAction)) {
					arrUpdateList.add(mRow);
				} else {
					mResult.put(Const.RESULT_FLAG, "F");
					mResult.put(Const.RESULT_MSG, "정상적인 데이터가 아닙니다(상태값 오류).");
					mResult.put("ROW_DATA", mRow);
					commonUtils.debugParams(mResult);
					return mResult;
				}
			}

			// 3. 저장 처리
			// 3.1. 삭제
			if (arrDeleteList.size() > 0) {
				for (int r = 0; r < arrDeleteList.size(); r++) {
					// - 용기브랜드 삭제
					iDeleteCnt += m08190DaoMapper.deleteItemBrandMap(arrDeleteList.get(r));
				}
				if (arrDeleteList.size() != iDeleteCnt) {
					throw new Exception("삭제 중 오류 발생 : 삭제할 대상(" + arrDeleteList.size() + "건)과 처리된 대상(" + iDeleteCnt + "건) 건수가 다릅니다");
				}
			}

			// 3.2. 수정
			if (arrUpdateList.size() > 0) {
				for (int r = 0; r < arrUpdateList.size(); r++) {
					// - 용기브랜드 추가
					iUpdateCnt += m08190DaoMapper.updateItemBrandMap(arrUpdateList.get(r));
				}
				if (arrUpdateList.size() != iUpdateCnt) {
					throw new Exception("수정 중 오류 발생 : 추가할 대상(" + arrUpdateList.size() + "건)과 처리된 대상(" + iUpdateCnt + "건) 건수가 다릅니다");
				}
			}

			// - 최종 처리
			mResult.put(Const.RESULT_FLAG, "S");
			mResult.put(Const.RESULT_MSG, "저장이 성공하였습니다.");
			mResult.put("DELETE_CNT", iDeleteCnt);
			mResult.put("UPDATE_CNT", iUpdateCnt);

		} catch (Exception e) {
			throw e;
		}

		return mResult;
	}
}
