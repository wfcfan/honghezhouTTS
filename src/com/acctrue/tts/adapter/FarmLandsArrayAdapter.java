package com.acctrue.tts.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.acctrue.tts.R;
import com.acctrue.tts.model.FarmLands;

public class FarmLandsArrayAdapter extends BaseAdapter{
	private List<FarmLands> mList;
	private Context mContext;

	public FarmLandsArrayAdapter(Context context, List<FarmLands> objects) {
		this.mContext = context;
		this.mList = objects;
	}

	static public void setSpinnerItemSelectedById(Spinner spinner, String id) {
		FarmLandsArrayAdapter adapter = (FarmLandsArrayAdapter) spinner
				.getAdapter();
		for (int i = 0; i < adapter.getCount(); i++) {
			FarmLands p = (FarmLands) adapter.getItem(i);
			if (p.getFarmLandCode().equals(id)) {
				spinner.setSelection(i, true);
				break;
			}
		}
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final FarmLands farm = (FarmLands) this.getItem(position);
		LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
		convertView = _LayoutInflater.inflate(R.layout.item, null);
		if (convertView != null) {
			TextView t1 = (TextView) convertView.findViewById(R.id.textView1);						
			t1.setText(farm.getFarmLandName());

			TextView t2 = (TextView) convertView.findViewById(R.id.textView2);
			t2.setVisibility(View.GONE);
//			t2.setText("(" + farm.getFarmLandCode() + ")");
		}
		return convertView;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int i) {
		return mList.get(i);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
