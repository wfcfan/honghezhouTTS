package com.acctrue.tts.enums;

public enum StoreSource {
	Local(0, "非任务"), Server(1, "任务");

	private int id;
	private String name;

	private StoreSource(int id, String name) {
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
	
	public static StoreSource getStoreSource(int id){
		StoreSource ss = null;
		for(StoreSource s :StoreSource.values()){
			if(s.id == id){
				ss = s;
				break;
			}
		}
		return ss;
	}
	

}
