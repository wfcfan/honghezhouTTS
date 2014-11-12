package com.acctrue.tts.model;

import java.util.ArrayList;
import java.util.List;

public class ChargeStoreIn {
	private String id;
	private int warehouseId;
	private String warehouseName;
	private String actor;
	private String actDate;
	private List<ChargeStoreInCode> codes;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public String getActDate() {
		return actDate;
	}

	public void setActDate(String actDate) {
		this.actDate = actDate;
	}

	public List<ChargeStoreInCode> getCodes() {
		return codes;
	}

	public void setCodes(List<ChargeStoreInCode> codes) {
		this.codes = codes;
	}
	
	public List<String> getCodesStr(){
		List<String> ss = new ArrayList<String>();
		if(codes != null && !codes.isEmpty())
			for(ChargeStoreInCode cc : codes)
				ss.add(cc.getCode());
		return ss;
	}

}
