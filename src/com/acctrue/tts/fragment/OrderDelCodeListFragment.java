package com.acctrue.tts.fragment;

import com.acctrue.tts.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OrderDelCodeListFragment extends Fragment {
	View root;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_order_delcode_list, null);
		
		return root;
	}
}
