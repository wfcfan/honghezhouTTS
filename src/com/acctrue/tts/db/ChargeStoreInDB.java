package com.acctrue.tts.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.acctrue.tts.Constants;
import com.acctrue.tts.model.ChargeStoreIn;
import com.acctrue.tts.model.ChargeStoreInCode;

public class ChargeStoreInDB {
	private SQLiteDatabase db = null;
	private Context _ctx;
	protected static final String TABLE_CHARGESTOREIN = "ChargeStoreIn";
	protected static final String TABLE_CHARGESTOREINCODE = "ChargeStoreInCode";
	
	public ChargeStoreInDB(Context ctx) {
		_ctx = ctx;
	}

	public void addChargeStoreIn(ChargeStoreIn csi,ChargeStoreInCode ...codes) {
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);

		ContentValues values = new ContentValues();
		values.put("Id", csi.getId());
		values.put("ActDate", csi.getActDate());
		values.put("Actor", csi.getActor());
		values.put("WarehouseId", csi.getWarehouseId());
		values.put("warehouseName", csi.getWarehouseName());

		db.insert(TABLE_CHARGESTOREIN, null, values);
		
		if(codes != null){
			for(ChargeStoreInCode c : codes){
				ContentValues values2 = new ContentValues();
				values2.put("StoreInId", c.getStoreInId());
				values2.put("Code", c.getCode());
				values2.put("IsStoreIn", c.getIsStoreIn());
				db.insert(TABLE_CHARGESTOREINCODE, null, values2);
			}
		}
		
		db.close();
	}
	
	public List<ChargeStoreInCode> getChargeStoreInCode(String storeId){
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		List<ChargeStoreInCode> list = new ArrayList<ChargeStoreInCode>();
		Cursor c = db.query(TABLE_CHARGESTOREINCODE, new String[] { "StoreInId",
				"Code", "IsStoreIn"}, String.format("StoreInId='%s'", storeId), null, null, null,
				null);
		while(c.moveToNext()){
			ChargeStoreInCode cic = new ChargeStoreInCode();
			cic.setStoreInId(storeId);
			cic.setCode(c.getString(c.getColumnIndex("Code")));
			cic.setIsStoreIn(c.getInt(c.getColumnIndex("IsStoreIn")));
			list.add(cic);
		}
		c.close();
		db.close();
		return list;
	}
	
	public void delChargeStoreIn(String id){
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		
		db.delete(TABLE_CHARGESTOREIN, String.format("Id='%s'", id), null);
		db.delete(TABLE_CHARGESTOREINCODE, String.format("StoreInId='%s'", id), null);		
	}
	
	public void delChargeStoreInByIds(List<String> ids){
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		
		db.delete(TABLE_CHARGESTOREIN, String.format("Id %s ", getSqlIn(ids)), null);
		db.delete(TABLE_CHARGESTOREINCODE, String.format("StoreInId %s ", getSqlIn(ids)), null);
	}
	
	public void delChargeStoreIn(List<ChargeStoreIn> ccs){
		List<String> list = new ArrayList<String>();
		for(ChargeStoreIn cc : ccs){
			if(!list.contains(cc.getId()))
			list.add(cc.getId());
		}
		this.delChargeStoreInByIds(list);
	}
	
	private String getSqlIn(List<String> ids){
		StringBuilder sb = new StringBuilder();
		sb.append(" in (");
		for(String s : ids)
			sb.append("'" + s + "',");
		
		sb.delete(sb.length()-1, sb.length());
		sb.append(")");
		return sb.toString();
	}
	
	public List<ChargeStoreIn> getAllChargeStoreIn() {
		List<ChargeStoreIn> list = new ArrayList<ChargeStoreIn>();
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		Cursor c = db.query(TABLE_CHARGESTOREIN, new String[] { "Id",
				"ActDate", "Actor", "WarehouseId","warehouseName" }, null, null, null, null,
				null);

		while (c.moveToNext()) {
			ChargeStoreIn cs = new ChargeStoreIn();
			cs.setId(c.getString(c.getColumnIndex("Id")));
			cs.setActor(c.getString(c.getColumnIndex("Actor")));
			cs.setActDate(c.getString(c.getColumnIndex("ActDate")));
			cs.setWarehouseId(c.getInt(c.getColumnIndex("WarehouseId")));
			cs.setWarehouseName(c.getString(c.getColumnIndex("warehouseName")));
			cs.setCodes(this.getChargeStoreInCode(cs.getId()));
			list.add(cs);
		}
		c.close();
		db.close();
		return list;
	}

	public List<ChargeStoreIn> getAllChargeStoreIn(int whId) {
		List<ChargeStoreIn> list = new ArrayList<ChargeStoreIn>();
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		Cursor c = db.query(TABLE_CHARGESTOREIN, new String[] { "Id",
				"ActDate", "Actor", "WarehouseId" },
				String.format("WarehouseId=%d", whId), null, null, null, null);

		while (c.moveToNext()) {
			ChargeStoreIn cs = new ChargeStoreIn();
			cs.setId(c.getString(c.getColumnIndex("Id")));
			cs.setActor(c.getString(c.getColumnIndex("Actor")));
			cs.setActDate(c.getString(c.getColumnIndex("ActDate")));
			cs.setWarehouseId(c.getInt(c.getColumnIndex("WarehouseId")));
			list.add(cs);
		}
		c.close();
		db.close();
		return list;
	}

	public List<ChargeStoreInCode> getChargeStoreInCodesListById(
			String storeInId) {
		List<ChargeStoreInCode> list = new ArrayList<ChargeStoreInCode>();
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		Cursor c = db.rawQuery(
				"SELECT StoreInId,Code,IsStoreIn from ChargeStoreInCode StoreInId='"
						+ storeInId + "'", null);

		if (c.moveToFirst()) {
			do {
				ChargeStoreInCode m = new ChargeStoreInCode();
				m.setStoreInId(c.getString(0));
				m.setCode(c.getString(1));
				m.setIsStoreIn(c.getInt(2));
				list.add(m);
			} while (c.moveToNext());
		}
		c.close();
		db.close();

		return list;
	}

	public void deleteCodes(List<ChargeStoreIn> delList) {
		// 调用接口入库后再删除数据
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		StringBuilder sb = new StringBuilder();
		sb.append(" in (");
		for (int i = 0; i < delList.size(); i++) {
			if (i != 0)
				sb.append(",");
			sb.append(delList.get(i).getId());
		}
		sb.append(")");
		db.execSQL("delete from ChargeStoreIn where Id in " + sb.toString());
		db.execSQL("delete from ChargeStoreInCode where StoreInId in "
				+ sb.toString());

		db.close();

	}
}
