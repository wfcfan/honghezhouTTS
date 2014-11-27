package com.acctrue.tts;

import java.lang.reflect.Field;
import java.util.List;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.acctrue.tts.adapter.Receive2MangermentAdapter;
import com.acctrue.tts.db.ChargesDB;
import com.acctrue.tts.dto.UploadChargeStoreInfoRequest;
import com.acctrue.tts.dto.UploadChargeStoreInfoResponse;
import com.acctrue.tts.dto.LoginResponse.UserInfo;
import com.acctrue.tts.dto.UploadChargeStoreInfoRequest.UploadChargeStoreInfo;
import com.acctrue.tts.model.Charges;
import com.acctrue.tts.model.Warehouse;
import com.acctrue.tts.rpc.OnCompleteListener;
import com.acctrue.tts.rpc.RpcAsyncTask;
import com.acctrue.tts.tasks.TaskUtils;
import com.acctrue.tts.utils.AccountUtil;
import com.acctrue.tts.utils.DateUtil;
import com.acctrue.tts.utils.Toaster;
import com.acctrue.tts.utils.ViewUtil;

@Deprecated
public class InStockHelper {
	
	final List<Charges> chargesList;
	final Receive2MangermentAdapter adapter;
	final Context context;
	
	public  InStockHelper(Context context,List<Charges> chargesList,Receive2MangermentAdapter adapter){
		this.context=context;
		this.chargesList = chargesList;
		this.adapter = adapter;
	}
	
	public void showWarehouse(){
		if (chargesList.size() == 0) {
			Toaster.show("请先选择数据行!");
			return;
		}

		LayoutInflater layoutInflater = LayoutInflater.from(context);
		// ================选择计量单位布局
		final View dialogView = layoutInflater.inflate(R.layout.dialog_weight,
				null);

		// 仓库控件
		final Spinner spwh = (Spinner) dialogView
				.findViewById(R.id.spinnerWarehouse);
		ViewUtil.bindWarehouse(context, spwh);
		LinearLayout ly2 = (LinearLayout) dialogView.findViewById(R.id.ly2);
		ly2.setVisibility(View.GONE);
		LinearLayout ly3 = (LinearLayout) dialogView.findViewById(R.id.ly3);
		ly3.setVisibility(View.GONE);

		Dialog detailDialog = new AlertDialog.Builder(context)
				.setTitle("入库称重")
				.setView(dialogView)
				.setPositiveButton(R.string.btn_ok_tip,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int arg1) {

								UserInfo user = AccountUtil.getCurrentUser()
										.getUserInfo();
								Warehouse wh = (Warehouse) spwh
										.getSelectedItem();

								for (Charges c : chargesList) {

									UploadChargeStoreInfo ucs = new UploadChargeStoreInfo();
									ucs.setActor(user.getUserName());
									ucs.setActorDate(DateUtil
											.parseDatetimeToJsonDate(c
													.getCreateDate()));
									ucs.setChargeCodes(c.getCodes());
									ucs.setCorpId(wh.getCorpId());

									c.setWeight("0");
									ucs.setWeight("0");
									//inStock(c, ucs, adapter);
								}

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
	
	void inStock(final Charges c, final UploadChargeStoreInfo ucs,
			final Receive2MangermentAdapter adapter) {
		UploadChargeStoreInfoRequest uc = new UploadChargeStoreInfoRequest();
		uc.setSign(AccountUtil.getDefaultSign());
		uc.setRequestInfo(ucs);

		final ChargesDB chargesDB = new ChargesDB(context);
		RpcAsyncTask task = new RpcAsyncTask(context, uc,
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
								inStockWeigth(c,ucs,adapter);//交差调用。
								return;
							} else if (usci.isError()) {
								Toaster.show(usci.getMessage());
								return;
							}
							
							if (usci.isError()) {
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
	
	void inStockWeigth(final Charges c,final UploadChargeStoreInfo ucs,
			final Receive2MangermentAdapter adapter){
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		// ================选择计量单位布局
		final View dialogView = layoutInflater.inflate(R.layout.dialog_weight,
				null);

		// ==============计量单位控件
		final Spinner sp1 = (Spinner) dialogView.findViewById(R.id.spinnerUnit);
		final String[] mItems = context.getResources().getStringArray(
				R.array.unitList);
		ArrayAdapter<String> unitsData = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, mItems);
		sp1.setAdapter(unitsData);// 设置计量单位数据

		LinearLayout whLayout = (LinearLayout) dialogView
				.findViewById(R.id.layoutWh);
		whLayout.setVisibility(View.GONE);

		Dialog detailDialog = new AlertDialog.Builder(context)
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
								inStock(c,ucs,adapter);//交差调用。
								
							}
					
				}).create();

		detailDialog.show();
	}

}
