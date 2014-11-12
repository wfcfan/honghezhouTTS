package com.acctrue.tts.utils;

import android.widget.Toast;

import com.acctrue.tts.GlobalApplication;

public class Toaster {
	static int defDuration = Toast.LENGTH_SHORT;

	public static void show(int resId) {
		show(GlobalApplication.mApp.getString(resId), defDuration);
	}

	public static void show(CharSequence text) {
		show(text, defDuration);
	}

	public static void show(CharSequence text, int duration) {
		try {
			Toast toast = Toast.makeText(GlobalApplication.mApp, text, duration);
			toast.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void show(int resId, int duration) {
		try {
			Toast toast = Toast.makeText(GlobalApplication.mApp, resId, duration);
			toast.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
}
