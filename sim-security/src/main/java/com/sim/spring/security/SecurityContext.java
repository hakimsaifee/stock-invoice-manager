package com.sim.spring.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContext {

	public static boolean isAdmin() {
		boolean isAdmin = false;
		UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Collection<? extends GrantedAuthority> authorities = userPrincipal.getAuthorities();
		if (authorities != null) {
			for (GrantedAuthority grantedAuthority : authorities) {
				if ("ADMIN".equals(grantedAuthority.getAuthority())) {
					isAdmin = true;
					break;
				}
			}
		}

		return isAdmin;
	}
}
