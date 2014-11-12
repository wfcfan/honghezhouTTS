package com.acctrue.tts.dto;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.acctrue.tts.model.ChargeStoreIn;
import com.acctrue.tts.model.ChargeStoreInCode;
import com.acctrue.tts.rpc.JsonRest;
import com.acctrue.tts.utils.AccountUtil;
import com.acctrue.tts.utils.DateUtil;

public class ChargeStoreInRequest implements JsonRest {
	private ChargeStoreIn chargeStore;
	private List<ChargeStoreInCode> items;
	
	public ChargeStoreInRequest(ChargeStoreIn chargeStore,List<ChargeStoreInCode> items){
		this.chargeStore = chargeStore;
		this.items = items;
	}

	@Override
	public String toJson() {
		JSONObject json = new JSONObject();
		
		try {
			//sign
			json.put("sign", AccountUtil.getDefaultSign().toJsonObject());
			
			//requestInfo
			JSONObject info = new JSONObject();
			info.put("Actor", chargeStore.getActor());
			info.put("ActorDate",DateUtil.parseDatetimeToJsonDate(chargeStore.getActDate()));
			info.put("CorpId", chargeStore.getWarehouseId());
			info.put("Weight","0");
			for(ChargeStoreInCode item : items){
				info.accumulate("chargeCodes", item.getCode());
			}
			json.put("requestInfo", info);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json.toString();
	}

}
