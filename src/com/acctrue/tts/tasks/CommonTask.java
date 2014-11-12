package com.acctrue.tts.tasks;

import android.os.AsyncTask;

public abstract class CommonTask extends AsyncTask<Object, Object, Boolean> {

	private OnTaskExecuteListener mTaskListener;
	protected Object object;
	protected Throwable excption;

	public CommonTask(OnTaskExecuteListener listener) {
		this.mTaskListener = listener;
	}

	@Override
	protected void onPreExecute() {
		if (mTaskListener != null) {
			mTaskListener.onPreExecute();
		}
	}

	@Override
	protected Boolean doInBackground(Object... params) {

		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (result) {
			if (mTaskListener != null) {
				mTaskListener.onSucceed(object);
			}
		} else {
			if (mTaskListener != null && object != null) {
				mTaskListener.onFailed(excption, object);
			} else if (mTaskListener != null) {
				mTaskListener.onFailed(excption);
			}
		}
	}

	@Override
	protected void onCancelled() {
		try {
			this.cancel(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (mTaskListener != null) {
			mTaskListener.onCancelled();
		}
	}

	public OnTaskExecuteListener getTaskListener() {
		return mTaskListener;
	}

	public void setTaskListener(OnTaskExecuteListener mTaskListener) {
		this.mTaskListener = mTaskListener;
	}

}
