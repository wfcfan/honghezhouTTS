package com.acctrue.tts.activity;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.acctrue.tts.Constants;
import com.acctrue.tts.GlobalApplication;
import com.acctrue.tts.R;
import com.acctrue.tts.utils.SharedPreferencesUtils;
import com.acctrue.tts.utils.Toaster;

/**
 * 闪屏
 * @author wangfeng
 *
 */
@SuppressWarnings("deprecation")
public class SplashActivity extends ActivityGroup {
	
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		if(SharedPreferencesUtils.getServerAddress() == ""){
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
				initLoginViews();
			}

		}, 2000);
		
		TextView txtVer = (TextView)findViewById(R.id.txtVer);
		txtVer.setText(GlobalApplication.currentVersion);
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
}
