package com.acctrue.tts.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.acctrue.tts.R;
import com.acctrue.tts.adapter.FarmLandsArrayAdapter;
import com.acctrue.tts.adapter.FarmersArrayAdapter;
import com.acctrue.tts.adapter.ProductArrayAdapter;
import com.acctrue.tts.db.FarmLandsDB;
import com.acctrue.tts.db.ProductDB;
import com.acctrue.tts.serial.SPEveryDaySerialNumber;
import com.acctrue.tts.serial.SerialNumber;
import com.acctrue.tts.utils.AccountUtil;
import com.acctrue.tts.utils.DateUtil;

public class ChargeFragment1 extends Fragment implements OnClickListener {

	View root;

	Spinner spinner1, spinnerFarm, spinnerProd, spinnerCodeType;
	EditText bt;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.charge_fragment1, container, true);
		this.init();

		Button btnGenerate = (Button) root.findViewById(R.id.button1);
		btnGenerate.setOnClickListener(this);

		// ========扫码类型
		spinnerCodeType = (Spinner) root.findViewById(R.id.spinnerCodeType);
		final String[] mItems = this.getResources().getStringArray(
				R.array.codeTypes);
		ArrayAdapter<String> codeTypeData = new ArrayAdapter<String>(
				this.getActivity(), android.R.layout.simple_spinner_item,
				mItems);
		spinnerCodeType.setAdapter(codeTypeData);
		spinnerCodeType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				mListener.onCodeTypeChanged(mItems[position]);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}

		});

		// =============农户
		spinner1 = (Spinner) root.findViewById(R.id.spinner1);
		FarmLandsDB farmLandsDB = new FarmLandsDB(this.getActivity());
		
		BaseAdapter farmersData = new FarmersArrayAdapter(this.getActivity(),
				farmLandsDB.getFarmersList());
		spinner1.setAdapter(farmersData);

		// ============农田
		spinnerFarm = (Spinner) root.findViewById(R.id.spinnerFarm);
		BaseAdapter farmLandsData = new FarmLandsArrayAdapter(
				this.getActivity(), farmLandsDB.getFarmLandsList());
		spinnerFarm.setAdapter(farmLandsData);

		// ===============产品
		spinnerProd = (Spinner) root.findViewById(R.id.spinnerProd);
		ProductDB productDB = new ProductDB(this.getActivity());
		BaseAdapter prodData = new ProductArrayAdapter(this.getActivity(),
				productDB.getProducts());
		spinnerProd.setAdapter(prodData);

		bt = (EditText) root.findViewById(R.id.editText1);
		bt.setText(getSerialNumber());
		return root;
	}
	
	private void init() {
		TextView dtView = (TextView) root.findViewById(R.id.txt_shouquRQ);
		dtView.setText(DateUtil.getDate());

		TextView userView = (TextView) root.findViewById(R.id.txt_shouquren);
		userView.setText(AccountUtil.getCurrentUser().getUserInfo().getUserName());
	}

	String getSerialNumber() {
		SerialNumber batchNum = new SPEveryDaySerialNumber(5);
		return batchNum.getSerialNumber();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.button1:
			bt.setText(getSerialNumber());
			break;
		}
	}

	public interface OnCodeTypeChangeListener {
		void onCodeTypeChanged(String text);
	}

	OnCodeTypeChangeListener mListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnCodeTypeChangeListener) activity;
		} catch (Exception ex) {
			throw new ClassCastException(activity.toString()
					+ "must implement OnCodeTypeChangeListener");
		}
	}

}
