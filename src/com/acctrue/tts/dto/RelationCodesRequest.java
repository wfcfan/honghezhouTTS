package com.acctrue.tts.dto;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.acctrue.tts.model.RelationCodes;
import com.acctrue.tts.rpc.JsonRest;
import com.acctrue.tts.utils.AccountUtil;
import com.acctrue.tts.utils.DateUtil;

public class RelationCodesRequest implements JsonRest{
	
	RelationCodes codes;
	
	public RelationCodesRequest(RelationCodes codes){
		this.codes = codes; 
	}
	
	public String getId(){
		return codes.getId();
	}

	@Override
	public String toJson() {
		JSONObject json = new JSONObject();
		JSONObject jcodes = new JSONObject();
		
		try {
			json.put("sign", AccountUtil.getDefaultSign().toJsonObject());
			jcodes.put("Actor", codes.getMan());
			jcodes.put("ActorDate", DateUtil.parseDatetimeToJsonDate(codes.getCreateDate()));
			JSONArray jsonArr = new JSONArray();
			jsonArr.put(codes.getSqCode());
			jcodes.put("ChargeCodes", jsonArr);
			String[] list = codes.getBoxCode().split(",");
			jsonArr = new JSONArray();
			for(String item : list){
				jsonArr.put(item);
			}
			jcodes.put("Codes", jsonArr);
			
			json.put("info", jcodes);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return json.toString();
	}

}
