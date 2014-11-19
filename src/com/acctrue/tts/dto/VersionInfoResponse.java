package com.acctrue.tts.dto;

import java.io.Serializable;

import org.json.JSONObject;

public class VersionInfoResponse implements Serializable{
	private String errorCode;
	private boolean isError;
	private String message;
	private boolean update;
	private String updateContent;
	private String url;
	
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

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public String getUpdateContent() {
		return updateContent;
	}

	public void setUpdateContent(String updateContent) {
		this.updateContent = updateContent;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public static VersionInfoResponse fromJson(JSONObject obj) {
		VersionInfoResponse cp = null;
		try {
			cp = new VersionInfoResponse();
			cp.errorCode = obj.getString("ErrorCode");
			cp.isError = obj.getBoolean("IsError");
			cp.message = obj.getString("Message");
			cp.update = obj.getBoolean("Update");
			cp.updateContent = obj.getString("UpdateContent");
			cp.url = obj.getString("Url");

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return cp;
	}
}
