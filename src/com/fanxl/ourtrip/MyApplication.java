package com.fanxl.ourtrip;

import android.app.Application;
import cn.bmob.v3.Bmob;

import com.baidu.mapapi.SDKInitializer;
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
		
		//初始化腾讯Budly的SDK 
		CrashReport.initCrashReport(getApplicationContext() , "900001858", true); 
		
		//在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现  
        SDKInitializer.initialize(getApplicationContext());
	}
}
