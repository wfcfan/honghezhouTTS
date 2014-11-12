package com.acctrue.tts.model;

import org.json.JSONObject;

public class OrderItemInfo {
	private int id;
	private String orderId;
	private String productName;
	private String productCode;
	private String batchNo;
	private float editAmount;
	private String displayUnit;
	private float scanAmount;
	private float scanMinAmount;
	private float displayAmount;
	private float minAmount;
	private String unit;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public float getEditAmount() {
		return editAmount;
	}

	public void setEditAmount(float editAmount) {
		this.editAmount = editAmount;
	}

	public String getDisplayUnit() {
		return displayUnit;
	}

	public void setDisplayUnit(String displayUnit) {
		this.displayUnit = displayUnit;
	}

	public float getScanAmount() {
		return scanAmount;
	}

	public void setScanAmount(float scanAmount) {
		this.scanAmount = scanAmount;
	}

	public float getScanMinAmount() {
		return scanMinAmount;
	}

	public void setScanMinAmount(float scanMinAmount) {
		this.scanMinAmount = scanMinAmount;
	}

	public float getDisplayAmount() {
		return displayAmount;
	}

	public void setDisplayAmount(float displayAmount) {
		this.displayAmount = displayAmount;
	}

	public float getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(float minAmount) {
		this.minAmount = minAmount;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public static OrderItemInfo fromJson(JSONObject obj,String storeId){
		OrderItemInfo item = null;
		try{
			item = new OrderItemInfo();
			item.displayAmount = obj.getInt("DisplayAmount");
			item.scanAmount = obj.getInt("Amount");
			item.unit = obj.getString("DisplayProductUnit");
			item.batchNo = obj.getString("ProduceBatchNo");
			item.productCode = obj.getString("ProductCode");
			item.productName = obj.getString("ProductName");
			item.orderId = storeId;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return item;
	}

}
