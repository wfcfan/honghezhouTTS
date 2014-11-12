package com.acctrue.tts.tasks;

import android.content.Context;

import com.acctrue.tts.dto.ScanCodeRequest;
import com.acctrue.tts.rpc.OnCompleteListener;
import com.acctrue.tts.rpc.RpcAsyncTask;

public class ScanTaskHelper {
	private final Context ctx;
	
	public ScanTaskHelper(Context ctx){
		this.ctx = ctx;
	}
	
	public void scanCodeTask(ScanCodeRequest scRequest){
		RpcAsyncTask task = new RpcAsyncTask(ctx,scRequest,new OnCompleteListener(){

			@Override
			public void onComplete(String content) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
	}

}
