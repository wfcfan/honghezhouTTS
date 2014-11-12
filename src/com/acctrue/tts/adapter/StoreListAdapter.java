package com.acctrue.tts.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.acctrue.tts.R;
import com.acctrue.tts.model.Store;

public class StoreListAdapter extends BaseAdapter{
	private Context ctx;
	private List<Store> datas = new ArrayList<Store>();

	public StoreListAdapter(Context ctx, List<Store> lst) {
		this.ctx = ctx;
		if (lst != null)
			datas = lst;
	}

	public List<Store> getAllDatas() {
		return this.datas;
	}

	public void RemoveItems(List<Store> dataList) {
		// 删除datas对应的数据,
		for (int i = 0; i < dataList.size(); i++) {
			datas.remove(dataList.get(i));
		}
		notifyDataSetChanged();
	}
	
	public void RemoveItem(Store item) {
		RemoveItem(item.getStoreId());
	}
	
	public void RemoveItem(String storeId) {
		// 删除datas对应的数据,
		int index = -1;
		for (int i = 0; i < datas.size(); i++) {
			if (datas.get(i).getStoreId().equals(storeId)) {
				index = i;
				break;
			}
		}
		if (index == -1)
			return;

		datas.remove(index);
		notifyDataSetChanged();
	}
	
	public void replaceItems(List<Store> dataList){
		datas.clear();
		datas.addAll(dataList);
		notifyDataSetChanged();
	}
	
	public void updateItem(Store data) {
		// 更新datas对应的数据,
		for (int i = 0; i < datas.size(); i++) {
			if (datas.get(i).getStoreId().equals(data.getStoreId())) {
				datas.set(i, data);
			}
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(ctx).inflate(
					R.layout.store_view, null);
			ItemViewCache item = new ItemViewCache();
			item.viewNo = (TextView) convertView.findViewById(R.id.view_storeNo);
			item.viewBizCorp = (TextView) convertView.findViewById(R.id.view_bizcorp);
			item.viewDate = (TextView) convertView.findViewById(R.id.view_date);
			item.viewMan = (TextView) convertView.findViewById(R.id.view_main);
			item.viewType = (TextView) convertView.findViewById(R.id.view_storeType);
			convertView.setTag(item);
		}
		ItemViewCache cache = (ItemViewCache) convertView.getTag();

		Store store = datas.get(position);

		cache.viewNo.setText(store.getStoreNo());
		cache.viewBizCorp.setText(store.getBizCorpName());
		cache.viewDate.setText(store.getStoreDateText());
		cache.viewMan.setText(store.getStoreMan());
		cache.viewType.setText(store.getStoreTypeText());
		
		return convertView;
	}

	private static class ItemViewCache {
		public TextView viewNo;
		public TextView viewBizCorp;
		public TextView viewDate;
		public TextView viewMan;
		public TextView viewType;
	}
}
