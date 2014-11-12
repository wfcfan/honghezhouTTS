package com.acctrue.tts.utils;

import com.acctrue.tts.R;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.net.Uri;
import android.text.TextUtils;

public class NetworkUtil {
	public class APNWrapper {
		public String getApn() {
			return apn;
		}

		public String getName() {
			return name;
		}

		public int getPort() {
			return port;
		}

		public String getProxy() {
			return proxy;
		}

		public String apn;
		public String name;
		public int port;
		public String proxy;

		APNWrapper() {
		}
	}

	public enum NetworkState {
		NOTHING("NOTHING", 0), MOBILE("MOBILE", 1), WIFI("WIFI", 2);
		private NetworkState(String s, int i) {
			this.name = s;
			this.code = i;
		}

		String name;
		Integer code;
	}

	private NetworkUtil() {
		;
	}

	@SuppressWarnings("deprecation")
	public static APNWrapper getAPN(Context context) {

		ContentResolver contentresolver = context.getContentResolver();
		Uri uri = PREFERRED_APN_URI;
		String as[] = new String[4];
		as[0] = "name";
		as[1] = "apn";
		as[2] = "proxy";
		as[3] = "port";
		String as1[] = null;
		String s = null;

		if (wrapper == null)
			wrapper = (new NetworkUtil()).new APNWrapper();

		wrapper.name = "N/A";
		wrapper.apn = "N/A";
		Cursor cursor = contentresolver.query(uri, as, null, as1, s);
		while (cursor != null && cursor.moveToNext()) {
			if (cursor.getString(0) == null)
				wrapper.name = "";
			else
				wrapper.name = cursor.getString(0).trim();
			if (cursor.getString(1) == null)
				wrapper.apn = "";
			else
				wrapper.apn = cursor.getString(1).trim();

		}
		cursor.close();
		wrapper.proxy = (TextUtils.isEmpty(Proxy.getDefaultHost()) ? Proxy.getDefaultHost() : "");
		wrapper.port = (Proxy.getDefaultPort() > 0 ? Proxy.getDefaultPort() : 80);

		return wrapper;
	}

	public static NetworkState getNetworkState(Context context) {

		NetworkInfo networkinfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
		NetworkState networkstate;
		if (networkinfo == null || !networkinfo.isConnected())
			networkstate = NetworkState.NOTHING;
		else if (networkinfo.getType() == 0)
			networkstate = NetworkState.MOBILE;
		else
			networkstate = NetworkState.WIFI;
		return networkstate;
	}
	
	public static String getNetworkStateString(Context context){
		NetworkState ns =  getNetworkState(context);
		String strStatus = context.getString(R.string.networkstatus_offline);
		if(ns == NetworkState.MOBILE || ns == NetworkState.WIFI){
			strStatus = context.getString(R.string.networkstatus_online);
		}
		return strStatus;		
	}

	static final Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
	static APNWrapper wrapper;
}
