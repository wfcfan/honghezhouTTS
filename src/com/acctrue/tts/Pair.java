package com.acctrue.tts;

public class Pair {
	private int key;
	private String value;

	public Pair() {
		;
	}

	public Pair(int k, String v) {
		this.key = k;
		this.value = v;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
