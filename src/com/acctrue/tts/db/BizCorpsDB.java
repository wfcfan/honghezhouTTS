package com.acctrue.tts.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.acctrue.tts.Constants;
import com.acctrue.tts.model.BizCorp;

public class BizCorpsDB {
	private SQLiteDatabase db = null;
	private Context _ctx;
	private static final String TABLE_BIZCORPS = "BizCorps";

	public BizCorpsDB(Context ctx) {
		_ctx = ctx;
	}

	public void addCorp(BizCorp corp) {
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		ContentValues values = new ContentValues();
		values.put("CorpId", corp.getCorpId());
		values.put("CorpName", corp.getCorpName());
		values.put("CorpCode", corp.getCorpCode());
		values.put("CorpPinYin", corp.getCorpPinYin());
		db.insert(TABLE_BIZCORPS, null, values);
		db.close();
	}

	public List<String> queryCorpNameByName(String name) {
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		Cursor c;
		List<String> list = new ArrayList<String>();
		if(name == null || name.length() == 0){
			c = db.rawQuery("select CorpName from " + TABLE_BIZCORPS, null);
		}else{
			c = db.rawQuery(String.format("select CorpName from %s WHERE CorpName like '%%%s%%'", TABLE_BIZCORPS,name), null);
		}
		
		if(c.moveToFirst()){
			do{
				list.add(c.getString(0));
			}while(c.moveToNext());
		}
		db.close();
		
		return list;
	}
	
	public BizCorp queryCorpByName(String name) {
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		BizCorp m = new BizCorp();
		Cursor c = db.rawQuery("select * from BizCorps where CorpName=?",new String[]{name} );
		if(c.moveToFirst()){
			m.setCorpId(c.getInt(c.getColumnIndex("CorpId")));
			//m.setCurrentCorpId(c.getInt(c.getColumnIndex("CurrentCorpId")));
			m.setCorpName(c.getString(c.getColumnIndex("CorpName")));
			m.setCorpCode(c.getString(c.getColumnIndex("CorpCode")));
			m.setCorpPinYin(c.getString(c.getColumnIndex("CorpPinYin")));
			//m.setCorpNumber(c.getString(c.getColumnIndex("CorpNumber")));
		}
		
		return m;
	}

	public void deleteCorp(int corpId) {
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		db.delete(TABLE_BIZCORPS, String.format("CorpId='%s'", corpId), null);
		db.close();
	}
	
	public BizCorp getBizCorp(int corpId){
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		BizCorp bc = null;
		Cursor c = db.query(TABLE_BIZCORPS, new String[] { "CorpId",
				"CurrentCorpId", "CorpName", "CorpCode", "CorpPinYin",
				"CorpNumber" }, "CorpId=" + corpId, null, null, null, null);
		while (c.moveToNext()) {
			bc = new BizCorp();
			bc.setCorpId(c.getInt(c.getColumnIndex("CorpId")));
			//bc.setCurrentCorpId(c.getInt(c.getColumnIndex("CurrentCorpId")));
			bc.setCorpName(c.getString(c.getColumnIndex("CorpName")));
			bc.setCorpCode(c.getString(c.getColumnIndex("CorpCode")));
			bc.setCorpPinYin(c.getString(c.getColumnIndex("CorpPinYin")));
			//bc.setCorpNumber(c.getString(c.getColumnIndex("CorpNumber")));
		}
		c.close();
		db.close();
		return bc;
	}
	
	public List<BizCorp> getBizCorps() {
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		List<BizCorp> list = new ArrayList<BizCorp>();
		Cursor c = db.query(TABLE_BIZCORPS, new String[] { "CorpId",
				"CurrentCorpId", "CorpName", "CorpCode", "CorpPinYin",
				"CorpNumber" }, null, null, null, null, null);
		while (c.moveToNext()) {
			BizCorp bc = new BizCorp();
			bc.setCorpId(c.getInt(c.getColumnIndex("CorpId")));
			//bc.setCurrentCorpId(c.getInt(c.getColumnIndex("CurrentCorpId")));
			bc.setCorpName(c.getString(c.getColumnIndex("CorpName")));
			bc.setCorpCode(c.getString(c.getColumnIndex("CorpCode")));
			bc.setCorpPinYin(c.getString(c.getColumnIndex("CorpPinYin")));
			//bc.setCorpNumber(c.getString(c.getColumnIndex("CorpNumber")));
			list.add(bc);
		}
		c.close();
		db.close();
		return list;
	}

}
