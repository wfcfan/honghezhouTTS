package com.acctrue.tts.model;

public class PackCode {
	private String id;
	private String orderId;
	private int orderItemId;
	private String codeValue;
	private String parentCode;
	private int byDelete;
	private int isParentScanCode;
	private String actor;
	private String actDate;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public int getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(int orderItemId) {
		this.orderItemId = orderItemId;
	}
	public String getCodeValue() {
		return codeValue;
	}
	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	public int getByDelete() {
		return byDelete;
	}
	public void setByDelete(int byDelete) {
		this.byDelete = byDelete;
	}
	public int getIsParentScanCode() {
		return isParentScanCode;
	}
	public void setIsParentScanCode(int isParentScanCode) {
		this.isParentScanCode = isParentScanCode;
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
}
