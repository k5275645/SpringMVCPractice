package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 공장/물류센터 용기일보
 * @author 남동희
 *
 */
@Repository
public interface M03020DaoMapper {

	/**
	 * 헤더 조회
	 * 
	 * case 1) 용기 = 병
	 * HEADER1 - 맥주(사업부문)
	 * HEADER2 - 병 하이트 330(용기/브랜드/용량)
	 * HEADER3 - 합계	강원_고병(공장/병구분)	강원_신병	전주_고병	전주_신병	물류센터
	 * 
	 * case 2) 용기 = PBOX
	 * HEADER1 - 맥주(사업부문)
	 * HEADER2 - PBOX(용기)
	 * HEADER3 - 합계	하이트 330(브랜드/용량)
	 * 
	 * case 3) 용기 = PALLET
	 * HEADER1 - 공통(사업부문)
	 * HEADER2 - PALLET(용기)
	 * HEADER3 - 합계	강원(공장)	물류센터
	 * 
	 * case 4) 용기 = 생통
	 * HEADER1 - 맥주(사업부문)
	 * HEADER2 - 생통 맥스(일반) 20000	생통 맥스(도색) 20000(용기/브랜드/생통구분/용량)
	 * HEADER3 - 합계	강원(공장)	물류센터
	 * 
	 * 위 4개 CASE에 대해서 GROUP BY 대상 속성(사업부문, 용기, 브랜드, 용량, 생통구분, 조직(공장/물류센터), 병구분) 산출
	 * 각 column의 header 명칭과 span, expand 산출
	 * @param params {liquorCode, vesselCode}
	 * @return [{LIQUOR_CODE, VESSEL_CODE, BRAND_CODE, VOLUME_VALUE, KEG_TYPE, ORG_CODE, BOTL_TYPE, DEF, VISIBLE, HEADER1_DESC, HEADER2_DESC, HEADER3_DESC...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchHeader(Map<String, Object> params) throws Exception;

	/**
	 * 조회
	 * 일자별 용기 현황(재고) 추출
	 * 각 column별로 집계기준이 다름(header 조회 결과에 따라 결정됨)
	 * 
	 * case1) 용기 = 병
	 * HEADER1 - 맥주
	 * HEADER2 - 병 하이트 330
	 * HEADER3 - 합계						강원_고병										물류센터
	 * ==================================================================================================================================
	 * 집계기준- 사업부문/용기/브랜드/용량	사업부문/용기/브랜드/용량/조직(공장별)/병구분	사업부문/용기/브랜드/용량/조직(전체 물류센터)
	 * 
	 * case2) 용기 = PBOX
	 * HEADER1 - 맥주
	 * HEADER2 - PBOX
	 * HEADER3 - 합계						하이트330
	 * ==============================================================
	 * 
	 * case3) 용기 = PALLET
	 * HEADER1 - 공통(사업부문)
	 * HEADER2 - PALLET(용기)
	 * HEADER3 - 합계				강원(공장)					물류센터
	 * ==================================================================================
	 * 집계기준- 사업부문/용기		사업부문/용기/조직(공장별)	사업부문/용기/조직(전체 물류센터)
	 * 
	 * case4) 용기 = 생통
	 * HEADER1 - 맥주(사업부문)
	 * HEADER2 - 생통 맥스(일반) 20000							생통 맥스(도색) 20000(용기/브랜드/생통구분/용량)
	 * ==================================================================================
	 * 집계기준- 사업부문/용기/브랜드/용량/생통구분(COMMON) 	사업부문/용기/브랜드/용량/생통구분(PAINT)
	 * @param params {startDate, endDate, liquorCode, vesselCode, acctType}
	 * @return [{PERIOD_YYYYMMDD, COL0, ...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception;

}
