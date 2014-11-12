package com.acctrue.tts.model;

import org.json.JSONObject;

public class Warehouse {

	private int corpId;
	private int warehouseId;
	private String warehouseName;

	public Warehouse() {
		;
	}

	public Warehouse(int cid, int wid, String name) {
		this.corpId = cid;
		this.warehouseId = wid;
		this.warehouseName = name;
	}

	public int getCorpId() {
		return corpId;
	}

	public void setCorpId(int corpId) {
		this.corpId = corpId;
	}

	public int getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(int warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	
	public static Warehouse fromJson(JSONObject obj){
		Warehouse w = null;
		try{
			w = new Warehouse();
			w.corpId = obj.getInt("CorpId");
			w.warehouseId = w.corpId;
			w.warehouseName = obj.getString("CorpName");
			
		}catch(Exception ex){
			
		}
		return w;
	}

}
