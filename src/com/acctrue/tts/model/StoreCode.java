package com.acctrue.tts.model;

import org.json.JSONObject;

import com.acctrue.tts.utils.AccountUtil;
import com.acctrue.tts.utils.DateUtil;

public class StoreCode  {
	public static final int DELETE_TRUE = 1;
	public static final int DELETE_FALSE = 0;
	
	private String id;
	private String storeId;
	private String codeId;
	private int codeCount;
	private String parentId;
	private String savedCodeId;
	private String productCode;
	private String productName;
	private String innerPacking;
	private String produceBatchNo;
	private String productUnit;
	private int currentAmount;
	private String createTime;
	private int isDelete;
	
	public static StoreCode formJson(JSONObject obj){
		StoreCode sc = null;
		try{
			sc = new StoreCode();
			sc.id = obj.getString("Id");
			sc.storeId = obj.getString("StoreId");
			sc.codeId = obj.getString("CodeId");
			sc.codeCount = obj.getInt("CodeCount");
			sc.parentId = obj.getString("ParentId");
			sc.savedCodeId = obj.getString("SavedCodeId");
			sc.productCode = obj.getString("ProductCode");
			sc.productName = obj.getString("ProductName");
			sc.innerPacking = obj.getString("InnerPacking");
			sc.produceBatchNo = obj.getString("ProduceBatchNo");
			sc.productUnit = obj.getString("ProductUnit");
			sc.currentAmount = obj.getInt("CurrentAmount");
			sc.createTime = obj.getString("CreateTime");
			sc.isDelete = obj.getInt("IsDelete");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return sc;
	}
	
	public JSONObject toJsonObject(){
		JSONObject obj = new JSONObject();
		try{
			obj.put("ActTime",DateUtil.parseDatetimeToJsonDate(createTime));
			obj.put("Actor", AccountUtil.getCurrentUser().getUserInfo().getUserName());
			obj.put("ByParent", false);
			obj.put("CodeId", codeId);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return obj;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getCodeId() {
		return codeId;
	}

	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}

	public int getCodeCount() {
		return codeCount;
	}

	public void setCodeCount(int codeCount) {
		this.codeCount = codeCount;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getSavedCodeId() {
		return savedCodeId;
	}

	public void setSavedCodeId(String savedCodeId) {
		this.savedCodeId = savedCodeId;
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

	public String getInnerPacking() {
		return innerPacking;
	}

	public void setInnerPacking(String innerPacking) {
		this.innerPacking = innerPacking;
	}

	public String getProduceBatchNo() {
		return produceBatchNo;
	}

	public void setProduceBatchNo(String produceBatchNo) {
		this.produceBatchNo = produceBatchNo;
	}

	public String getProductUnit() {
		return productUnit;
	}

	public void setProductUnit(String productUnit) {
		this.productUnit = productUnit;
	}

	public int getCurrentAmount() {
		return currentAmount;
	}

	public void setCurrentAmount(int currentAmount) {
		this.currentAmount = currentAmount;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

}
