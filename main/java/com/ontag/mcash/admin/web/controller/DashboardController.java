package com.ontag.mcash.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
	
	@RequestMapping(value = "", method = RequestMethod.GET)
    public String getDashboardPage(HttpServletRequest request) {
    	return "dashboard";
    }

}
