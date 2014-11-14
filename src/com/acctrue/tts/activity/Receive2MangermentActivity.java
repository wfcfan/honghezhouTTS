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
import com.acctrue.tts.enums.ChargesStatusEnum;
import com.acctrue.tts.enums.CodeTypeEnum;
import com.acctrue.tts.model.ChargeCodes;
import com.acctrue.tts.model.Charges;
import com.acctrue.tts.model.Warehouse;
import com.acctrue.tts.rpc.OnCompleteListener;
import com.acctrue.tts.rpc.RpcAsyncTask;
import com.acctrue.tts.tasks.TaskUtils;
import com.acctrue.tts.utils.AccountUtil;
import com.acctrue.tts.utils.DateUtil;
import com.acctrue.tts.utils.Toaster;
import com.acctrue.tts.utils.ViewUtil;

/**
 * 收取码管理
 * @author wangfeng
 *
 */
public class Receive2MangermentActivity extends Activity implements
		OnClickListener {
	private final String TAG = Receive2MangermentActivity.class.getSimpleName();
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_receive2mgmt);
		
		ViewUtil.initHeader(this, "收取码管理");
		this.init();
	}

	void init() {
		// 初始化下拉列表
		ListView lstReps = (ListView) findViewById(R.id.lstReps);
		ChargesDB db = new ChargesDB(this);
		
		List<Charges> list = db.getCharges();
		ChargeCodesDB db2 = new ChargeCodesDB(this);
		
		for (int i = 0; i < list.size(); i++) {
			Charges c = list.get(i);
			List<ChargeCodes> ccList = db2.getChargeCodes2(list.get(i).getId());
			c.setChargeCodes(ccList);
			//c.getCodes().add(UUID.randomUUID().toString());//测试使用
		}
		
		final Receive2MangermentAdapter adptRev = new Receive2MangermentAdapter(this,
				list, (CheckBox) findViewById(R.id.chkSelAll));
		Log.d(TAG, String.valueOf(adptRev.getCount()));
		lstReps.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lstReps.setAdapter(adptRev);
		lstReps.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Charges c = (Charges)adptRev.getItem(position);
				showDetail(c);
			}
			
		});
		

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

	final void showDetail(Charges c){
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		
		final View detailView = layoutInflater.inflate(R.layout.charges_detail_view, null);
		this.setViewText((TextView)detailView.findViewById(R.id.textView1), c.getBatchno());
		this.setViewText((TextView)detailView.findViewById(R.id.textView2), c.getManNo());
		this.setViewText((TextView)detailView.findViewById(R.id.textView3), c.getFarmlandNo());
		this.setViewText((TextView)detailView.findViewById(R.id.textView4), c.getProductId());
		this.setViewText((TextView)detailView.findViewById(R.id.textView5), c.getCreateDate());
		this.setViewText((TextView)detailView.findViewById(R.id.textView6), c.getMan());
		this.setViewText((TextView)detailView.findViewById(R.id.textView7), c.getPackCodeName());
		
		Dialog detailDialog = new AlertDialog.Builder(this)
		.setTitle("详情")
		.setView(detailView)
		.setPositiveButton(R.string.btn_close, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
			}
			
		})
		.create();
		detailDialog.show();
	}
	
	final void setViewText(TextView tv,String v){
		tv.setText(v);
	}

	@Override
	public void onClick(View view) {
		
		ListView lstReps = (ListView) findViewById(R.id.lstReps);
		final Receive2MangermentAdapter adpt = (Receive2MangermentAdapter) lstReps.getAdapter();
		List<Charges> dataList = null;
		
		CheckBox chkInStock = (CheckBox)findViewById(R.id.chkIn);
		//ChargesStatusEnum status = chkInStock.isChecked() ? ChargesStatusEnum.InStock : ChargesStatusEnum.Uploaded;
		
		int isAutoStorage = chkInStock.isChecked() ? 1 : 0;

		switch (view.getId()) {
		case R.id.btnUpload:
			dataList = adpt.getCheckedData();
//			if(status == ChargesStatusEnum.Uploaded)
//				uploadCharges(adpt, dataList,isAutoStorage);
//			else
//				showWarehouse(false, dataList, adpt);
			uploadCharges(adpt, dataList,isAutoStorage);
			break;
		case R.id.btnUploadAll:
			dataList = adpt.getAllDatas();
//			if(status == ChargesStatusEnum.Uploaded)
//				uploadCharges(adpt, dataList,isAutoStorage);
//			else
//				showWarehouse(false, dataList, adpt);
			uploadCharges(adpt, dataList,isAutoStorage);
			break;
		case R.id.btnDel:
			dataList = adpt.getCheckedData();
			delCharges(dataList, adpt);
			break;
		case R.id.btnRepo:
			dataList = adpt.getCheckedData();
			showWarehouse(true, dataList, adpt);
			break;
		case R.id.btnNew:
			Intent intent = new Intent(Receive2MangermentActivity.this,ChargeActivity.class);
			startActivity(intent);
			//this.finish();
			break;
		case R.id.btnBack:
			this.finish();
			break;
		}
		
	}
	
	void delCharges(List<Charges> chargesList,Receive2MangermentAdapter adpt){
		if(chargesList.size() == 0){
			Toaster.show("请先选择数据行!");
			return;
		}
		
		final ChargesDB chargesDB = new ChargesDB(this);
		for(Charges charges : chargesList){
//			if(charges.getState() == ChargesStatusEnum.Uploaded.getStateId()){
//				Toaster.show("已上传的收取不能进行删除!");
//				break;
//			}
			chargesDB.deleteCharges(charges.getId());
			adpt.RemoveItems(chargesList);
		}
	}
	
	void uploadCharges(Receive2MangermentAdapter adapter,final List<Charges> chargesList,int isAutoStorage){
		if(chargesList.size() == 0){
			Toaster.show("请先选择数据行!");
			return;
		}
		
		for(Charges charges : chargesList){
			upload(charges,adapter,isAutoStorage);//上传数据到服务器
		}
	}
	
	void showWeight(final Charges c,final Receive2MangermentAdapter adapter,final int isAutoStorage){
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
		.setTitle(c.getBatchno() + "--入库称重")
		.setView(dialogView)
		.setPositiveButton(R.string.btn_ok_tip, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				EditText editWeight = (EditText)dialogView.findViewById(R.id.editWeight);
				String strWeight = editWeight.getText().toString();
				String strUnit = (String)sp1.getSelectedItem();
				strWeight += strUnit;//完整重量

				UserInfo user = AccountUtil.getCurrentUser().getUserInfo();
				UploadChargeStoreInfo ucs = new UploadChargeStoreInfo();
				ucs.setActor(user.getUserName());
				ucs.setActorDate(DateUtil.parseDatetimeToJsonDate(c.getCreateDate()));
				ucs.setChargeCodes(c.getCodes());
				ucs.setCorpId(user.getCorpId());
				
				c.setWeight(strWeight);
				ucs.setWeight(strWeight);
				if(isAutoStorage == 1)
					upload(c,adapter,1);//自动入库，交叉调用函数
				else
					inStock(c, ucs, adapter);				
			}
			
		}).create();
		
		detailDialog.show();
			
	}
	
	void upload(final Charges c,final Receive2MangermentAdapter adapter,final int isAutoStorage){
//		if(c.getState() == ChargesStatusEnum.Uploaded.getStateId()){
//			Toaster.show(String.format("单据:%s已经上传",c.getBatchno()));
//			return;
//		}
		ChargeCodesDB db = new ChargeCodesDB(this);
		List<ChargeCodes> ccList = db.getChargeCodes2(c.getId());//获取单据下的所有扫码
		if(ccList.size() == 0){
			Toaster.show(String.format("单据：%s下没有扫码,无法进行上传!",c.getBatchno()));
			return;
		}
		
		if(TextUtils.isEmpty(c.getWeight())){//首次上传时重随便填写一个
			c.setWeight("0");
		}
		
		if(c.getIsPackCode() == CodeTypeEnum.TrackCode.getId()){
			c.setState(2);//追溯码
		}else{
			c.setState(isAutoStorage);//收取码
		}
		
		UploadChargeCodesRequest uc = new UploadChargeCodesRequest();
		uc.setSign(AccountUtil.getDefaultSign());
		uc.setInfo(c);
		
		final ChargesDB chargesDB = new ChargesDB(this);
		RpcAsyncTask task = new RpcAsyncTask(this, uc, new OnCompleteListener() {

			@Override
			public void onComplete(String content) {
				UploadChargeCodesResponse uccr = null;
				try{
					uccr = UploadChargeCodesResponse.fromJson(new JSONObject(content));
				}catch(Exception ex){
					ex.printStackTrace();
				}
				
				if(uccr != null){
					if(uccr.isError() && uccr.getMessage().startsWith("WEIGHT:")){
						showWeight(c,adapter,isAutoStorage);
						return;
					}
					else if(uccr.isError()){
						Toaster.show(uccr.getMessage());
						return;
					}
					
					
					if(isAutoStorage == 1){
						chargesDB.deleteCharges(c.getId());//删除数据
						adapter.RemoveItems(c);
					}
					else{
						chargesDB.modifyChangeState(c.getId(), ChargesStatusEnum.Uploaded);//修改上传状态
						c.setState(ChargesStatusEnum.Uploaded.getStateId());
						adapter.updateItem(c);
					}
					Toaster.show("上传成功!");
				}
			}
			
		});
		TaskUtils.execute(task, TaskUtils.POST, Constants.URL_UPLOADCHARGECODES);
	}
	
	void inStock(final Charges c,UploadChargeStoreInfo ucs,final Receive2MangermentAdapter adapter){
		UploadChargeStoreInfoRequest uc = new UploadChargeStoreInfoRequest();
		uc.setSign(AccountUtil.getDefaultSign());
		uc.setRequestInfo(ucs);
		
		final ChargesDB chargesDB = new ChargesDB(this);
		RpcAsyncTask task = new RpcAsyncTask(this, uc, new OnCompleteListener() {

			@Override
			public void onComplete(String content) {
				UploadChargeStoreInfoResponse usci = null;
				try{
					usci = UploadChargeStoreInfoResponse.fromJson(new JSONObject(content));
				}catch(Exception ex){
					ex.printStackTrace();
				}
				
				if(usci != null){
					if(usci.isError()){
						Toaster.show(usci.getMessage());
						return;
					}
					
					chargesDB.deleteCharges(c.getId());//删除数据
					adapter.RemoveItems(c);
				}
			}
		});
		TaskUtils.execute(task, TaskUtils.POST, Constants.URL_UPLOADCHARGESTOREINFO);
	}
	
	/**
	 * 农产品入库（调用接口，保存到服务器端）
	 * @param task 
	 * @param isShowWarehouse 是否需要选择仓库
	 * @param chargesList 表单
	 */
	void showWarehouse(final boolean isShowWarehouse,final List<Charges> chargesList,final Receive2MangermentAdapter adapter){
		if(chargesList.size() == 0){
			Toaster.show("请先选择数据行!");
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
		
		//仓库控件
		final Spinner spwh = (Spinner)dialogView.findViewById(R.id.spinnerWarehouse);
		ViewUtil.bindWarehouse(this, spwh);
		
		if(!isShowWarehouse){//如果点击界面上方的入库，用户不需要再选择仓库直接入库到自己所属公司。
			LinearLayout whLayout = (LinearLayout)dialogView.findViewById(R.id.layoutWh);
			whLayout.setVisibility(View.GONE);
		}
		
		Dialog detailDialog = new AlertDialog.Builder(this)
		.setTitle("入库称重")
		.setView(dialogView)
		.setPositiveButton(R.string.btn_ok_tip, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				
				EditText editWeight = (EditText)dialogView.findViewById(R.id.editWeight);
				String strWeight = editWeight.getText().toString();
				String strUnit = (String)sp1.getSelectedItem();
				strWeight += strUnit;//完整重量

				UserInfo user = AccountUtil.getCurrentUser().getUserInfo();
				Warehouse wh  = (Warehouse)spwh.getSelectedItem();
				
				for(Charges c : chargesList){
					
					UploadChargeStoreInfo ucs = new UploadChargeStoreInfo();
					ucs.setActor(user.getUserName());
					ucs.setActorDate(DateUtil.parseDatetimeToJsonDate(c.getCreateDate()));
					ucs.setChargeCodes(c.getCodes());
					
					if(isShowWarehouse)//按用户选择的仓库
						ucs.setCorpId(wh.getCorpId());
					else//直接入库到用户所公司的仓库
						ucs.setCorpId(user.getCorpId());
					
					c.setWeight(strWeight);
					ucs.setWeight(strWeight);
					inStock(c,ucs,adapter);
				}
				
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
}
