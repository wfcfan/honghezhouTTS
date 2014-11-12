package com.acctrue.tts.dto;

import org.json.JSONObject;

public class UploadChargeStoreInfoResponse {
	private String errorCode;
	private boolean isError;
	private String message;
	private String newToken;
	private boolean updateToken;

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
	
	public static UploadChargeStoreInfoResponse fromJson(JSONObject obj){
		UploadChargeStoreInfoResponse uc = null;
		try{
			uc = new UploadChargeStoreInfoResponse();
			uc.errorCode = obj.getString("ErrorCode");
			uc.isError = obj.getBoolean("IsError");
			uc.message = obj.getString("Message");
			uc.newToken = obj.getString("NewToken");
			uc.updateToken = obj.getBoolean("UpdateToken");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return uc;
	}

}
