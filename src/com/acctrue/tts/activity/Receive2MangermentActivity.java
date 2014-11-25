package com.acctrue.tts.activity;

import java.lang.reflect.Field;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.acctrue.tts.Constants;
import com.acctrue.tts.R;
import com.acctrue.tts.adapter.Receive2MangermentAdapter;
import com.acctrue.tts.db.ChargeCodesDB;
import com.acctrue.tts.db.ChargesDB;
import com.acctrue.tts.dto.LoginResponse.UserInfo;
import com.acctrue.tts.dto.UploadChargeCodesRequest;
import com.acctrue.tts.dto.UploadChargeCodesResponse;
import com.acctrue.tts.dto.UploadChargeStoreInfoRequest;
import com.acctrue.tts.dto.UploadChargeStoreInfoRequest.UploadChargeStoreInfo;
import com.acctrue.tts.dto.UploadChargeStoreInfoResponse;
import com.acctrue.tts.enums.ChargesChargesEnum;
import com.acctrue.tts.enums.ChargesStatusEnum;
import com.acctrue.tts.enums.CodeTypeEnum;
import com.acctrue.tts.model.ChargeCodes;
import com.acctrue.tts.model.Charges;
import com.acctrue.tts.rpc.OnCompleteListener;
import com.acctrue.tts.rpc.RpcAsyncTask;
import com.acctrue.tts.tasks.TaskUtils;
import com.acctrue.tts.utils.AccountUtil;
import com.acctrue.tts.utils.DateUtil;
import com.acctrue.tts.utils.Toaster;
import com.acctrue.tts.utils.ViewUtil;

/**
 * 收取上传
 * 
 * @author wangfeng
 * 
 */
public class Receive2MangermentActivity extends Activity implements
		OnClickListener {
	private final String TAG = Receive2MangermentActivity.class.getSimpleName();
	private static final int ITEM_MODIFY = 1;
	private static final int ITEM_DELETE = 2;
	ChargeCodesDB db2;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_receive2mgmt);

		ViewUtil.initHeader(this, "收取上传");
		db2 = new ChargeCodesDB(this);
		this.init();
	}

	void init() {
		// 初始化下拉列表
		ListView lstReps = (ListView) findViewById(R.id.lstReps);
		ChargesDB db = new ChargesDB(this);

		List<Charges> list = db.getCharges();

		for (int i = 0; i < list.size(); i++) {
			Charges c = list.get(i);
			List<ChargeCodes> ccList = db2.getChargeCodes2(list.get(i).getId());
			c.setChargeCodes(ccList);
			// c.getCodes().add(UUID.randomUUID().toString());//测试使用
		}

		final Receive2MangermentAdapter adptRev = new Receive2MangermentAdapter(
				this, list, (CheckBox) findViewById(R.id.chkSelAll));
		Log.d(TAG, String.valueOf(adptRev.getCount()));
		lstReps.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lstReps.setAdapter(adptRev);
		lstReps.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Charges c = (Charges) adptRev.getItem(position);
				showDetail(c);
			}

		});
		this.registerForContextMenu(lstReps);

		Button btnUpload = (Button) this.findViewById(R.id.btnUpload);
		btnUpload.setOnClickListener(this);

		Button btnUploadAll = (Button) this.findViewById(R.id.btnUploadAll);
		btnUploadAll.setOnClickListener(this);

		Button btnNew = (Button) this.findViewById(R.id.btnNew);
		btnNew.setOnClickListener(this);

		Button btnDel = (Button) this.findViewById(R.id.btnDel);
		btnDel.setOnClickListener(this);

		Button btnRepo = (Button) this.findViewById(R.id.btnRepo);
		btnRepo.setOnClickListener(this);

		Button btnBack = (Button) this.findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
	}

	final void showDetail(Charges c) {
		LayoutInflater layoutInflater = LayoutInflater.from(this);

		final View detailView = layoutInflater.inflate(
				R.layout.charges_detail_view, null);
		this.setViewText((TextView) detailView.findViewById(R.id.textView1),
				c.getBatchno());
		this.setViewText((TextView) detailView.findViewById(R.id.textView2),
				c.getManName());
		this.setViewText((TextView) detailView.findViewById(R.id.textView3),
				c.getFarmlandName());
		this.setViewText((TextView) detailView.findViewById(R.id.textView4),
				c.getProductName());
		this.setViewText((TextView) detailView.findViewById(R.id.textView5),
				c.getCreateDate());
		this.setViewText((TextView) detailView.findViewById(R.id.textView6),
				c.getMan());
		this.setViewText((TextView) detailView.findViewById(R.id.textView7),
				c.getPackCodeName());

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

	final void setViewText(TextView tv, String v) {
		tv.setText(v);
	}

	@Override
	public void onClick(View view) {

		ListView lstReps = (ListView) findViewById(R.id.lstReps);
		final Receive2MangermentAdapter adpt = (Receive2MangermentAdapter) lstReps
				.getAdapter();
		List<Charges> dataList = null;

		CheckBox chkInStock = (CheckBox) findViewById(R.id.chkIn);
		// ChargesStatusEnum status = chkInStock.isChecked() ?
		// ChargesStatusEnum.InStock : ChargesStatusEnum.Uploaded;

		int isAutoStorage = chkInStock.isChecked() ? 1 : 0;

		switch (view.getId()) {
		case R.id.btnUpload:
			dataList = adpt.getCheckedData();
			// if(status == ChargesStatusEnum.Uploaded)
			// uploadCharges(adpt, dataList,isAutoStorage);
			// else
			// showWarehouse(false, dataList, adpt);
			uploadCharges(adpt, dataList, isAutoStorage);
			break;
		case R.id.btnUploadAll:
			//dataList = adpt.getAllDatas();
			// if(status == ChargesStatusEnum.Uploaded)
			// uploadCharges(adpt, dataList,isAutoStorage);
			// else
			// showWarehouse(false, dataList, adpt);
			//uploadCharges(adpt, dataList, isAutoStorage);
			break;
		case R.id.btnDel:
			dataList = adpt.getCheckedData();
			delCharges(dataList, adpt);
			break;
		case R.id.btnRepo:
			//dataList = adpt.getCheckedData();
			// showWarehouse(dataList, adpt);
			break;
		case R.id.btnNew:
			Intent intent = new Intent(Receive2MangermentActivity.this,
					ChargeActivity.class);
			startActivity(intent);
			// this.finish();
			break;
		case R.id.btnBack:
			this.finish();
			break;
		}

	}

	@Override
	protected void onRestart() {
		super.onRestart();
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
		final Receive2MangermentAdapter adpt = (Receive2MangermentAdapter) lstReps
				.getAdapter();

		Charges currentCharges = (Charges) adpt.getItem(info.position);
		final String id = currentCharges.getId();

		switch (item.getItemId()) {
		case ITEM_MODIFY:
			Intent intent = new Intent(Receive2MangermentActivity.this,
					ChargeActivity.class);
			intent.putExtra("chargesId", id);
			startActivity(intent);
			break;
		case ITEM_DELETE:
			ChargesDB chargesDB = new ChargesDB(this);
			chargesDB.deleteCharges(id);
			Toaster.show("删除成功!");
			adpt.RemoveItems(currentCharges);
			break;
		}
		return false;
	}

	void delCharges(final List<Charges> chargesList,
			final Receive2MangermentAdapter adpt) {
		if (chargesList.size() == 0) {
			Toaster.show("请先选择数据行!");
			return;
		}

		Dialog detailDialog = new AlertDialog.Builder(this)
				.setTitle("确认删除？")
				.setPositiveButton(R.string.btn_ok_tip,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								final ChargesDB chargesDB = new ChargesDB(
										Receive2MangermentActivity.this);
								for (Charges charges : chargesList) {
									chargesDB.deleteCharges(charges.getId());
									adpt.RemoveItems(chargesList);
								}
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

	void uploadCharges(Receive2MangermentAdapter adapter,
			final List<Charges> chargesList, int isAutoStorage) {
		if (chargesList.size() == 0) {
			Toaster.show("请先选择数据行!");
			return;
		}

		for (Charges charges : chargesList) {
			if(charges.getIsPackCode() == CodeTypeEnum.TrackCode.getId() && isAutoStorage == 1){
				Toaster.show("追溯码无需入库!");
				break;
			}
			
			if (charges.getState() == ChargesChargesEnum.Uploaded.getStateId()) {
				UserInfo user = AccountUtil.getCurrentUser().getUserInfo();
				UploadChargeStoreInfo ucs = new UploadChargeStoreInfo();
				ucs.setActor(user.getUserName());
				ucs.setActorDate(DateUtil.parseDatetimeToJsonDate(charges
						.getCreateDate()));
				ucs.setChargeCodes(charges.getCodes());
				ucs.setCorpId(user.getCorpId());
				charges.setWeight("0");
				ucs.setWeight("0");
				inStock(charges, ucs, adapter);
			} else {
				upload(charges, adapter, isAutoStorage);// 上传数据到服务器
			}
		}
	}

	void showWeight(final Charges c, final Receive2MangermentAdapter adapter,
			final int isAutoStorage) {
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
				.setTitle(c.getManName() + "--入库称重")
				.setView(dialogView)
				.setPositiveButton(R.string.btn_ok_tip,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int arg1) {
								EditText editWeight = (EditText) dialogView
										.findViewById(R.id.editWeight);
								String strWeight = editWeight.getText()
										.toString();

								if (TextUtils.isEmpty(strWeight)) {
									Toaster.show("重量不能为空!");

									try {
										Field field = dialog.getClass()
												.getSuperclass()
												.getDeclaredField("mShowing");
										field.setAccessible(true);
										field.set(dialog, false);
									} catch (Exception e) {
										e.printStackTrace();
									}

									return;
								}

								String strUnit = (String) sp1.getSelectedItem();
								strWeight += strUnit;// 完整重量

								UserInfo user = AccountUtil.getCurrentUser()
										.getUserInfo();
								UploadChargeStoreInfo ucs = new UploadChargeStoreInfo();
								ucs.setActor(user.getUserName());
								ucs.setActorDate(DateUtil
										.parseDatetimeToJsonDate(c
												.getCreateDate()));
								ucs.setChargeCodes(c.getCodes());
								ucs.setCorpId(user.getCorpId());

								c.setWeight(strWeight);
								ucs.setWeight(strWeight);
								
								if (isAutoStorage == 1 || c.getIsPackCode() == CodeTypeEnum.TrackCode.getId())
									upload(c, adapter, 1);// 自动入库，交叉调用函数
								else
									inStock(c, ucs, adapter);
							}

						}).create();

		detailDialog.show();

	}

	void upload(final Charges c, final Receive2MangermentAdapter adapter,
			final int isAutoStorage) {
		ChargeCodesDB db = new ChargeCodesDB(this);
		List<ChargeCodes> ccList = db.getChargeCodes2(c.getId());// 获取单据下的所有扫码
		if (ccList.size() == 0) {
			Toaster.show(String.format("单据：%s下没有扫码,无法进行上传!", c.getBatchno()));
			return;
		}

		if (TextUtils.isEmpty(c.getWeight())) {// 首次上传时重随便填写一个
			c.setWeight("0");
		}

		if (c.getIsPackCode() == CodeTypeEnum.TrackCode.getId()) {
			c.setIsAutoStorage(2);// 追溯码
		} else {
			c.setIsAutoStorage(isAutoStorage);// 收取码
		}

		UploadChargeCodesRequest uc = new UploadChargeCodesRequest();
		uc.setSign(AccountUtil.getDefaultSign());
		uc.setInfo(c);

		final ChargesDB chargesDB = new ChargesDB(this);
		RpcAsyncTask task = new RpcAsyncTask(this, uc,
				new OnCompleteListener() {

					@Override
					public void onComplete(String content) {
						UploadChargeCodesResponse uccr = null;
						try {
							uccr = UploadChargeCodesResponse
									.fromJson(new JSONObject(content));
						} catch (Exception ex) {
							ex.printStackTrace();
						}

						if (uccr != null) {

							if (uccr.isError()
									&& uccr.getMessage().startsWith("WEIGHT:")) {
								showWeight(c, adapter, isAutoStorage);
								return;
							} else if (uccr.isError()) {
								Toaster.show(uccr.getMessage());
								return;
							}
							// if(c.getState())

							//===========自动入库或者追溯码的时候
							if (isAutoStorage == 1 || c.getIsPackCode() == CodeTypeEnum.TrackCode.getId()) {
								chargesDB.deleteCharges(c.getId());// 删除数据
								adapter.RemoveItems(c);
							} else {
								chargesDB.modifyChangeState(c.getId(),
										ChargesStatusEnum.Uploaded);// 修改上传状态
								c.setState(ChargesStatusEnum.Uploaded
										.getStateId());
								adapter.updateItem(c);
							}
							Toaster.show("上传成功!");
						}
					}

				});
		TaskUtils
				.execute(task, TaskUtils.POST, Constants.URL_UPLOADCHARGECODES);

	}

	void inStock(final Charges c, final UploadChargeStoreInfo ucs,
			final Receive2MangermentAdapter adapter) {
		UploadChargeStoreInfoRequest uc = new UploadChargeStoreInfoRequest();
		uc.setSign(AccountUtil.getDefaultSign());
		uc.setRequestInfo(ucs);

		final ChargesDB chargesDB = new ChargesDB(this);
		RpcAsyncTask task = new RpcAsyncTask(this, uc,
				new OnCompleteListener() {

					@Override
					public void onComplete(String content) {
						UploadChargeStoreInfoResponse usci = null;
						try {
							usci = UploadChargeStoreInfoResponse
									.fromJson(new JSONObject(content));
						} catch (Exception ex) {
							ex.printStackTrace();
						}

						if (usci != null) {

							if (usci.isError()
									&& usci.getMessage().startsWith("WEIGHT:")) {
								inStockWeigth(c, ucs, adapter);
								return;
							} else if (usci.isError()) {
								Toaster.show(usci.getMessage());
								return;
							}

							chargesDB.deleteCharges(c.getId());// 删除数据
							adapter.RemoveItems(c);
						}
					}
				});
		TaskUtils.execute(task, TaskUtils.POST,
				Constants.URL_UPLOADCHARGESTOREINFO);
	}

	void inStockWeigth(final Charges c, final UploadChargeStoreInfo ucs,
			final Receive2MangermentAdapter adapter) {
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
				.setTitle(c.getBatchno() + "--入库称重")
				.setView(dialogView)
				.setPositiveButton(R.string.btn_ok_tip,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int arg1) {
								EditText editWeight = (EditText) dialogView
										.findViewById(R.id.editWeight);
								String strWeight = editWeight.getText()
										.toString();

								if (TextUtils.isEmpty(strWeight)) {
									Toaster.show("重量不能为空!");

									try {
										Field field = dialog.getClass()
												.getSuperclass()
												.getDeclaredField("mShowing");
										field.setAccessible(true);
										field.set(dialog, false);
									} catch (Exception e) {
										e.printStackTrace();
									}

									return;
								}

								String strUnit = (String) sp1.getSelectedItem();
								strWeight += strUnit;// 完整重量
								c.setWeight(strWeight);
								ucs.setWeight(strWeight);
								inStock(c, ucs, adapter);// 交差調用

							}

						}).create();

		detailDialog.show();
	}

	@Deprecated
	void showWarehouse(Charges c, final Receive2MangermentAdapter adapter) {

		/*
		 * if (chargesList.size() == 0) { Toaster.show("请先选择数据行!"); return; }
		 * 
		 * LayoutInflater layoutInflater = LayoutInflater.from(this); //
		 * ================选择计量单位布局 final View dialogView =
		 * layoutInflater.inflate(R.layout.dialog_weight, null);
		 * 
		 * // 仓库控件 final Spinner spwh = (Spinner) dialogView
		 * .findViewById(R.id.spinnerWarehouse); ViewUtil.bindWarehouse(this,
		 * spwh); LinearLayout ly2 = (LinearLayout)
		 * dialogView.findViewById(R.id.ly2); ly2.setVisibility(View.GONE);
		 * LinearLayout ly3 = (LinearLayout) dialogView.findViewById(R.id.ly3);
		 * ly3.setVisibility(View.GONE);
		 * 
		 * Dialog detailDialog = new AlertDialog.Builder(this) .setTitle("入库称重")
		 * .setView(dialogView) .setPositiveButton(R.string.btn_ok_tip, new
		 * DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int arg1) {
		 * 
		 * UserInfo user = AccountUtil.getCurrentUser() .getUserInfo();
		 * Warehouse wh = (Warehouse) spwh .getSelectedItem();
		 * 
		 * for (Charges c : chargesList) {
		 * 
		 * UploadChargeStoreInfo ucs = new UploadChargeStoreInfo();
		 * ucs.setActor(user.getUserName()); ucs.setActorDate(DateUtil
		 * .parseDatetimeToJsonDate(c .getCreateDate()));
		 * ucs.setChargeCodes(c.getCodes()); ucs.setCorpId(wh.getCorpId());
		 * 
		 * c.setWeight("0"); ucs.setWeight("0"); inStock(c, ucs, adapter); }
		 * 
		 * }
		 * 
		 * }) .setNegativeButton(R.string.cancel, new
		 * DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int arg1) {
		 * dialog.dismiss(); }
		 * 
		 * }).create(); detailDialog.show();
		 */
	}
}
