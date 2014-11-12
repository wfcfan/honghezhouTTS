package com.acctrue.tts.adapter;

import java.util.ArrayList;
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
import com.acctrue.tts.model.StoreCode;

public class StockCodeListAdapter extends BaseAdapter{
	private Context ctx;
	private List<Boolean> mChecked;
	private List<StoreCode> datas = new ArrayList<StoreCode>();
	
	
	public StockCodeListAdapter(Context ctx, List<StoreCode> lst){
		this.ctx = ctx;
		if(lst != null) datas = lst;
		mChecked = new ArrayList<Boolean>();
		for(int i=0;i<datas.size();i++)
			mChecked.add(false);
	}
	
	public List<StoreCode> getCheckedData(){
		List<StoreCode> lst = new ArrayList<StoreCode>();
		for(int i=0;i<mChecked.size();i++){
			if(mChecked.get(i))
				lst.add(datas.get(i));
		}
		
		return lst;
	}
	
	public void RemoveItems(List<StoreCode> dataList){
		//删除datas对应的数据,
		for(int i=0;i<dataList.size();i++){
			datas.remove(dataList.get(i));
		}
		
		//重建 mChecked
		mChecked = new ArrayList<Boolean>();
		for(int i=0;i<datas.size();i++){
			mChecked.add(false);
		}
		notifyDataSetChanged();
	}
	
	public void RemoveItems(StoreCode... codes) {
		for (StoreCode sc : codes)
			datas.remove(sc);
		// 重建 mChecked
		mChecked = new ArrayList<Boolean>();
		for (int i = 0; i < datas.size(); i++) {
			mChecked.add(false);
		}
		notifyDataSetChanged();
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
	
	public void addData(StoreCode data){
		datas.add(data);
		mChecked.add(false);
		notifyDataSetChanged();
	}
	
	public void addAllData(List<StoreCode> dataList){
		datas.addAll(dataList);
		for(int i=0;i<dataList.size();i++){
			mChecked.add(false);
		}
		notifyDataSetChanged();
	}
	
	public List<StoreCode> getAllData(){
		return datas;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = LayoutInflater.from(ctx).inflate(R.layout.code_item, null);
			ItemViewCache item = new ItemViewCache();
			final int p = position;
			item.checkBox = (CheckBox)convertView.findViewById(R.id.selecter);
			item.checkBox.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					CheckBox cb = (CheckBox) v;
					mChecked.set(p, cb.isChecked());
				}
			});
			item.desc = (TextView)convertView.findViewById(R.id.name);
			convertView.setTag(item);
		}
		ItemViewCache cache = (ItemViewCache)convertView.getTag();
		
		cache.checkBox.setChecked(mChecked.get(position));
		cache.desc.setText(datas.get(position).getCodeId());
		
		return convertView;
	}
	
	private static class ItemViewCache{
		public CheckBox checkBox;
		public TextView desc;
	}
}
