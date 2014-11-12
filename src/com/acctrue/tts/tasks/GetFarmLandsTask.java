package com.acctrue.tts.tasks;

import android.content.Context;

import com.acctrue.tts.dto.GetFarmLandsRequest;
import com.acctrue.tts.rpc.RPCHelper;

public class GetFarmLandsTask extends CommonTask{
	
	private RPCHelper mHelper;
	private Context mContext;
	
	public GetFarmLandsTask(Context context, OnTaskExecuteListener listener) {
		super(listener);
		this.mContext = context;
	}

	@Override
	protected Boolean doInBackground(Object... params) {
		if (mHelper == null) {
			mHelper = RPCHelper.getInstance(mContext);
		}
		try {
			GetFarmLandsRequest  gr = (GetFarmLandsRequest )params[0];
			gr.getSign().setTimestamp(mHelper.getServerTime());
			object = mHelper.getFarmLands(gr);
			return object != null;
		}catch(Exception ex){
			ex.printStackTrace();
			excption = ex;
		}
		return false;
	}
	
	
}
