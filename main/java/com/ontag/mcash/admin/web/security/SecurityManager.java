
package com.ontag.mcash.admin.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.ontag.mcash.dal.domain.BoUsers;


import java.util.Collection;

public class SecurityManager {

    protected static Logger logger = LoggerFactory.getLogger(SecurityManager.class);

    public static interface Authorities {

        String ROLE_PREFIX = "ROLE_";
        String ROLE_USER  = ROLE_PREFIX + "USER";
        String ROLE_ADMIN = ROLE_PREFIX + "ADMIN";
        String ROLE_FINANCE = ROLE_PREFIX + "FINANCE";
    }

    public enum BaseRoles {
        SUPER_USER(1),
        BUSINESS_USER(7);

        private int id;

        BaseRoles(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    public static UserDetails getUserDetails() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return (UserDetails) principal;
        }

        return null;
    }

    public static BoUsers getCurrentUser() {

        UserDetails ud = getUserDetails();

        if (ud instanceof OnTagUserDetails) {
            return ((OnTagUserDetails) ud).getUser();
        }

        return null;
    }

    public static String getDefaultAuthority(String... auths) {
        if (getUserDetails() != null) {
            for (String auth : auths) {
                logger.debug("check {}", auth);
                for (GrantedAuthority grantedAuthority : getUserDetails().getAuthorities()) {
                    if (auth.equals(grantedAuthority.getAuthority())) {
                        return grantedAuthority.getAuthority();
                    }
                }
            }
        }
        return null;
    }

    public static class OnTagUserDetails extends org.springframework.security.core.userdetails.User {

        private BoUsers user;

        public OnTagUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, BoUsers user) {
            super(username, password, authorities);
            this.user = user;
        }

        public OnTagUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, BoUsers user) {
            super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
            this.user = user;
        }

        public BoUsers getUser() {
            return user;
        }

        public void setUser(BoUsers user) {
            this.user = user;
        }
    }

    public static void encodePassword(String pwd) {
        Md5PasswordEncoder s = new Md5PasswordEncoder();
        logger.debug("{} - {}", pwd, s.encodePassword(pwd, null));
    }

}
