package com.acctrue.tts.dto;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.acctrue.tts.GlobalApplication;
import com.acctrue.tts.db.BizCorpsDB;
import com.acctrue.tts.model.BizCorp;
import com.acctrue.tts.model.OrderInfo;
import com.acctrue.tts.model.PackCode;
import com.acctrue.tts.rpc.JsonRest;
import com.acctrue.tts.utils.AccountUtil;
import com.acctrue.tts.utils.DateUtil;

@Deprecated
@SuppressWarnings("serial")
public class OrderInfoRequest implements JsonRest{
	private OrderInfo order;
	private List<PackCode> packCodes;
	private List<PackCode> delPackCodes;
	
	public OrderInfoRequest(OrderInfo order,
			List<PackCode> packCodes,List<PackCode> delPackCodes){
		this.order = order;
		this.packCodes = packCodes;
		this.delPackCodes = delPackCodes;
	}
	
	public String getStoreId(){
		return order.getStoreId();
	}

	private BizCorp getBizCorp(int corpId){
		BizCorpsDB db = new BizCorpsDB(GlobalApplication.mApp);
		BizCorp corp = db.getBizCorp(corpId);
		if(corp == null){
			corp = new BizCorp(corpId,"","");
		}
		return corp;
	}
	
	@Override
	public String toJson() {
		JSONObject json = new JSONObject();
		
		try {
			//sign
			json.put("sign", AccountUtil.getDefaultSign().toJsonObject());
			//codes,removeCodes
			addCode(json,packCodes,"codes");
			addCode(json,delPackCodes,"removeCodes");
			//info
			JSONObject info = new JSONObject();
			info.put("Items", new JSONArray());
			info.put("BizCorpId", order.getCorpId());
			info.put("BizCorpCode", order.getBizCorpCode());
			info.put("BizCorpName", order.getBizCorpName());
			
			BizCorp corp = this.getBizCorp(order.getCorpId());
			info.put("CorpCode", corp.getCorpCode());
			info.put("CorpId", corp.getCorpId());
			info.put("CorpName", corp.getCorpName());
			info.put("Description", "");
			//info.put("RecCorpId", "");
			//info.put("RecCorpCode", order.getBizCorpCodeZD());
			//info.put("RecCorpName", order.getBizCorpNameZD());
			info.put("StoreDate", DateUtil.parseDatetimeToJsonDate());
			info.put("StoreId", order.getStoreId());
			info.put("StoreKind", order.getOrderKind());
			info.put("StoreMan", order.getActor());
			info.put("StoreNo", order.getOrderNo());
			info.put("StoreStatus", order.getFlag());
			info.put("StoreType", order.getOrderType());
			info.put("StoreTypeText", order.getOrderTypeText());

			json.put("info", info);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return json.toString();
	}
	
	private void addCode(JSONObject json,List<PackCode> codes,String key) throws JSONException{
		JSONArray arr = new JSONArray();
		for(PackCode item : codes){
			JSONObject code = new JSONObject();
			code.put("ActTime", DateUtil.parseDatetimeToJsonDate(item.getActDate()));
			code.put("Actor", item.getActor());
			code.put("ByParent", item.getIsParentScanCode() == 1);
			code.put("CodeId", item.getCodeValue());
			code.put("IsParentCode", item.getIsParentScanCode() == 1);
			code.put("SavedCodeId", item.getId());
			code.put("SavedCodeLevel", 0);
			code.put("SavedCount", 1);
			arr.put(code);
		}
		json.put(key, arr);
	}

}
