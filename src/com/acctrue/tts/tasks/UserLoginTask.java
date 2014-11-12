package com.acctrue.tts.tasks;

import android.content.Context;

import com.acctrue.tts.dto.LoginModel;
import com.acctrue.tts.rpc.RPCHelper;


public class UserLoginTask extends CommonTask{

	private RPCHelper mHelper;
	private Context mContext;
	
	public UserLoginTask(Context context, OnTaskExecuteListener listener) {
		super(listener);
		this.mContext = context;
	}

	@Override
	protected Boolean doInBackground(Object... params) {
		if (mHelper == null) {
			mHelper = RPCHelper.getInstance(mContext);
		}
		try {
			LoginModel lm = (LoginModel)params[0];
			object = mHelper.userLogin(lm);
			return object != null;
		}catch(Exception ex){
			ex.printStackTrace();
			excption = ex;
		}
		return false;
	}
	
	

}
