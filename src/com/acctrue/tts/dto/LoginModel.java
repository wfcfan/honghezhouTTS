package com.acctrue.tts.dto;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.acctrue.tts.rpc.JsonRest;

import android.text.TextUtils;

@SuppressWarnings("serial")
public class LoginModel implements Serializable,JsonRest {

	private String password;
	private String serialNo;
	private String userName;
	private String version;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public ArrayList<BasicNameValuePair> toHttpParams() {
		ArrayList<BasicNameValuePair> listPair = new ArrayList<BasicNameValuePair>();
		if (!TextUtils.isEmpty(userName)) {
			listPair.add(new BasicNameValuePair("userName", userName));
		}

		if (!TextUtils.isEmpty(password)) {
			listPair.add(new BasicNameValuePair("password", password));
		}
		listPair.add(new BasicNameValuePair("serialNo", serialNo));
		listPair.add(new BasicNameValuePair("version", version));
		return listPair;
	}

	public String toJson() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("password", password);
			obj.put("serialNo", serialNo);
			obj.put("userName", userName);
			obj.put("version", version);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return obj.toString();
	}

	@Override
	public String toString() {
		return "LoginModel [password=" + password + ", serialNo=" + serialNo
				+ ", userName=" + userName + ", version=" + version + "]";
	}

}
