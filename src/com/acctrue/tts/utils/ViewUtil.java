package com.acctrue.tts.utils;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.acctrue.tts.Constants;
import com.acctrue.tts.GlobalApplication;
import com.acctrue.tts.R;
import com.acctrue.tts.adapter.WarehouseAdapter;
import com.acctrue.tts.db.WarehouseDB;
import com.acctrue.tts.dto.LoginResponse.UserInfo;
import com.acctrue.tts.model.Warehouse;

public class ViewUtil {

	public static int ImageWidth = 0;
	public static int ImageHeight = 0;
	public static int ContainerSize = 0;
	public static int PADDING = 10;

	public static void setSize(Activity a) {
		DisplayMetrics dm = a.getResources().getDisplayMetrics();
		Constants.WINDOW_WIDTH = dm.widthPixels;
		Constants.WINDOW_HEIGHT = dm.heightPixels;

		PADDING = Util.dip2px(10);

		ContainerSize = (Constants.WINDOW_WIDTH / 2);
		ImageWidth = Util.dip2px(60);
		ImageHeight = Util.dip2px(60);
	}

	public static int getDrawableResIdByName(String name) {
		Context context = GlobalApplication.mApp;
		int resId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
		return resId;
	}
	
	public static void bindWarehouse(final Context c,Spinner spinner){
		WarehouseDB warehouseDB = new WarehouseDB(c);
		List<Warehouse> warehouses = warehouseDB.getWarehouses();
		WarehouseAdapter adapter = new WarehouseAdapter(c,warehouses);
		spinner.setAdapter(adapter);
	}
	
	@SuppressWarnings("unchecked")
	static public void setSpinnerItemSelectedById(Spinner spinner, String id) {
		ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner
				.getAdapter();
		for (int i = 0; i < adapter.getCount(); i++) {
			if (adapter.getItem(i).toString().equals(id)) {
				spinner.setSelection(i, true);
				break;
			}
		}
	}
	
	
	public static void initHeader(final Activity activity,String title){
		final OnClickListener backAct = new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				activity.finish();
			}
		};
		
		Button btnlogout = (Button)activity.findViewById(R.id.btnlogout);
		btnlogout.setOnClickListener(backAct);
		
		TextView lblUser = (TextView) activity.findViewById(R.id.lblUser);
		UserInfo u = AccountUtil.getCurrentUser().getUserInfo();
		lblUser.setText(u.getUserDisplayName());
		lblUser.setOnClickListener(backAct);

		TextView lblTitle = (TextView) activity.findViewById(R.id.lblTitle);
		lblTitle.setText(title);

		TextView lblStatus = (TextView) activity.findViewById(R.id.lblStatus);
		lblStatus.setText(NetworkUtil
				.getNetworkStateString(activity));
	}

}
