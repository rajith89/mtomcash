package com.ontag.mcash.admin.web.controller;

import java.util.LinkedList;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ontag.mcash.admin.web.controller.BankController.ListData;
import com.ontag.mcash.admin.web.controller.json.DataGridData;
import com.ontag.mcash.admin.web.controller.json.PostResponse;
import com.ontag.mcash.admin.web.service.AppPermissionsService;
import com.ontag.mcash.admin.web.service.BoRolesPermissionsService;
import com.ontag.mcash.admin.web.service.BoRolesService;
import com.ontag.mcash.admin.web.util.Constants;
import com.ontag.mcash.dal.domain.AppPermissions;
import com.ontag.mcash.dal.domain.BoRoles;
import com.ontag.mcash.dal.domain.BoRolesPermission;
import com.ontag.mcash.dal.domain.BoUserRole;
import com.ontag.mcash.dal.domain.CurrencyCode;

@Controller
@RequestMapping("/role")
public class BoRolesController {

	protected static org.slf4j.Logger logger = LoggerFactory
			.getLogger(BoRolesController.class);

	@Autowired
	private BoRolesService boRolesService;

	@Autowired
	private AppPermissionsService appPermissionsService;
	
	@Autowired
	private BoRolesPermissionsService boRolesPermissionsService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String getBoRolesListPage() {
		logger.info("show borole-list");
		return "bo-roles-list";
	}

	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String getAddPage(Model model) {

		BoRoles boRoles = new BoRoles();
		short id = 0;
		boRoles.setId(id);
		model.addAttribute("boroles", boRoles);
		model.addAttribute("screenMode", "add");
		return "bo-roles-edit";
	}

	@RequestMapping(value = "edit-{boRoleId}", method = RequestMethod.GET)
	public String getEditPage(Model model, @PathVariable int boRoleId) {

		try {
			logger.info("getEditPage ########### 4 : " + boRoleId);
			BoRoles boRoles = boRolesService.findById(boRoleId);
			
			List<BoRolesPermission> boRolesPermissions = boRolesPermissionsService.getRolesPermissionsByRoleId(boRoleId);
			List<Long> permissionIdList = new LinkedList<Long>();
			
			for (BoRolesPermission permission : boRolesPermissions) {
				permissionIdList.add(permission.getPermissionId().getId());
				System.out.println(permission.getPermissionId().getId());
			}
					
			model.addAttribute("permmissionIds",permissionIdList);
			model.addAttribute("boroles", boRoles);
			model.addAttribute("screenMode", "edit");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "bo-roles-edit";
	}

	@RequestMapping(value = "/list.json", method = RequestMethod.POST, headers = Constants.REQUEST_HEADER_ACCEPT_JSON)
	public @ResponseBody
	DataGridData getListData(
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "rows", defaultValue = "10") int rows) {
		try {

			logger.debug("/list.json--> {}-{}", page, rows);
			int start = (page - 1) * rows;
			start = start < 0 ? 0 : start;
			ListData[] listData = ListData.getArray(boRolesService.getList(
					start, rows));

			DataGridData<ListData> d = new DataGridData<>();
			d.setRows(listData);
			d.setTotal(boRolesService.countAll());

			logger.debug("returning start = {}", start);
			return d;

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}

		return null;
	}

	@RequestMapping(value = "/permissions.json", method = RequestMethod.POST, headers = Constants.REQUEST_HEADER_ACCEPT_JSON)
	public @ResponseBody
	DataGridData getPermissionsListData(
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "rows", defaultValue = "10") int rows) {
		try {

			logger.debug("/list.json--> {}-{}", page, rows);
			int start = (page - 1) * rows;
			start = start < 0 ? 0 : start;
			PermissionsListData[] listData = PermissionsListData
					.getArray(appPermissionsService.getAdminPermissionsList(start,
							rows));

			DataGridData<PermissionsListData> d = new DataGridData<>();
			d.setRows(listData);
			d.setTotal(appPermissionsService.countAll());

			logger.debug("returning start = {}", start);
			return d;

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}

		return null;
	}

	@RequestMapping(value = "save-{permissionIds}", method = RequestMethod.POST)
	public @ResponseBody
	PostResponse saveBoRole(@ModelAttribute("boroles") BoRoles boRole,
			BindingResult result, @PathVariable String permissionIds) {

		logger.info("saveBoRole ################## 1");
		if (!result.hasErrors()) {

			try {
				logger.info("saveBoRole ################## 2");
				if (boRole.getId() > 0) {
					logger.info("saveBoRole ################## 3");
				    boRolesService.edit(boRole);
					boRolesPermissionsService.editBoRolesPermissions(boRole,permissionIds);
				} else {
					logger.info("saveBoRole ################## 4 :" + boRole.getName() + " : " + permissionIds);
					 boRolesService.add(boRole);					
					
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				result.addError(new ObjectError("save", e.getMessage()));
			}
		}

		PostResponse response = new PostResponse(result.getAllErrors());
		return response;
	}

	static class ListData {
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

		public static ListData[] getArray(List<Object> dataList) {

			ListData[] result = new ListData[dataList.size()];
			int count = 0;
			BoRoles boRole;
			for (Object object : dataList) {
				boRole = (BoRoles) object;
				ListData row = new ListData();

				row.id = boRole.getId();
				row.name = boRole.getName();
				result[count++] = row;
			}
			return result;
		}
	}

	static class PermissionsListData {
		private long id;
		private String displayName;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}

		public static PermissionsListData[] getArray(
				List<AppPermissions> dataList) {

			PermissionsListData[] result = new PermissionsListData[dataList
					.size()];
			int count = 0;
			AppPermissions appPermissions;
			for (Object object : dataList) {
				appPermissions = (AppPermissions) object;
				PermissionsListData row = new PermissionsListData();

				row.id = appPermissions.getId();
				row.displayName = appPermissions.getDisplayName();
				result[count++] = row;
			}
			return result;
		}
	}
}
