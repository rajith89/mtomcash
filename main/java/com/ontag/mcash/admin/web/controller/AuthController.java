package com.ontag.mcash.admin.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ontag.mcash.admin.web.controller.json.PostResponse;
import com.ontag.mcash.admin.web.service.BoUserService;
import com.ontag.mcash.dal.domain.BoUsers;
import com.ontag.mcash.dal.domain.CorporateBank;
import com.ontag.mcash.dal.domain.MerchantUsers;


@Controller
@RequestMapping("/auth")
public class AuthController {

	protected static Logger logger = LoggerFactory.getLogger(AuthController.class);


//    @Autowired
//    private OffUserService appUserService;
//    
    @Autowired
    private BoUserService boUserService;
    
    /**
     * Handles and retrieves the login JSP page
     *
     * @param error
     * @return the name of the JSP page
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginPage(@RequestParam(value = "error", required = false) boolean error,
                               ModelMap model) {
        logger.info("Received request to show login page");

        if (error) {
            model.put("error", "You have entered an invalid username or password!");
        } else {
            model.put("error", "");
        }

        return "loginpage";
    }
    
    @RequestMapping(value = "/resetpassword-page", method = RequestMethod.GET)
    public String getResetPasswordPage(Model model) {
    	
    	BoUsers user = new BoUsers();
    	model.addAttribute("bouser", user);
        return "passwordreset";
    }
    
    @RequestMapping(value = "resetpassword", method = RequestMethod.POST)
    public
    @ResponseBody PostResponse resetPassword(@ModelAttribute("bouser") BoUsers bouser, BindingResult result) {

    	if (!result.hasErrors()) {

            try {
            	boUserService.resetPassword(bouser);
            } catch (Exception e) {
            	logger.error(e.getMessage(), e);
                result.addError(new ObjectError("save", e.getMessage()));
            }
        }
        PostResponse response = new PostResponse(result.getAllErrors());
        return response;
    }
    
    @RequestMapping(value = "/validatePasswordPin", method = RequestMethod.GET)  
    public @ResponseBody ResultData validatePasswordPin(@RequestParam(value = "username", required = true) String username, @RequestParam(value = "password", required = true) String passwordPin, HttpServletRequest request, HttpServletResponse response) throws Exception {  
        ResultData resultData = new ResultData();
		try {
				System.out.println(username);
				System.out.println(passwordPin);
				boolean resultValidate = boUserService.validatePasswordPin(username, passwordPin);
				if(resultValidate)
					resultData.setCode(ResultData.SUCCESS_CODE);
				else{
					resultData.setCode(ResultData.FAILED_CODE);
				}
					
		} catch (Exception ex) {
			resultData.setCode(ResultData.FAILED_CODE);
			ex.printStackTrace();
		}
  		return resultData;
    }  
    
    @RequestMapping(value = "password_change_page", method = RequestMethod.GET)
    public String getChangePasswordPage(@RequestParam(value = "error", required = false) boolean error,
                               ModelMap model) {
        logger.debug("Received request to show lostlogin page");
        
        BoUsers user = null;
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        
        try {
	        user = boUserService.getUserByUserName(name);
	        logger.info("Logged Name : " + name);	       
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        model.addAttribute("user", user);
        return "change-password";
    }
    
    @RequestMapping(value = "changepassword", method = RequestMethod.POST)
    public
    @ResponseBody
    PostResponse saveUser(@ModelAttribute("user") BoUsers user, BindingResult result) {

    	logger.info("changepassword  1 ###### : " + user);
        if (!result.hasErrors()) {

            try {
               
            	boUserService.changePassword(user);
            } catch (Exception e) {
            	logger.info("group save Error : " + e.getMessage());
                logger.error(e.getMessage(), e);
                result.addError(new ObjectError("save", "Error occurred while saving"));
            }
        }
        PostResponse response = new PostResponse(result.getAllErrors());
        logger.debug("response:" + response);
        return response;
    }

    
    /**
     * Handles and retrieves the denied JSP page. This is shown whenever a regular user
     * tries to access an admin only page.
     *
     * @return the name of the JSP page
     */
    @RequestMapping(value = "/denied", method = RequestMethod.GET)
    public String getDeniedPage(Model model) {
        logger.debug("error.access.denied");
        model.addAttribute("title", "Access is denied.");
        model.addAttribute("text", "You do not have permission to access this page!");
        return "error-page";
    }

    @RequestMapping(value = "/duplicateLogin", method = RequestMethod.GET)
    public String getDuplicateLogin(Model model) {
        logger.debug("error.session.duplicated");
        model.addAttribute("title", "Session Terminated.");
        model.addAttribute("text", "Your session was terminated since another session has been created with the same credentials.");
        return "error-page";
    }
    
    
  public static class ResultData {
		
		public final static String SUCCESS_CODE = "00";
		public final static String FAILED_CODE = "01";
		private String code;
		private String message;
		
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		
    }
}
