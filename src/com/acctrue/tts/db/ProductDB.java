package com.acctrue.tts.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.acctrue.tts.Constants;
import com.acctrue.tts.model.Product;

public final class ProductDB {
	private SQLiteDatabase db = null;
	private final Context _ctx;
	private static final String TABLE_PRODUCTS = "Products";

	public ProductDB(Context ctx) {
		_ctx = ctx;
	}

	public void addProduct(Product prod) {
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		ContentValues values = new ContentValues();
		values.put("ProductId", prod.getProductId());
		values.put("ProductName", prod.getProductName());
		db.insert(TABLE_PRODUCTS, null, values);
		db.close();
	}

	public void deleteProduct(int prodId) {
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		db.delete(TABLE_PRODUCTS, "ProductId=" + prodId, null);
		db.close();
	}

	public void deleteAll() {
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		db.delete(TABLE_PRODUCTS, "1=1", null);
		db.close();
	}

	public List<Product> getProducts() {
		List<Product> prodList = new ArrayList<Product>();
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);

		Cursor c = db.query(TABLE_PRODUCTS, new String[] { "ProductId",
				"ProductName" }, null, null, null, null, null);

		while (c.moveToNext()) {
			Product prod = new Product(
					c.getInt(c.getColumnIndex("ProductId")), c.getString(c
							.getColumnIndex("ProductName")));
			prodList.add(prod);
		}
		c.close();
		db.close();

		return prodList;
	}

	public Product getProduct(int prodId) {
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		Cursor c = db.query(TABLE_PRODUCTS, new String[] { "ProductId",
				"ProductName" }, "ProductId=" + prodId, null, null, null, null);
		Product prod = null;
		while (c.moveToNext()) {
			prod = new Product(c.getInt(c.getColumnIndex("ProductId")),
					c.getString(c.getColumnIndex("ProductName")));

		}
		c.close();
		db.close();
		return prod;
	}

}
