package com.acctrue.tts.dto;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.acctrue.tts.rpc.JsonRest;

@SuppressWarnings("serial")
public class Sign implements JsonRest,Serializable {

	private int corpId;
	private String serialNo;
	private String timestamp;
	private String token;
	private int userId;
	private String version;

	public int getCorpId() {
		return corpId;
	}

	public void setCorpId(int corpId) {
		this.corpId = corpId;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String toJson() {
		return toJsonObject().toString();
	}

	public JSONObject toJsonObject() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("CorpId", this.corpId);
			obj.put("SerialNo", this.serialNo);
			obj.put("Timestamp", this.timestamp);
			obj.put("Token", this.token);
			obj.put("UserId", this.userId);
			obj.put("Version", this.version);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
}
