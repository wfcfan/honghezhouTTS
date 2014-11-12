package com.acctrue.tts.model;

public class ChargeCodes {
	private String chargeId;
	private String code;
	private boolean isStoreIn;
	private int state;

	public String getChargeId() {
		return chargeId;
	}

	public void setChargeId(String chargeId) {
		this.chargeId = chargeId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isStoreIn() {
		return isStoreIn;
	}

	public void setStoreIn(boolean isStoreIn) {
		this.isStoreIn = isStoreIn;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "ChargeCodes [chargeId=" + chargeId + ", code=" + code
				+ ", isStoreIn=" + isStoreIn + ", state=" + state + "]";
	}

}
