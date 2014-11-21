package com.acctrue.tts.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.acctrue.tts.Constants;
import com.acctrue.tts.R;
import com.acctrue.tts.db.ChargeStoreInDB;
import com.acctrue.tts.dto.UploadChargeStoreInfoRequest;
import com.acctrue.tts.dto.UploadChargeStoreInfoResponse;
import com.acctrue.tts.dto.UploadChargeStoreInfoRequest.UploadChargeStoreInfo;
import com.acctrue.tts.model.ChargeStoreIn;
import com.acctrue.tts.model.ChargeStoreInCode;
import com.acctrue.tts.model.Warehouse;
import com.acctrue.tts.rpc.OnCompleteListener;
import com.acctrue.tts.rpc.RpcAsyncTask;
import com.acctrue.tts.tasks.TaskUtils;
import com.acctrue.tts.utils.AccountUtil;
import com.acctrue.tts.utils.DateUtil;
import com.acctrue.tts.utils.Toaster;
import com.acctrue.tts.utils.ViewUtil;

/**
 * 收取入库
 * 
 * @author peng
 * 
 */
public class Receive2Repository extends Activity implements OnClickListener {

	EditText etCode;
	Spinner spWh;
	ListView ctrlCodes;
	final List<String> codes = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_receive2repository);

		ViewUtil.initHeader(this, "收取入库");
		init();
	}

	private void init() {
		etCode = (EditText) this.findViewById(R.id.edtDel);
		// 初始化仓库下拉框
		spWh = (Spinner) findViewById(R.id.lstRepos);
		ViewUtil.bindWarehouse(this, spWh);

		// 返回
		findViewById(R.id.btnBack).setOnClickListener(this);
		// 扫码
		findViewById(R.id.btnScan).setOnClickListener(this);
		// 确定
		findViewById(R.id.btnSave).setOnClickListener(this);
		// 入库
		findViewById(R.id.btnRepo).setOnClickListener(this);
		// 删除
		findViewById(R.id.btnDel).setOnClickListener(this);
		// 添加
		findViewById(R.id.btnAdd).setOnClickListener(this);
		
		Intent intent = getIntent(); 
		int wid = intent.getIntExtra("wid", 0);
		if(wid != 0){
			;
		}
		
		String id = this.getIntent().getStringExtra("id");
		if(!TextUtils.isEmpty(id)){
			ChargeStoreInDB db = new ChargeStoreInDB(this);
			List<ChargeStoreInCode> ciclist = db.getChargeStoreInCode(id);
			for(ChargeStoreInCode cic  : ciclist){
				codes.add(cic.getCode());
			}
		}
		
		ArrayAdapter<String> adptRev = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_checked, codes);
		ctrlCodes = (ListView) findViewById(R.id.noList);
		ctrlCodes.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		ctrlCodes.setAdapter(adptRev);
	}
	
	

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btnBack:
			finish();
			break;
		case R.id.btnScan:
			Intent intent = new Intent();
			intent.setClass(Receive2Repository.this, CaptureActivity.class);
			startActivityForResult(intent,
					Constants.REQCODE_SCANNIN_GREQUEST_CODE);
			break;
		case R.id.btnAdd:
			addCode(etCode.getText().toString());
			etCode.setText("");
			break;
		case R.id.btnSave:
			save2db();
			break;
		case R.id.btnRepo:
			uploadData();
			break;
		case R.id.btnDel:
			delCode();
			break;
		}
	}
	
	void uploadData(){
		if(codes.size() == 0){
			Toaster.show("请先添加数据项!");
			return;
		}
		
		
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		//================选择计量单位布局
		final View dialogView = layoutInflater.inflate(R.layout.dialog_weight, null);
		
		//==============计量单位控件
		final Spinner sp1 = (Spinner)dialogView.findViewById(R.id.spinnerUnit);
		final String[] mItems = this.getResources().getStringArray(R.array.unitList);
		ArrayAdapter<String> unitsData = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item,
				mItems);
		sp1.setAdapter(unitsData);//设置计量单位数据
		
		LinearLayout whLayout = (LinearLayout)dialogView.findViewById(R.id.layoutWh);
		whLayout.setVisibility(View.GONE);
		
		Dialog detailDialog = new AlertDialog.Builder(this)
		.setTitle("称重")
		.setView(dialogView)
		.setPositiveButton(R.string.btn_ok_tip, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				
				EditText editWeight = (EditText)dialogView.findViewById(R.id.editWeight);
				String strWeight = editWeight.getText().toString();
				String strUnit = (String)sp1.getSelectedItem();
				strWeight += strUnit;//完整重量
				
				ChargeStoreIn csi = new ChargeStoreIn();
				csi.setActDate(DateUtil.getDatetime());
				csi.setActor(AccountUtil.getCurrentUser().getUserInfo().getUserName());
				csi.setId(UUID.randomUUID().toString());
				Warehouse wh = (Warehouse)spWh.getSelectedItem();
				csi.setWarehouseId(wh.getWarehouseId());
				csi.setWarehouseName(wh.getWarehouseName());
				uploadData(strWeight,csi);
			}
			
		})
		.setNegativeButton(R.string.cancel,  new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
			}
			
		})
		.create();
		detailDialog.show();
	}
	
	void uploadData(String weigth,final ChargeStoreIn us){
		
		UploadChargeStoreInfoRequest uc = new UploadChargeStoreInfoRequest();
		uc.setSign(AccountUtil.getDefaultSign());
		
		UploadChargeStoreInfo ucs = new UploadChargeStoreInfo();
		ucs.setActor(us.getActor());
		ucs.setActorDate(DateUtil.parseDatetimeToJsonDate(us.getActDate()));
		ucs.setCorpId(us.getWarehouseId());
		ucs.setChargeCodes(codes);
		ucs.setWeight(weigth);
		uc.setRequestInfo(ucs);
		
		RpcAsyncTask task = new RpcAsyncTask(this, uc, new OnCompleteListener() {

			@Override
			public void onComplete(String content) {
				UploadChargeStoreInfoResponse response = null;
				try{
					response = UploadChargeStoreInfoResponse.fromJson(new JSONObject(content));
				}catch(Exception ex){
					ex.printStackTrace();
				}
				
				if(response != null){
					if(response.isError()){
						Toaster.show(response.getMessage());
						return;
					}
					codes.clear();
					refreshListView();
					Toaster.show("已全部入库成功!");
				}
			}
		});
		
		TaskUtils.execute(task, TaskUtils.POST, Constants.URL_UPLOADCHARGESTOREINFO);
	}
	
	void save2db(){
		if(codes.size() == 0){
			Toaster.show("列表为空,无法保存!");
			return;
		}
		
		ChargeStoreInDB db = new ChargeStoreInDB(this);
		
		ChargeStoreIn csi = new ChargeStoreIn();
		csi.setId(UUID.randomUUID().toString());
		csi.setActDate(DateUtil.getDatetime());
		csi.setActor(AccountUtil.getCurrentUser().getUserInfo().getUserName());
		Warehouse wh = (Warehouse)spWh.getSelectedItem();
		csi.setWarehouseId(wh.getWarehouseId());
		csi.setWarehouseName(wh.getWarehouseName());
		
		ChargeStoreInCode[] csArr = new ChargeStoreInCode[codes.size()];
		for (int i = 0; i < codes.size(); i++) {
			csArr[i] = new ChargeStoreInCode(); 
			csArr[i].setIsStoreIn(0);
			csArr[i].setStoreInId(csi.getId());
			csArr[i].setCode(codes.get(i));
		}
		
		db.addChargeStoreIn(csi, csArr);
		
		Toaster.show("保存成功!");
		finish();
	}

	@SuppressWarnings("unchecked")
	void refreshListView() {
		ArrayAdapter<String> adpt = (ArrayAdapter<String>) ctrlCodes
				.getAdapter();
		adpt.notifyDataSetChanged();
	}

	void addCode(String code) {
		if (TextUtils.isEmpty(code)) {
			Toaster.show("码不能为空!");
			return;
		}

		if (!codes.contains(code)) {
			codes.add(code);
			this.refreshListView();
		} else {
			Toaster.show("该码已存在!");
		}
	}

	void delCode() {
		if(ctrlCodes.getCheckedItemCount() == 0){
			Toaster.show("请先选择数据项!");
			return;
		}
		
		for (int i = 0; i < ctrlCodes.getCount(); i++) {
			if (ctrlCodes.isItemChecked(i)) {
				codes.remove(ctrlCodes.getItemAtPosition(i).toString());
			}
		}
		this.refreshListView();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (resultCode != Activity.RESULT_OK) {
				return;
			}
			if (requestCode != Constants.REQCODE_SCANNIN_GREQUEST_CODE) {
				return;
			}
			String scanResult = data.getStringExtra(Constants.EXTRA_RESULT);
			addCode(scanResult);
			//etCode.setText(scanResult);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}