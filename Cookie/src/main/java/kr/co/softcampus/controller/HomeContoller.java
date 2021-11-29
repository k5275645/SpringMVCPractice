package kr.co.softcampus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeContoller {
	
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String home() {
		return "index";
	}
}
