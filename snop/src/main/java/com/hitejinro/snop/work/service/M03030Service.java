package com.hitejinro.snop.work.service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hitejinro.snop.common.dao.CommonComboDaoMapper;
import com.hitejinro.snop.common.util.Const;
import com.hitejinro.snop.common.util.UserException;
import com.hitejinro.snop.work.dao.M03030DaoMapper;

/**
 * 일일 용기 현황
 * @author 남동희
 *
 */
@Service
public class M03030Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M03030Service.class);

	@Inject
	private M03030DaoMapper m03030DaoMapper;

	@Inject
	private CommonComboDaoMapper commonComboDaoMapper;

	/**
	 * 주차 콤보 데이터 조회
	 * 해당년도의 주차
	 * @param params {year : 대상년도}
	 * @return [{CODE, NAME}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchWeek(Map<String, Object> params) throws Exception {
		return m03030DaoMapper.searchWeek(params);
	}

	/**
	 * 시그널 목록 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchSign(Map<String, Object> params) throws Exception {
		params.put("groupCode", "RNG_SN");
		return commonComboDaoMapper.getComCodeCombo(params);
	}

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
	public Map<String, Object> searchPeriod(Map<String, Object> params) throws Exception {
		return m03030DaoMapper.searchPeriod(params);
	}

	/**
	 * 헤더 조회(M03033, M03034)
	 * @param params {START_WEEK_YYYYMMDD, END_WEEK_YYYYMMDD}
	 * @return [{PERIOD_SCM_YYYYWW, HEADER1_DESC}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchHeader(Map<String, Object> params) throws Exception {
		return m03030DaoMapper.searchHeader(params);
	}

	/**
	 * M03031 : 부문별 용기 전체 재고 현황
	 * @param params {liquorCode, orgType, period...}
	 * @return [{TYPE, TOTAL_STOCK_CS_QTY, DAY_AVG_PRDT_PLAN_QTY, STOCK_DAY, VESSEL_STOCK_STATS_NAME, RETURN_RATE, PRE_MONTH_RETURN_RATE...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchM03031(Map<String, Object> params) throws Exception {
		return m03030DaoMapper.searchM03031(params);
	}

	/**
	 * M03032 : 공장별 공병 재고 현황
	 * @param params {liquorCode, orgType, period...}
	 * @return [{TYPE, TOTAL_STOCK_CS_QTY, OLD_BOTL_STOCK_DAY, TOTAL_BOTL_STOCK_DAY, OLD_VESSEL_STOCK_STATS_NAME, TOTAL_VESSEL_STOCK_STATS_NAME...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchM03032(Map<String, Object> params) throws Exception {
		return m03030DaoMapper.searchM03032(params);
	}

	/**
	 * M03033 : 주차별 일평균 용기회수 현황
	 * @param params {liquorCode, orgType, header, period...}
	 * @return [{TYPE, NAME, COL0, COL1, ...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchM03033(Map<String, Object> params) throws Exception {
		return m03030DaoMapper.searchM03033(params);
	}

	/**
	 * M03034 : 주차별 일평균 용기 재고/입고 현황
	 * @param params {liquorCode, orgType, header, period...}
	 * @return [{TYPE, ACCT_TYPE, BOTL_TYPE, COL0, COL1, ...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchM03034(Map<String, Object> params) throws Exception {
		return m03030DaoMapper.searchM03034(params);
	}

	/**
	 * M03035 : 일평균생산량 관리 조회
	 * @param params {week, mfgCode}
	 * @return [{PERIOD_SCM_YYYYWW, TYPE, DAY_AVG_PRDT_PLAN_QTY...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchM03035(Map<String, Object> params) throws Exception {
		return m03030DaoMapper.searchM03035(params);
	}

	/**
	 * M03035 : 일평균생산량 관리 저장
	 * @param params {week, mfgCode, saveDate}
	 * @return {_RETURN_FLAG, _RETURN_MSG}
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
	public Map<String, Object> saveM03035(Map<String, Object> params) throws Exception {

		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> saveList = (List<Map<String, Object>>) params.get("saveData");
		List<Map<String, Object>> updateList = new ArrayList<Map<String, Object>>();

		if (saveList == null || saveList.size() == 0) {
			throw new UserException("유효하지 않은 데이터입니다.");
		}

		Object action;
		for (Map<String, Object> row : saveList) {
			action = row.get(Const.ROW_STATUS);

			if (Const.ROW_STATUS_INSERT.equals(action) || Const.ROW_STATUS_UPDATE.equals(action)) {
				updateList.add(row);
			} else {
				throw new UserException("유효하지 않은 데이터입니다.");
			}
		}

		// 추가 및 수정
		if (updateList.size() > 0) {
			Map<String, Object> updateData = new HashMap<String, Object>();
			updateData.put("updateList", updateList);
			updateData.put("userId", params.get("userId"));

			m03030DaoMapper.updateM03035(updateData);
		}

		result.put(Const.RESULT_FLAG, "S");
		result.put(Const.RESULT_MSG, "저장이 완료되었습니다.");

		return result;
	}

	/**
	 * M03036 : 시그널 조회
	 * @param params
	 * @return [{SEQNO, VESSEL_STOCK_STATS_NAME, FR_STOCK_STATS_RNG_SN, TO_STOCK_STATS_RNG_SN...}]
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchM03036(Map<String, Object> params) throws Exception {
		return m03030DaoMapper.searchM03036(params);
	}

	/**
	 * M03036 : 시그널 저장
	 * @param params {saveDate}
	 * @return {_RETURN_FLAG, _RETURN_MSG}
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class }, readOnly = false)
	public Map<String, Object> saveM03036(Map<String, Object> params) throws Exception {

		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> saveList = (List<Map<String, Object>>) params.get("saveData");
		List<Map<String, Object>> updateList = new ArrayList<Map<String, Object>>();

		if (saveList == null || saveList.size() == 0) {
			throw new UserException("유효하지 않은 데이터입니다.");
		}

		Object action;
		for (Map<String, Object> row : saveList) {
			action = row.get(Const.ROW_STATUS);

			if (Const.ROW_STATUS_INSERT.equals(action) || Const.ROW_STATUS_UPDATE.equals(action)) {
				updateList.add(row);
			} else {
				throw new UserException("유효하지 않은 데이터입니다.");
			}
		}

		// 추가 및 수정
		if (updateList.size() > 0) {
			Map<String, Object> updateData = new HashMap<String, Object>();
			updateData.put("updateList", updateList);
			updateData.put("userId", params.get("userId"));

			m03030DaoMapper.updateM03036(updateData);

			// validation
			List<Map<String, Object>> errorList = m03030DaoMapper.validateM03036(params);

			if (errorList != null && errorList.size() > 0) {
				StringBuilder error = new StringBuilder();
				error.append("[시그널]에 오류\r\n");

				for (Map<String, Object> row : errorList) {
					error.append(row.get("ERR_MSG"));
					error.append("\r\n");
				}

				throw new UserException(error.toString());
			}
		}

		result.put(Const.RESULT_FLAG, "S");
		result.put(Const.RESULT_MSG, "저장이 완료되었습니다.");

		return result;
	}

	/**
	 * 엑셀 다운로드
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked" })
	public byte[] excelDownload(Map<String, Object> params) throws Exception {
		// 데이터 조회용 파라미터 분리
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("endDate", params.get("endDate"));
		searchParams.put("liquorCode", params.get("liquorCode"));
		searchParams.put("vesselCode", params.get("vesselCode"));
		searchParams.put("volumeValue", params.get("volumeValue"));
		searchParams.put("brandCode", params.get("brandCode"));

		Map<String, Object> period = searchPeriod(searchParams);
		searchParams.putAll(period);
		searchParams.put("header", searchHeader(searchParams));

		List<Map<String, Object>> m03031Body = searchM03031(searchParams);
		List<Map<String, Object>> m03032Body = searchM03032(searchParams);
		List<Map<String, Object>> m03033Body = searchM03033(searchParams);
		List<Map<String, Object>> m03034Body = searchM03034(searchParams);

		// 화면에서 넘어온 header data
		List<Map<String, Object>> m03031Header = (List<Map<String, Object>>) params.get("M03031");
		List<Map<String, Object>> m03032Header = (List<Map<String, Object>>) params.get("M03032");
		List<Map<String, Object>> m03033Header = (List<Map<String, Object>>) params.get("M03033");
		List<Map<String, Object>> m03034Header = (List<Map<String, Object>>) params.get("M03034");

		byte[] result = null;
		XSSFWorkbook wb = null;
		ByteArrayOutputStream bos = null;

		try {
			wb = new XSSFWorkbook();
			bos = new ByteArrayOutputStream();

			// default style
			XSSFCellStyle defaultStyle = wb.createCellStyle();
			defaultStyle.setBorderRight(BorderStyle.THIN);
			defaultStyle.setBorderLeft(BorderStyle.THIN);
			defaultStyle.setBorderTop(BorderStyle.THIN);
			defaultStyle.setBorderBottom(BorderStyle.THIN);

			// header style
			XSSFFont headerFont = wb.createFont();
			headerFont.setBold(true);
			headerFont.setFontName("맑은 고딕");
			headerFont.setColor(IndexedColors.WHITE.getIndex());

			XSSFCellStyle headerStyle = wb.createCellStyle();
			headerStyle.cloneStyleFrom(defaultStyle);
			headerStyle.setFont(headerFont);
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			headerStyle.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerStyle.setWrapText(true);

			// title style
			XSSFFont titleFont = wb.createFont();
			titleFont.setBold(true);
			titleFont.setFontName("맑은 고딕");

			XSSFCellStyle titleStyle = wb.createCellStyle();
			titleStyle.setFont(titleFont);
			titleStyle.setAlignment(HorizontalAlignment.LEFT);
			titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

			// body font
			XSSFFont bodyFont = wb.createFont();
			bodyFont.setFontName("맑은 고딕");

			// text 항목
			XSSFCellStyle text = wb.createCellStyle();
			text.cloneStyleFrom(defaultStyle);
			text.setFont(bodyFont);
			text.setAlignment(HorizontalAlignment.LEFT);
			text.setVerticalAlignment(VerticalAlignment.CENTER);

			// numeric 항목
			XSSFCellStyle numeric = wb.createCellStyle();
			numeric.cloneStyleFrom(defaultStyle);
			numeric.setFont(bodyFont);
			numeric.setAlignment(HorizontalAlignment.RIGHT);
			numeric.setVerticalAlignment(VerticalAlignment.CENTER);
			numeric.setDataFormat(wb.createDataFormat().getFormat("#,##0"));

			// day count 항목
			XSSFCellStyle dcnt = wb.createCellStyle();
			dcnt.cloneStyleFrom(defaultStyle);
			dcnt.setFont(bodyFont);
			dcnt.setAlignment(HorizontalAlignment.RIGHT);
			dcnt.setVerticalAlignment(VerticalAlignment.CENTER);
			dcnt.setDataFormat(wb.createDataFormat().getFormat("#,##0.0"));

			// percent 항목
			XSSFCellStyle percent = wb.createCellStyle();
			percent.cloneStyleFrom(defaultStyle);
			percent.setFont(bodyFont);
			percent.setAlignment(HorizontalAlignment.RIGHT);
			percent.setVerticalAlignment(VerticalAlignment.CENTER);
			percent.setDataFormat(wb.createDataFormat().getFormat("#,##0.0%"));

			// sheet
			XSSFSheet sheet = wb.createSheet("일일 용기 현황");
			int rownum = 0;
			XSSFCell cell = null;
			XSSFRow row = null;

			String column = null;
			String type = null;
			int rowspan = 0;
			int colspan = 0;
			Object value = null;

			// 1. 부문별 용기 전체 재고 현황 타이틀
			row = this.getRow(sheet, rownum);
			cell = this.getCell(row, 0);
			cell.setCellStyle(titleStyle);
			cell.setCellValue("1. 부문별 용기 전체 재고 현황");

			// 1. 부문별 용기 전체 재고 현황 헤더
			rownum++;
			row = this.getRow(sheet, rownum);
			for (int i = 0; i < m03031Header.size(); i++) {
				cell = this.getCell(row, i);
				cell.setCellStyle(headerStyle);
				// 구분자 _를 기준으로 치환
				cell.setCellValue(String.valueOf(m03031Header.get(i).get("MenuName")).replace("_", "\n"));
			}

			// 1. 부문별 용기 전체 재고 현황 데이터
			rownum++;
			for (Map<String, Object> data : m03031Body) {
				row = this.getRow(sheet, rownum);

				for (int i = 0; i < m03031Header.size(); i++) {
					cell = this.getCell(row, i);

					column = String.valueOf(m03031Header.get(i).get("Name"));
					type = String.valueOf(m03031Header.get(i).get("Def"));
					value = data.get(column);

					if ("numeric".equals(type) && value instanceof BigDecimal) {
						cell.setCellStyle(numeric);
						cell.setCellValue(((BigDecimal) value).doubleValue());

					} else if ("dcnt".equals(type) && value instanceof BigDecimal) {
						cell.setCellStyle(dcnt);
						cell.setCellValue(((BigDecimal) value).doubleValue());

					} else if ("percent".equals(type) && value instanceof BigDecimal) {
						cell.setCellStyle(percent);
						cell.setCellValue(((BigDecimal) value).doubleValue());

					} else if ("text".equals(type) && value instanceof String) {
						cell.setCellStyle(text);
						cell.setCellValue(String.valueOf(value));
					}
				}
				rownum++;
			}

			// 2. 공장별 공병 재고 현황 타이틀
			rownum++;
			row = this.getRow(sheet, rownum);
			cell = this.getCell(row, 0);
			cell.setCellStyle(titleStyle);
			cell.setCellValue("2. 공장별 공병 재고 현황 타이틀");

			// 2. 공장별 공장 재고 현황 헤더
			rownum++;
			row = getRow(sheet, rownum);
			for (int i = 0; i < m03032Header.size(); i++) {
				cell = this.getCell(row, i);
				cell.setCellStyle(headerStyle);
				// 구분자 _를 기준으로 치환
				cell.setCellValue(String.valueOf(m03032Header.get(i).get("MenuName")).replace("_", "\n"));
			}

			// 2. 공장별 공장 재고 현황 데이터
			rownum++;
			for (Map<String, Object> data : m03032Body) {
				row = this.getRow(sheet, rownum);

				for (int i = 0; i < m03032Header.size(); i++) {
					cell = this.getCell(row, i);

					column = String.valueOf(m03032Header.get(i).get("Name"));
					type = String.valueOf(m03032Header.get(i).get("Def"));
					value = data.get(column);

					if ("numeric".equals(type) && value instanceof BigDecimal) {
						cell.setCellStyle(numeric);
						cell.setCellValue(((BigDecimal) value).doubleValue());

					} else if ("dcnt".equals(type) && value instanceof BigDecimal) {
						cell.setCellStyle(dcnt);
						cell.setCellValue(((BigDecimal) value).doubleValue());

					} else if ("text".equals(type) && value instanceof String) {
						cell.setCellStyle(text);
						cell.setCellValue(String.valueOf(value));
					}
				}

				// row span
				if (data.get("TYPERowSpan") instanceof BigDecimal && data.get("TYPERowSpan") != null) {
					rowspan = rownum + ((BigDecimal) data.get("TYPERowSpan")).intValue() - 1;

					if (rowspan >= rownum) {
						sheet.addMergedRegion(new CellRangeAddress(rownum, rowspan, 0, 0));
					}
				}

				rownum++;
			}

			// 3. 공장별 공병 재고 현황 타이틀
			rownum++;
			row = this.getRow(sheet, rownum);
			cell = this.getCell(row, 0);
			cell.setCellStyle(titleStyle);
			cell.setCellValue("3. 주차별 일평균 용기 회수 현황");

			// 3. 공장별 공장 재고 현황 헤더
			rownum++;
			row = getRow(sheet, rownum);
			for (int i = 0; i < m03033Header.size(); i++) {
				cell = this.getCell(row, i);
				cell.setCellStyle(headerStyle);
				// 구분자 _를 기준으로 치환
				cell.setCellValue(String.valueOf(m03033Header.get(i).get("MenuName")).replace("_", "\n"));
			}

			// 3. 공장별 공장 재고 현황 데이터
			rownum++;
			for (Map<String, Object> data : m03033Body) {
				row = this.getRow(sheet, rownum);

				for (int i = 0; i < m03033Header.size(); i++) {
					cell = this.getCell(row, i);

					column = String.valueOf(m03033Header.get(i).get("Name"));
					type = String.valueOf(m03033Header.get(i).get("Def"));
					value = data.get(column);

					if ("TYPE".equals(column) || "ACCT_DESC".equals(column)) {
						cell.setCellStyle(text);
					} else {
						cell.setCellStyle(numeric);
					}
					cell.setCellValue(String.valueOf(value));
				}

				// row span
				if (data.get("TYPERowSpan") instanceof BigDecimal && data.get("TYPERowSpan") != null) {
					rowspan = rownum + ((BigDecimal) data.get("TYPERowSpan")).intValue() - 1;

					if (rowspan >= rownum) {
						sheet.addMergedRegion(new CellRangeAddress(rownum, rowspan, 0, 0));
					}
				}
				rownum++;
			}

			// 4. 공장별 공병 재고 현황 타이틀
			rownum++;
			row = this.getRow(sheet, rownum);
			cell = this.getCell(row, 0);
			cell.setCellStyle(titleStyle);
			cell.setCellValue("4. 주차별 일평균 용기/재고 입고 현황");

			// 4. 공장별 공장 재고 현황 헤더
			rownum++;
			row = getRow(sheet, rownum);
			for (int i = 0; i < m03034Header.size(); i++) {
				cell = this.getCell(row, i);
				cell.setCellStyle(headerStyle);
				// 구분자 _를 기준으로 치환
				cell.setCellValue(String.valueOf(m03034Header.get(i).get("MenuName")).replace("_", "\n"));
			}

			// 4. 공장별 공장 재고 현황 데이터
			rownum++;
			for (Map<String, Object> data : m03034Body) {
				row = this.getRow(sheet, rownum);

				for (int i = 0; i < m03034Header.size(); i++) {
					cell = this.getCell(row, i);

					column = String.valueOf(m03034Header.get(i).get("Name"));
					type = String.valueOf(m03034Header.get(i).get("Def"));
					value = data.get(column);

					if (value instanceof BigDecimal) {
						cell.setCellStyle(numeric);
						cell.setCellValue(((BigDecimal) value).doubleValue());
					} else {
						cell.setCellStyle(text);
						cell.setCellValue(String.valueOf(value));
					}
				}

				// row span
				if (data.get("TYPERowSpan") instanceof BigDecimal && data.get("TYPERowSpan") != null) {
					rowspan = rownum + ((BigDecimal) data.get("TYPERowSpan")).intValue() - 1;

					if (rowspan >= rownum) {
						sheet.addMergedRegion(new CellRangeAddress(rownum, rowspan, 0, 0));
					}
				}

				if (data.get("ACCT_DESCRowSpan") instanceof BigDecimal && data.get("ACCT_DESCRowSpan") != null) {
					rowspan = rownum + ((BigDecimal) data.get("ACCT_DESCRowSpan")).intValue() - 1;

					if (rowspan >= rownum) {
						sheet.addMergedRegion(new CellRangeAddress(rownum, rowspan, 1, 1));
					}
				}

				// col span
				if (data.get("ACCT_DESCSpan") instanceof BigDecimal && data.get("ACCT_DESCSpan") != null) {
					colspan = ((BigDecimal) data.get("ACCT_DESCSpan")).intValue();

					if (colspan >= 1) {
						sheet.addMergedRegion(new CellRangeAddress(rownum, rownum, 1, colspan));
					}
				}
				rownum++;
			}

			// colume size
			for (int i = 0; i < m03034Header.size(); i++) {
				sheet.autoSizeColumn(i);
			}

			wb.write(bos);
			result = bos.toByteArray();
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (wb != null) {
					wb.close();
				}

				if (bos != null) {
					bos.close();
				}

			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
		return result;
	}

	/**
	 * 엑셀 시트에서 행 생성
	 * @param sh
	 * @param iRowNum
	 * @return
	 */
	private XSSFRow getRow(XSSFSheet sheet, int rownum) {
		XSSFRow row = sheet.getRow(rownum);
		if (row == null) {
			row = sheet.createRow(rownum);
		}
		return row;
	}

	/**
	 * 엑셀 시트에서 셀 생성
	 * @param row
	 * @param iColIndex
	 * @return
	 */
	private XSSFCell getCell(XSSFRow row, int colIndex) {
		XSSFCell cell = row.getCell(colIndex);
		if (cell == null) {
			cell = row.createCell(colIndex);
		}
		return cell;
	}
}
