package kr.co.softcampus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TestController {

	@RequestMapping(value="/test1", method = RequestMethod.GET)
	public String test1() {
		return "test1";
	}
	
	@RequestMapping(value="/test2", method = RequestMethod.POST)
	public String test2() {
		return "test2";
	}
	
	@GetMapping("/test3")
	public String test3() {
		return "test3";
	}
	
	@PostMapping("/test4")
	public String test4() {
		return "test4";
	}
	
	@RequestMapping(value="/test5", method = {RequestMethod.POST, RequestMethod.GET})
	public String test5() {
		return "test5";
	}
}
