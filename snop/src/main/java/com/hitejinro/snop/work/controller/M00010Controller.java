package com.hitejinro.snop.work.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hitejinro.snop.common.util.FileUtil;
import com.hitejinro.snop.common.util.ReadExcel;
import com.hitejinro.snop.common.util.SessionUtil;
import com.hitejinro.snop.work.service.M00010Service;

/**
 * 프로그램 :: M00010 : 초기화면
 * 작성일자 :: 2021.7.27
 * 작 성 자 :: 김태환
 */
@Controller
public class M00010Controller {

	private static final Logger logger = LoggerFactory.getLogger(M00010Controller.class);

	@Inject
	private M00010Service M00010Service;
	
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
	@RequestMapping(value = "/work/M00010", method = RequestMethod.GET)
	public ModelAndView getView(@RequestParam Map<String, Object> params) throws Exception {
		ModelAndView view = new ModelAndView("/work/M00010");
		
		return view;
	}
	
	/**
	 * 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/work/M00010/search", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> search(@RequestBody Map<String, Object> oParams) throws Exception {
		logger.info("/work/M00010/search");

		Map<String, Object> result = new HashMap<String, Object>();
		
		
		List<Map<String, Object>> salesGrid = M00010Service.searchSalesGrid(oParams);	//제품수급_판매(맥주/소주 공통)
		List<Map<String, Object>> prdtGrid = M00010Service.searchPrdtGrid(oParams);		//제품수급_생산(맥주/소주 공통)
		List<Map<String, Object>> stockGrid = M00010Service.searchStockGrid(oParams);	//제품수급_재고(맥주/소주 공통)
		List<Map<String, Object>> salesPie = M00010Service.searchSalePie(oParams);		//판매구성비
		List<Map<String, Object>> stockPie = M00010Service.searchStockPie(oParams);		//재고구성비
		List<Map<String, Object>> vesselGrid = M00010Service.searchVesselGrid(oParams);	//용기수급

		if (salesGrid.size() < 1 && prdtGrid.size() < 1 && stockGrid.size() < 1 && salesPie.size() < 1 && stockPie.size() < 1 && vesselGrid.size() < 1) {
			result.put("MEESSAGE", "조회된 데이터가 없습니다.");
			result.put("RESULT", "NO_DATA");
			return result;
		}

		//참이슬공병 / 진로공병의 회수량을 합산으로 계산한다.
		double actualQty = 0;
		double inputQty = 0;
		double inputRate = 0;
		for(int i=0; i<vesselGrid.size(); i++) {
			Map<String, Object> vesselData = vesselGrid.get(i);
			
			if(((String)vesselData.get("VESSEL_CODE")).equals("1") && (((String)vesselData.get("BRAND_CODE")).equals("B2010") || ((String)vesselData.get("BRAND_CODE")).equals("B2020"))) {
				actualQty += Double.parseDouble(""+vesselData.get("ACTUAL_QTY"));
				inputQty += Double.parseDouble(""+vesselData.get("INPUT_QTY"));
			}
			
			if(i==(vesselGrid.size()-1)) {
				inputRate = (Math.round(inputQty / actualQty * 1000)) / 10.0;
			}
		}
		
		
		result.put("SALESDATA", salesGrid);
		result.put("PRDTDATA", prdtGrid);
		result.put("STOCKDATA", stockGrid);
		result.put("inputRate", inputRate);
		result.put("SALESPIEDATA", salesPie);
		result.put("STOCKPIEDATA", stockPie);
		result.put("VESSELDATA", vesselGrid);
		result.put("RESULT", "SUCCESS");

		return result;
	}

}
