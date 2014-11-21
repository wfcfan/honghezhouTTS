package com.acctrue.tts.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.acctrue.tts.Constants;
import com.acctrue.tts.R;
import com.acctrue.tts.activity.CaptureActivity;
import com.acctrue.tts.adapter.CodesAdapter;
import com.acctrue.tts.db.ChargeCodesDB;
import com.acctrue.tts.utils.Toaster;

public class ChargeFragment2 extends Fragment implements OnClickListener {

	//private final String TAG = ChargeFragment2.class.getSimpleName();

	View root;
	ListView noList;
	List<Integer> listItemID = new ArrayList<Integer>();
	List<String> codesData;
	CodesAdapter adapter;
	Activity mAct;
	String chargeId;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mAct = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.charge_fragment2, container, true);

		Intent intent = this.getActivity().getIntent();
		final String chargesId = intent.getStringExtra("chargesId");
		if(!TextUtils.isEmpty(chargesId)){
			ChargeCodesDB ccDb = new ChargeCodesDB(this.getActivity());
			codesData =  ccDb.getChargeCodes2String(chargesId);
		}else{
			codesData = new ArrayList<String>();
		}
		
		// -----------生成测试数据
		//codesData.add("114000051292422");

		adapter = new CodesAdapter(mAct, codesData);
		// 初始化下拉列表
		noList = (ListView) root.findViewById(R.id.lstCode);
		noList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		noList.setAdapter(adapter);

		Button btndelCode = (Button) root.findViewById(R.id.btndelCode);
		btndelCode.setOnClickListener(this);

		Button btnSmq = (Button) root.findViewById(R.id.btnSmq);
		btnSmq.setOnClickListener(this);

		return root;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btndelCode:
			
			List<String> checkList = adapter.getCheckedData();
			adapter = (CodesAdapter)noList.getAdapter();
			if(checkList.size() == 0){
				Toaster.show("没有选定任何记录!");
				return;
			}
			adapter.RemoveItems(checkList);
			Toaster.show("删除成功!");
			break;
		case R.id.btnSmq:
			Intent intent = new Intent();
			intent.setClass(mAct, CaptureActivity.class);
			this.startActivityForResult(intent,
					Constants.REQCODE_SCANNIN_GREQUEST_CODE);
			break;
		}
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
			String scanResult = data.getStringExtra(Constants.EXTRA_RESULT);

			// ChargeCodesDB ccDb = new ChargeCodesDB(mAct);
			// ChargeCodes cc = new ChargeCodes();
			// cc.setChargeId(this.getChargeId());
			// cc.setCode(scanResult);
			// cc.setState(0);
			// ccDb.addChargeCode(cc);
			// Toaster.show("OK");
			
			adapter = (CodesAdapter)noList.getAdapter();
			if(adapter.getAllData().contains(scanResult)){
				Toaster.show("该码已存在!");
				return;
			}
			
			adapter.addData(scanResult);
			Toaster.show("添加成功!");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getChargeId() {
		return chargeId;
	}

	public void setChargeId(String chargeId) {
		this.chargeId = chargeId;
	}

}
