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
import com.acctrue.tts.model.Store;

public class DownTaskFormAdapter extends BaseAdapter {
	private Context ctx;
	private List<Boolean> mChecked;
	private List<Store> datas = new ArrayList<Store>();

	public DownTaskFormAdapter(Context ctx, List<Store> lst) {
		this.ctx = ctx;
		if (lst != null)
			datas = lst;
		mChecked = new ArrayList<Boolean>();
		for (int i = 0; i < datas.size(); i++)
			mChecked.add(false);
	}

	public List<Store> getAllDatas() {
		return this.datas;
	}

	public List<Store> getCheckedData() {
		List<Store> lst = new ArrayList<Store>();
		for (int i = 0; i < mChecked.size(); i++) {
			if (mChecked.get(i))
				lst.add(datas.get(i));
		}

		return lst;
	}

	public void RemoveItems(List<Store> dataList) {
		// 删除datas对应的数据,
		for (int i = 0; i < dataList.size(); i++) {
			datas.remove(dataList.get(i));
		}

		// 重建 mChecked
		mChecked = new ArrayList<Boolean>();
		for (int i = 0; i < datas.size(); i++) {
			mChecked.add(false);
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
		mChecked.remove(index);

		notifyDataSetChanged();
	}
	
	
	public void replaceItems(List<Store> dataList){
		datas.clear();
		datas.addAll(dataList);
		mChecked = new ArrayList<Boolean>();
		for (int i = 0; i < datas.size(); i++) {
			mChecked.add(false);
		}
		
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
					R.layout.track_rev_list_item, null);
			ItemViewCache item = new ItemViewCache();
			final int p = position;
			item.checkBox = (CheckBox) convertView.findViewById(R.id.chk);
			item.checkBox.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					CheckBox cb = (CheckBox) v;
					mChecked.set(p, cb.isChecked());
					for (int i = 0; i < mChecked.size(); i++) {
						if (!mChecked.get(i))
							return;
					}
				}
			});
			item.revno = (TextView) convertView.findViewById(R.id.revno);
			item.trackno = (TextView) convertView.findViewById(R.id.trackno);
			convertView.setTag(item);
		}
		ItemViewCache cache = (ItemViewCache) convertView.getTag();

		Store order = datas.get(position);

		cache.checkBox.setChecked(mChecked.get(position));
		cache.revno.setText(order.getStoreNo());
		String secondCol = String.format("来往企业:%s...,%s",
				order.getBizCorpName(), order.getStoreTypeText());
		cache.trackno.setText(secondCol);

		return convertView;
	}

	private static class ItemViewCache {
		public CheckBox checkBox;
		public TextView revno;
		public TextView trackno;
	}

}
