package com.acctrue.tts.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.acctrue.tts.R;
import com.acctrue.tts.model.PackCode;

public class PackCodeListAdapter extends BaseAdapter{
	private Context ctx;
	private List<Boolean> mChecked;
	private List<PackCode> datas = new ArrayList<PackCode>();
	
	
	public PackCodeListAdapter(Context ctx, List<PackCode> lst){
		this.ctx = ctx;
		if(lst != null) datas = lst;
		mChecked = new ArrayList<Boolean>();
		for(int i=0;i<datas.size();i++)
			mChecked.add(false);
	}
	
	public List<PackCode> getCheckedData(){
		List<PackCode> lst = new ArrayList<PackCode>();
		for(int i=0;i<mChecked.size();i++){
			if(mChecked.get(i))
				lst.add(datas.get(i));
		}
		
		return lst;
	}
	
	public void RemoveItems(List<PackCode> dataList){
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
	
	public void addData(PackCode data){
		datas.add(data);
		mChecked.add(false);
		notifyDataSetChanged();
	}
	
	public void addAllData(List<PackCode> dataList){
		datas.addAll(dataList);
		for(int i=0;i<dataList.size();i++){
			mChecked.add(false);
		}
		notifyDataSetChanged();
	}
	
	public List<PackCode> getAllData(){
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
		cache.desc.setText(datas.get(position).getCodeValue());
		
		return convertView;
	}
	
	private static class ItemViewCache{
		public CheckBox checkBox;
		public TextView desc;
	}
}
