package com.acctrue.tts.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.acctrue.tts.Constants;
import com.acctrue.tts.R;
import com.acctrue.tts.adapter.ModuleAdapter;
import com.acctrue.tts.dto.LoginResponse;
import com.acctrue.tts.utils.AccountUtil;
import com.acctrue.tts.utils.NetworkUtil;
import com.acctrue.tts.utils.Toaster;

/**
 * 主页
 * @author wangfeng
 *
 */
public class HomeActivity extends Activity implements OnClickListener {
	

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		AccountUtil.loadAccount();
		final Button btnLogout = (Button) this.findViewById(R.id.btnlogout);
		btnLogout.setOnClickListener(this);
		
		this.init();
		
		GridView listView = (GridView) this.findViewById(R.id.mainlist);
		listView.setAdapter(new ModuleAdapter(this));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterview, View view,
					int position, long l) {
				
				Intent intent = null;
				switch(position){
					case Constants.DATA_DOWNLOAD: // ---------数据下载
						intent = new Intent(HomeActivity.this,DataDownloadActivity.class);
						break;
					case Constants.PRODUCT_RECEIVE: // ---------农产品收取
						intent = new Intent(HomeActivity.this,ChargeActivity.class); 
						break;
					case Constants.RECEIVE_NO_MGNT: // ---------收取码管理
						intent = new Intent(HomeActivity.this,Receive2MangermentActivity.class); 
						break;
					case Constants.RECEIVE_TO_REPOS: // ---------收取入库
						intent = new Intent(HomeActivity.this,Receive2Repository.class); 
						break;
					case Constants.SAVE_REPOS_MGNT: // ---------入库管理
						intent = new Intent(HomeActivity.this,Save2ReposMgntActivity.class); 
						break;
					case Constants.PRODUCT_WRAP: // ---------农产品转装
						intent = new Intent(HomeActivity.this,ProductWrapActivity.class); 
						break;
					case Constants.TRACK_NO_MGNT: // ---------追溯码管理
						intent = new Intent(HomeActivity.this,TrackNoMgntActivity.class); 
						break;
					case Constants.E_BUSINESS_OUT: // ---------电商出库 
						if(NetworkUtil.isOffLine()){
							Toaster.show(R.string.offline_not_action);
						}else{
							intent = new Intent(HomeActivity.this,EBusinessOutStoreActivity.class);
						}
						break;
					case Constants.DIRECT_OUT_STORE: // ---------直接出库 
						if(NetworkUtil.isOffLine()){
							Toaster.show(R.string.offline_not_action);
						}else{
							intent = new Intent(HomeActivity.this,DirectOutStoreActivity.class);
						}
						break;
					case Constants.FROM_MGR://--------单据管理
						intent = new Intent(HomeActivity.this,FormMgrActivity.class); 
						break;
					default:
						Toast.makeText(getBaseContext(), R.string.function_todo, Toast.LENGTH_LONG).show();
						break;
						
				}
				
				if(intent != null)
					startActivity(intent);
				//finish();
			}

		});
	}
	
	protected void init(){
		TextView lblUser = (TextView) this.findViewById(R.id.lblUser);
		LoginResponse u = AccountUtil.getCurrentUser();
		String userName = "";
		if(u != null)
			userName = u.getUserInfo().getUserDisplayName();

		lblUser.setText(userName);
		lblUser.setOnClickListener(this);
		TextView lblTitle = (TextView) this.findViewById(R.id.lblTitle);
		lblTitle.setText("主界面");

		TextView lblStatus = (TextView) this.findViewById(R.id.lblStatus);
		lblStatus.setText(NetworkUtil.getNetworkStateString(HomeActivity.this));
	}
	
	@Override
	public void onClick(View view) {

		if (view.getId() == R.id.btnlogout || view.getId() == R.id.lblUser) {
			Dialog logoutDialog = new AlertDialog.Builder(this)
			.setTitle(R.string.prompt)
			.setMessage(R.string.msg_login_message)
			.setPositiveButton(R.string.btn_ok_tip, new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int arg1) {
					dialog.dismiss();
					Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
					HomeActivity.this.startActivity(intent);
					AccountUtil.clearCache();
					HomeActivity.this.finish();
				}
				
			})
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int arg1) {
					dialog.dismiss();
				}
				
			})
			.create();
			logoutDialog.show();
		}

	}

	boolean canClose = false;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (canClose) {
				this.finish();
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
