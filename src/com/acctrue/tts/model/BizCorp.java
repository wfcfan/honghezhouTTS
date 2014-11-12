package com.acctrue.tts.model;

import org.json.JSONObject;

public class BizCorp {
	private int corpId;
	private String corpName;
	private String corpCode;
	private String corpPinYin;

	public BizCorp(){
		;
	}
	public BizCorp(int cid,String code,String name){
		this.corpId=cid;
		this.corpCode = code;
		this.corpName = name;
	}
	
	public int getCorpId() {
		return corpId;
	}

	public void setCorpId(int corpId) {
		this.corpId = corpId;
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public String getCorpPinYin() {
		return corpPinYin;
	}

	public void setCorpPinYin(String corpPinYin) {
		this.corpPinYin = corpPinYin;
	}
	
	public static BizCorp formJson(JSONObject obj){
		BizCorp b = null;
		try{
			b = new BizCorp();
			b.corpId = obj.getInt("CorpId");
			b.corpCode = obj.getString("CorpCode");
			b.corpName = obj.getString("CorpName");
			b.corpPinYin = obj.getString("CorpPinyin");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return b;
	}

}
