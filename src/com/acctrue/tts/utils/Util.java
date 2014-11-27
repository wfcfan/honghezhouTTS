package com.acctrue.tts.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.acctrue.tts.Constants;
import com.acctrue.tts.GlobalApplication;
import com.acctrue.tts.R;
import com.acctrue.tts.rpc.RPCBase;

public class Util {

	public static Fragment getActiveFragment(FragmentActivity f,
			ViewPager container, int position) {
		try {
			String name = makeFragmentName(container.getId(), position);
			return f.getSupportFragmentManager().findFragmentByTag(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getNullText(String s){
		if(s == null || s.equals("null")){
			return "暂无";
		}
		return s;
	}
	
	public static String subStr(String str, int len) {
		if (TextUtils.isEmpty(str))
			return str;
		if (str.length() <= len) {
			return str;
		} else {
			return str.substring(0, len);
		}
	}
	
	private static String makeFragmentName(int viewId, int index) {
		return "android:switcher:" + viewId + ":" + index;
	}

	public static void startLoadingAnimation(View v) {
		startLoadingAnimation(v, true);
	}

	public static void startLoadingAnimation(View v, boolean clockwise) {
		if (v == null) {
			return;
		}
		final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();
		final int ROTATION_ANIMATION_DURATION = 1200;
		RotateAnimation rotate = null;
		if (clockwise) {
			rotate = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF,
					0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		} else {
			rotate = new RotateAnimation(720, 0, Animation.RELATIVE_TO_SELF,
					0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		}
		rotate.setInterpolator(ANIMATION_INTERPOLATOR);
		rotate.setDuration(ROTATION_ANIMATION_DURATION);
		rotate.setRepeatMode(Animation.RESTART);
		rotate.setRepeatCount(Animation.INFINITE);

		v.startAnimation(rotate);
	}

	public static void stopLoadingAnimation(View v) {
		if (v == null) {
			return;
		}
		v.clearAnimation();
	}

	public static DisplayMetrics getDisplayMetrics() {
		DisplayMetrics dm = new DisplayMetrics();
		try {
			dm = GlobalApplication.mApp.getResources().getDisplayMetrics();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dm;
	}

	public static int dip2px(float dipValue) {
		final float scale = getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(float pxValue) {
		final float scale = getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static void log(String TAG, Object o) {
		if (Constants.DEBUGGABLE) {
			String methodName = Thread.currentThread().getStackTrace()[3]
					.getMethodName();
			int lineNumber = Thread.currentThread().getStackTrace()[3]
					.getLineNumber();
			Log.d(TAG, methodName + "():" + lineNumber + "> " + o);
		}
	}

	public static void log(Object o) {
		log(Constants.APP_NAME, o);
	}

	public static void logRequst(HttpUriRequest httpurirequest) {

		if (httpurirequest != null) {
			String requestLine = httpurirequest.getMethod() + " "
					+ httpurirequest.getRequestLine().getUri();
			if (httpurirequest instanceof HttpPost) {
				HttpEntity entity = ((HttpPost) httpurirequest).getEntity();
				try {
					if (requestLine.endsWith("?")) {
						requestLine = String.format("%s%s", requestLine,
								EntityUtils.toString(entity));
					} else {
						requestLine = String.format("%s?%s", requestLine,
								EntityUtils.toString(entity));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			Util.log(RPCBase.TAG, requestLine);
		}
	}

	public static void logResponse(String result) {
		Util.log(RPCBase.TAG, String.format("RESPONSE %s", result));
	}

	public static String replaceCodeStrings(String str) {
		String[] strArr = GlobalApplication.mApp.getResources().getStringArray(
				R.array.repl_strs);
		for (String s : strArr) {
			str = str.replaceAll(s, "");
		}
		str = str.trim();
		return str;
	}
}
