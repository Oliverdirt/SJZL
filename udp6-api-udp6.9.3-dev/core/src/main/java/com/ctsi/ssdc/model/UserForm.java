package com.ctsi.ssdc.model;

public class UserForm {

	private String username;

	private String password;
	
	private String validCode;

	private int rememberme;
	
	private String data;

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
	

	public String getValidCode() {
		return validCode;
	}

	public void setValidCode(String validCode) {
		this.validCode = validCode;
	}

	public int getRememberme() {
		return rememberme;
	}

	public void setRememberme(int rememberme) {
		this.rememberme = rememberme;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	
}
