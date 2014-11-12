package com.acctrue.tts.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.acctrue.tts.Constants;
import com.acctrue.tts.model.Warehouse;

public class WarehouseDB {

	private static final String TABLE_WAREHOUSE = "Warehouse";
	private SQLiteDatabase db = null;
	private final Context _ctx;

	public WarehouseDB(Context ctx) {
		_ctx = ctx;
	}

	public void addWarehouse(Warehouse w) {
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		ContentValues values = new ContentValues();
		values.put("CorpId", w.getCorpId());
		values.put("WarehouseId", w.getWarehouseId());
		values.put("WarehouseName", w.getWarehouseName());
		db.insert(TABLE_WAREHOUSE, null, values);
		db.close();
	}

	public int getWarehouseIdByName(String name) {
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		Cursor c = db.rawQuery(
				"select WarehouseId from Warehouse where WarehouseName=?",
				new String[] { name });
		int wid = c.getInt(0);
		db.close();
		return wid;
	}

	public void deleteWarehouse(int wid) {
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		db.delete(TABLE_WAREHOUSE, "WarehouseId=" + wid, null);
		db.close();
	}

	public Warehouse getWarehouse(int wid) {
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		Cursor c = db.query(TABLE_WAREHOUSE, new String[] { "CorpId",
				"WarehouseId", "WarehouseName" }, "WarehouseId=" + wid, null,
				null, null, null);

		Warehouse w = null;
		while (c.moveToNext()) {
			w = new Warehouse();
			w.setCorpId(c.getInt(c.getColumnIndex("CorpId")));
			w.setWarehouseId(c.getInt(c.getColumnIndex("WarehouseId")));
			w.setWarehouseName(c.getString(c.getColumnIndex("WarehouseName")));
		}

		c.close();
		db.close();
		return w;
	}

	public List<Warehouse> getWarehouses() {
		List<Warehouse> list = new ArrayList<Warehouse>();
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		Cursor c = db.query(TABLE_WAREHOUSE, new String[] { "CorpId",
				"WarehouseId", "WarehouseName" }, null, null, null, null, null);
		while (c.moveToNext()) {
			Warehouse w = new Warehouse();
			w.setCorpId(c.getInt(c.getColumnIndex("CorpId")));
			w.setWarehouseId(c.getInt(c.getColumnIndex("WarehouseId")));
			w.setWarehouseName(c.getString(c.getColumnIndex("WarehouseName")));
			list.add(w);
		}
		c.close();
		db.close();
		
		return list;
	}

}
