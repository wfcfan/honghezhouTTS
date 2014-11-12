package com.acctrue.tts.dto;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.acctrue.tts.rpc.JsonRest;

@SuppressWarnings("serial")
public class GetBizCorpCountRequest implements JsonRest,Serializable{
	private Sign sign;

	public Sign getSign() {
		return sign;
	}

	public void setSign(Sign sign) {
		this.sign = sign;
	}

	@Override
	public String toJson() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("sign", sign.toJsonObject());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return obj.toString();
	} 
}
