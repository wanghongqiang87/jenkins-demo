package com.neo.pojo;

import java.io.Serializable;

public class UserLogin implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private String userId;
	private String roleId;

	public String getRoleId() {
		return roleId;
	}

	public UserLogin(String username, String password, String userId, String roleId) {
		this.username = username;
		this.password = password;
		this.userId = userId;
	}

	public void setRoleID(String roleId) {
		this.roleId = roleId;
	}

	public UserLogin() {
	}

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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
