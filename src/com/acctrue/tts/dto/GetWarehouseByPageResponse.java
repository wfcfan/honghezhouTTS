package com.acctrue.tts.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.acctrue.tts.model.Warehouse;

@SuppressWarnings("serial")
public class GetWarehouseByPageResponse implements Serializable {
	private String errorCode;
	private boolean isError;
	private String message;
	private String newToken;
	private boolean updateToken;
	private List<Warehouse> warehouses;

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

	public List<Warehouse> getWarehouses() {
		return warehouses;
	}

	public void setWarehouses(List<Warehouse> warehouses) {
		this.warehouses = warehouses;
	}

	public static GetWarehouseByPageResponse fromJson(JSONObject obj) {
		GetWarehouseByPageResponse gw = null;
		try {
			gw = new GetWarehouseByPageResponse();
			gw.errorCode = obj.getString("ErrorCode");
			gw.isError = obj.getBoolean("IsError");
			gw.message = obj.getString("Message");
			gw.newToken = obj.getString("NewToken");
			gw.updateToken = obj.getBoolean("UpdateToken");
			JSONArray arr = obj.optJSONArray("Warehouses");
			if (arr != null) {
				gw.warehouses = new ArrayList<Warehouse>();
				for (int i = 0; i < arr.length(); i++) {
					Warehouse w = Warehouse.fromJson(arr.getJSONObject(i));
					gw.warehouses.add(w);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return gw;
	}

}
