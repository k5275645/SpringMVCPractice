package com.hitejinro.snop.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 엑셀 파일을 읽어들인다.
 * 
 * @author ykw
 *
 */
@Component("readExcel")
public class ReadExcel {
	private static final Logger logger = LoggerFactory.getLogger(ReadExcel.class);

	/**
	 * 엑셀 파일을 읽어서 반환한다.
	 * 
	 * @param sFileFullPath
	 * @param iSkipRowCnt
	 * @param iSkipColumnCnt
	 * @return [{ COL0, COL1, ... }]
	 * @throws Exception
	 */
	public ArrayList<Map<String, Object>> readExcel(String sFileFullPath, int iSkipRowCnt, int iSkipColumnCnt) throws Exception {
		ArrayList<Map<String, Object>> arrData = new ArrayList<Map<String, Object>>();

		// 1. 파일 읽기
		File file = new File(sFileFullPath);
		FileInputStream inputStream = new FileInputStream(file);
		String fileExtensionName = sFileFullPath.substring(sFileFullPath.lastIndexOf("."));
		Workbook workbook = null;
		logger.info("Excel File Start :: " + file.getName() + " :: Server File Full Path : " + sFileFullPath);

		// 2. 확장자에 따른 엑셀 처리
		if (".xlsx".equals(fileExtensionName)) {
			workbook = new XSSFWorkbook(inputStream);
		} else if (".xls".equals(fileExtensionName)) {
			workbook = new HSSFWorkbook(inputStream);
		}

		// 3. 시트 체크
		Sheet sheet = workbook.getSheetAt(0);
		int iLastRowNum = sheet.getLastRowNum();
		logger.info("Excel File Info :: " + file.getName() + " :: iLastRowNum : " + iLastRowNum);
		if (iSkipRowCnt > iLastRowNum)
			return arrData;

		// 4. 행별로 데이터 체크
		for (int r = iSkipRowCnt; r <= iLastRowNum; r++) {
			Row row = sheet.getRow(r);
			Map<String, Object> mData = new HashMap<String, Object>();
			int iCol = 0;
			if (row != null) {
				for (int c = iSkipColumnCnt; c < row.getLastCellNum(); c++) {
					Cell cell = row.getCell(c);
					mData.put("COL" + iCol++, getValue(cell));
				}
				mData.put("_ROWNUM", String.valueOf(row.getRowNum() + 1));
				arrData.add(mData);
			}
		}
		logger.info("Excel File End :: " + file.getName() + " :: arrData.size() : " + arrData.size());

		return arrData;
	}

	/**
	 * 엑셀 파일을 읽어서 반환한다.
	 * 
	 * @param sFileFullPath
	 * @param iSkipRowCnt
	 * @param iSkipColumnCnt
	 * @param arrCheckColIndex 해당 컬럼의 값이 비어있으면, 해당 행을 패스한다. iSkipColumnCnt를 제외하고
	 *                         컬럼Index를 체크한다.
     * @return [{ _ROWNUM(행번호), COL0, COL1, ... }]
	 * @throws Exception
	 */
	public ArrayList<Map<String, Object>> readExcel(String sFileFullPath, int iSkipRowCnt, int iSkipColumnCnt, int[] arrCheckColIndex) throws Exception {
		ArrayList<Map<String, Object>> arrData = new ArrayList<Map<String, Object>>();
		File file = null;
		FileInputStream inputStream = null;
		Workbook workbook = null;
		
		try {
			// 1. 파일 읽기
			file = new File(sFileFullPath);
			inputStream = new FileInputStream(file);
			String fileExtensionName = sFileFullPath.substring(sFileFullPath.lastIndexOf("."));

			logger.info("Excel File Start :: " + file.getName() + " :: Server File Full Path : " + sFileFullPath);

			// 2. 확장자에 따른 엑셀 처리
			if (".xlsx".equalsIgnoreCase(fileExtensionName)) {
				workbook = new XSSFWorkbook(inputStream);
			} else if (".xls".equalsIgnoreCase(fileExtensionName)) {
				workbook = new HSSFWorkbook(inputStream);
			}

			// 3. 시트 체크
			Sheet sheet = workbook.getSheetAt(0);
			int iLastRowNum = sheet.getLastRowNum();
			logger.info("Excel File Info :: " + file.getName() + " :: iLastRowNum : " + iLastRowNum);
			if (iSkipRowCnt > iLastRowNum)
				return arrData;

			// 4. 행별로 데이터 체크
			for (int r = iSkipRowCnt; r <= iLastRowNum; r++) {
				Row row = sheet.getRow(r);
				Map<String, Object> mData = new HashMap<String, Object>();
				int iCol = 0;
				if (row != null) {
					for (int c = iSkipColumnCnt; c < row.getLastCellNum(); c++) {
						Cell cell = row.getCell(c);
						mData.put("COL" + iCol++, getValue(cell));
					}
					if (arrCheckColIndex != null) {
						boolean bFlag = false;
						for (int i = 0; i < arrCheckColIndex.length; i++) {
							if (StringUtils.isEmpty((String) mData.get("COL" + arrCheckColIndex[i]))) {
								bFlag = true;
								i = arrCheckColIndex.length;
							}
						}
						if (bFlag)
							continue;
					}
					mData.put("_ROWNUM", String.valueOf(row.getRowNum() + 1));
					arrData.add(mData);
				}
			}
			logger.info("Excel File End :: " + file.getName() + " :: arrData.size() : " + arrData.size());
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (workbook != null) {
					workbook.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return arrData;
	}

	/**
	 * 엑셀 파일을 읽어서 반환한다.
	 * 
	 * @param sFileFullPath
	 * @param iRowIndex
	 * @param iColIndex
	 * @return
	 * @throws Exception
	 */
	public String readExcelCell(String sFileFullPath, int iRowIndex, int iColIndex) throws Exception {
		String value = "";

		// 1. 파일 읽기
		File file = new File(sFileFullPath);
		FileInputStream inputStream = new FileInputStream(file);
		String fileExtensionName = sFileFullPath.substring(sFileFullPath.indexOf("."));
		Workbook workbook = null;

		// 2. 확장자에 따른 엑셀 처리
		if (".xlsx".equals(fileExtensionName)) {
			workbook = new XSSFWorkbook(inputStream);
		} else if (".xls".equals(fileExtensionName)) {
			workbook = new HSSFWorkbook(inputStream);
		}

		// 3. 시트 체크
		Sheet sheet = workbook.getSheetAt(0);

		// 4. 해당 셀 읽기
		try {
			Row row = sheet.getRow(iRowIndex);
			if (row != null) {
				Cell cell = row.getCell(iColIndex);
				value = getValue(cell);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 셀의 값을 문자열형식으로 반환한다.
	 * 
	 * @param cell
	 * @return
	 * @throws Exception
	 */
	private String getValue(Cell cell) throws Exception {
		String value = "";
		if (cell == null) {
			value = "";
		} else if (cell.getCellType() == CellType.STRING) {
			value = cell.getStringCellValue();
		} else if (cell.getCellType() == CellType.NUMERIC) {
			value = (new BigDecimal(cell.getNumericCellValue())).toPlainString();
		} else if (cell.getCellType() == CellType.FORMULA) {
			switch (cell.getCachedFormulaResultType()) {
			case NUMERIC:
				value = (new BigDecimal(cell.getNumericCellValue())).toString();
				break;
			case STRING:
				value = cell.getStringCellValue();
				break;
			}
		} else if (cell.getCellType() == CellType.BOOLEAN) {
			value = new Boolean(cell.getBooleanCellValue()).toString();
		} else if (cell.getCellType() == CellType.ERROR) {
			value = new Byte(cell.getErrorCellValue()).toString();
		} else {
			value = cell.getStringCellValue();
		}
		return value;
	}
}
