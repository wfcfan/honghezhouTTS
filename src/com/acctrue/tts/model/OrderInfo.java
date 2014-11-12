package com.acctrue.tts.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.acctrue.tts.utils.DateUtil;
import com.acctrue.tts.utils.Util;

@Deprecated
public class OrderInfo {
	private String StoreId;
	private int corpId;
	private String orderNo;
	private int orderType;
	private String orderKind;
	private String orderTypeText;
	private String bizCorpCode;
	private String bizCorpName;
	private String bizCorpCodeZD;
	private String bizCorpNameZD;
	private String actor;
	private String actDate;
	private int flag;
	private int userId;
	private int storeStatus;
	private List<OrderItemInfo> orderItem;
	
	public String getStoreId() {
		return StoreId;
	}

	public void setStoreId(String storeId) {
		StoreId = storeId;
	}

	public int getCorpId() {
		return corpId;
	}

	public void setCorpId(int corpId) {
		this.corpId = corpId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public String getOrderKind() {
		return orderKind;
	}

	public void setOrderKind(String orderKind) {
		this.orderKind = orderKind;
	}

	public String getOrderTypeText() {
		return orderTypeText;
	}

	public void setOrderTypeText(String orderTypeText) {
		this.orderTypeText = orderTypeText;
	}

	public String getBizCorpCode() {
		return bizCorpCode;
	}

	public void setBizCorpCode(String bizCorpCode) {
		this.bizCorpCode = bizCorpCode;
	}

	public String getBizCorpName() {
		return bizCorpName;
	}

	public void setBizCorpName(String bizCorpName) {
		this.bizCorpName = bizCorpName;
	}

	public String getBizCorpCodeZD() {
		return bizCorpCodeZD;
	}

	public void setBizCorpCodeZD(String bizCorpCodeZD) {
		this.bizCorpCodeZD = bizCorpCodeZD;
	}

	public String getBizCorpNameZD() {
		return bizCorpNameZD;
	}

	public void setBizCorpNameZD(String bizCorpNameZD) {
		this.bizCorpNameZD = bizCorpNameZD;
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
	
	public String getActDateString(){
		return DateUtil.transDate(actDate);
	}

	public void setActDate(String actDate) {
		this.actDate = actDate;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public List<OrderItemInfo> getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(List<OrderItemInfo> orderItem) {
		this.orderItem = orderItem;
	}

	public int getStoreStatus() {
		return storeStatus;
	}

	public void setStoreStatus(int storeStatus) {
		this.storeStatus = storeStatus;
	}

	public static OrderInfo fromJson(JSONObject obj){
		OrderInfo order = null;
		try{
			order = new OrderInfo();
			order.corpId = obj.getInt("CorpId");
			order.bizCorpCode = obj.getString("BizCorpCode");
			order.bizCorpName = obj.getString("BizCorpName");
			order.bizCorpCodeZD = Util.getNullText(obj.getString("RecCorpCode"));
			order.bizCorpNameZD = Util.getNullText(obj.getString("RecCorpName"));
			order.actDate = obj.getString("StoreDate");
			order.StoreId = obj.getString("StoreId");
			order.orderKind = obj.getString("StoreKind");
			order.orderNo = obj.getString("StoreNo");
			order.orderType = obj.getInt("StoreType");
			order.orderTypeText = obj.getString("StoreTypeText");
			order.storeStatus = obj.getInt("StoreStatus");
			order.flag = 1;//从接口下载的默认为电商单据
			
			JSONArray arr = obj.getJSONArray("Items");
			if(arr != null){
				order.orderItem = new ArrayList<OrderItemInfo>();
				for(int i=0;i<arr.length();i++){
					OrderItemInfo item = OrderItemInfo.fromJson(arr.getJSONObject(i),order.StoreId);
					order.orderItem.add(item);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return order;
	}
}
