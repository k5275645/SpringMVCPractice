package com.hitejinro.snop.work.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.hitejinro.snop.common.util.FileUtil;
import com.hitejinro.snop.common.util.ReadExcel;
import com.hitejinro.snop.common.util.SessionUtil;
import com.hitejinro.snop.security.vo.User;
import com.hitejinro.snop.work.service.M08110Service;

/**
 * 프로그램 :: M08110 : 영업예상판매량
 * 작성일자 :: 2021.05.24
 * 작 성 자 :: 김태환
 */
@Controller
public class M08110Controller {

	private static final Logger logger = LoggerFactory.getLogger(M08110Controller.class);

	@Inject
	private M08110Service m08110Service;
	
	@Inject
	private SessionUtil sessionUtil;

	@Resource(name="readExcel")
	ReadExcel readExcel;
	
	@Resource(name="fileUtil")
	private FileUtil fileUtil;
	
	/**
	 * UI08110
	 * @param params
	 * @return /U108110
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08110", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		ModelAndView view = new ModelAndView("/work/M08110");

		return view;
	}
	
	/**
	 * 상단 sum 그리드 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08110/searchSum", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> searchSum(@RequestBody Map<String, Object> oParams) throws Exception {
		logger.info("/work/M08110/searchSum");

		Map<String, Object> result = new HashMap<String, Object>();

		List<List<Map<String, Object>>> body = m08110Service.searchSum(oParams);

		if (body.get(0).size() < 1) {
			result.put("MEESSAGE", "조회된 데이터가 없습니다.");
			result.put("RESULT", "NO_DATA");
			return result;
		}

		result.put("BODY", body);
		result.put("RESULT", "SUCCESS");

		return result;
	}

	/**
	 * 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08110/search", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> search(@RequestBody Map<String, Object> oParams) throws Exception {
		logger.info("/work/M08110/search");

		Map<String, Object> result = new HashMap<String, Object>();
		
		List<List<Map<String, Object>>> body = m08110Service.search(oParams);

		if (body.get(0).size() < 1) {
			result.put("MEESSAGE", "조회된 데이터가 없습니다.");
			result.put("RESULT", "NO_DATA");
			return result;
		}

		result.put("BODY", body);
		result.put("RESULT", "SUCCESS");

		return result;
	}

	/**
	 * 삭제
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08110/deleteVersion", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteVersion(@RequestBody Map<String, Object> params) throws Exception {

		logger.info("/work/M08110/deleteVersion");
		logger.info(params.toString());

		Map<String, Object> result = new HashMap<String, Object>();

		try {
			User user = sessionUtil.getUserInfo();
			params.put("employeeNumber", user.getUsername());
			
			result = m08110Service.deleteVersion(params);

		} catch (Exception e) {
			result.put("MESSAGE", "오류가 발생하였습니다.");
			result.put("RESULT", "ERROR");
			return result;
		}

		return result;
	}
	
	/**
	 * 엑셀 업로드 처리
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/work/M08110/excelUpload.json", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> excelUpload(@RequestParam Map<String,Object> params, MultipartHttpServletRequest request)  throws Exception{
		Map<String, Object> oResult = new HashMap<String, Object>();
		
		try {
			// 1. 업로드된 파일을 서버에 저장 : fileUploadResult[{"originalFileName", "storedFileName", "storedFilePath"}]
			params = fileUtil.uploadFile(params, request);
			List<Map<String, String>> arrFileUploadResult = (List<Map<String, String>>)params.get("fileUploadResult");
			
			// 2. 업로드된 파일(엑셀)을 읽어들인다.
			if(arrFileUploadResult == null || arrFileUploadResult.size() < 1) {
				oResult.put("RETURN_FLAG", "F");
				oResult.put("RETURN_MSG", "파일 업로드가 실패하였습니다.<BR/>관리자에게 문의바랍니다.");
				return oResult;
				
			} else {
				String sStoredFilePath = arrFileUploadResult.get(0).get("storedFilePath");
				int iSkipRowCount = 2;
				
				try {
					User user = sessionUtil.getUserInfo();
					params.put("userId", user.getUsername());
					
					// 3. 템플릿파일(영업예상판매량_업로드양식.xlsx)을 기반으로 엑셀 읽어들인 데이터
					List<Map<String, Object>> arrData = new ArrayList<Map<String, Object>>();
					arrData = readExcel.readExcel(sStoredFilePath, iSkipRowCount, 0, new int[]{ 0 }); // - 제품코드가 없는 행은 제외
					if(arrData == null || arrData.size() < 1) {
						oResult.put("RETURN_FLAG", "F");
						oResult.put("RETURN_MSG", "엑셀파일에 데이터가 존재하지 않습니다.\n확인 후 다시 시도해주세요.");
						return oResult;
					}
					params.put("arrUploadData", arrData);
					params.put("SKIP_ROW_COUNT", iSkipRowCount);
					
					// 4. 업로드 처리
					oResult = m08110Service.uploadData(params);
					
				} catch(Exception e) {
					e.printStackTrace();
					logger.info("Read Excel Error :: originalFileName=" + arrFileUploadResult.get(0).get("originalFileName") + ", storedFilePath=" + sStoredFilePath + " :: " + e.getMessage(), e);
					oResult.put("RETURN_FLAG", "F");
					oResult.put("RETURN_MSG", "엑셀파일을 읽는 도중 오류가 발생하였습니다.\n엑셀파일을 확인하시기 바랍니다.\n(DRM이 해제된 엑셀파일만 가능합니다)");
				}
			}
			
			oResult.put("message", "OK");
			oResult.put("result", "0");
			
		} catch(Exception e) {
			logger.info("[ E99 ] " + e.getMessage());
			e.printStackTrace();
			oResult.put("message", e.getMessage());
			oResult.put("result", "99");
		};
		
		return oResult;
	}
	
	/**
	 * version Combo 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M08110/versionCombo", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> versionCombo(@RequestParam Map<String, Object> params) throws Exception {
		logger.info("/work/M08110/versionCombo");
		logger.info(params.toString());

		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> body = m08110Service.versionCombo(params);

		if (body.size() < 1) {
			result.put("MEESSAGE", "조회된 데이터가 없습니다.");
			result.put("RESULT", "NO_DATA");
			return result;
		}

		result.put("BODY", body);
		result.put("RESULT", "SUCCESS");

		return result;
	}

    /**
    * 템플릿 파일 다운로드
    * @param req
    * @param res
    * @param params
    * @param model
    * @throws Exception
    */
	@SuppressWarnings("unchecked")
    @RequestMapping(value = "/work/M08110/downloadTemplate", produces = "text/plain; charset=UTF-8", method = RequestMethod.POST)
	public void downloadTemplate(HttpServletRequest req, HttpServletResponse res, @RequestParam Map<String, Object> params, ModelMap model) throws Exception {
        // 1. 다운할 파일정보 조회
        String sStoredFileName = "영업예상판매량_Template.xlsx"; // - 서버에 저장된 파일 명. 위치는 "C:\SCP\template\"
        String sDownloadFileName = "영업예상판매량_Template.xlsx"; // - 클라이언트에 다운로드될 파일 명
        
        // 3. 파일 다운로드
        params.put("storedFileName", sStoredFileName);
        params.put("downloadFileName", sDownloadFileName);
        
        fileUtil.downloadFile(params, res, true); // - 템플릿 파일경로(C:\HITEJINRO\snop\template)
    }
    
}
