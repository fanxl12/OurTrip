package com.fanxl.ourtrip.utils;

import android.content.Context;
import android.widget.Toast;

public class UtilHelper {
	
	public static void showMsg(Context mContext, String msg){
		Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
	}

}
