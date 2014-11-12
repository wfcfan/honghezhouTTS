package com.acctrue.tts.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.acctrue.tts.model.Product;

@SuppressWarnings("serial")
public class GetProductByPageResponse implements Serializable {
	private String errorCode;
	private boolean isError;
	private String message;
	private String newToken;
	private boolean updateToken;
	private List<Product> products;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public boolean isError() {
		return isError;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getNewToken() {
		return newToken;
	}

	public void setNewToken(String newToken) {
		this.newToken = newToken;
	}

	public boolean isUpdateToken() {
		return updateToken;
	}

	public void setUpdateToken(boolean updateToken) {
		this.updateToken = updateToken;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public static GetProductByPageResponse fromJson(JSONObject obj) {
		GetProductByPageResponse gp = null;
		try {
			gp = new GetProductByPageResponse();
			gp.errorCode = obj.getString("ErrorCode");
			gp.isError = obj.getBoolean("IsError");
			gp.message = obj.getString("Message");
			gp.newToken = obj.getString("NewToken");
			gp.updateToken = obj.getBoolean("UpdateToken");
			JSONArray arr = obj.optJSONArray("Products");
			if (arr != null) {
				gp.products = new ArrayList<Product>();
				for (int i = 0; i < arr.length(); i++) {
					Product p = Product.fromJson(arr.getJSONObject(i));
					gp.products.add(p);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return gp;
	}

}
