package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 일일 용기 현황
 * @author 남동희
 *
 */
@Repository
public interface M03030DaoMapper {

	/**
	 * 주차 콤보 데이터 조회
	 * 해당년도의 주차
	 * @param params {year : 대상년도}
	 * @return [{CODE, NAME}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchWeek(Map<String, Object> params) throws Exception;

	/**
	 * 기간 조회
	 * ex) endYYYYMMDD : '20210513'
	 * START_YYYYMMDD	END_YYYYMMDD	PERIOD_SCM_YYYYWW	PRE_MONTH_YYYYMMDD	START_WEEK_YYYYMMDD	END_WEEK_YYYYMMDD	START_WEEK	END_WEEK
	 * ======================================================================================================================================
	 * 20210501			20210513		202120				20210413			20210214			20210508			202108		202119
	 * 
	 * END_YYYYMMDD : 화면의 기준일자
	 * START_YYYYMMDD : 해당월의 1일(기준일자 기준)
	 * PERIOD_SCM_YYYYWW : 해당주차(기준일자 기준)
	 * PRE_MONTH_YYYYMMDD : 전월 동일날짜(영업일이 아닌경우 전월 마지막 영업일)
	 * START_WEEK_YYYYMMDD : 전주 기준 12주 전 일요일
	 * END_WEEK_YYYYMMDD : 전주의 토요일(기준일자 기준)
	 * START_WEEK : 전주 기준 12주전 주차
	 * END_WEEK : 전주 주차(기준일자 기준)
	 * @param params
	 * @return {START_YYYYMMDD, END_YYYYMMDD, PERIOD_SCM_YYYYWW, PRE_MONTH_YYYYMMDD, START_WEEK_YYYYMMDD, END_WEEK_YYYYMMDD, START_WEEK, END_WEEK}
	 * @throws Exception
	 */
	public Map<String, Object> searchPeriod(Map<String, Object> params) throws Exception;

	/**
	 * 헤더 조회(M03033, M03034)
	 * @param params {START_WEEK_YYYYMMDD, END_WEEK_YYYYMMDD}
	 * @return [{PERIOD_SCM_YYYYWW, HEADER1_DESC}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchHeader(Map<String, Object> params) throws Exception;

	/**
	 * M03031 : 부문별 용기 전체 재고 현황
	 * @param params {liquorCode, orgType, period...}
	 * @return [{TYPE, TOTAL_STOCK_CS_QTY, DAY_AVG_PRDT_PLAN_QTY, STOCK_DAY, VESSEL_STOCK_STATS_NAME, RETURN_RATE, PRE_MONTH_RETURN_RATE...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchM03031(Map<String, Object> params) throws Exception;

	/**
	 * M03032 : 공장별 공병 재고 현황
	 * @param params {liquorCode, orgType, period...}
	 * @return [{TYPE, TOTAL_STOCK_CS_QTY, OLD_BOTL_STOCK_DAY, TOTAL_BOTL_STOCK_DAY, OLD_VESSEL_STOCK_STATS_NAME, TOTAL_VESSEL_STOCK_STATS_NAME...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchM03032(Map<String, Object> params) throws Exception;

	/**
	 * M03033 : 주차별 일평균 용기회수 현황
	 * @param params {liquorCode, orgType, header, period...}
	 * @return [{TYPE, NAME, COL0, COL1, ...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchM03033(Map<String, Object> params) throws Exception;

	/**
	 * M03034 : 주차별 일평균 용기 재고/입고 현황
	 * @param params {liquorCode, orgType, header, period...}
	 * @return [{TYPE, ACCT_TYPE, BOTL_TYPE, COL0, COL1, ...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchM03034(Map<String, Object> params) throws Exception;

	/**
	 * M03035 : 일평균생산량 관리 조회
	 * @param params {week, mfgCode}
	 * @return [{PERIOD_SCM_YYYYWW, TYPE, DAY_AVG_PRDT_PLAN_QTY...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchM03035(Map<String, Object> params) throws Exception;

	/**
	 * 일평균생산량 관리 저장
	 * @param params {week, mfgCode, saveDate}
	 * @throws Exception
	 */
	public void updateM03035(Map<String, Object> params) throws Exception;

	/**
	 * M03036 : 시그널 조회
	 * @param params
	 * @return [{SEQNO, VESSEL_STOCK_STATS_NAME, FR_STOCK_STATS_RNG_SN, TO_STOCK_STATS_RNG_SN...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchM03036(Map<String, Object> params) throws Exception;
	
	/**
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> validateM03036(Map<String, Object> params) throws Exception;

	/**
	 * M03036 : 시그널 저장
	 * @param params {saveDate}
	 * @throws Exception
	 */
	public void updateM03036(Map<String, Object> params) throws Exception;

}
