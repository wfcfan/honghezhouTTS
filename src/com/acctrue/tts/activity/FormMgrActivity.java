package com.acctrue.tts.activity;

import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.acctrue.tts.Constants;
import com.acctrue.tts.R;
import com.acctrue.tts.adapter.DownTaskFormAdapter;
import com.acctrue.tts.db.StoreDB;
import com.acctrue.tts.dto.UploadStoreRequest;
import com.acctrue.tts.dto.UploadStoreResponse;
import com.acctrue.tts.model.Store;
import com.acctrue.tts.model.StoreCode;
import com.acctrue.tts.model.StoreItem;
import com.acctrue.tts.rpc.OnCompleteListener;
import com.acctrue.tts.rpc.RpcAsyncTask;
import com.acctrue.tts.tasks.TaskUtils;
import com.acctrue.tts.utils.AccountUtil;
import com.acctrue.tts.utils.Toaster;
import com.acctrue.tts.utils.ViewUtil;

/**
 * 出库上传
 * 
 * @author wangfeng
 * 
 */
public class FormMgrActivity extends Activity implements OnClickListener {
	DownTaskFormAdapter adptRev;
	private StoreDB db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_form_mgr);
		ViewUtil.initHeader(this, "出库上传");
		db = new StoreDB(this);
		init();
	}

	void init() {
		Button btnSearch = (Button) this.findViewById(R.id.btn_search);
		btnSearch.setOnClickListener(this);

		Button btnEdit = (Button) this.findViewById(R.id.btn_edit);
		btnEdit.setOnClickListener(this);

		Button btnBack = (Button) this.findViewById(R.id.btn_back);
		btnBack.setOnClickListener(this);

		findViewById(R.id.btn_del).setOnClickListener(this);

		findViewById(R.id.btn_upload).setOnClickListener(this);

		// 初始化下拉列表
		ListView lstReps = (ListView) findViewById(R.id.lstReps);
		StoreDB db = new StoreDB(this);

		adptRev = new DownTaskFormAdapter(this, db.getStores(null));

		lstReps.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lstReps.setAdapter(adptRev);
		
		lstReps.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Store c = (Store) adptRev.getItem(position);
				showDetail(c);
			}

		});
	}
	
	void showDetail(Store store){
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		final View detailView = layoutInflater.inflate(R.layout.orderinfo_detail_view, null);
		this.setViewValue(detailView,R.id.textView1, store.getStoreNo());
		this.setViewValue(detailView,R.id.textView2, store.getStoreTypeText());
		this.setViewValue(detailView,R.id.textView3, store.getStoreStatusText());
		this.setViewValue(detailView,R.id.textView4, store.getStoreMan());
		this.setViewValue(detailView,R.id.textView5, store.getStoreDateText());
		this.setViewValue(detailView,R.id.textView6, store.getBizCorpName());
		this.setViewValue(detailView,R.id.textView7, store.getDescription());
		String ivs = StoreItem.toArrayString(store.getItem());
		this.setViewValue(detailView,R.id.textView8, ivs);
		Dialog detailDialog = new AlertDialog.Builder(this)
		.setTitle("详情")
		.setView(detailView)
		.setPositiveButton(R.string.btn_close,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						dialog.dismiss();
					}

				}).create();
		detailDialog.show();
	}
	
	void setViewValue(View v,int resId,String str){
		TextView tv = (TextView)v.findViewById(resId);
		tv.setText(str);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_back:
			this.finish();
			break;
		case R.id.btn_edit:
			editOrder();
			break;
		case R.id.btn_search:
			search();
			break;
		case R.id.btn_del:
			delete();
			break;
		case R.id.btn_upload:
			upload();
			break;
		}
	}

	private void upload() {
		List<Store> list = adptRev.getCheckedData();
		if (list == null || list.size() == 0) {
			Toaster.show(getResources().getString(
					R.string.warnning_no_uploaddata));
			return;
		}

		for (Store s : list) {
			uploadData("", s);
		}
		// showWeight(list);//先弹出重窗口，然后再上传数据
		// uploadData(list);
	}

	private void showWeight(final Store store) {
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		// ================选择计量单位布局
		final View dialogView = layoutInflater.inflate(R.layout.dialog_weight,
				null);

		// ==============计量单位控件
		final Spinner sp1 = (Spinner) dialogView.findViewById(R.id.spinnerUnit);
		final String[] mItems = this.getResources().getStringArray(
				R.array.unitList);
		ArrayAdapter<String> unitsData = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mItems);
		sp1.setAdapter(unitsData);// 设置计量单位数据

		// 仓库控件
		LinearLayout whLayout = (LinearLayout) dialogView
				.findViewById(R.id.layoutWh);
		whLayout.setVisibility(View.GONE);

		Dialog detailDialog = new AlertDialog.Builder(this)
				.setTitle("出库称重")
				.setView(dialogView)
				.setPositiveButton(R.string.btn_ok_tip,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								EditText editWeight = (EditText) dialogView
										.findViewById(R.id.editWeight);
								String strWeight = editWeight.getText()
										.toString();
								String strUnit = (String) sp1.getSelectedItem();
								strWeight += strUnit;// 完整重量
								if (TextUtils.isEmpty(strWeight)) {
									Toaster.show("重量不能为空!");
									return;
								}
								uploadData(strWeight, store);// 交差调用
							}

						}).create();
		detailDialog.show();
	}

	private void uploadData(String weigth, final Store store) {
		UploadStoreRequest request = new UploadStoreRequest();
		request.setSign(AccountUtil.getDefaultSign());
		store.setWeight(weigth);
		request.setStore(store);
		List<StoreCode> codes = db.getStoreCodes(store.getStoreId(),
				StoreCode.DELETE_FALSE);
		request.setCodes(codes);

		request.setRemoveCodes(db.getStoreCodes(store.getStoreId(),
				StoreCode.DELETE_TRUE));
		Log.i("UploadStoreRequest", request.toJson());
		RpcAsyncTask task = new RpcAsyncTask(this, request,
				new OnCompleteListener() {

					@Override
					public void onComplete(String content) {
						UploadStoreResponse response = null;
						try {
							response = UploadStoreResponse
									.fromJson(new JSONObject(content));
						} catch (Exception ex) {
							ex.printStackTrace();
						}

						if (response != null) {

							if (response.isError()
									&& response.getMessage().startsWith(
											"WEIGHT:")) {
								showWeight(store);
								return;
							}
							if (response.isError()) {
								Toaster.show(response.getMessage());
								return;
							}
							Toaster.show("上传成功!");
							// 刷新界面
							delete(store.getStoreId());
						}
					}

				});
		TaskUtils.execute(task, TaskUtils.POST, Constants.URL_UPLOADSTORE);
	}

	void delete() {
		final List<Store> list = adptRev.getCheckedData();
		if (list == null || list.size() == 0) {
			Toaster.show(getResources().getString(
					R.string.warnning_need_sel_delddata));
			return;
		}

		Dialog detailDialog = new AlertDialog.Builder(this)
				.setTitle("确认删除？")
				.setPositiveButton(R.string.btn_ok_tip,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								db.delStore(list);
								adptRev.RemoveItems(list);
							}

						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();

							}
						}).create();
		detailDialog.show();

	}

	void delete(String storeId) {
		db.delStore(storeId);
		adptRev.RemoveItem(storeId);
	}

	void editOrder() {
		List<Store> list = adptRev.getCheckedData();
		if (list.size() == 0) {
			Toaster.show(getResources().getString(
					R.string.warnning_need_sel_data));
			return;
		}

		Intent intent = new Intent(FormMgrActivity.this,
				OrderScanActivity.class);
		intent.putExtra("order_id", list.get(0).getStoreId());
		startActivity(intent);
	}

	void search() {
		TextView txt = (TextView) findViewById(R.id.txt_keyword);

		List<Store> list = db.searchStores(txt.getText());
		adptRev.replaceItems(list);
	}

}
