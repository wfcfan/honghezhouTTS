package com.acctrue.tts.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.acctrue.tts.R;
import com.acctrue.tts.model.Warehouse;

public class WarehouseAdapter extends BaseAdapter{
	private final List<Warehouse> dataList;
	private final Context mContext;
	
	public WarehouseAdapter(Context ctx,List<Warehouse> warehouses){
		mContext = ctx;
		dataList = warehouses;
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Warehouse w = (Warehouse)this.getItem(position);
		LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
		convertView = _LayoutInflater.inflate(R.layout.item, null);
		if (convertView != null) {
			TextView t1 = (TextView) convertView.findViewById(R.id.textView1);						
			t1.setText(w.getWarehouseName());

			TextView t2 = (TextView) convertView.findViewById(R.id.textView2);
			t2.setVisibility(View.GONE);
		}
		return convertView;
	}

}
