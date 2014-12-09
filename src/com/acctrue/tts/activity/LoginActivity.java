package com.acctrue.tts.activity;

import java.lang.reflect.Field;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.acctrue.tts.Constants;
import com.acctrue.tts.GlobalApplication;
import com.acctrue.tts.R;
import com.acctrue.tts.db.UserInfoDB;
import com.acctrue.tts.dto.LoginModel;
import com.acctrue.tts.dto.LoginResponse;
import com.acctrue.tts.dto.LoginResponse.UserInfo;
import com.acctrue.tts.rpc.OnCompleteListener;
import com.acctrue.tts.rpc.RpcAsyncTask;
import com.acctrue.tts.tasks.TaskUtils;
import com.acctrue.tts.utils.AccountUtil;
import com.acctrue.tts.utils.NetworkUtil;
import com.acctrue.tts.utils.NetworkUtil.NetworkState;
import com.acctrue.tts.utils.SharedPreferencesUtils;
import com.acctrue.tts.utils.Toaster;

/**
 * 用户登录
 * @author wangfeng
 *
 */
@SuppressWarnings("deprecation")
public class LoginActivity extends ActivityGroup implements OnClickListener {
	private final String TAG = LoginActivity.class.getSimpleName();
	EditText pwdView;
	Switch sw;
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		Button btnLogin = (Button) this.findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);

		Button btnSettings = (Button) this.findViewById(R.id.butn_settings);
		btnSettings.setOnClickListener(this);
		
		sw = (Switch)this.findViewById(R.id.loginType);
		if(NetworkUtil.getNetworkState(this) == NetworkState.NOTHING){
			sw.setChecked(true);//无网络的时候，自动匹配离线模式
		}
		
		pwdView = (EditText) this.findViewById(R.id.txtuserpwd);
		pwdView.setOnEditorActionListener(new TextView.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				/* 判断是否是“GO”键 */
				if (actionId == EditorInfo.IME_ACTION_GO) {
					/* 隐藏软键盘 */
					InputMethodManager imm = (InputMethodManager) v
							.getContext().getSystemService(
									Context.INPUT_METHOD_SERVICE);
					if (imm.isActive()) {
						imm.hideSoftInputFromWindow(
								v.getApplicationWindowToken(), 0);
					}
					login();
				}
				return false;
			}

		});
		
		TextView txtVer = (TextView)findViewById(R.id.txtVer);
		String info  = "30天试用版      " + GlobalApplication.currentVersion;
		txtVer.setText(info);
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

	final void login() {
		EditText userNameView = (EditText) this.findViewById(R.id.username);
		String userNameValue = userNameView.getText().toString();
		if (userNameValue.equals("")) {
			Toaster.show(R.string.msg_loginName_empty);
			return;
		}

		String userPwdValue = pwdView.getText().toString();
		if (userPwdValue.equals("")) {
			Toaster.show(R.string.msg_loginPwd_emtpy);
			return;
		}
		
		final UserInfoDB udb = new UserInfoDB(this);
		if(sw.isChecked()){//离线模式
			UserInfo ui = udb.getUserInfo(userNameValue);
			if(ui == null){
				Toaster.show("该用户不存在,请先在联网状态下登录一次!");
				return;
			}
			
			if(!ui.getPassword().equals(userPwdValue)){
				Toaster.show("用户密码输入有误!");
				pwdView.setText("");
				return;
			}
			
			LoginResponse localReq = LoginResponse.fromLocal(ui);
			AccountUtil.saveAccount(localReq);//保存用户信息
			Toaster.show(R.string.login_succeed);
			Intent intent = new Intent(LoginActivity.this,
					HomeActivity.class);
			startActivity(intent);
			finish();
			return;
		}
		
		final LoginModel lm = new LoginModel();
		lm.setUserName(userNameValue);
		lm.setPassword(userPwdValue);
		lm.setSerialNo(GlobalApplication.deviceId);
		lm.setVersion(GlobalApplication.currentVersion);
		Log.d(TAG, lm.toString());
		
		
		
		RpcAsyncTask task = new RpcAsyncTask(this,lm,new OnCompleteListener() {
			@Override
			public void onComplete(String content) {
				LoginResponse req;
				try {
					req = LoginResponse.fromJson(new JSONObject(content));
				} catch (JSONException e) {
					req = null;
					Toaster.show(e.getMessage());
					return;
				}
				
				if(req != null){
					if(req.isError()){
						Toaster.show(req.getMessage());
						return;
					}
					
					AccountUtil.saveAccount(req);//保存用户信息
					
					//将用户信息存在本地数据库中，用于离线的验证
					
					UserInfo ui = req.getUserInfo();
					ui.setPassword(lm.getPassword());
					udb.addUserInfo(ui);
					
					Toaster.show(R.string.login_succeed);
					Intent intent = new Intent(LoginActivity.this,
							HomeActivity.class);
					startActivity(intent);
					finish();
				}else{
					Toaster.show(R.string.login_failed);
				}
			}
		});
		
		TaskUtils.execute(task, TaskUtils.POST,Constants.URL_USER_LOGIN);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btnLogin:
			this.login();
			break;
		case R.id.butn_settings:

			LayoutInflater layoutInflater = LayoutInflater.from(this);

			final View networkViewSettingsView = layoutInflater.inflate(
					R.layout.networksettings_view, null);
			final EditText naView = (EditText) networkViewSettingsView
					.findViewById(R.id.server_address);
			naView.setText(SharedPreferencesUtils.getServerAddress(false));

			Dialog settingsDialog = new AlertDialog.Builder(this)
					.setTitle(R.string.settings)
					.setView(networkViewSettingsView)
					.setPositiveButton(R.string.submit,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									String serverAddress = naView.getText().toString().trim();
									
									if (serverAddress.equals("")) {
										Toaster.show(R.string.msg_serverAddress_empty);

										try {
											Field field = dialog
													.getClass()
													.getSuperclass()
													.getDeclaredField(
															"mShowing");
											field.setAccessible(true);
											field.set(dialog, false);
										} catch (Exception e) {
											e.printStackTrace();
										}

										return;
									}

									// todo

									SharedPreferencesUtils
											.setServerAddress(naView.getText()
													.toString().trim());
									Constants.URL_HOST = SharedPreferencesUtils.getServerAddress(true);
									Toaster.show(R.string.msg_settings_success);

									try {
										Field field = dialog.getClass()
												.getSuperclass()
												.getDeclaredField("mShowing");
										field.setAccessible(true);
										field.set(dialog, true);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							})
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// dialog.dismiss();
									Field field;
									try {
										field = dialog.getClass()
												.getSuperclass()
												.getDeclaredField("mShowing");
										field.setAccessible(true);
										field.set(dialog, true);
									} catch (Exception e) {
										e.printStackTrace();
									}

								}
							}).create();

			settingsDialog.show();
			break;
		}
	}

	boolean verify(String u, String p) {
		return true;
	}
}
