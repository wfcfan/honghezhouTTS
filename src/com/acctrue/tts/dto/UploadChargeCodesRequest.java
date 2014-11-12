package com.acctrue.tts.dto;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.acctrue.tts.model.Charges;
import com.acctrue.tts.rpc.JsonRest;

@SuppressWarnings("serial")
public class UploadChargeCodesRequest implements JsonRest,Serializable {
	
	private Sign sign;
	private Charges info;

	public Sign getSign() {
		return sign;
	}

	public void setSign(Sign sign) {
		this.sign = sign;
	}
	
	public Charges getInfo() {
		return info;
	}

	public void setInfo(Charges info) {
		this.info = info;
	}

	@Override
	public String toJson() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("sign", sign.toJsonObject());
			obj.put("info", info.toJsonObject());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return obj.toString();
	}

}
