package com.acctrue.tts.enums;

public enum ChargesStatusEnum {
	Init(0, "未上传"), Uploaded(1, "已上传"), InStock(2, "已入库");
	private int stateId;
	private String stateName;

	private ChargesStatusEnum(int id, String name) {
		this.stateId = id;
		this.stateName = name;
	}

	public int getStateId() {
		return stateId;
	}

	public void setStateId(int stateId) {
		this.stateId = stateId;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	
	public static ChargesStatusEnum getChargesStatusEnumById(int id){
		ChargesStatusEnum cs = ChargesStatusEnum.Init;
		for(ChargesStatusEnum c : ChargesStatusEnum.values()){
			if(c.getStateId() == id){
				cs = c;
				break;
			}
		}
		return cs;
	}

}
