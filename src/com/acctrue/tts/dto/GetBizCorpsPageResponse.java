package com.acctrue.tts.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.acctrue.tts.model.BizCorp;

@SuppressWarnings("serial")
public class GetBizCorpsPageResponse implements Serializable {
	private String errorCode;
	private boolean isError;
	private String message;
	private String newToken;
	private boolean updateToken;
	private List<BizCorp> corpList;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public boolean isError() {
		return isError;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getNewToken() {
		return newToken;
	}

	public void setNewToken(String newToken) {
		this.newToken = newToken;
	}

	public boolean isUpdateToken() {
		return updateToken;
	}

	public void setUpdateToken(boolean updateToken) {
		this.updateToken = updateToken;
	}

	public List<BizCorp> getCorpList() {
		return corpList;
	}

	public void setCorpList(List<BizCorp> corpList) {
		this.corpList = corpList;
	}
	
	public static GetBizCorpsPageResponse fromJson(JSONObject obj){
		GetBizCorpsPageResponse cp = null;
		try{
			cp = new GetBizCorpsPageResponse();
			cp.errorCode = obj.getString("ErrorCode");
			cp.isError = obj.getBoolean("IsError");
			cp.message = obj.getString("Message");
			cp.newToken = obj.getString("NewToken");
			cp.updateToken = obj.getBoolean("UpdateToken");
			JSONArray arr = obj.optJSONArray("CorpList");
			
			if(arr != null){
				cp.corpList = new ArrayList<BizCorp>();
				for(int i=0;i<arr.length();i++){
					BizCorp c = BizCorp.formJson(arr.getJSONObject(i));
					cp.corpList.add(c);
				}
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return cp;
	}

}
