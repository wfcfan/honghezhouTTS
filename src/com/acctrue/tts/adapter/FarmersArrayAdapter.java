package com.acctrue.tts.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.acctrue.tts.R;
import com.acctrue.tts.model.Farmers;

public class FarmersArrayAdapter extends BaseAdapter{
	private List<Farmers> mList;
	private Context mContext;

	public FarmersArrayAdapter(Context context, List<Farmers> objects) {
		this.mContext = context;
		this.mList = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Farmers farm = (Farmers) this.getItem(position);
		LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
		convertView = _LayoutInflater.inflate(R.layout.item, null);
		if (convertView != null) {
			TextView t1 = (TextView) convertView.findViewById(R.id.textView1);						
			t1.setText(farm.getFarmerName());

			TextView t2 = (TextView) convertView.findViewById(R.id.textView2);
			t2.setVisibility(View.GONE);
//			t2.setText("(" + farm.getFarmerId() + ")");
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
