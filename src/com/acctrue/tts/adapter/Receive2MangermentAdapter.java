package com.acctrue.tts.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.acctrue.tts.R;
import com.acctrue.tts.enums.ChargesStatusEnum;
import com.acctrue.tts.enums.CodeTypeEnum;
import com.acctrue.tts.model.Charges;

public class Receive2MangermentAdapter extends BaseAdapter {
	private Context ctx;
	private List<Boolean> mChecked;
	private CheckBox chkAll;
	private List<Charges> datas = new ArrayList<Charges>();

	public Receive2MangermentAdapter(Context ctx, List<Charges> lst,
			CheckBox chkAll) {
		this.ctx = ctx;
		if (lst != null)
			datas = lst;
		mChecked = new ArrayList<Boolean>();
		for (int i = 0; i < datas.size(); i++)
			mChecked.add(false);
		this.chkAll = chkAll;
		this.chkAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (int i = 0; i < mChecked.size(); i++)
					mChecked.set(i,
							Receive2MangermentAdapter.this.chkAll.isChecked());
				notifyDataSetChanged();
			}

		});
	}

	public List<Charges> getAllDatas() {
		return this.datas;
	}

	public List<Charges> getCheckedData() {
		List<Charges> lst = new ArrayList<Charges>();
		for (int i = 0; i < mChecked.size(); i++) {
			if (mChecked.get(i))
				lst.add(datas.get(i));
		}

		return lst;
	}

	public int getCheckedDataLength() {
		return this.getCheckedData().size();
	}

	public void RemoveItems(List<Charges> dataList) {
		// 删除datas对应的数据,
		for (int i = 0; i < dataList.size(); i++) {
			datas.remove(dataList.get(i));
		}

		// 重建 mChecked
		mChecked = new ArrayList<Boolean>();
		for (int i = 0; i < datas.size(); i++) {
			mChecked.add(false);
		}

		chkAll.setChecked(false);
		notifyDataSetChanged();
	}

	public void RemoveItems(Charges c) {
		datas.remove(c);
		// 重建 mChecked
		mChecked = new ArrayList<Boolean>();
		for (int i = 0; i < datas.size(); i++) {
			mChecked.add(false);
		}

		chkAll.setChecked(false);
		notifyDataSetChanged();
	}

	public void updateItem(List<Charges> datas) {
		for (Charges c : datas) {
			updateItem(c);
		}
	}

	public void updateItem(Charges data) {
		// 更新datas对应的数据,
		for (int i = 0; i < datas.size(); i++) {
			if (datas.get(i).getId().equals(data.getId())) {
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
					if (chkAll.isChecked()) {
						chkAll.setChecked(cb.isChecked());
					} else {
						for (int i = 0; i < mChecked.size(); i++) {
							if (!mChecked.get(i))
								return;
						}
						chkAll.setChecked(true);
					}
				}
			});
			item.revno = (TextView) convertView.findViewById(R.id.revno);
			item.trackno = (TextView) convertView.findViewById(R.id.trackno);
			convertView.setTag(item);
		}
		ItemViewCache cache = (ItemViewCache) convertView.getTag();

		Charges charges = datas.get(position);

		// 收取码是上传状态时，变色
		if (charges.getIsPackCode() == CodeTypeEnum.ChargesCode.getId()
				&& charges.getState() == ChargesStatusEnum.Uploaded
						.getStateId()) {
			convertView.setBackgroundColor(Color.RED);
		} else if (charges.getIsPackCode() == CodeTypeEnum.TrackCode.getId()) {
			// 追溯码变色
			convertView.setBackgroundColor(Color.YELLOW);
		}

		cache.checkBox.setChecked(mChecked.get(position));
		cache.revno.setText(charges.getBatchno());
		String secondCol = String.format("农田编号:%s...,%s", charges.getManNo(),
				charges.getStateName());
		cache.trackno.setText(secondCol);

		return convertView;
	}

	private static class ItemViewCache {
		public CheckBox checkBox;
		public TextView revno;
		public TextView trackno;
	}

}
