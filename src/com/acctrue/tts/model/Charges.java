package com.acctrue.tts.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.acctrue.tts.dto.LoginResponse.UserInfo;
import com.acctrue.tts.enums.ChargesStatusEnum;
import com.acctrue.tts.enums.CodeTypeEnum;
import com.acctrue.tts.rpc.JsonRest;
import com.acctrue.tts.utils.AccountUtil;
import com.acctrue.tts.utils.DateUtil;

@SuppressWarnings("serial")
public class Charges implements JsonRest, Serializable {
	private String id;
	private String batchno;
	private String manNo;
	private String farmlandNo;
	private String productId;
	private String createDate;
	private String man;
	private int state;
	private int isPackCode;
	private String weight;
	private List<String> codes;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBatchno() {
		return batchno;
	}

	public void setBatchno(String batchno) {
		this.batchno = batchno;
	}

	public String getManNo() {
		return manNo;
	}

	public void setManNo(String manNo) {
		this.manNo = manNo;
	}

	public String getFarmlandNo() {
		return farmlandNo;
	}

	public void setFarmlandNo(String farmlandNo) {
		this.farmlandNo = farmlandNo;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getMan() {
		return man;
	}

	public void setMan(String man) {
		this.man = man;
	}

	public int getState() {
		return state;
	}

	public final String getStateName() {
		ChargesStatusEnum statusEnum = ChargesStatusEnum
				.getChargesStatusEnumById(state);
		return statusEnum.getStateName();
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getIsPackCode() {
		return isPackCode;
	}

	public void setIsPackCode(int isPackCode) {
		this.isPackCode = isPackCode;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public final String getPackCodeName() {
		CodeTypeEnum ct = CodeTypeEnum.getCodeTypeEnumById(isPackCode);
		return ct.getName();
	}

	public List<String> getCodes() {
		return codes;
	}

	public void setCodes(List<String> codes) {
		this.codes = codes;
	}
	
	public void setChargeCodes(List<ChargeCodes> codes){
		List<String> list = new ArrayList<String>();
		for(ChargeCodes c : codes){
			list.add(c.getCode());
		}
		this.codes = list;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null)
			return false;
		if(o instanceof Charges){
			Charges c = (Charges)o;
			return this.batchno.equals(c.batchno);
		}else{
			return false;
		}
	}

	@Override
	public String toString() {
		return "Charges [id=" + id + ", batchno=" + batchno + ", manNo="
				+ manNo + ", farmlandNo=" + farmlandNo + ", productId="
				+ productId + ", createDate=" + createDate + ", man=" + man
				+ ", state=" + state + ", isPackCode=" + isPackCode
				+ ", weight=" + weight + "]";
	}

	@Override
	public String toJson() {
		return toJsonObject().toString();
	}

	public JSONObject toJsonObject() {
		JSONObject obj = new JSONObject();
		try {
			UserInfo u = AccountUtil.getCurrentUser().getUserInfo();
			obj.put("Actor", this.man);
			obj.put("ActorDate", DateUtil.parseDatetimeToJsonDate());
			obj.put("BatchNo", this.batchno);
			obj.put("FarmlandCode", this.farmlandNo);
			obj.put("IsAutoStorage", this.state);
			obj.put("ProductId", this.productId);
			obj.put("UserId", u.getUserId());
			obj.put("Weight", this.weight);
			
			if (this.codes != null && !this.codes.isEmpty()) {
				JSONArray arr = new JSONArray();
				for (String s : this.codes) {
					arr.put(s);
				}
				obj.put("Codes", arr);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}

}
