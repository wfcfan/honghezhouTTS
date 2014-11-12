package com.acctrue.tts.tasks;

import com.acctrue.tts.utils.Util;

public abstract class OnTaskExecuteListener {
	public String TAG = "OnTaskExecuteListener";

	public void onPreExecute() {
		Util.log(TAG, "onPreExecute");
	}

	public void onCancelled() {
		Util.log(TAG, "onCancelled");

	}

	public void onSucceed(Object result) {
		Util.log(TAG, "onSucceed");

	}

	public void onFailed(Throwable thr) {
		Util.log(TAG, "onFailed");

	}

	public void onFailed(Throwable thr, Object result) {
		Util.log(TAG, "onFailed");
	}
}
