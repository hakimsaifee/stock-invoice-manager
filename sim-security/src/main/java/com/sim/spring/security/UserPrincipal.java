package com.sim.spring.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.sim.dto.RoleDTO;
import com.sim.dto.UserDetailDTO;

public class UserPrincipal implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserDetailDTO user;

	public UserPrincipal(UserDetailDTO user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> simpleGrantedAuthorities = null;
		if (user.getUserRoles() != null && !user.getUserRoles().isEmpty()) {
			simpleGrantedAuthorities = new ArrayList<>();
			for (RoleDTO roleDTO : user.getUserRoles()) {
				simpleGrantedAuthorities.add(new SimpleGrantedAuthority(roleDTO.getRole()));
			}
		}
		return simpleGrantedAuthorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}