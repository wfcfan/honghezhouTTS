package com.acctrue.tts.dto;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.acctrue.tts.rpc.JsonRest;

@SuppressWarnings("serial")
public class DownloadStoresRequest implements JsonRest, Serializable {

	private Sign sign;
	private StoreSearch search;

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
			obj.put("search", search.toJsonObject());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return obj.toString();
	}

	public StoreSearch getSearch() {
		return search;
	}

	public void setSearch(StoreSearch search) {
		this.search = search;
	}

	public void setSearch(int corpId, int userId) {
		StoreSearch s = new StoreSearch(corpId, userId);
		this.setSearch(s);
	}

	public static class StoreSearch implements Serializable {
		private int corpId;
		private int userId;
		private int[] queryStatus;

		public StoreSearch() {
			queryStatus = new int[0];
		}

		public StoreSearch(int cid, int uid) {
			this.corpId = cid;
			this.userId = uid;
		}

		public int getCorpId() {
			return corpId;
		}

		public void setCorpId(int corpId) {
			this.corpId = corpId;
		}

		public int getUserId() {
			return userId;
		}

		public void setUserId(int userId) {
			this.userId = userId;
		}

		public int[] getQueryStatus() {
			return queryStatus;
		}

		public void setQueryStatus(int[] queryStatus) {
			this.queryStatus = queryStatus;
		}

		public JSONObject toJsonObject() {
			JSONObject obj = new JSONObject();
			try {
				obj.put("CorpId", this.corpId);
				obj.put("UserId", this.userId);
				JSONArray arr = new JSONArray();
				obj.put("QueryStatus", arr);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return obj;
		}

	}

}
