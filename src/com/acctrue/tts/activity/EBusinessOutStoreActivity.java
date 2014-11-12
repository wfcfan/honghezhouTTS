package com.acctrue.tts.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.acctrue.tts.R;
import com.acctrue.tts.adapter.StoreListAdapter;
import com.acctrue.tts.db.StoreDB;
import com.acctrue.tts.enums.StoreSource;
import com.acctrue.tts.model.Store;
import com.acctrue.tts.utils.Toaster;
import com.acctrue.tts.utils.ViewUtil;

public class EBusinessOutStoreActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ebusiness_out_store);

		ViewUtil.initHeader(this, "电商出库");
		init();
	}

	public void init() {
		// 初始化下拉列表
		ListView lstReps = (ListView) findViewById(R.id.lstReps);
		final StoreDB db = new StoreDB(this);

		final StoreListAdapter adptRev = new StoreListAdapter(this,
				db.getStores(StoreSource.Server));

		//lstReps.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lstReps.setAdapter(adptRev);
		lstReps.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Store store = (Store)adptRev.getItem(position);
				Intent intent = new Intent(EBusinessOutStoreActivity.this,
						OrderScanActivity.class);
				intent.putExtra("order_id", store.getStoreId());
				startActivity(intent);
			}
		});
		
		if(adptRev.getCount() == 0){
			Toaster.show("暂无数据!");
			return;
		}
	}
}
