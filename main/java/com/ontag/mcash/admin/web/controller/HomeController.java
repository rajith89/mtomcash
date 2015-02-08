package com.ontag.mcash.admin.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ontag.mcash.admin.web.service.AppPermissionsService;
import com.ontag.mcash.admin.web.service.BoRolesPermissionsService;
import com.ontag.mcash.admin.web.service.BoUserRoleService;
import com.ontag.mcash.admin.web.service.BoUserService;
import com.ontag.mcash.dal.domain.AppPermissions;
import com.ontag.mcash.dal.domain.BoUserRole;
import com.ontag.mcash.dal.domain.BoUsers;

@Controller
@RequestMapping("/home")
public class HomeController {

	protected static org.slf4j.Logger logger = LoggerFactory
			.getLogger(HomeController.class);

	@Autowired
	BoUserService boUserService;

	@Autowired
	BoUserRoleService boUserRoleService;

	@Autowired
	BoRolesPermissionsService boRolesPermissionsService;

	@Autowired
	AppPermissionsService appPermissionsService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String getCommonPage(HttpServletRequest request, Model model) {
		try {

			Authentication auth = SecurityContextHolder.getContext()
					.getAuthentication();
			String userName = auth.getName();

			BoUsers boUser = boUserService.getUserByUserName(userName);

			List<BoUserRole> userRolesList = boUserRoleService
					.getBoUserRolesById(boUser.getId());

			// System.out.println(userRolesList.size());

			Short[] ids = new Short[userRolesList.size()];
			int i = 0;
			for (BoUserRole userRole : userRolesList) {
				ids[i] = userRole.getBoRoleId().getId();
				// System.out.println(ids[i]);
				i++;
			}

			List<Object[]> PermissionsIdList = boRolesPermissionsService
					.getPermissionsByRoles(ids);

			// System.out.println("############### rolePermissionsList.get(0)" +
			// PermissionsIdList.get(0));
			// System.out.println(PermissionsIdList.size());
			
			List<AppPermissions> permissionsList = new ArrayList<AppPermissions>();

			for (Object[] list : PermissionsIdList) {

				AppPermissions permission = appPermissionsService.findById((long)list[0]);
				permissionsList.add(permission);

				// System.out.println(id);
			}

			model.addAttribute("permissionsList", permissionsList);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return "home";

	}
}
