package com.acctrue.tts.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.acctrue.tts.model.FarmLands;

@SuppressWarnings("serial")
public class GetFarmLandsResponse implements Serializable {
	private String errorCode;
	private boolean isError;
	private String message;
	private String newToken;
	private boolean updateToken;
	private List<FarmLands> farmLandList;

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

	public List<FarmLands> getFarmLandList() {
		return farmLandList;
	}

	public void setFarmLandList(List<FarmLands> farmLandList) {
		this.farmLandList = farmLandList;
	}

	public static GetFarmLandsResponse formJson(JSONObject obj) {
		GetFarmLandsResponse gl = null;
		try {
			gl = new GetFarmLandsResponse();
			gl.errorCode = obj.getString("ErrorCode");
			gl.isError = obj.getBoolean("IsError");
			gl.message = obj.getString("Message");
			gl.newToken = obj.getString("NewToken");
			gl.updateToken = obj.getBoolean("UpdateToken");
			JSONArray arr = obj.optJSONArray("FarmLandList");
			if (arr != null) {
				gl.farmLandList = new ArrayList<FarmLands> ();
				for (int i = 0; i < arr.length(); i++) {
					FarmLands f = FarmLands.formJson(arr.getJSONObject(i));
					gl.farmLandList.add(f);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return gl;
	}

}
