package com.acctrue.tts.dto;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class ScanRemoveRequest extends ScanCodeRequest{
	
	private List<String> parentCodes;

	public List<String> getParentCodes() {
		return parentCodes;
	}

	public void setParentCodes(List<String> parentCodes) {
		this.parentCodes = parentCodes;
	}

	public String toJson() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("sign", this.getSign().toJsonObject());
			obj.put("code", this.getCode().toJsonObject());
			obj.put("info", this.getInfo().toJsonObject());
			if(parentCodes != null && !parentCodes.isEmpty()){
				JSONArray arr = new JSONArray(parentCodes);
				obj.put("parentCodes", arr);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return obj.toString();
	}
	
	

}
