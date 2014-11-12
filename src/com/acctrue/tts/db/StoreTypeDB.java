package com.acctrue.tts.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.acctrue.tts.Constants;
import com.acctrue.tts.model.StoreTypes;

public class StoreTypeDB {
	private SQLiteDatabase db = null;
	private final Context _ctx;
	private static final String TABLE_NAME = "StoreTypes";

	public StoreTypeDB(Context ctx) {
		_ctx = ctx;
	}

	public void addStoreType(StoreTypes st) {
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);

		ContentValues values = new ContentValues();
		values.put("DownStoreType", st.getDownStoreType());
		values.put("HasBizCorp", st.isHasBizCorp());
		values.put("HasRecCorp", st.isHasRecCorp());
		values.put("StoreKind", st.getStoreKind());
		values.put("StoreSort", st.getStoreSort());
		values.put("StoreType", st.getStoreType());
		values.put("StoreTypeKey", st.getStoreTypeKey());
		values.put("StoreTypeText", st.getStoreTypeText());
		values.put("UpStoreType", st.getUpStoreType());

		db.insert(TABLE_NAME, null, values);
		db.close();
	}

	public void delStoreTypes() {
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		db.delete(TABLE_NAME, null, null);
		db.close();
	}

	public List<StoreTypes> getStoreTypes() {
		List<StoreTypes> list = new ArrayList<StoreTypes>();
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);

		Cursor c = db.query(TABLE_NAME, new String[] { "DownStoreType",
				"HasBizCorp", "HasRecCorp", "StoreKind", "StoreSort",
				"StoreType", "StoreTypeKey", "StoreTypeText", "UpStoreType" },
				null, null, null, null, null);
		
		while (c.moveToNext()) {
			StoreTypes s = new StoreTypes();
			s.setDownStoreType(c.getInt(c.getColumnIndex("DownStoreType")));
			s.setHasBizCorp(c.getInt(c.getColumnIndex("HasRecCorp")) == 1);
			s.setHasRecCorp(c.getInt(c.getColumnIndex("HasRecCorp")) == 1);
			s.setStoreKind(c.getInt(c.getColumnIndex("StoreKind")));
			s.setStoreSort(c.getInt(c.getColumnIndex("StoreSort")));
			s.setStoreType(c.getInt(c.getColumnIndex("StoreType")));
			s.setStoreTypeKey(c.getString(c.getColumnIndex("StoreTypeKey")));
			s.setStoreTypeText(c.getString(c.getColumnIndex("StoreTypeText")));
			s.setUpStoreType(c.getInt(c.getColumnIndex("UpStoreType")));
			list.add(s);
		}
		c.close();
		db.close();
		
		return list;
	}
	
	public List<StoreTypes> getAllTypeByType(int storeKind){
		List<StoreTypes> list = new ArrayList<StoreTypes>();
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);

		Cursor c = db.query(TABLE_NAME, new String[] { "DownStoreType",
				"HasBizCorp", "HasRecCorp", "StoreKind", "StoreSort",
				"StoreType", "StoreTypeKey", "StoreTypeText", "UpStoreType" },
				"StoreKind=" + storeKind, null, null, null, null);
		
		while (c.moveToNext()) {
			StoreTypes s = new StoreTypes();
			s.setDownStoreType(c.getInt(c.getColumnIndex("DownStoreType")));
			s.setHasBizCorp(c.getInt(c.getColumnIndex("HasRecCorp")) == 1);
			s.setHasRecCorp(c.getInt(c.getColumnIndex("HasRecCorp")) == 1);
			s.setStoreKind(c.getInt(c.getColumnIndex("StoreKind")));
			s.setStoreSort(c.getInt(c.getColumnIndex("StoreSort")));
			s.setStoreType(c.getInt(c.getColumnIndex("StoreType")));
			s.setStoreTypeKey(c.getString(c.getColumnIndex("StoreTypeKey")));
			s.setStoreTypeText(c.getString(c.getColumnIndex("StoreTypeText")));
			s.setUpStoreType(c.getInt(c.getColumnIndex("UpStoreType")));
			list.add(s);
		}
		c.close();
		db.close();
		
		return list;
	}
}
