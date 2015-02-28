package com.fanxl.ourtrip.bean;

import cn.bmob.v3.BmobObject;

/**
 * 经纬度位置保存类
 * @author fanxl
 *
 */
public class MyLocation extends BmobObject{

	private static final long serialVersionUID = 1L;
	
	//当前位置的经纬度
	private double mLatitude;
	private double mLongtitude;
	private String userId;
	
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	

}
