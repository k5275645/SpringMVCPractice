package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 당일 용기회수량 예측
 * @author 남동희
 *
 */
@Repository
public interface M03010DaoMapper {

	/**
	 * 기간 조회
	 * - 차트 조회시 필요한 기간
	 * 해당년도 기간 : 조회일자의 전주 기준 12주 전 일요일 ~ 조회일자의 전주의 토요일
	 * 전년도 동기간(주차) : 주차기준 시작일자 ~ 종료일자
	 * 
	 * ex) endYYYYMMDD : '20210712'
	 * START_WEEK_YYYYMMDD 			END_WEEK_YYYYMMDD 			PRE_START_WEEK_YYYYMMDD 			PRE_END_WEEK_YYYYMMDD
	 * ===========================================================================================================================
	 * 20210418(2021년 17주차)		20210710(2021년 28주차)		20200419(2020년 17주차의 일요일)	20200711(202년 28주차의 토요일)
	 * 
	 * - 그리드 조회시 필요한 기간
	 * PRE_DAY_YYYYMMDD : 전일(영업일 기준)
	 * PRE_WEEK_YYYYMMDD : 전주(endYYYYMMDD - 7)
	 * PRE_MONTH_YYYYMMDD : 전월 동일날짜(영업일이 아닌경우 전월 마지막 영업일)
	 * PRE_YEAR_YYYYMMDD : 전년 동일날짜(영업일이 아닌경우 전년도 동일월 마지막 영업일)
	 * CRITERIA_YYYYMMDD : 예측기준(endYYYYMMDD - 영업일 기준 40일, 20일, 10일)
	 * RECENT_HHMMSS : 종료일자의 최신 시간(없는 경우 06시)
	 * 
	 * @param params {endYYYYMMDD...}
	 * @return {START_WEEK_YYYYMMDD, END_WEEK_YYYYMMDD, PRE_START_WEEK_YYYYMMDD, PRE_END_WEEK_YYYYMMDD, PRE_DAY_YYYYMMDD, PRE_WEEK_YYYYMMDD, PRE_MONTH_YYYYMMDD, PRE_YEAR_YYYYMMDD, CRITERIA_YYYYMMDD, RECENT_HHMMSS}
	 * @throws Exception
	 */
	public Map<String, Object> searchPeriod(Map<String, Object> params) throws Exception;

	/**
	 * 차트데이터 조회
	 * @param params {START_WEEK_YYYYMMDD, END_WEEK_YYYYMMDD, PRE_START_WEEK_YYYYMMDD, PRE_END_WEEK_YYYYMMDD, liquorCode, vesselCode, volumeValue, includeYN, acctType}
	 * @return [ {PERIOD_YYYYWW : 주차, PERIOD_YYYYWW_QTY : 해당기간 주차별 회수량, PRE_PREIOD_YYYYWW_QTY : 전년도 동기간(주차별) 회수량}] 
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchChart(Map<String, Object> params) throws Exception;

	/**
	 * 헤더 조회
	 * @param params {startDate, endDate} 
	 * @return [{PERIOD_YYYYMMDD, HEADER1_DESC...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchHeader(Map<String, Object> params) throws Exception;

	/**
	 * 조회
	 * @param params {startDate, endDate, liquorCode, acctType, PRE_DAY_YYYYMMDD, PRE_WEEK_YYYYMMDD, PRE_MONTH_YYYYMMDD, PRE_YEAR_YYYYMMDD, CRITERIA_YYYYMMDD, RECENT_HHMMSS}
	 * @return [{LIQUOR_CODE, VESSEL_CODE, VOLUME_VALUE, PERIOD_YYYYMMDD, QTY ...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception;

}
