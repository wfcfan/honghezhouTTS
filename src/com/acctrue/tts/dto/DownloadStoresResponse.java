package com.acctrue.tts.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.acctrue.tts.model.Store;

@SuppressWarnings("serial")
public class DownloadStoresResponse implements Serializable {

	private String errorCode;
	private boolean isError;
	private String message;
	private String newToken;
	private boolean updateToken;
	private List<Store> stores;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public boolean isError() {
		return isError;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getNewToken() {
		return newToken;
	}

	public void setNewToken(String newToken) {
		this.newToken = newToken;
	}

	public boolean isUpdateToken() {
		return updateToken;
	}

	public void setUpdateToken(boolean updateToken) {
		this.updateToken = updateToken;
	}

	public List<Store> getStores() {
		return stores;
	}

	public void setStores(List<Store> stores) {
		this.stores = stores;
	}

	public static DownloadStoresResponse fromJson(JSONObject obj) {
		DownloadStoresResponse gw = null;
		try {
			gw = new DownloadStoresResponse();
			gw.errorCode = obj.getString("ErrorCode");
			gw.isError = obj.getBoolean("IsError");
			gw.message = obj.getString("Message");
			gw.newToken = obj.getString("NewToken");
			gw.updateToken = obj.getBoolean("UpdateToken");

			JSONArray arr = obj.optJSONArray("Stores");
			if (arr != null) {
				gw.stores = new ArrayList<Store>();
				for (int i = 0; i < arr.length(); i++) {
					Store order = Store.fromJson(arr.getJSONObject(i));
					gw.stores.add(order);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return gw;
	}

}
