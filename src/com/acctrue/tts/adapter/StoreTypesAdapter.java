package com.acctrue.tts.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.acctrue.tts.R;
import com.acctrue.tts.model.StoreTypes;

public class StoreTypesAdapter extends BaseAdapter{
	private Context mContext;
	private List<StoreTypes> mList;
	
	public StoreTypesAdapter(Context context, List<StoreTypes> sts) {
		this.mContext = context;
		this.mList = sts;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final StoreTypes st = (StoreTypes) this.getItem(position);
		LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
		convertView = _LayoutInflater.inflate(R.layout.item, null);
		if (convertView != null) {
			TextView t1 = (TextView) convertView.findViewById(R.id.textView1);						
			t1.setText(st.getStoreTypeText());

			TextView t2 = (TextView) convertView.findViewById(R.id.textView2);
			t2.setVisibility(View.GONE);
			t2.setText("(" + st.getStoreTypeKey() + ")");
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
