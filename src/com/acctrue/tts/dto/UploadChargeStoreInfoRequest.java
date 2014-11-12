package com.acctrue.tts.dto;

import java.io.Serializable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.acctrue.tts.rpc.JsonRest;

@SuppressWarnings("serial")
public class UploadChargeStoreInfoRequest implements JsonRest, Serializable {

	private Sign sign;
	private UploadChargeStoreInfo requestInfo;

	public Sign getSign() {
		return sign;
	}

	public void setSign(Sign sign) {
		this.sign = sign;
	}

	public UploadChargeStoreInfo getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(UploadChargeStoreInfo requestInfo) {
		this.requestInfo = requestInfo;
	}

	@Override
	public String toJson() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("sign", sign.toJsonObject());
			obj.put("requestInfo", requestInfo.toJsonObject());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return obj.toString();
	}

	public static class UploadChargeStoreInfo {
		public JSONObject toJsonObject() {
			JSONObject obj = new JSONObject();
			try {
				obj.put("Actor", actor);
				obj.put("ActorDate", actorDate);
				obj.put("CorpId", corpId);
				obj.put("Weight", weight);
				if(this.chargeCodes != null){
					JSONArray arr = new JSONArray();
					for(int i =0;i<this.chargeCodes.size();i++){
						arr.put(this.chargeCodes.get(i));
					}
					obj.put("chargeCodes", arr);
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return obj;
		}

		private String actor;
		private String actorDate;
		private int corpId;
		private String weight;
		private List<String> chargeCodes;

		public String getActor() {
			return actor;
		}

		public void setActor(String actor) {
			this.actor = actor;
		}

		public String getActorDate() {
			return actorDate;
		}

		public void setActorDate(String actorDate) {
			this.actorDate = actorDate;
		}

		public int getCorpId() {
			return corpId;
		}

		public void setCorpId(int corpId) {
			this.corpId = corpId;
		}

		public String getWeight() {
			return weight;
		}

		public void setWeight(String weight) {
			this.weight = weight;
		}

		public List<String> getChargeCodes() {
			return chargeCodes;
		}

		public void setChargeCodes(List<String> chargeCodes) {
			this.chargeCodes = chargeCodes;
		}

	}

}
