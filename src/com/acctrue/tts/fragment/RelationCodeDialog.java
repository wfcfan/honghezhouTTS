package com.acctrue.tts.fragment;

import com.acctrue.tts.R;
import com.acctrue.tts.model.RelationCodes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class RelationCodeDialog extends DialogFragment{
	
	public interface ModifyRelationCode{
		void doModify(RelationCodes data);
	}
	
	EditText revNo;
	EditText trackNo;
	RelationCodes relation;
	
	public RelationCodeDialog(RelationCodes m){
		super();
		relation = m;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.relation_code_dialog, null);
		revNo = (EditText)view.findViewById(R.id.revno);
		revNo.setText(relation.getSqCode());
		trackNo = (EditText)view.findViewById(R.id.trackno);
		trackNo.setText(relation.getBoxCode());
		
		builder.setView(view);
		builder.setPositiveButton(R.string.btn_ok_tip, 
				new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ModifyRelationCode action = (ModifyRelationCode)getActivity();
						relation.setSqCode(revNo.getText().toString());
						relation.setBoxCode(trackNo.getText().toString());
						action.doModify(relation);
					}
				});
		builder.setNegativeButton(R.string.cancel, null);
		return builder.create();
	}
}
