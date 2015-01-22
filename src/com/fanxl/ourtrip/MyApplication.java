package com.fanxl.ourtrip;

import android.app.Application;
import cn.bmob.v3.Bmob;

import com.tencent.bugly.crashreport.CrashReport;
/**
 * Initialize ImageLoader
 * @Author Ryan
 * @Create 2013-7-3 下午9:44:10
 */
public class MyApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		// 初始化 Bmob SDK
		Bmob.initialize(this, "e80fd0fa15944c5b0d6ce7596ac5c6b0");
		
		CrashReport.initCrashReport(getApplicationContext() , "900001858", true);  //初始化SDK 
	}
}
