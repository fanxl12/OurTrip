package com.fanxl.ourtrip.bean;

import cn.bmob.v3.BmobObject;

/**
 * 注册信息保存类
 * @author fanxl
 *
 */
public class Person extends BmobObject{

	private static final long serialVersionUID = 1L;

	private String userName;
	private String passWord;
	private String phoneNumber;
	private String userId;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
}
