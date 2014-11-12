package com.acctrue.tts.dto;

import java.io.Serializable;
import java.util.UUID;

import org.json.JSONObject;

import com.acctrue.tts.model.StoreCode;
import com.acctrue.tts.utils.DateUtil;

@SuppressWarnings("serial")
public class ScanCodeResponse implements Serializable {
	private String errorCode;
	private boolean isError;
	private String message;
	private String newToken;
	private boolean updateToken;
	private int codeCount;
	private String codeId;
	private int currentAmount;
	private String innerPacking;
	private String parentId;
	private String produceBatchNo;
	private String productCode;
	private String productName;
	private String productUnit;
	private String savedCodeId;

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

	public int getCodeCount() {
		return codeCount;
	}

	public void setCodeCount(int codeCount) {
		this.codeCount = codeCount;
	}

	public String getCodeId() {
		return codeId;
	}

	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}

	public int getCurrentAmount() {
		return currentAmount;
	}

	public void setCurrentAmount(int currentAmount) {
		this.currentAmount = currentAmount;
	}

	public String getInnerPacking() {
		return innerPacking;
	}

	public void setInnerPacking(String innerPacking) {
		this.innerPacking = innerPacking;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
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

	public String getProductUnit() {
		return productUnit;
	}

	public void setProductUnit(String productUnit) {
		this.productUnit = productUnit;
	}

	public String getAavedCodeId() {
		return savedCodeId;
	}

	public void setAavedCodeId(String aavedCodeId) {
		this.savedCodeId = aavedCodeId;
	}
	
	public StoreCode toStoreCode(String storeId){
		StoreCode sc = new StoreCode();
		sc.setCodeCount(codeCount);
		sc.setCodeId(codeId);
		sc.setCreateTime(DateUtil.getDatetime());
		sc.setCurrentAmount(currentAmount);
		sc.setInnerPacking(innerPacking);
		sc.setIsDelete(StoreCode.DELETE_FALSE);
		sc.setParentId(parentId);
		sc.setProduceBatchNo(produceBatchNo);
		sc.setProductCode(productCode);
		sc.setProductName(productName);
		sc.setProductUnit(productUnit);
		sc.setSavedCodeId(savedCodeId);
		sc.setStoreId(storeId);
		sc.setId(UUID.randomUUID().toString());
		return sc;
	}

	public static ScanCodeResponse fromJson(JSONObject obj) {
		ScanCodeResponse sc = null;
		try {
			sc = new ScanCodeResponse();
			sc.errorCode = obj.getString("ErrorCode");
			sc.isError = obj.getBoolean("IsError");
			sc.message = obj.getString("Message");
			sc.newToken = obj.getString("NewToken");
			sc.updateToken = obj.getBoolean("UpdateToken");
			sc.codeCount = obj.getInt("CodeCount");
			sc.codeId = obj.getString("CodeId");
			sc.currentAmount = obj.getInt("CurrentAmount");
			sc.parentId = obj.getString("ParentId");
			sc.produceBatchNo = obj.getString("ProduceBatchNo");
			sc.productCode = obj.getString("ProductCode");
			sc.productName = obj.getString("ProductName");
			sc.productUnit = obj.getString("ProductUnit");
			sc.savedCodeId = obj.getString("SavedCodeId");
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return sc;
	}

}