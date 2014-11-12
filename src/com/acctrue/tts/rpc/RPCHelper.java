package com.acctrue.tts.rpc;

import java.io.UnsupportedEncodingException;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.acctrue.tts.Constants;
import com.acctrue.tts.dto.GetFarmLandsRequest;
import com.acctrue.tts.dto.GetFarmLandsResponse;
import com.acctrue.tts.dto.LoginModel;
import com.acctrue.tts.dto.LoginResponse;
import com.acctrue.tts.rpc.exception.RpcException;

public class RPCHelper extends RPCBase {

	private static RPCHelper mRPCHelper;

	private RPCHelper(Context context) {
		mContext = context;
	}

	public static RPCHelper getInstance(Context context) {
		if (mRPCHelper == null) {
			mRPCHelper = new RPCHelper(context.getApplicationContext());
		}
		return mRPCHelper;
	}
	
//	private <T extends Serializable> getData(FormatDataService formatDataService,final String apiUrl){
//		StringEntity strContent = null;
//		String result = "";
//		try {
//			strContent = new StringEntity(formatDataService.toJson());
//			strContent.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
//			result = execute(strContent, apiUrl);
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		
//		JSONObject obj = new JSONObject(result);
//		return LoginResponse.fromJson(obj);
//	}

	public LoginResponse userLogin(LoginModel lm)
			throws ConnectTimeoutException, RpcException, JSONException {
		StringEntity strContent = null;
		String result = "";
		try {
			strContent = new StringEntity(lm.toJson());
			strContent.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			result = execute(strContent, Constants.URL_USER_LOGIN);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		JSONObject obj = new JSONObject(result);
		return LoginResponse.fromJson(obj);
	}
	
	public String getServerTime() throws ConnectTimeoutException, RpcException{
		return this.executeGet(Constants.URL_TIME);
	}
	
	public GetFarmLandsResponse getFarmLands(GetFarmLandsRequest gr) 
			throws ConnectTimeoutException, RpcException, JSONException{
		StringEntity strContent = null;
		String result = "";
		try {
			strContent = new StringEntity(gr.toJson());
			strContent.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			result = execute(strContent, Constants.URL_GETFARMLANDS);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		JSONObject obj = new JSONObject(result);
		return GetFarmLandsResponse.formJson(obj);
	}
}
