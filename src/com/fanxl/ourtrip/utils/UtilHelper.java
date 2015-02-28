package com.fanxl.ourtrip.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import m.framework.utils.HEX;

import android.content.Context;
import android.widget.Toast;

public class UtilHelper {
	
	public static void showMsg(Context mContext, String msg){
		Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
	}
	
	public static String jdkMD5(String src){
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] md5Bytes = md.digest(src.getBytes());
			return HEX.encodeHexString(md5Bytes);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	

}
