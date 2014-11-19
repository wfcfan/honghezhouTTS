package com.acctrue.tts.dto;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.acctrue.tts.rpc.JsonRest;

public class VersionInfoRequest implements JsonRest,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String serialNo;
	private int versionNum;
	
	public VersionInfoRequest(String serialNo,int versionNum){
		this.serialNo = serialNo;
		this.versionNum = versionNum;
	}

	@Override
	public String toJson() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("serialNo", serialNo);
			obj.put("versionNum", versionNum);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return obj.toString();
	}

}
