package com.acctrue.tts.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.acctrue.tts.R;

public class CodesAdapter extends BaseAdapter {
	private final String TAG = CodesAdapter.class.getSimpleName();

	public List<Boolean> mChecked;
	List<String> listCode;
	private Context mContext;
	HashMap<Integer, View> map = new HashMap<Integer, View>();

	public CodesAdapter(Context context, List<String> codes) {
		listCode = codes;
		mContext = context;

		mChecked = new ArrayList<Boolean>();
		for (int i = 0; i < codes.size(); i++)
			mChecked.add(false);

	}
	
	@Override
	public int getCount() {
		return listCode.size();
	}

	@Override
	public Object getItem(int position) {
		return listCode.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		mChecked = new ArrayList<Boolean>();
		for (int i = 0; i < listCode.size(); i++)
			mChecked.add(false);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder holder = null;
		if (map.get(position) == null) {
			LayoutInflater mInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = mInflater.inflate(R.layout.code_item, null);
			holder = new ViewHolder();
			holder.selected = (CheckBox) view.findViewById(R.id.selecter);
			holder.name = (TextView) view.findViewById(R.id.name);
			final int p = position;
			map.put(position, view);
			holder.selected.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					CheckBox cb = (CheckBox) v;
					mChecked.set(p, cb.isChecked());
				}
			});
			view.setTag(holder);
		} else {
			view = map.get(position);
			holder = (ViewHolder) view.getTag();
		}

		Log.d(TAG, String.valueOf(position));

		Boolean isS = mChecked.get(position);
		if (isS != null) {
			holder.selected.setChecked(isS);
		} else {
			holder.selected.setChecked(false);
		}
		holder.name.setText(listCode.get(position));
		return view;
	}

	static class ViewHolder {
		CheckBox selected;
		TextView name;
	}

}
