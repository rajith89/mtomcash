package com.ontag.mcash.admin.web.controller;
 
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

 
@Controller
@RequestMapping("/welcome")
public class HelloController {
 
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {
		
		model.addAttribute("message", "Test");
		return "hello";
 
	}

    
	@RequestMapping(value="test", method = RequestMethod.GET)
	public @ResponseBody Shop getShopInJSON() {
		Shop shop = new Shop();
		shop.setName("test");
		shop.setStaffName(new String[]{"NAME1", "NAME2"});
		return shop;
	}
 
}

class Shop {

	String name;
	
	String staffName[];
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String[] getStaffName() {
		return staffName;
	}
	public void setStaffName(String[] staffName) {
		this.staffName = staffName;
	}
	public Shop() {
	} 
	
}