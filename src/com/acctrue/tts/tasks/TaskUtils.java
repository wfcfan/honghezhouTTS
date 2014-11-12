package com.acctrue.tts.tasks;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;

public class TaskUtils {
	public static final String POST;
	public static final String GET;
	
	static{
		POST = HttpPost.METHOD_NAME;
		GET  = HttpGet.METHOD_NAME;
	}

	@SuppressLint("NewApi")
	public static <P, T extends AsyncTask<P, ?, ?>> void execute(T task, P... params) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
		} else {
			task.execute(params);
		}
	}

	@SuppressLint("NewApi")
	public static <P, T extends CommonTask> void execute1(T task, P... params) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
		} else {
			task.execute(params);
		}
	}
}