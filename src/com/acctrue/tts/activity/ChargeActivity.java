package com.acctrue.tts.activity;

import java.util.List;
import java.util.UUID;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import com.acctrue.tts.R;
import com.acctrue.tts.adapter.CodesAdapter;
import com.acctrue.tts.db.ChargeCodesDB;
import com.acctrue.tts.db.ChargesDB;
import com.acctrue.tts.enums.ChargesStatusEnum;
import com.acctrue.tts.enums.CodeTypeEnum;
import com.acctrue.tts.fragment.ChargeFragment1;
import com.acctrue.tts.fragment.ChargeFragment2;
import com.acctrue.tts.model.ChargeCodes;
import com.acctrue.tts.model.Charges;
import com.acctrue.tts.model.Farmers;
import com.acctrue.tts.model.Product;
import com.acctrue.tts.utils.AccountUtil;
import com.acctrue.tts.utils.DateUtil;
import com.acctrue.tts.utils.Toaster;
import com.acctrue.tts.utils.ViewUtil;

/**
 * 农产品收取
 * 
 * @author wangfeng
 * 
 */
public class ChargeActivity extends FragmentActivity implements
		OnClickListener, ChargeFragment1.OnCodeTypeChangeListener {

	TabHost mTabHost;
	private final String TAG = ChargeActivity.class.getSimpleName();

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_charge);

		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();

		View tabView0 = LayoutInflater.from(this).inflate(R.layout.com_tabmini,
				null);
		TextView tv0 = (TextView) tabView0.findViewById(R.id.tabLblTab);
		tv0.setText(R.string.tab_fragment_tip1);

		View tabView1 = LayoutInflater.from(this).inflate(R.layout.com_tabmini,
				null);
		TextView tv1 = (TextView) tabView1.findViewById(R.id.tabLblTab);
		tv1.setText(R.string.tab_fragment_tip2);

		mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator(tabView0)
				.setContent(R.id.tab1));
		mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator(tabView1)
				.setContent(R.id.tab2));

		// TabWidget tabWidget = mTabHost.getTabWidget();
		// for (int i = 0; i < tabWidget.getChildCount(); i++) {
		// View child = tabWidget.getChildAt(i);
		// final TextView tv = (TextView) child
		// .findViewById(android.R.id.title);
		// RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv
		// .getLayoutParams();
		// params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0); // 取消文字底边对齐
		// params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		// // 设置文字居中对齐
		// child.getLayoutParams().height = 60;
		// final ImageView iv = (ImageView) tabWidget.getChildAt(i)
		// .findViewById(android.R.id.icon);
		// iv.setVisibility(View.GONE);
		// }

		Button btnBack = (Button) this.findViewById(R.id.btnlogout);
		btnBack.setOnClickListener(this);

		Button btnOK = (Button) this.findViewById(R.id.btnOk);
		btnOK.setOnClickListener(this);

		Button btnCancel = (Button) this.findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(this);

		ViewUtil.initHeader(this, "农产品收取");
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btnlogout:
		case R.id.btnCancel:
		case R.id.lblUser:
			finish();
			break;
		case R.id.btnOk:
			this.saveData();
			break;
		}
	}

	private void saveData() {
		String newId = UUID.randomUUID().toString();
		Charges charges = new Charges();
		charges.setId(newId);

		ChargeFragment1 cf1 = (ChargeFragment1) getFragmentManager()
				.findFragmentById(R.id.tab1);
		View rooViewF1 = cf1.getView();
		
		//==================禁用批号
//		EditText editText1 = (EditText) rooViewF1.findViewById(R.id.editText1);
//		charges.setBatchno(editText1.getText().toString());

		Spinner sct = (Spinner) rooViewF1.findViewById(R.id.spinnerCodeType);
		String ct = sct.getSelectedItem().toString();
		CodeTypeEnum codeType = CodeTypeEnum.getCodeTypeEnumByName(ct);
		charges.setIsPackCode(codeType.getId());

		Spinner sman = (Spinner) rooViewF1.findViewById(R.id.spinner1);
		Farmers farmers = (Farmers) sman.getSelectedItem();
		charges.setManNo(farmers.getFarmerId());
		charges.setManName(farmers.getFarmerName());

		//--禁用农田
//		Spinner stian = (Spinner) rooViewF1.findViewById(R.id.spinnerFarm);
//		FarmLands farmlads = (FarmLands) stian.getSelectedItem();
//		charges.setFarmlandNo(farmlads.getFarmLandCode());
//		charges.setFarmlandName(farmlads.getFarmLandName());
		
		Spinner sprod = (Spinner) rooViewF1.findViewById(R.id.spinnerProd);
		Product prd = (Product) sprod.getSelectedItem();
		charges.setProductId(String.valueOf(prd.getProductId()));
		charges.setProductName(prd.getProductName());
		
		charges.setMan(AccountUtil.getCurrentUser().getUserInfo().getUserName());
		charges.setCreateDate(DateUtil.getDatetime());
		charges.setState(ChargesStatusEnum.Init.getStateId());

		// =====================================收取码列表
		ChargeFragment2 cf2 = (ChargeFragment2) getFragmentManager()
				.findFragmentById(R.id.tab2);
		View rooViewF2 = cf2.getView();

		ListView listView = (ListView) rooViewF2.findViewById(R.id.lstCode);
		CodesAdapter adapter = (CodesAdapter) listView.getAdapter();
		Log.d(TAG, String.valueOf(adapter.getCount()));

		ChargeCodesDB ccDb = new ChargeCodesDB(this);
		List<String> codeList = adapter.getAllData();
		if (codeList.size() == 0) {
			Toaster.show("扫码数据为空,无法提交!");
			return;
		}

		Intent intent = this.getIntent();
		String chargesId = intent.getStringExtra("chargesId");

		ChargesDB cdb = new ChargesDB(this);
		if (TextUtils.isEmpty(chargesId)) {
			cdb.addCharges(charges);// 保存单据
			for (String s : codeList) {
				ChargeCodes cc = new ChargeCodes();
				cc.setChargeId(newId);
				cc.setCode(s);
				cc.setState(0);
				ccDb.addChargeCode(cc);
			}
		} else {
			charges.setId(chargesId);
			cdb.updateCharges(charges);
			ccDb.updateChargeCode(chargesId, codeList);
		}

		Toaster.show("保存成功!");

		finish();// 返回主界面
	}

	boolean isNew() {
		Intent intent = this.getIntent();
		String chargesId = intent.getStringExtra("chargesId");
		return TextUtils.isEmpty(chargesId);
	}

	/**
	 * 实现与fragment通信接口的方法
	 */
	@Override
	public void onCodeTypeChanged(String text) {
		Log.d(TAG, text);
	}

}
