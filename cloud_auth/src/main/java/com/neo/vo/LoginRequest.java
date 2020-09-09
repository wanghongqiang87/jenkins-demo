package com.neo.vo;

public class LoginRequest {
	private String username;
	private String password;
	private String other;

	public LoginRequest() {
	}

	public LoginRequest(String username, String password, String other) {
		super();
		this.username = username;
		this.password = password;
		this.other = other;
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

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

}
