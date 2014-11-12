package com.acctrue.tts.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

public class BaseArrayAdapter<T> extends ArrayAdapter<T> {
	private LayoutInflater mInflater;
	
	public BaseArrayAdapter(Context context, List<T> objects) {
		super(context, 0, 0, objects);
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	protected LayoutInflater getInflater() {
		return mInflater;
	}
}
