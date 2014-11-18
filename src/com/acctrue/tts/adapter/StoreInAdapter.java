package com.acctrue.tts.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.acctrue.tts.R;
import com.acctrue.tts.model.ChargeStoreIn;

public class StoreInAdapter extends BaseAdapter{
	private Context ctx;
	private List<Boolean> mChecked;
	private CheckBox chkAll;
	private List<ChargeStoreIn> datas = new ArrayList<ChargeStoreIn>();
	
	
	public StoreInAdapter(Context ctx, List<ChargeStoreIn> lst, CheckBox chkAll){
		this.ctx = ctx;
		if(lst != null) datas = lst;
		mChecked = new ArrayList<Boolean>();
		for(int i=0;i<datas.size();i++)
			mChecked.add(false);
		this.chkAll = chkAll;
		this.chkAll.setChecked(false);
		this.chkAll.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				for(int i=0;i<mChecked.size();i++)
					mChecked.set(i,StoreInAdapter.this.chkAll.isChecked());
				notifyDataSetChanged();
			}
			
		});
	}
	
	public List<ChargeStoreIn> getCheckedData(){
		List<ChargeStoreIn> lst = new ArrayList<ChargeStoreIn>();
		for(int i=0;i<mChecked.size();i++){
			if(mChecked.get(i))
				lst.add(datas.get(i));
		}
		
		return lst;
	}
	
	public void RemoveItems(List<ChargeStoreIn> dataList){
		//删除datas对应的数据,
		for(int i=0;i<dataList.size();i++){
			datas.remove(dataList.get(i));
		}
		
		//重建 mChecked
		mChecked = new ArrayList<Boolean>();
		for(int i=0;i<datas.size();i++){
			mChecked.add(false);
		}
		
		chkAll.setChecked(false);
		notifyDataSetChanged();
	}

	public void RemoveItems(ChargeStoreIn ...csArr){
		if(csArr != null){
			RemoveItems(Arrays.asList(csArr));
		}
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = LayoutInflater.from(ctx).inflate(R.layout.storein_list_item, null);
			ItemViewCache item = new ItemViewCache();
			final int p = position;
			item.checkBox = (CheckBox)convertView.findViewById(R.id.chk);
			item.checkBox.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					CheckBox cb = (CheckBox) v;
					mChecked.set(p, cb.isChecked());
					if(chkAll.isChecked()){
						chkAll.setChecked(cb.isChecked());
					}else{
						for(int i=0;i<mChecked.size();i++){
							if(!mChecked.get(i))
								return;
						}
						chkAll.setChecked(true);
					}
				}
			});
			item.desc = (TextView)convertView.findViewById(R.id.desc);
			item.time = (TextView)convertView.findViewById(R.id.time);
			convertView.setTag(item);
		}
		ItemViewCache cache = (ItemViewCache)convertView.getTag();
		
		cache.checkBox.setChecked(mChecked.get(position));
		cache.desc.setText(datas.get(position).getWarehouseName());
		cache.time.setText(datas.get(position).getActDate());
		
		return convertView;
	}
	
	private static class ItemViewCache{
		public CheckBox checkBox;
		public TextView desc;
		public TextView time;
	}
	
}
