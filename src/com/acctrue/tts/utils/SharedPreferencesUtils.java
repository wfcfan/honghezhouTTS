package com.acctrue.tts.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Pair;

import com.acctrue.tts.Constants;
import com.acctrue.tts.GlobalApplication;

public class SharedPreferencesUtils {
	
	private static final String FILE_COMMON = "profile";
	
	private static final String KEY_USER_ID = "KEY_USER_ID";
	private static final String KEY_SERVER_ADDRESS = "KEY_SERVER_ADDRESS";
	private static final String KEY_MAX_SERIAL ="KEY_MAX_SERIAL";
	
	private static final String KEY_LAST_USERNAME = "KEY_LAST_USERNAME";
	private static final String KEY_LAST_PASSWORD = "KEY_LAST_PASSWORD";
	
	public static void setLastUser(String userName,String password){
		Context context = GlobalApplication.mApp;
		Editor editor = context.getSharedPreferences(FILE_COMMON, Context.MODE_PRIVATE).edit();
		editor.putString(KEY_LAST_USERNAME, userName);
		editor.putString(KEY_LAST_PASSWORD, password);
		editor.commit();
	}
	
	public static Pair<String,String> getLastUser(){
		Context context = GlobalApplication.mApp;
		SharedPreferences preferences = context.getSharedPreferences(FILE_COMMON, Context.MODE_PRIVATE);
		String uv = preferences.getString(KEY_LAST_USERNAME, ""),
			   pv = preferences.getString(KEY_LAST_PASSWORD, "");
		return new Pair<String,String>(uv, pv);
	}

	public static void setUserId(int uid) {
		Context context = GlobalApplication.mApp;
		Editor editor = context.getSharedPreferences(FILE_COMMON, Context.MODE_PRIVATE).edit();
		editor.putInt(KEY_USER_ID, uid);
		editor.commit();
	}
	
	public static int getUserId() {
		Context context = GlobalApplication.mApp;
		SharedPreferences preferences = context.getSharedPreferences(FILE_COMMON, Context.MODE_PRIVATE);
		return preferences.getInt(KEY_USER_ID, 0);
	}
	
	public static void setServerAddress(String serverAddress){
		Context context = GlobalApplication.mApp;
		Editor editor = context.getSharedPreferences(FILE_COMMON, Context.MODE_PRIVATE).edit();
		//String fullPath = serverAddress + Constants.DEFAULT_URL_PATH;
		editor.putString(KEY_SERVER_ADDRESS, serverAddress);
		editor.commit();
	}
	
	public static String getServerAddress(boolean isFullPath){
		Context context = GlobalApplication.mApp;
		SharedPreferences preferences = context.getSharedPreferences(FILE_COMMON, Context.MODE_PRIVATE);
		String address = preferences.getString(KEY_SERVER_ADDRESS, "");
		if(isFullPath)
			address += Constants.DEFAULT_URL_PATH;
		return address;
	}
	
	public static String getServerAddress(){
		return getServerAddress(false);
	}
	
	public static void setSerialNumber(String num){
		Context context = GlobalApplication.mApp;
		Editor editor = context.getSharedPreferences(FILE_COMMON, Context.MODE_PRIVATE).edit();
		editor.putString(KEY_MAX_SERIAL,num);
		editor.commit();
	}
	
	public static String getSerialNumber(){
		Context context = GlobalApplication.mApp;
		SharedPreferences preferences = context.getSharedPreferences(FILE_COMMON, Context.MODE_PRIVATE);
		return preferences.getString(KEY_MAX_SERIAL, "");
	}
}
