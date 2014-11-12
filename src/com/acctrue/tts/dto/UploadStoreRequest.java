package com.acctrue.tts.dto;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.acctrue.tts.model.Store;
import com.acctrue.tts.model.StoreCode;
import com.acctrue.tts.rpc.JsonRest;

@SuppressWarnings("serial")
public class UploadStoreRequest implements JsonRest {
	private Sign sign;
	private Store store;
	private List<StoreCode> codes;
	private List<StoreCode> removeCodes;

	public Sign getSign() {
		return sign;
	}

	public void setSign(Sign sign) {
		this.sign = sign;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public List<StoreCode> getCodes() {
		return codes;
	}

	public void setCodes(List<StoreCode> codes) {
		this.codes = codes;
	}

	public List<StoreCode> getRemoveCodes() {
		return removeCodes;
	}

	public void setRemoveCodes(List<StoreCode> removeCodes) {
		this.removeCodes = removeCodes;
	}

	@Override
	public String toJson() {
		JSONObject json = new JSONObject();
		try{
			json.put("sign", sign.toJsonObject());
			JSONArray arr = new JSONArray();
			for(StoreCode sc : codes)
				arr.put(sc.toJsonObject());
			json.put("codes", arr);
			json.put("info", store.toJsonObject());
			arr = new JSONArray();
			for(StoreCode sc : removeCodes)
				arr.put(sc.toJsonObject());
			json.put("removeCodes", arr);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return json.toString();
	}

}
