package com.acctrue.tts.model;

import org.json.JSONObject;

public class FarmLands {
	private int farmLandId;
	private String farmLandCode;
	private String farmLandName;
	private int userOwnerId;
	private String userOwnerDisplayName;

	public FarmLands() {
		;
	}
	
	public FarmLands(String code, String name) {
		this.farmLandCode = code;
		this.farmLandName = name;
	}

	public int getFarmLandId() {
		return farmLandId;
	}

	public void setFarmLandId(int farmLandId) {
		this.farmLandId = farmLandId;
	}

	public String getFarmLandCode() {
		return farmLandCode;
	}

	public void setFarmLandCode(String farmLandCode) {
		this.farmLandCode = farmLandCode;
	}

	public String getFarmLandName() {
		return farmLandName;
	}

	public void setFarmLandName(String farmLandName) {
		this.farmLandName = farmLandName;
	}

	public int getUserOwnerId() {
		return userOwnerId;
	}

	public void setUserOwnerId(int userOwnerId) {
		this.userOwnerId = userOwnerId;
	}

	public String getUserOwnerDisplayName() {
		return userOwnerDisplayName;
	}

	public void setUserOwnerDisplayName(String userOwnerDisplayName) {
		this.userOwnerDisplayName = userOwnerDisplayName;
	}

	public static FarmLands formJson(JSONObject obj){
		FarmLands f = null;
		try{
			f = new FarmLands();
			f.farmLandCode = obj.getString("FarmLandCode");
			f.farmLandId = obj.getInt("FarmLandId");
			f.farmLandName = obj.getString("FarmLandName");
			f.userOwnerDisplayName = obj.getString("UserOwnerDisplayName");
			f.userOwnerId = obj.getInt("UserOwnerId");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return f;
	}
}
