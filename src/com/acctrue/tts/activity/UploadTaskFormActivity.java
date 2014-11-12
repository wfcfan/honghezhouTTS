package com.acctrue.tts.activity;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.acctrue.tts.R;
import com.acctrue.tts.adapter.DownTaskFormAdapter;
import com.acctrue.tts.db.StoreDB;
import com.acctrue.tts.model.Store;
import com.acctrue.tts.utils.Toaster;
import com.acctrue.tts.utils.ViewUtil;

@Deprecated
public class UploadTaskFormActivity extends Activity implements OnClickListener {
	DownTaskFormAdapter adptRev;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_taskform);
		
		ViewUtil.initHeader(this, "上传入出库单");
		init();
	}

	void init() {
		Button btnBack = (Button) this.findViewById(R.id.btn_back);
		btnBack.setOnClickListener(this);

		Button btnDown = (Button) this.findViewById(R.id.btn_upload);
		btnDown.setOnClickListener(this);

		// 初始化下拉列表
		ListView lstReps = (ListView) findViewById(R.id.lstReps);
		StoreDB db = new StoreDB(this);

		adptRev = new DownTaskFormAdapter(this, db.getStores(null));
		lstReps.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lstReps.setAdapter(adptRev);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_back:
			this.finish();
			break;
		case R.id.btn_upload:
			submitOrder();
			break;
		}
	}

	private void submitOrder() {
		List<Store> list = adptRev.getCheckedData();
		if (list == null || list.size() != 1) {
			Toaster.show(getResources().getString(
					R.string.warnning_need_sel_uploaddata));
			return;
		}
		
		uploadData(list);

	}

	// 该方法应该在单据上传中调用
	private void uploadData(List<Store> list) {
		/*
		PackCodeDB packDB = new PackCodeDB(this);

		List<JsonRest> request = new ArrayList<JsonRest>();
		for(StoreDB item : list){
			List<PackCode> packCodes = packDB.getPackCodeByOrderId(item.getStoreId(), false);
			List<PackCode> delPackCodes = packDB
					.getPackCodeByOrderId(item.getStoreId(), true);
			request.add(new OrderInfoRequest(item, packCodes, delPackCodes));
		}

		RestAsyncTask task = new RestAsyncTask(this, R.string.tip_title,
				R.string.tip_dataupload, R.string.tip_dataupload_failed,
				request, new OnCompleteListener() {

					@Override
					public void onComplete(String content) {
						try {
							boolean fail = true;
							if (content != null) {
								JSONObject ret = new JSONObject(content);
								fail = ret.getBoolean("IsError");
							}
							if (content == null || fail) {
								Toaster.show(getBaseContext().getResources()
										.getString(
												R.string.tip_dataupload_failed));
							} else {
								// 刷新界面
								List<OrderInfo> list = adptRev.getCheckedData();
								adptRev.RemoveItems(list);
							}
						} catch (JSONException e) {
							Toaster.show(e.getMessage());
						}
					}
				});

		task.execute("post", Constants.URL_HOST + "/rest/UploadStore");
	*/
	}
}
