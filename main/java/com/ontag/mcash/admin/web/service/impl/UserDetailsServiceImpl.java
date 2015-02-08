package com.ontag.mcash.admin.web.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ontag.mcash.dal.dao.BoUsersDao;
import com.ontag.mcash.dal.domain.BoUsers;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {

    protected static Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private BoUsersDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException, DataAccessException {

        UserDetails userDetails = null;

        try {

        	BoUsers user = userDao.findByUsername(username);
            logger.error("User exisit #################### : " + user);
            if (user == null) {
                throw new BadCredentialsException(username);
            }

            
            userDetails = new com.ontag.mcash.admin.web.security.SecurityManager.OnTagUserDetails(
                    user.getUserName(), user.getPasswordEnc().toLowerCase(),
                    true, true, true, true,
                    getAuthorities(user),
                    user);

        } catch (Exception e) {
            logger.error("Error occurred while retrieving user", e);
            throw new BadCredentialsException(username);

        }
        
        logger.error(" userDetails #################### : " + userDetails);
        
        return userDetails;
    }

    public Collection<GrantedAuthority> getAuthorities(BoUsers user) {
    	
    	
        List<GrantedAuthority> authList = new ArrayList<>(10);
        authList.add(new SimpleGrantedAuthority("ROLE_USER"));
        
//        if(user.getRole() == 1)
//        	authList.add(new SimpleGrantedAuthority("ROLE_USER"));
//        else if(user.getRole() == 2)
//        	authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//        else if(user.getRole() == 3)
//        	authList.add(new SimpleGrantedAuthority("ROLE_FINANCE"));
        
        return authList;
    }

   


}
