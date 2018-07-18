package com.sim.spring.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class UserAuthentication {

	private final static Logger LOGGER = LoggerFactory.getLogger(UserAuthentication.class);

	
	@Autowired
	private AuthenticationManager authManager;

	public UserPrincipal authenticate(String username, String password) {
		try {
			LOGGER.info("Authenticating user : {} ", username);
			Authentication request = new UsernamePasswordAuthenticationToken(username, password);
			Authentication result = authManager.authenticate(request);
			SecurityContextHolder.getContext().setAuthentication(result);
			UserPrincipal userPrincipal = (UserPrincipal) result.getPrincipal();
			LOGGER.info("User authenticated successfully.");
			return userPrincipal;
		} catch (AuthenticationException e) {
			LOGGER.error("User Authentication Failed.");
			throw e;
		}
	}
}
