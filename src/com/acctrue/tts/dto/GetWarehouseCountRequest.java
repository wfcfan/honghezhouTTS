package com.acctrue.tts.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GetWarehouseCountRequest implements Serializable{
	private Sign sign;

	public Sign getSign() {
		return sign;
	}

	public void setSign(Sign sign) {
		this.sign = sign;
	}
}
