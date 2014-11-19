package com.acctrue.tts.activity;

import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.acctrue.tts.Constants;
import com.acctrue.tts.R;
import com.acctrue.tts.adapter.StockCodeListAdapter;
import com.acctrue.tts.db.StoreDB;
import com.acctrue.tts.dto.ScanCodeRequest;
import com.acctrue.tts.dto.ScanCodeRequest.ScanCode;
import com.acctrue.tts.dto.ScanCodeRequest.ScanInfo;
import com.acctrue.tts.dto.ScanCodeResponse;
import com.acctrue.tts.dto.ScanRemoveRequest;
import com.acctrue.tts.dto.ScanRemoveResponse;
import com.acctrue.tts.dto.ScanVerifyRequest;
import com.acctrue.tts.dto.ScanVerifyResponse;
import com.acctrue.tts.model.Store;
import com.acctrue.tts.model.StoreCode;
import com.acctrue.tts.model.StoreItem;
import com.acctrue.tts.rpc.OnCompleteListener;
import com.acctrue.tts.rpc.RpcAsyncTask;
import com.acctrue.tts.tasks.TaskUtils;
import com.acctrue.tts.testdata.TestAPI;
import com.acctrue.tts.utils.AccountUtil;
import com.acctrue.tts.utils.DateUtil;
import com.acctrue.tts.utils.Toaster;
import com.acctrue.tts.utils.ViewUtil;
/**
 * 扫码
 * @author peng
 *
 */
public class OrderScanActivity extends FragmentActivity implements OnClickListener{
	TabHost mTabHost;
	TextView lblOrderInfo;
	TextView lblCount;
	StockCodeListAdapter adptCode;
	StockCodeListAdapter adptDelCode;
	String storeId; //当前任务单ID
	StoreDB db;
	Store currentStore; //当前任务订单
	int currentProgress = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_order_scan);
		ViewUtil.initHeader(this, "扫码");
		initTabHost();
		
		Intent intent = getIntent();
		storeId = intent.getExtras().getString("order_id");
		db = new StoreDB(this);
		currentStore = db.getStore(storeId);
		
		init();
		
		findViewById(R.id.btnScan).setOnClickListener(this);
		findViewById(R.id.btnDel).setOnClickListener(this);
		findViewById(R.id.btnOk).setOnClickListener(this);
		findViewById(R.id.btnback).setOnClickListener(this);
	}

	private void initTabHost(){
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();

		View tabView0 = LayoutInflater.from(this).inflate(R.layout.com_tabmini,	null);
		TextView tv0 = (TextView) tabView0.findViewById(R.id.tabLblTab);
		tv0.setText(R.string.orderinfo_tip);

		View tabView1 = LayoutInflater.from(this).inflate(R.layout.com_tabmini,null);
		TextView tv1 = (TextView) tabView1.findViewById(R.id.tabLblTab);
		tv1.setText(R.string.tab_fragment_tip2);
		
		View tabView2 = LayoutInflater.from(this).inflate(R.layout.com_tabmini,null);
		TextView tv2 = (TextView) tabView2.findViewById(R.id.tabLblTab);
		tv2.setText(R.string.delcodelist_tip);

		mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator(tabView0)
				.setContent(R.id.tab1));
		mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator(tabView1)
				.setContent(R.id.tab2));
		mTabHost.addTab(mTabHost.newTabSpec("tab3").setIndicator(tabView2)
				.setContent(R.id.tab3));
	}

	private void init(){
		//显示码数量
		lblCount = (TextView)findViewById(R.id.lblCount);
		
		//初始化单据信息
		lblOrderInfo = (TextView)findViewById(R.id.lblOrderInfo);
		StringBuilder sb = new StringBuilder();
		sb.append("单据号：" + currentStore.getStoreNo());
		sb.append("\n单据类型：" + currentStore.getStoreTypeText());
		sb.append("\n创建时间：" + currentStore.getStoreDateText());
		sb.append("\n往来企业：" + currentStore.getBizCorpName());
		
		currentProgress = db.getAmount(storeId);
		for(StoreItem item : currentStore.getItem()){
			sb.append("\n  产品名称：" + item.getProductName());
			sb.append("\n  产品编码：" + item.getProductCode());
			sb.append("\n    扫描最小包装进度："+ String.format("%d/%d", currentProgress , item.getAmount()));
			sb.append("\n    任务量：" + item.getDisplayAmount() + item.getDisplayProductUnit());
			sb.append("\n");
		}
		lblOrderInfo.setText(sb.toString());
		
		//初始化码列表
		List<StoreCode> codeList = db.getStoreCodes(storeId, StoreCode.DELETE_FALSE);
		//codeList.add(TestAPI.getStoreCode(storeId));
		//codeList.add(TestAPI.getStoreCode(storeId));
		
		adptCode = new StockCodeListAdapter(this,codeList);
		ListView packList = (ListView)findViewById(R.id.orderList);
		packList.setAdapter(adptCode);
		
		//初始化删除码列表
		codeList =  db.getStoreCodes(storeId, StoreCode.DELETE_TRUE);
		adptDelCode = new StockCodeListAdapter(this,codeList,true);
		ListView delPackList = (ListView)findViewById(R.id.orderDelList);
		delPackList.setAdapter(adptDelCode);
		
		setcodeCount();
	}
	
	void setcodeCount(){
		lblCount.setText(adptCode.getCount() + "/" + adptCode.getCount());
	}
	
	static final int TYPE_ADD = 0;
	static final int TYPE_SUBTRACTION = 1;
	
	void codeProgress(String code,int currentAmount,int algorithm){
		
		String t = lblOrderInfo.getText().toString();
		Log.i("lblOrderInfo_old", t);
		int ca = 0;
		if(algorithm == TYPE_ADD)
			ca = currentProgress + currentAmount;
		else
			ca = currentProgress - currentAmount;
		String old = repProgressStr(code, currentProgress);
		Log.i("lblOrderInfo_new", old);
		t = t.replace(old,repProgressStr(code, ca));
		currentProgress = ca;
		lblOrderInfo.setText(t);
		setcodeCount();
	}
	
	String repProgressStr(String code,int p){
		return String.format("产品编码：%s\n    扫描最小包装进度：%d/", code, p);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnScan:
			Intent intent = new Intent(this,CaptureActivity.class);
			this.startActivityForResult(intent,
					Constants.REQCODE_SCANNIN_GREQUEST_CODE);
			break;
		case R.id.btnDel:
			delPackCode();
			break;
		case R.id.btnback:
			finish();
			break;
		case R.id.btnOk:
			submit();			
			break;
		}
		
	}

	private void delPackCode(){
		
		List<StoreCode> list = adptCode.getCheckedData();
		if(list.size() == 0){
			Toast.makeText(this, getResources().getString(R.string.warnning_need_sel_data), Toast.LENGTH_LONG).show();
			return;
		}
		
		for(final StoreCode storeCode : list){
			ScanRemoveRequest sr = new ScanRemoveRequest();
			sr.setSign(AccountUtil.getDefaultSign());
			
			ScanCode sc = new ScanCode();
			sc.setActTime(DateUtil.parseDatetimeToJsonDate());
			sc.setActor(AccountUtil.getCurrentUser().getUserInfo().getUserName());
			sc.setByParent(false);
			sc.setCodeId(storeCode.getCodeId());
			sr.setCode(sc);
			
			ScanInfo si = new ScanInfo();
			si.setCorpId(currentStore.getCorpId());
			si.setStoreId(currentStore.getStoreId());
			sr.setInfo(si);
			Log.i("ScanRemoveRequest", sr.toJson());
			RpcAsyncTask task = new RpcAsyncTask(this,sr,new OnCompleteListener(){
	
				@Override
				public void onComplete(String content) {
					ScanRemoveResponse sr = null;
					try{
						sr = ScanRemoveResponse.fromJson(new JSONObject(content));
					}catch(Exception ex){
						ex.printStackTrace();
					}
					
					if(sr != null){
						if(sr.isError()){
							Toaster.show(sr.getMessage());
							return;
						}
						
						db.deleteStoreCode(storeCode);
						codeProgress(sr.getProductCode(), sr.getCurrentAmount(),TYPE_SUBTRACTION);
						adptCode.RemoveItems(storeCode);//从码表中移除
						adptDelCode.addData(storeCode);//添加到删除码表中
					}
				}
				
			});
			
			TaskUtils.execute(task, TaskUtils.POST, Constants.URL_SCANREMOVE);
		}
	}
	
	private void submit(){
		
		List<StoreCode> lst = adptCode.getAllData();
		int size = lst.size();
//		if(size == 0){
//			Toaster.show("请扫码后再提交");
//			return;
//		}
		
		if(size > 0){
			db.addStoreCode(adptCode.getAllData());//保存新增数据
			db.addStoreCode(adptDelCode.getAllData());//保存删除数据的状态
			Toaster.show("保存成功");
		}
		finish();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (resultCode != Activity.RESULT_OK) {
				return;
			}
			if (requestCode != Constants.REQCODE_SCANNIN_GREQUEST_CODE) {
				return;
			}
			
			//添加
			String scanResult = data.getStringExtra(Constants.EXTRA_RESULT);
			scanCode(scanResult);		

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void scanCode(final String code){
		final ScanCodeRequest scRequest = new ScanCodeRequest();
		scRequest.setSign(AccountUtil.getDefaultSign());
		
		ScanCode sc = new ScanCode();
		sc.setActTime(DateUtil.parseDatetimeToJsonDate());
		sc.setActor(AccountUtil.getCurrentUser().getUserInfo().getUserName());
		sc.setByParent(false);
		sc.setCodeId(code);
		scRequest.setCode(sc);
		
		ScanInfo si = new ScanInfo();
		si.setBizCorpId(currentStore.getBizCorpId());
		si.setCorpId(currentStore.getCorpId());
		si.setStoreId(currentStore.getStoreId());
		si.setStoreType(currentStore.getStoreType());
		
		scRequest.setInfo(si);
		
		Log.i("ScanCodeRequest", scRequest.toJson());
		
		RpcAsyncTask task = new RpcAsyncTask(this,scRequest,new OnCompleteListener(){

			@Override
			public void onComplete(String content) {
				try{
					Log.i("ScanCodeResponse", content);
					final ScanCodeResponse uc = ScanCodeResponse.fromJson(new JSONObject(content));
					
					String msg = String.format("该码%s已经扫描过,是否继续增加?", code);
					//====================没有失败，但是有请求信息需要客户端确认
					if(!uc.isError() && !TextUtils.isEmpty(uc.getMessage())){
						Dialog confirmDialog = new AlertDialog.Builder(OrderScanActivity.this)
						.setTitle(R.string.prompt)
						.setMessage(msg)
						.setPositiveButton(R.string.btn_ok_tip, new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface dialog, int arg1) {
								scanCodeVerify(code,uc);
							}
							
						})
						.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface dialog, int arg1) {
								dialog.dismiss();
							}
							
						})
						.create();
						confirmDialog.show();
						return;
					}
					
					if(uc.isError()) {
						Toaster.show(uc.getMessage());
					}else{
						//scanCodeVerify(code,uc);//保存扫码
						StoreCode sc = uc.toStoreCode(storeId);
						db.addStoreCode(sc);
						adptCode.addData(sc);
						codeProgress(uc.getProductCode(), uc.getCurrentAmount(),TYPE_ADD);
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
			
		});
		TaskUtils.execute(task, TaskUtils.POST, Constants.URL_SCANCODE);
	}
	
	private void scanCodeVerify(final String code,final ScanCodeResponse savePackCode){
		
		final ScanVerifyRequest sv = new ScanVerifyRequest();
		sv.setSign(AccountUtil.getDefaultSign());
		
		ScanCode sc = new ScanCode();
		sc.setActTime(DateUtil.parseDatetimeToJsonDate());
		sc.setActor(AccountUtil.getCurrentUser().getUserInfo().getUserName());
		sc.setByParent(false);
		sc.setCodeId(code);
		sv.setCode(sc);
		
		ScanInfo si = new ScanInfo();
		si.setBizCorpId(currentStore.getBizCorpId());
		si.setCorpId(currentStore.getCorpId());
		si.setStoreId(currentStore.getStoreId());
		si.setStoreType(currentStore.getStoreType());
		sv.setInfo(si);
		Log.i("ScanVerifyRequest", sv.toJson());
		//====================在线验证扫码
		RpcAsyncTask scanVerifyTask = new RpcAsyncTask(this,sv,new OnCompleteListener(){

			@Override
			public void onComplete(String content) {
				ScanVerifyResponse svr = null;
				try{
					svr = ScanVerifyResponse.fromJson(new JSONObject(content));
				}catch(Exception ex){
					ex.printStackTrace();
				}
				
				if(svr != null){
					if(svr.isError()){
						Toaster.show(svr.getMessage());
						return;
					}
					
					StoreCode sc = savePackCode.toStoreCode(storeId);
					db.addStoreCode(sc);
					adptCode.addData(sc);
					codeProgress(sc.getProductCode(), sc.getCurrentAmount(),TYPE_ADD);
				}
			}
			
		});
		TaskUtils.execute(scanVerifyTask, TaskUtils.POST, Constants.URL_SCANVERIFY);
	}
}

