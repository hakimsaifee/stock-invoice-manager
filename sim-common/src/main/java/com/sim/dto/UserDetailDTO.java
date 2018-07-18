package com.sim.dto;

import java.util.List;

public class UserDetailDTO {

	private String username;

	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private List<RoleDTO> userRoles;

	public List<RoleDTO> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<RoleDTO> userRoles) {
		this.userRoles = userRoles;
	}

}
