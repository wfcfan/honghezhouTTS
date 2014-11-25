package com.acctrue.tts.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;

import com.acctrue.tts.Constants;
import com.acctrue.tts.GlobalApplication;
import com.acctrue.tts.R;
import com.acctrue.tts.dto.VersionInfoRequest;
import com.acctrue.tts.dto.VersionInfoResponse;
import com.acctrue.tts.rpc.OnCompleteListener;
import com.acctrue.tts.rpc.RpcAsyncTask;
import com.acctrue.tts.tasks.TaskUtils;
import com.acctrue.tts.tasks.UpdateManager;
import com.acctrue.tts.utils.SharedPreferencesUtils;
import com.acctrue.tts.utils.Toaster;

/**
 * 闪屏
 * 
 * @author wangfeng
 * 
 */
@SuppressWarnings("deprecation")
public class SplashActivity extends ActivityGroup {

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		if (SharedPreferencesUtils.getServerAddress() == "") {
			SharedPreferencesUtils.setServerAddress(Constants.DEFAULT_HOST);
		}

		Constants.URL_HOST = SharedPreferencesUtils.getServerAddress(true);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// if (NetworkUtil.getNetworkState(SplashActivity.this) ==
				// NetworkUtil.NetworkState.NOTHING) {
				// Toaster.show("手机不能连接到网络，请检查网络配置");
				// finish();
				// return;
				// }
				VersionInfoRequest version = new VersionInfoRequest(
						GlobalApplication.deviceId,
						GlobalApplication.currentNum);
				RpcAsyncTask task = new RpcAsyncTask(SplashActivity.this,
						version, new OnCompleteListener() {
							@Override
							public void onComplete(String content) {
								VersionInfoResponse req;
								try {
									req = VersionInfoResponse
											.fromJson(new JSONObject(content));
								} catch (JSONException e) {
									req = null;
								}

								if (req != null && !req.isError()
										&& req.isUpdate()) {
									updateVersion(req.getUrl(),
											req.getUpdateContent());
								}

								initLoginViews();
							}
						}, false);

				TaskUtils.execute(task, TaskUtils.POST,
						Constants.URL_UPDATE_VERSION);

			}

		}, 2000);
	}

	private void initLoginViews() {
		Intent intent = new Intent(this, LoginActivity.class);
		View loginView = this.getLocalActivityManager()
				.startActivity(LoginActivity.class.getName(), intent)
				.getDecorView();

		this.setContentView(loginView);
	}

	boolean canClose = false;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (canClose) {
				finish();
			} else {
				Toaster.show(R.string.msg_exit_message);
				canClose = true;
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						canClose = false;
					}
				}, 1500);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void updateVersion(String url, String msg) {
		UpdateManager upManager = new UpdateManager(this, url);
		upManager.setUpdateMsg(msg);
		upManager.checkUpdateInfo();
	}
}
