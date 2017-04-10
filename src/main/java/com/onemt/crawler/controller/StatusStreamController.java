package com.onemt.crawler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.onemt.crawler.twitter.StatusStreamService;

@Controller
public class StatusStreamController {

	@Autowired
	private StatusStreamService statusStreamService;
	
	@RequestMapping("/test")
	public String test(){
		System.out.println(statusStreamService);
		statusStreamService.getUserStatus();
		
		
		return "index";
	}
	
}
