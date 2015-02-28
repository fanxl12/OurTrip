package com.fanxl.ourtrip.bean;

import cn.bmob.v3.BmobObject;

public class Friend extends BmobObject{

	private static final long serialVersionUID = 1L;
	private String friendObjectId;
	private String friendName;
	private String userId;
	private String sex;
	private double mLatitude;
	private double mLongtitude;
	private String address;
	
	public String getFriendObjectId() {
		return friendObjectId;
	}
	public void setFriendObjectId(String friendObjectId) {
		this.friendObjectId = friendObjectId;
	}
	public String getFriendName() {
		return friendName;
	}
	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public double getmLatitude() {
		return mLatitude;
	}
	public void setmLatitude(double mLatitude) {
		this.mLatitude = mLatitude;
	}
	public double getmLongtitude() {
		return mLongtitude;
	}
	public void setmLongtitude(double mLongtitude) {
		this.mLongtitude = mLongtitude;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
}
