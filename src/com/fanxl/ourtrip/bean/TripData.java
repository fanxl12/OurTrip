package com.fanxl.ourtrip.bean;

public class TripData {
	
	private TripData(){};
	//用户id，作为每个用户唯一的标示
	private String userId;
	//用户位置的ObjectId,用户对位置进行更新时使用
	private String locationObjectId;
	
//	private double mLatitude;
//	private double mLongtitude;
//	private String address;
//	private String userName;
//	private String objectId;
	
	private static class TripHolder{
		//静态初始化器，由JVM来保证线程安全
		
		private static TripData tripData = new TripData();
	}
	
	public static TripData getInstance(){
		return TripHolder.tripData;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLocationObjectId() {
		return locationObjectId;
	}

	public void setLocationObjectId(String locationObjectId) {
		this.locationObjectId = locationObjectId;
	}

//	public double getmLatitude() {
//		return mLatitude;
//	}
//
//	public void setmLatitude(double mLatitude) {
//		this.mLatitude = mLatitude;
//	}
//
//	public double getmLongtitude() {
//		return mLongtitude;
//	}
//
//	public void setmLongtitude(double mLongtitude) {
//		this.mLongtitude = mLongtitude;
//	}
//
//	public String getAddress() {
//		return address;
//	}
//
//	public void setAddress(String address) {
//		this.address = address;
//	}
//
//	public String getUserName() {
//		return userName;
//	}
//
//	public void setUserName(String userName) {
//		this.userName = userName;
//	}
//
//	public String getObjectId() {
//		return objectId;
//	}
//
//	public void setObjectId(String objectId) {
//		this.objectId = objectId;
//	}


}
