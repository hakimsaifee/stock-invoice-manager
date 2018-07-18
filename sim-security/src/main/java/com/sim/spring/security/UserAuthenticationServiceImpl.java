package com.sim.spring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sim.dto.UserDetailDTO;
import com.sim.service.UserDetailService;

@Service
public class UserAuthenticationServiceImpl implements UserDetailsService {

	@Autowired
	private UserDetailService userService;

	@Override
	public UserDetails loadUserByUsername(String username) {

		UserDetailDTO user = userService.getUserByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}

		return new UserPrincipal(user);
	}
}