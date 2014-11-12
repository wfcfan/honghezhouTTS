package com.acctrue.tts.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ListViewDialogFragment extends  DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return super.onCreateDialog(savedInstanceState);
	}
	
	View root;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//root =  inflater.inflate(R., root)
		return super.onCreateView(inflater, container, savedInstanceState);
		//return null;
	}
	
	

}
