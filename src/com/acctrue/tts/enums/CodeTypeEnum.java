package com.acctrue.tts.enums;

public enum CodeTypeEnum {
	ChargesCode(0, "收取码"), TrackCode(1, "追溯码");

	private int id;
	private String name;

	private CodeTypeEnum(int id, String name) {
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
	
	public static CodeTypeEnum getCodeTypeEnumByName(String name){
		CodeTypeEnum ct = CodeTypeEnum.ChargesCode;
		for(CodeTypeEnum c :CodeTypeEnum.values()){
			if(c.getName().equals(name)){
				ct = c;
				break;
			}
		}
		return ct;
	}
	
	public static CodeTypeEnum getCodeTypeEnumById(int id){
		CodeTypeEnum ct = CodeTypeEnum.ChargesCode;
		for(CodeTypeEnum c :CodeTypeEnum.values()){
			if(c.getId() == id){
				ct = c;
				break;
			}
		}
		return ct;
	}

}
