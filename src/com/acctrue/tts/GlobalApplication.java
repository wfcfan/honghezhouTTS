package com.acctrue.tts;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.acctrue.tts.db.HongHeZhouDB;
import com.acctrue.tts.utils.AccountUtil;
import com.acctrue.tts.utils.SharedPreferencesUtils;
import com.acctrue.tts.utils.Util;

public class GlobalApplication extends Application {

	public static GlobalApplication mApp;
	public static String deviceId;
	public static String currentVersion;
	public static int currentCode;
	
	@Override
	public void onCreate() {
		super.onCreate();
		try {
			mApp = this;
			setPath(mApp);
			
			AccountUtil.loadAccount();
			
			if(SharedPreferencesUtils.getServerAddress() == ""){
				//String fullPath = Constants.DEFAULT_HOST + Constants.DEFAULT_URL_PATH;
				SharedPreferencesUtils.setServerAddress(Constants.DEFAULT_HOST);
			}
			
			Constants.URL_HOST = SharedPreferencesUtils.getServerAddress(true);
			
			
			new HongHeZhouDB(this);// 数据库初始化
			TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			deviceId = tm.getDeviceId();
			currentVersion = getPackageManager().getPackageInfo(
					getPackageName(), 0).versionName;
			currentCode = getPackageManager().getPackageInfo(
					getPackageName(), 0).versionCode;

			try {
				// 这个的作用就是将 AsyncTask初始化到主线程
				Class.forName("android.os.AsyncTask");
			} catch (ClassNotFoundException e) {
				Util.log(e);
				e.printStackTrace();
			}

		} catch (Exception e) {
			Util.log(e);
			e.printStackTrace();
		}
	}

	public void setPath(Context c) {
		File cacheDir = c.getCacheDir();
		//File sdcard = Environment.getExternalStorageDirectory();
		
		Constants.PATH = cacheDir.getAbsolutePath();		
		Constants.PATH_ACCOUNT = Constants.PATH + "/account/";
		Constants.PATH_LOG = Constants.PATH + "/ttslog/";
		
		File d1 = new File(Constants.PATH_ACCOUNT);
		if(!d1.exists()){
			d1.mkdir();
		}
		
		File d2 = new File(Constants.PATH_LOG);
		if (!d2.exists()) {
			d2.mkdirs();
		}
	}
}