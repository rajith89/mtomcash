package com.ontag.mcash.admin.web.controller;


import javax.servlet.http.HttpServletRequest;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ontag.mcash.admin.web.controller.json.MessageFilter;
import com.ontag.mcash.admin.web.controller.json.PostResponse;
import com.ontag.mcash.admin.web.service.AdministrationService;
import com.ontag.mcash.dal.domain.BoUsers;
import com.ontag.mcash.dal.domain.Merchant;
import com.ontag.mcash.dal.domain.SystemStatus;



@Controller
@RequestMapping("/system")
public class SystemStatusController {

	
    protected static org.slf4j.Logger logger = LoggerFactory.getLogger(SystemStatusController.class);

    @Autowired
    private AdministrationService administrationService;
    
    @RequestMapping(value = "/system-status", method = RequestMethod.GET)
    public String getCommonPage(Model model) {
    	SystemStatus systemStatus = null;
    	try{
    		systemStatus = administrationService.getSystemStatus();
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	
    	model.addAttribute("systemstatus", systemStatus);
    	return "system-control";
    }
    
    @RequestMapping(value = "changestatus-{status}", method = RequestMethod.POST)
    public
    @ResponseBody
    PostResponse postForChangeStatus(@PathVariable String status,@ModelAttribute("systemStatus") SystemStatus sysStatus) {
    	SystemStatus systemStatus = null;
    	PostResponse response = new PostResponse();
    	
    	try {
        	 Integer statusInt = Integer.parseInt(status);
        	 systemStatus = administrationService.getSystemStatus();
	         if(systemStatus.getTransactionStatus() == statusInt){
	        	 if(systemStatus.getTransactionStatus() == SystemStatus.TRAN_STATUS_START)
	        		 throw new Exception("Transaction status already started.");
	        	 else
	        		 throw new Exception("Transaction status already stopped.");
	         }
	        	 	
	         systemStatus.setTransactionStatus(statusInt);
	         administrationService.edit(systemStatus);
        }
	    catch (Exception e) {
	    	//result.addError(new ObjectError("save", e.getMessage()));
	    	String[] strArr = {e.getMessage()};
	    	response.setErrors(strArr);
	    	logger.error(e.getMessage(), e);
	    }
         
        

    	return response;
    }

}
