package com.acctrue.tts.dto;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.acctrue.tts.rpc.JsonRest;

@SuppressWarnings("serial")
public class GetBizCorpsPageRequest implements JsonRest,Serializable {
	private Sign sign;
	private int pageIndex;
	private int pageSize;

	public Sign getSign() {
		return sign;
	}

	public void setSign(Sign sign) {
		this.sign = sign;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	@Override
	public String toJson() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("pageIndex", pageIndex);
			obj.put("pageSize", pageSize);
			obj.put("sign", sign.toJsonObject());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return obj.toString();
	} 

}
