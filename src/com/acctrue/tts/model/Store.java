package com.acctrue.tts.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;

import com.acctrue.tts.enums.StoreSource;
import com.acctrue.tts.rpc.JsonRest;
import com.acctrue.tts.utils.DateUtil;

@SuppressWarnings("serial")
public class Store implements JsonRest, Serializable {
	private String storeId;
	private String storeNo;
	private Integer storeType;
	private String storeTypeText;
	private Integer storeStatus;
	private String storeStatusText;
	private Integer storeKind;
	private String storeMan;
	private String storeDate;
	private Integer corpId;
	private String corpCode;
	private String corpName;
	private Integer bizCorpId;
	private String bizCorpCode;
	private String bizCorpName;
	private Integer recCorpId;
	private String recCorpCode;
	private String recCorpName;
	private String description;
	private List<StoreItem> item;
	private List<StoreCode> codes;
	private List<StoreCode> removeCodes;
	private String createTime;// 非接口字段
	private StoreSource source;// 非接口字段
	private String weight;

	public Store() {
		;
	}

	public Store(Cursor c) {
		storeId = c.getString(c.getColumnIndex("StoreId"));
		storeNo = c.getString(c.getColumnIndex("StoreNo"));
		storeType = c.getInt(c.getColumnIndex("StoreType"));
		storeTypeText = c.getString(c.getColumnIndex("StoreTypeText"));
		storeStatus = c.getInt(c.getColumnIndex("StoreStatus"));
		storeStatusText = c.getString(c.getColumnIndex("StoreStatusText"));
		storeKind = c.getInt(c.getColumnIndex("StoreKind"));
		storeMan = c.getString(c.getColumnIndex("StoreMan"));
		storeDate = c.getString(c.getColumnIndex("StoreDate"));
		corpId = c.getInt(c.getColumnIndex("CorpId"));
		corpCode = c.getString(c.getColumnIndex("CorpCode"));
		corpName = c.getString(c.getColumnIndex("CorpName"));
		bizCorpId = c.getInt(c.getColumnIndex("BizCorpId"));
		bizCorpCode = c.getString(c.getColumnIndex("BizCorpCode"));
		bizCorpName = c.getString(c.getColumnIndex("BizCorpName"));
		recCorpId = c.getInt(c.getColumnIndex("RecCorpId"));
		recCorpCode = c.getString(c.getColumnIndex("RecCorpCode"));
		recCorpName = c.getString(c.getColumnIndex("RecCorpName"));
		description = c.getString(c.getColumnIndex("Description"));
		createTime = c.getString(c.getColumnIndex("CreateTime"));
		source = StoreSource
				.getStoreSource(c.getInt(c.getColumnIndex("source")));
	}

	@Override
	public String toJson() {
		try {

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	public JSONObject toJsonObject() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("CorpId", corpId);
			obj.put("BizCorpId", bizCorpId);
			obj.put("StoreDate", storeDate);
			obj.put("StoreId", storeId);
			obj.put("StoreNo", storeNo);
			obj.put("StoreType", storeType);
			obj.put("Weight", weight);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}

	public static Store fromJson(JSONObject obj) {
		Store s = null;
		try {
			s = new Store();
			s.storeId = obj.getString("StoreId");
			s.storeNo = obj.getString("StoreNo");
			s.storeType = obj.getInt("StoreType");
			s.storeTypeText = obj.getString("StoreTypeText");
			s.storeStatus = obj.getInt("StoreStatus");
			s.storeStatusText = obj.getString("StoreStatusText");
			s.storeKind = obj.getInt("StoreKind");
			s.storeMan = obj.getString("StoreMan");
			s.storeDate = obj.getString("StoreDate");
			s.corpId = obj.getInt("CorpId");
			s.corpCode = obj.getString("CorpCode");
			s.corpName = obj.getString("CorpName");
			s.bizCorpId = obj.getInt("BizCorpId");
			s.bizCorpCode = obj.getString("BizCorpCode");
			s.bizCorpName = obj.getString("BizCorpName");
			// s.recCorpId = obj.getInt("RecCorpId");
			// s.recCorpCode = obj.getString("RecCorpCode");
			// s.recCorpName = obj.getString("RecCorpName");
			s.description = obj.getString("Description");
			s.createTime = DateUtil.getDatetime();
			s.source = StoreSource.Server;

			JSONArray arr = obj.getJSONArray("Items");
			if (arr != null) {
				s.item = new ArrayList<StoreItem>();
				for (int i = 0; i < arr.length(); i++) {
					StoreItem si = StoreItem.fromJson(arr.getJSONObject(i),
							s.storeId);
					s.item.add(si);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return s;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getStoreNo() {
		return storeNo;
	}

	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}

	public Integer getStoreType() {
		return storeType;
	}

	public void setStoreType(Integer storeType) {
		this.storeType = storeType;
	}

	public String getStoreTypeText() {
		return storeTypeText;
	}

	public void setStoreTypeText(String storeTypeText) {
		this.storeTypeText = storeTypeText;
	}

	public Integer getStoreStatus() {
		return storeStatus;
	}

	public void setStoreStatus(Integer storeStatus) {
		this.storeStatus = storeStatus;
	}

	public String getStoreStatusText() {
		return storeStatusText;
	}

	public void setStoreStatusText(String storeStatusText) {
		this.storeStatusText = storeStatusText;
	}

	public Integer getStoreKind() {
		return storeKind;
	}

	public String getStoreKindText() {
		if (storeKind == null)
			return "";
		return storeKind == 0 ? "入库" : "出库";
	}

	public void setStoreKind(Integer storeKind) {
		this.storeKind = storeKind;
	}

	public String getStoreMan() {
		return storeMan;
	}

	public void setStoreMan(String storeMan) {
		this.storeMan = storeMan;
	}

	public String getStoreDate() {
		return storeDate;
	}

	public String getStoreDateText() {
		if (storeDate != null && !storeDate.equals("")) {
			return DateUtil.transDate(storeDate);
		}
		return "";
	}

	public void setStoreDate(String storeDate) {
		this.storeDate = storeDate;
	}

	public Integer getCorpId() {
		return corpId;
	}

	public void setCorpId(Integer corpId) {
		this.corpId = corpId;
	}

	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	public Integer getBizCorpId() {
		return bizCorpId;
	}

	public void setBizCorpId(Integer bizCorpId) {
		this.bizCorpId = bizCorpId;
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

	public Integer getRecCorpId() {
		return recCorpId;
	}

	public void setRecCorpId(Integer recCorpId) {
		this.recCorpId = recCorpId;
	}

	public String getRecCorpCode() {
		return recCorpCode;
	}

	public void setRecCorpCode(String recCorpCode) {
		this.recCorpCode = recCorpCode;
	}

	public String getRecCorpName() {
		return recCorpName;
	}

	public void setRecCorpName(String recCorpName) {
		this.recCorpName = recCorpName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<StoreItem> getItem() {
		return item;
	}

	public void setItem(List<StoreItem> item) {
		this.item = item;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public StoreSource getSource() {
		return source;
	}

	public void setSource(StoreSource source) {
		this.source = source;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public List<StoreCode> getCodes() {
		return codes;
	}

	public void setCodes(List<StoreCode> codes) {
		this.codes = codes;
	}

	public List<StoreCode> getRemoveCodes() {
		return removeCodes;
	}

	public void setRemoveCodes(List<StoreCode> removeCodes) {
		this.removeCodes = removeCodes;
	}

}
