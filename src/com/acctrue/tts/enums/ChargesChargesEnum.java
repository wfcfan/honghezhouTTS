package com.acctrue.tts.enums;

public enum ChargesChargesEnum {
	Init(0, "起始状态"), Uploaded(1, "已上传"), InStock(2, "已入库");
	private int stateId;
	private String stateName;

	private ChargesChargesEnum(int id, String name) {
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

}
