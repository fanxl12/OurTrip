package com.fanxl.ourtrip.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fanxl.ourtrip.R;

public class Info implements Serializable
{
	private static final long serialVersionUID = -1010711775392052966L;
	private double latitude;
	private double longitude;
	private int imgId;
	private String name;
	private String distance;
	private int zan;

	public static List<Info> infos = new ArrayList<Info>();

	static
	{
		infos.add(new Info(31.023497, 121.415359, R.drawable.a01, "英伦贵族小旅馆",
				"距离209米", 1456));
		infos.add(new Info(31.020588, 121.412862, R.drawable.a02, "沙井国际洗浴会所",
				"距离897米", 456));
		infos.add(new Info(31.019706, 121.416689, R.drawable.a03, "五环服装城",
				"距离249米", 1456));
		infos.add(new Info(31.021795, 121.417174, R.drawable.a04, "老米家泡馍小炒",
				"距离679米", 1456));
	}

	public Info(double latitude, double longitude, int imgId, String name,
			String distance, int zan)
	{
		this.latitude = latitude;
		this.longitude = longitude;
		this.imgId = imgId;
		this.name = name;
		this.distance = distance;
		this.zan = zan;
	}

	public double getLatitude()
	{
		return latitude;
	}

	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}

	public double getLongitude()
	{
		return longitude;
	}

	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}

	public int getImgId()
	{
		return imgId;
	}

	public void setImgId(int imgId)
	{
		this.imgId = imgId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDistance()
	{
		return distance;
	}

	public void setDistance(String distance)
	{
		this.distance = distance;
	}

	public int getZan()
	{
		return zan;
	}

	public void setZan(int zan)
	{
		this.zan = zan;
	}

}
