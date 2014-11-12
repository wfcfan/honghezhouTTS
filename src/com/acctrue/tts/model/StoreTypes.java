package com.acctrue.tts.model;

import java.io.Serializable;

import org.json.JSONObject;

@SuppressWarnings("serial")
public class StoreTypes implements Serializable {

	private String id;
	private boolean hasBizCorp;
	private boolean hasRecCorp;
	private int storeKind;
	private int storeSort;
	private int storeType;
	private String storeTypeKey;
	private String storeTypeText;
	private Integer upStoreType;
	private Integer downStoreType;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isHasBizCorp() {
		return hasBizCorp;
	}

	public void setHasBizCorp(boolean hasBizCorp) {
		this.hasBizCorp = hasBizCorp;
	}

	public boolean isHasRecCorp() {
		return hasRecCorp;
	}

	public void setHasRecCorp(boolean hasRecCorp) {
		this.hasRecCorp = hasRecCorp;
	}

	public int getStoreKind() {
		return storeKind;
	}

	public void setStoreKind(int storeKind) {
		this.storeKind = storeKind;
	}

	public int getStoreSort() {
		return storeSort;
	}

	public void setStoreSort(int storeSort) {
		this.storeSort = storeSort;
	}

	public int getStoreType() {
		return storeType;
	}

	public void setStoreType(int storeType) {
		this.storeType = storeType;
	}

	public String getStoreTypeKey() {
		return storeTypeKey;
	}

	public void setStoreTypeKey(String storeTypeKey) {
		this.storeTypeKey = storeTypeKey;
	}

	public String getStoreTypeText() {
		return storeTypeText;
	}

	public void setStoreTypeText(String storeTypeText) {
		this.storeTypeText = storeTypeText;
	}

	public Integer getUpStoreType() {
		return upStoreType;
	}

	public void setUpStoreType(Integer upStoreType) {
		this.upStoreType = upStoreType;
	}

	public Integer getDownStoreType() {
		return downStoreType;
	}

	public void setDownStoreType(Integer downStoreType) {
		this.downStoreType = downStoreType;
	}

	public static StoreTypes fromJson(JSONObject obj) {
		StoreTypes st = null;

		try {
			st = new StoreTypes();

			if (!obj.getString("DownStoreType").equals("null")) {
				st.downStoreType = obj.getInt("DownStoreType");
			}
			st.hasBizCorp = obj.getBoolean("HasBizCorp");
			st.hasRecCorp = obj.getBoolean("HasRecCorp");
			st.storeKind = obj.getInt("StoreKind");
			st.storeSort = obj.getInt("StoreSort");
			st.storeType = obj.getInt("StoreType");
			st.storeTypeKey = obj.getString("StoreTypeKey");
			st.storeTypeText = obj.getString("StoreTypeText");
			if (!obj.getString("UpStoreType").equals("null")) {
				st.upStoreType = obj.getInt("UpStoreType");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return st;
	}

}
