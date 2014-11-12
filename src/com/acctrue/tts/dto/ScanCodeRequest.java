package com.acctrue.tts.dto;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.acctrue.tts.rpc.JsonRest;

@SuppressWarnings("serial")
public class ScanCodeRequest implements JsonRest,Serializable {
	private Sign sign;
	private ScanCode code;
	private ScanInfo info;

	@Override
	public String toJson() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("sign", sign.toJsonObject());
			obj.put("code", code.toJsonObject());
			obj.put("info", info.toJsonObject());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return obj.toString();
	}
	
	public Sign getSign() {
		return sign;
	}

	public void setSign(Sign sign) {
		this.sign = sign;
	}

	public ScanCode getCode() {
		return code;
	}

	public void setCode(ScanCode code) {
		this.code = code;
	}

	public ScanInfo getInfo() {
		return info;
	}

	public void setInfo(ScanInfo info) {
		this.info = info;
	}

	public static class ScanCode {
		private String actTime;
		private String actor;
		private boolean byParent;
		private String codeId;

		public String getActTime() {
			return actTime;
		}

		public void setActTime(String actTime) {
			this.actTime = actTime;
		}

		public String getActor() {
			return actor;
		}

		public void setActor(String actor) {
			this.actor = actor;
		}

		public boolean isByParent() {
			return byParent;
		}

		public void setByParent(boolean byParent) {
			this.byParent = byParent;
		}

		public String getCodeId() {
			return codeId;
		}

		public void setCodeId(String codeId) {
			this.codeId = codeId;
		}
		
		public JSONObject toJsonObject(){
			JSONObject obj = new JSONObject();
			try{
				obj.put("ActTime", actTime);
				obj.put("Actor", actor);
				obj.put("ByParent", byParent);
				obj.put("CodeId", codeId);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			return obj;
		}
	}

	public static class ScanInfo {
		private int corpId;
		private String storeId;
		private int storeType;
		private int bizCorpId;

		public int getCorpId() {
			return corpId;
		}

		public void setCorpId(int corpId) {
			this.corpId = corpId;
		}

		public String getStoreId() {
			return storeId;
		}

		public void setStoreId(String storeId) {
			this.storeId = storeId;
		}

		public int getStoreType() {
			return storeType;
		}

		public void setStoreType(int storeType) {
			this.storeType = storeType;
		}
		
		public int getBizCorpId() {
			return bizCorpId;
		}

		public void setBizCorpId(int bizCorpId) {
			this.bizCorpId = bizCorpId;
		}

		public JSONObject toJsonObject(){
			JSONObject obj = new JSONObject();
			try{
				obj.put("CorpId", corpId);
				obj.put("StoreId", storeId);
				obj.put("StoreType", storeType);
				obj.put("BizCorpId", bizCorpId);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			return obj;
		}
	}

	

}
