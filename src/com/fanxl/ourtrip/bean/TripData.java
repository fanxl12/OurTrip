package com.fanxl.ourtrip.bean;

public class TripData {
	
	private TripData(){};
	//用户id，作为每个用户唯一的标示
	private String userId;
	//用户位置的ObjectId,用户对位置进行更新时使用
	private String locationObjectId;
	
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


}
