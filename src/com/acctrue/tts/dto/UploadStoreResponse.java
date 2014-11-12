package com.acctrue.tts.dto;

import java.io.Serializable;

import org.json.JSONObject;

@SuppressWarnings("serial")
public class UploadStoreResponse implements Serializable{
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

	public static UploadStoreResponse fromJson(JSONObject obj) {
		UploadStoreResponse cp = null;
		try {
			cp = new UploadStoreResponse();
			cp.errorCode = obj.getString("ErrorCode");
			cp.isError = obj.getBoolean("IsError");
			cp.message = obj.getString("Message");
			cp.newToken = obj.getString("NewToken");
			cp.updateToken = obj.getBoolean("UpdateToken");

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return cp;
	}
}
