package com.acctrue.tts.activity;

import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.acctrue.tts.Constants;
import com.acctrue.tts.R;
import com.acctrue.tts.adapter.StoreInAdapter;
import com.acctrue.tts.db.ChargeStoreInDB;
import com.acctrue.tts.dto.UploadChargeStoreInfoRequest;
import com.acctrue.tts.dto.UploadChargeStoreInfoRequest.UploadChargeStoreInfo;
import com.acctrue.tts.dto.UploadChargeStoreInfoResponse;
import com.acctrue.tts.model.ChargeStoreIn;
import com.acctrue.tts.model.ChargeStoreInCode;
import com.acctrue.tts.rpc.OnCompleteListener;
import com.acctrue.tts.rpc.RpcAsyncTask;
import com.acctrue.tts.tasks.TaskUtils;
import com.acctrue.tts.utils.AccountUtil;
import com.acctrue.tts.utils.DateUtil;
import com.acctrue.tts.utils.Toaster;
import com.acctrue.tts.utils.ViewUtil;

/**
 * 入库上传
 * 
 * @author peng
 * 
 */
public class Save2ReposMgntActivity extends Activity {

	ListView lstReps;
	ChargeStoreInDB db;
	private static final int ITEM_MODIFY = 1;
	private static final int ITEM_DELETE = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_2repo_mgnt);

		db = new ChargeStoreInDB(this);
		ViewUtil.initHeader(this, "入库上传");
		init();
	}

	private void init() {
		lstReps = (ListView) findViewById(R.id.lstReps);
		// 初始化下拉列表
		ChargeStoreInDB db = new ChargeStoreInDB(this);
		final StoreInAdapter adpt = new StoreInAdapter(this,
				db.getAllChargeStoreIn(),
				(CheckBox) findViewById(R.id.chkSelAll));

		lstReps.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lstReps.setAdapter(adpt);
		lstReps.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ChargeStoreIn c = (ChargeStoreIn) adpt.getItem(position);
				showDetail(c);
			}

		});

		registerForContextMenu(lstReps);

		// 新建
		findViewById(R.id.btnNew).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(Save2ReposMgntActivity.this,
						Receive2Repository.class));
				// finish();
			}
		});

		// 设置返回按钮事件
		findViewById(R.id.btnBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Save2ReposMgntActivity.this.finish();
			}
		});

		// 删除
		findViewById(R.id.btnDel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteData();

			}
		});

		// 入库
		findViewById(R.id.btnRepo).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				uploadData();

			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		this.init();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("请选择操作");
		menu.add(0, ITEM_MODIFY, 0, "编辑");
		menu.add(0, ITEM_DELETE, 1, "删除");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		ListView lstReps = (ListView) findViewById(R.id.lstReps);
		final StoreInAdapter adpt = (StoreInAdapter) lstReps.getAdapter();

		ChargeStoreIn currentcsi = (ChargeStoreIn) adpt.getItem(info.position);
		final String id = currentcsi.getId();

		switch (item.getItemId()) {
		case ITEM_MODIFY:
			Intent intent = new Intent(Save2ReposMgntActivity.this,
					Receive2Repository.class);
			intent.putExtra("id", id);
			startActivity(intent);
			break;
		case ITEM_DELETE:
			db.delChargeStoreIn(id);
			Toaster.show("删除成功!");
			adpt.RemoveItems(currentcsi);
			break;
		}
		return false;
	}

	void showDetail(ChargeStoreIn c) {
		LayoutInflater layoutInflater = LayoutInflater.from(this);

		final View detailView = layoutInflater.inflate(R.layout.item, null);
		TextView tv1 = (TextView) detailView.findViewById(R.id.textView1);
		tv1.setTextColor(this.getResources().getColor(R.color.white));
		tv1.setText("收取码：");

		String codes = "";
		List<ChargeStoreInCode> codeList = db.getChargeStoreInCode(c.getId());
		for (ChargeStoreInCode cc : codeList) {
			codes += cc.getCode() + "\n\r";
		}
		TextView tv2 = (TextView) detailView.findViewById(R.id.textView2);
		tv2.setTextColor(this.getResources().getColor(R.color.white));
		tv2.setText(codes);

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

	void uploadData() {
		StoreInAdapter adpt = (StoreInAdapter) lstReps.getAdapter();
		final List<ChargeStoreIn> selData = adpt.getCheckedData();

		if (selData.size() == 0) {
			Toaster.show("请先选择数据项!");
			return;
		}

		for (ChargeStoreIn us : selData) {
			uploadData("", us);// 上传数据到接口
		}
	}

	private void uploadData(final ChargeStoreIn us) {
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

		LinearLayout whLayout = (LinearLayout) dialogView
				.findViewById(R.id.layoutWh);
		whLayout.setVisibility(View.GONE);

		Dialog detailDialog = new AlertDialog.Builder(this)
				.setTitle("入库称重")
				.setView(dialogView)
				.setPositiveButton(R.string.btn_ok_tip,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int arg1) {

								EditText editWeight = (EditText) dialogView
										.findViewById(R.id.editWeight);
								String strWeight = editWeight.getText()
										.toString();
								String strUnit = (String) sp1.getSelectedItem();
								strWeight += strUnit;// 完整重量
								uploadData(strWeight, us);// 上传数据到接口,交叉调用
							}

						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int arg1) {
								dialog.dismiss();
							}

						}).create();
		detailDialog.show();
	}

	void uploadData(String weigth, final ChargeStoreIn us) {

		UploadChargeStoreInfoRequest uc = new UploadChargeStoreInfoRequest();
		uc.setSign(AccountUtil.getDefaultSign());

		UploadChargeStoreInfo ucs = new UploadChargeStoreInfo();
		ucs.setActor(us.getActor());
		ucs.setActorDate(DateUtil.parseDatetimeToJsonDate(us.getActDate()));
		ucs.setCorpId(AccountUtil.getCurrentUser().getUserInfo().getCorpId());
		ucs.setChargeCodes(us.getCodesStr());
		ucs.setWeight(weigth);
		uc.setRequestInfo(ucs);

		final ChargeStoreInDB db = new ChargeStoreInDB(this);
		RpcAsyncTask task = new RpcAsyncTask(this, uc,
				new OnCompleteListener() {

					@Override
					public void onComplete(String content) {
						UploadChargeStoreInfoResponse response = null;
						try {
							response = UploadChargeStoreInfoResponse
									.fromJson(new JSONObject(content));
						} catch (Exception ex) {
							ex.printStackTrace();
						}

						if (response != null) {

							if (response.isError()
									&& response.getMessage().startsWith(
											"WEIGHT:")) {
								uploadData(us);
								return;
							} else if (response.isError()) {
								Toaster.show(response.getMessage());
								return;
							}

							db.delChargeStoreIn(us.getId());
							StoreInAdapter sa = (StoreInAdapter) lstReps
									.getAdapter();
							sa.RemoveItems(us);
							Toaster.show("入库成功!");
						}
					}
				});

		TaskUtils.execute(task, TaskUtils.POST,
				Constants.URL_UPLOADCHARGESTOREINFO);
	}

	@Override
	protected void onRestart() {
		super.onResume();
		this.init();
	}

	private void deleteData() {
		ListView lstReps = (ListView) findViewById(R.id.lstReps);
		final StoreInAdapter adpt = (StoreInAdapter) lstReps.getAdapter();
		final List<ChargeStoreIn> delData = adpt.getCheckedData();

		if (delData.size() == 0) {
			Toaster.show("请先选择数据项!");
			return;
		}

		Dialog detailDialog = new AlertDialog.Builder(this)
				.setTitle("确认删除？")
				.setPositiveButton(R.string.btn_ok_tip,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// 删除数据库中的数据
								ChargeStoreInDB db = new ChargeStoreInDB(
										Save2ReposMgntActivity.this);
								db.delChargeStoreIn(delData);

								// 刷新界面
								adpt.RemoveItems(delData);
								Toaster.show("删除成功!");
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
}