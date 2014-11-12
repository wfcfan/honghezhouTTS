package com.acctrue.tts.model;

import java.io.Serializable;
import java.util.UUID;

import org.json.JSONObject;

@SuppressWarnings("serial")
public class StoreItem implements Serializable {
	private String itemId;
	private String storeId;
	private String produceBatchNo;
	private String productCode;
	private String productName;
	private int amount;
	private int displayAmount;
	private String displayProductUnit;
	
	
	public static StoreItem fromJson(JSONObject obj,String storeId){
		StoreItem si = null;
		try{
			si = new StoreItem();
			si.itemId = UUID.randomUUID().toString();
			si.storeId = storeId;
			si.produceBatchNo = obj.getString("ProduceBatchNo");
			si.productCode = obj.getString("ProductCode");
			si.productName = obj.getString("ProductName");
			si.amount = obj.getInt("Amount");
			si.displayAmount = obj.getInt("DisplayAmount");
			si.displayProductUnit = obj.getString("DisplayProductUnit");
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return si;
	}


	public String getItemId() {
		return itemId;
	}


	public void setItemId(String itemId) {
		this.itemId = itemId;
	}


	public String getStoreId() {
		return storeId;
	}


	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}


	public String getProduceBatchNo() {
		return produceBatchNo;
	}


	public void setProduceBatchNo(String produceBatchNo) {
		this.produceBatchNo = produceBatchNo;
	}


	public String getProductCode() {
		return productCode;
	}


	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}


	public String getProductName() {
		return productName;
	}


	public void setProductName(String productName) {
		this.productName = productName;
	}


	public int getAmount() {
		return amount;
	}


	public void setAmount(int amount) {
		this.amount = amount;
	}


	public int getDisplayAmount() {
		return displayAmount;
	}


	public void setDisplayAmount(int displayAmount) {
		this.displayAmount = displayAmount;
	}


	public String getDisplayProductUnit() {
		return displayProductUnit;
	}


	public void setDisplayProductUnit(String displayProductUnit) {
		this.displayProductUnit = displayProductUnit;
	}


}
