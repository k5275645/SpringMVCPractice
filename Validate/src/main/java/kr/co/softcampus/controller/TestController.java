package kr.co.softcampus.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import kr.co.softcampus.beans.DataBean1;

@Controller
public class TestController {
	
	@GetMapping("/input_data")
	public String input_data() {
		return "input_data";
	}
	
	@PostMapping("/input_pro")
	public String input_pro(@Valid DataBean1 dataBean1, BindingResult result) {
		System.out.printf("data1 : %s\n", dataBean1.getData1());
		System.out.printf("data2 : %s\n", dataBean1.getData2());
		
		System.out.printf("BindingResult : %s\n", result);
		
		// 유효성 검사에서 위반된 부분이 있다면
		if (result.hasErrors()) {
			// 위반된 결과를 출력
			for (ObjectError obj : result.getAllErrors()) {
				System.out.printf("메시지 : %s\n", obj.getDefaultMessage());
				System.out.printf("code : %s\n", obj.getCode());
				System.out.printf("object name : %s\n", obj.getObjectName());
				
				String[] codes = obj.getCodes();
				for(String c1 : codes) {
					System.out.println(c1);
				}
				
				if ( codes[0].equals("Size.dataBean1.data1") ) {
					System.out.println("data1은 2 ~ 10 글자여야합니다.");
				} else if ( codes[0].equals("Max.dataBean1.data2") ) {
					System.out.println("data2는 100이하여야합니다.");
				}
				
				System.out.println("------------------------------------");
			}
			return "input_data";
		}
		
		return "input_success";
	}
}
