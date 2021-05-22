package com.covid.minus.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UIController {
	@RequestMapping("/")
	public String welcome() {
	 System.out.println("INDEX CALLED");
	    return "index";
	}
	
	
	
	@RequestMapping("/requests")
	public String requests() {
	    return "myRequests";
	}
	
	@RequestMapping("/about-us")
	public String aboutUs() {
	    return "about";
	}
	
	@RequestMapping("/invite")
	public String invite() {
	    return "invite";
	}
	
}
