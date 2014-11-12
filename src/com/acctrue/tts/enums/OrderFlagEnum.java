package com.acctrue.tts.enums;

public enum OrderFlagEnum {
	General(0, "普通单据"), Task(1, "任务单据");

	private int id;
	private String name;

	private OrderFlagEnum(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
