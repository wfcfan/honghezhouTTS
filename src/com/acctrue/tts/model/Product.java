package com.acctrue.tts.model;

import org.json.JSONObject;

public class Product {

	private int productId;
	private String productName;

	public Product() {
		;
	}

	public Product(int pid, String prodName) {
		this.productId = pid;
		this.productName = prodName;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public static Product fromJson(JSONObject obj) {
		Product p = null;
		
		try {
			p = new Product();
			p.productId = obj.getInt("ProductId");
			p.productName = obj.getString("ProductName");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return p;
	}

}
