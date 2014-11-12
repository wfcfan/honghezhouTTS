package com.acctrue.tts.dto;

import java.io.Serializable;

import org.json.JSONObject;

@SuppressWarnings("serial")
public class GetProductCountResponse implements Serializable{
	private String errorCode;
	private boolean isError;
	private String message;
	private String newToken;
	private boolean updateToken;
	private int count;

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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public static GetProductCountResponse fromJson(JSONObject obj) {
		GetProductCountResponse cc = null;
		try {
			cc = new GetProductCountResponse();
			cc.errorCode = obj.getString("ErrorCode");
			cc.isError = obj.getBoolean("IsError");
			cc.message = obj.getString("Message");
			cc.newToken = obj.getString("NewToken");
			cc.updateToken = obj.getBoolean("UpdateToken");
			cc.count = obj.getInt("Count");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return cc;
	}
}
