package com.acctrue.tts.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.acctrue.tts.Constants;
import com.acctrue.tts.activity.CaptureActivity;

public class IntentUtil {
	
	public static void showCapture(Activity a){
		Intent intent = new Intent();
		intent.setClass(a, CaptureActivity.class);
		a.startActivity(intent);
	}
	public static void showCapture(Activity a, Fragment f) {
		Intent intent = new Intent();
		intent.setClass(a, CaptureActivity.class);
		f.startActivityForResult(intent, Constants.REQCODE_SCANNIN_GREQUEST_CODE);
	}
}
