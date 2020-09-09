package com.neo.vo;

import java.io.Serializable;
import java.util.List;

public class LoginResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userid;
	private String token;
	private String refreshToken;
	private String userName;
	private List<?> menus;

	public LoginResponse() {
	}

	public LoginResponse(String userid, String token, String refreshToken, String userName, List<?> menus) {
		this.userid = userid;
		this.token = token;
		this.refreshToken = refreshToken;
		this.userName = userName;
		this.menus = menus;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<?> getMenus() {
		return menus;
	}

	public void setMenus(List<?> menus) {
		this.menus = menus;
	}

}
