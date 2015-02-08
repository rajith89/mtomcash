package com.ontag.mcash.admin.web.controller;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ontag.mcash.admin.web.controller.BoRolesController.ListData;
import com.ontag.mcash.admin.web.controller.json.DataGridData;
import com.ontag.mcash.admin.web.controller.json.PostResponse;
import com.ontag.mcash.admin.web.service.BoRolesService;
import com.ontag.mcash.admin.web.service.BoUserRoleService;
import com.ontag.mcash.admin.web.service.BoUserService;
import com.ontag.mcash.admin.web.util.Constants;
import com.ontag.mcash.dal.domain.BoRoles;
import com.ontag.mcash.dal.domain.BoUserRole;
import com.ontag.mcash.dal.domain.BoUsers;
import com.ontag.mcash.dal.domain.CurrencyRates;
import com.ontag.mcash.dal.domain.MerchantRolesUsers;

@Controller
@RequestMapping("/user")
public class BoUserController {

	protected static org.slf4j.Logger logger = LoggerFactory
			.getLogger(BoUserController.class);

	@Autowired
	private BoUserService boUserService;

	@Autowired
	private BoRolesService boRolesService;
	
	@Autowired
	private BoUserRoleService boUserRoleService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	
	public String getUserListPage() {
		logger.info("show currencycode-list");
		return "bo-user-list";
	}

	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String getAddPage(Model model) {

		BoUsers boUsers = new BoUsers();
		boUsers.setId(0);
		model.addAttribute("bouser", boUsers);
		model.addAttribute("screenMode", "add");
		return "bo-user-edit";
	}

	@RequestMapping(value = "edit-{bouserId}", method = RequestMethod.GET)
	public String getEditPage(Model model, @PathVariable int bouserId) {

		try {
			logger.info("getEditPage ########### 4 : " + bouserId);
			BoUsers boUsers = boUserService.findById(bouserId);

			List<BoUserRole> boUserRole = boUserRoleService.getBoUserRolesById(bouserId);
			List<Short> roleIdList = new LinkedList<Short>();

			for (BoUserRole role : boUserRole) {
				roleIdList.add(role.getBoRoleId().getId());
				System.out.println(role.getBoRoleId().getId());
			}
			
			model.addAttribute("rolesusers", roleIdList);
			model.addAttribute("bouser", boUsers);
			model.addAttribute("screenMode", "edit");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "bo-user-edit";
	}

	@RequestMapping(value = "/list.json", method = RequestMethod.POST, headers = Constants.REQUEST_HEADER_ACCEPT_JSON)
	public @ResponseBody
	DataGridData getListData(
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "rows", defaultValue = "10") int rows) {
		try {

			int start = (page - 1) * rows;
			start = start < 0 ? 0 : start;
			
			Authentication auth = SecurityContextHolder.getContext()
					.getAuthentication();
			
			String userName = auth.getName();
			int userId = boUserService.getUserByUserName(userName)
					.getId();
			
			ListData[] listData = ListData.getArray(boUserService.getList(
					start, rows), userId);
			
			DataGridData<ListData> d = new DataGridData<>();
			d.setRows(listData);
			d.setTotal(boUserService.countAll());

			logger.debug("returning start = {}", start);
			return d;

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}

		return null;
	}

	@RequestMapping(value = "/boRoles.json", method = RequestMethod.POST, headers = Constants.REQUEST_HEADER_ACCEPT_JSON)
	public @ResponseBody
	DataGridData getListDataForAdd(
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "rows", defaultValue = "10") int rows) {
		try {

			logger.debug("/boRoles.json--> {}-{}", page, rows);
			int start = (page - 1) * rows;
			start = start < 0 ? 0 : start;
			
			ListData_boRoles[] listData = ListData_boRoles
					.getArray(boRolesService.getList(start, rows));

			DataGridData<ListData_boRoles> d = new DataGridData<>();
			d.setRows(listData);
			d.setTotal(boRolesService.countAll());

			logger.debug("returning start = {}", start);
			return d;

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}

		return null;
	}

	@RequestMapping(value = "save-{roleIds}-{roleNames}", method = RequestMethod.POST)
	public @ResponseBody
	PostResponse saveBoUsers(@ModelAttribute("bouser") BoUsers boUsers,
			BindingResult result, @PathVariable String roleIds,
			@PathVariable String roleNames) {

		logger.info("saveBotUser  ################## 1; inside POST for roleIds :"
				+ roleIds + " " + roleNames);
		if (!result.hasErrors()) {
			logger.info("Edit/SaveBotUser ################## 2");
			try {
				if (boUsers.getId() > 0) {
					logger.info("EditBotUser ################## 3");
					boUserService.edit(boUsers);					
					boUserRoleService.editBoUserRoles(boUsers, roleIds, roleNames);
				} else {
					//boUsers.setId(null);
					logger.info("saveBotUser ################## 4 : "
							+ boUsers.getUserName() + " , "
							+ boUsers.getFirstName() + " , "
							+ boUsers.getLastName());
					
					logger.info("saveMerchantRolesUsers ################## 4");
					boUserRoleService.addBoUserRole(boUsers, roleIds, roleNames);
				}
				
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				result.addError(new ObjectError("save", e.getMessage()));
			}
		}
		PostResponse response = new PostResponse(result.getAllErrors());
		return response;
	}
	
	@RequestMapping(value = "activate-{ids}", method = RequestMethod.POST)
    public
    @ResponseBody
    PostResponse postForActivate(@PathVariable String ids) {
       
        try {
        	String []userIdsArr = ids.split(",");
        	Integer[] userIds = new Integer[userIdsArr.length];
	        
	        for(int i=0;i<userIdsArr.length;i++){
	        	userIds[i] = Integer.parseInt(userIdsArr[i]);
	        	System.out.println(userIds[i]);
	        }
	        System.out.println("Activate Controller");
	        
	        boUserService.activateUsers(userIds);
        }
	     catch (Exception e) {
	    	logger.info("group save Error : " + e.getMessage());
	        logger.error(e.getMessage(), e);
	     }
         
        PostResponse response = new PostResponse();

    	return response;
    }
	
	@RequestMapping(value = "deactivate-{ids}", method = RequestMethod.POST)
    public
    @ResponseBody
    PostResponse postForInactivate(@PathVariable String ids) {
       
        try {
        	String []userIdsArr = ids.split(",");
        	Integer[] userIds = new Integer[userIdsArr.length];
	        
	        for(int i=0;i<userIdsArr.length;i++){
	        	userIds[i] = Integer.parseInt(userIdsArr[i]);
	        	System.out.println(userIds[i]);
	        }
	        System.out.println("Deactivate Controller");
	        boUserService.deactivateUsers(userIds);
        }
	     catch (Exception e) {
	    	logger.info("group save Error : " + e.getMessage());
	        logger.error(e.getMessage(), e);
	     }
         
        PostResponse response = new PostResponse();

    	return response;
    }
	
	 @InitBinder
	 public void initBinder(WebDataBinder binder) { 
	  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"); 
	  dateFormat.setLenient(false); 
	  binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false)); 
	 }

	static class ListData {
		private int id;
		private String userName;
		private String passwordEnc;
		private String firstName;
		private String lastName;
		private String telephone;
		private long createdUser;
		private Date createdDate;
		private long lastUpdateUser;
		private Date lastUpdateDate;
		private String status;
		private int editable;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getPasswordEnc() {
			return passwordEnc;
		}

		public void setPasswordEnc(String passwordEnc) {
			this.passwordEnc = passwordEnc;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getTelephone() {
			return telephone;
		}

		public void setTelephone(String telephone) {
			this.telephone = telephone;
		}

		public long getCreatedUser() {
			return createdUser;
		}

		public void setCreatedUser(long createdUser) {
			this.createdUser = createdUser;
		}

		public Date getCreatedDate() {
			return createdDate;
		}

		public void setCreatedDate(Date createdDate) {
			this.createdDate = createdDate;
		}

		public long getLastUpdateUser() {
			return lastUpdateUser;
		}

		public void setLastUpdateUser(long lastUpdateUser) {
			this.lastUpdateUser = lastUpdateUser;
		}

		public Date getLastUpdateDate() {
			return lastUpdateDate;
		}

		public void setLastUpdateDate(Date lastUpdateDate) {
			this.lastUpdateDate = lastUpdateDate;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
		
		public int getEditable() {
			return editable;
		}

		public void setEditable(int editable) {
			this.editable = editable;
		}

		public static ListData[] getArray(List<Object> dataList, int userId) {

			ListData[] result = new ListData[dataList.size()];
			int count = 0;
			BoUsers boUser;
			for (Object object : dataList) {
				boUser = (BoUsers) object;
				ListData row = new ListData();

				row.id = boUser.getId();
				row.userName = boUser.getUserName();
				row.passwordEnc = boUser.getPasswordEnc();
				row.firstName = boUser.getFirstName();
				row.lastName = boUser.getLastName();
				row.telephone = boUser.getTelephone();			
				row.createdUser = boUser.getCreatedUser();
		
				if (boUser.getCreatedDate() != null) {
					row.createdDate = boUser.getCreatedDate();
				}
	
					row.lastUpdateUser = boUser.getLastUpdateUser();			

				if (boUser.getLastUpdateDate() != null) {
					row.lastUpdateDate = boUser.getLastUpdateDate();
				}
		
				row.status = (boUser.getStatus().equals(BoUsers.BOUSER_STATUS_ACTIVE)) ? BoUsers.BOUSER_STATUS_ACTIVE_DESC : BoUsers.BOUSER_STATUS_INACTIVE_DESC;
				row.editable =  boUser.getId() == userId ? 1 : 0;
				
				System.out.println(row.editable);
				
				result[count++] = row;
			}
			
			return result;
		}
	}

	static class ListData_boRoles {
		private int id;
		private String name;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public static ListData_boRoles[] getArray(List<Object> dataList) {

			ListData_boRoles[] result = new ListData_boRoles[dataList.size()];
			int count = 0;
			BoRoles boRole;
			for (Object object : dataList) {
				boRole = (BoRoles) object;
				ListData_boRoles row = new ListData_boRoles();
				row.id = boRole.getId();
				row.name = boRole.getName();
				result[count++] = row;
			}
			return result;
		}
	}
}
