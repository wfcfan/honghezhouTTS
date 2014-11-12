package com.acctrue.tts.activity;

import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.acctrue.tts.R;
import com.acctrue.tts.adapter.StoreTypesAdapter;
import com.acctrue.tts.db.BizCorpsDB;
import com.acctrue.tts.db.StoreDB;
import com.acctrue.tts.db.StoreTypeDB;
import com.acctrue.tts.dto.LoginResponse.UserInfo;
import com.acctrue.tts.enums.StoreSource;
import com.acctrue.tts.model.BizCorp;
import com.acctrue.tts.model.Store;
import com.acctrue.tts.model.StoreTypes;
import com.acctrue.tts.serial.SPEveryDaySerialNumber;
import com.acctrue.tts.serial.SerialNumber;
import com.acctrue.tts.utils.AccountUtil;
import com.acctrue.tts.utils.DateUtil;
import com.acctrue.tts.utils.Toaster;
import com.acctrue.tts.utils.ViewUtil;

/**
 * 直接出库
 * 
 * @author peng
 * 
 */
public class DirectOutStoreActivity extends Activity implements OnClickListener {
	TextView editOrderNo;
	EditText editQueryCorp;
	ListView lstCorp;
	List<StoreTypes> storeTypeList = null;
	ArrayAdapter<String> adpt = null;
	Spinner ctrlStores;
	StoreDB db;
	UserInfo u;//当前用户
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_direct_out_store);
		
		u = AccountUtil.getCurrentUser().getUserInfo();
		db = new StoreDB(this);
		ViewUtil.initHeader(this, "直接出库");
		this.init();

		findViewById(R.id.btnGen).setOnClickListener(this);
		findViewById(R.id.btnQuery).setOnClickListener(this);
		findViewById(R.id.btnConfirm).setOnClickListener(this);
		findViewById(R.id.btnBack).setOnClickListener(this);
	}
	
	void init() {
		editOrderNo = (TextView) findViewById(R.id.editOrderNo);
		editOrderNo.setText(getSerialNumber());
		
		editQueryCorp = (EditText) findViewById(R.id.editQueryCorp);
		lstCorp = (ListView) findViewById(R.id.lstCorp);
		lstCorp.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		// 初始化单据类型下拉框
		ctrlStores = (Spinner) findViewById(R.id.lstStoreType);
		final StoreTypeDB storeTypesDB = new StoreTypeDB(this);
		// 出库
		storeTypeList = storeTypesDB.getAllTypeByType(1);
		StoreTypesAdapter adptData = new StoreTypesAdapter(this,storeTypeList);
		ctrlStores.setAdapter(adptData);
		ctrlStores.setSelection(1);//默认为销售出库
	}
	
	String getSerialNumber() {
		SerialNumber batchNum = new SPEveryDaySerialNumber(5);
		return batchNum.getSerialNumber();
	}
	
	StoreTypes getSelStoreTypes() {
		return (StoreTypes)ctrlStores.getSelectedItem();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnGen:
			editOrderNo.setText(getSerialNumber());
			break;
		case R.id.btnQuery:
			query();
			break;
		case R.id.btnConfirm:
			gotoScanCode();
			break;
		case R.id.btnBack:
			finish();
			break;
		}
	}

	private void gotoScanCode() {
		String storeNo = editOrderNo.getText().toString();
		if (TextUtils.isEmpty(storeNo)) {
			Toaster.show(R.string.warnning_gen_orderno);
			return;
		}

		if (lstCorp.getCount() == 0) {
			Toaster.show(R.string.warnning_sel_corp);
			return;
		}

		final int pos = lstCorp.getCheckedItemPosition();
		if (pos == ListView.INVALID_POSITION) {
			Toaster.show(R.string.warnning_sel_corp);
			return;
		}

		Intent intent = new Intent(this, OrderScanActivity.class);
		// 保存单据信息
		BizCorpsDB corpDB = new BizCorpsDB(this);
		BizCorp corp = corpDB.queryCorpByName(adpt.getItem(pos));//选择的往来企业
		if(db.getStoreByNo(storeNo) != null){
			Toaster.show("该订单已存在!");
			return;
		}
		
		Store newStore = getDefaultStore(storeNo,corp);
		db.addStore(newStore);
		intent.putExtra("order_id", newStore.getStoreId());
		startActivity(intent);
	}
	
	Store getDefaultStore(String storeNo,BizCorp corp){
		
		Store store = new Store();
		store.setStoreId(UUID.randomUUID().toString());
		store.setStoreNo(storeNo);
		
		StoreTypes st = this.getSelStoreTypes();
		store.setStoreType(st.getStoreSort());
		store.setStoreTypeText(st.getStoreTypeText());
		store.setStoreStatus(0);
		store.setStoreStatusText("未扫码");
		store.setStoreKind(st.getStoreKind());
		store.setStoreMan(u.getUserName());
		store.setStoreDate(DateUtil.parseDatetimeToJsonDate2());
		store.setCorpId(u.getCorpId());
		store.setCorpCode(u.getCorpCode());
		store.setCorpName(u.getCorpName());
		store.setBizCorpId(corp.getCorpId());
		store.setBizCorpCode(corp.getCorpCode());
		store.setBizCorpName(corp.getCorpName());
		store.setDescription("非任务性订单");
		store.setCreateTime(DateUtil.getDatetime());
		store.setSource(StoreSource.Local);
		return store;
	}

	private void query() {

		BizCorpsDB db = new BizCorpsDB(this);
		List<String> nameList = db.queryCorpNameByName(editQueryCorp.getText()
				.toString().trim());
		if (adpt == null) {
			adpt = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_checked, nameList);
			lstCorp.setAdapter(adpt);
		} else {
			adpt.clear();
			adpt.addAll(nameList);
			adpt.notifyDataSetChanged();
		}
		
		if(adpt.getCount() == 0){
			Toaster.show("查无结果!");
			return;
		}

	}
}
