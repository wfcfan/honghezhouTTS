package com.acctrue.tts.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.acctrue.tts.Constants;
import com.acctrue.tts.model.PackCode;

public class PackCodeDB {
	private final String TAG = PackCodeDB.class.getSimpleName();
	private SQLiteDatabase db = null;
	private Context _ctx;
	protected static final String TABLE_NAME= "PackCode";
	
	public PackCodeDB(Context ctx){
		_ctx = ctx;
	}
	
	public List<PackCode> getPackCodeByOrderId(String orderId,boolean isDelete){
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		List<PackCode> list = new ArrayList<PackCode>();
		PackCode packCode;
		Cursor c = db.rawQuery("select * from " + TABLE_NAME + " where OrderId=? and ByDelete=?", 
				new String[]{orderId,isDelete?"1":"0"});
		if(c.moveToFirst()){
			do{
				packCode = new PackCode();
				packCode.setId(c.getString(c.getColumnIndex("ID")));
				packCode.setOrderId(c.getString(c.getColumnIndex("OrderId")));
				packCode.setOrderItemId(c.getInt(c.getColumnIndex("OrderItemId")));
				packCode.setCodeValue(c.getString(c.getColumnIndex("CodeValue")));
				packCode.setParentCode(c.getString(c.getColumnIndex("ParentCode")));
				packCode.setByDelete(c.getInt(c.getColumnIndex("ByDelete")));
				packCode.setIsParentScanCode(c.getInt(c.getColumnIndex("IsParentScanCode")));
				packCode.setActor(c.getString(c.getColumnIndex("Actor")));
				packCode.setActDate(c.getString(c.getColumnIndex("ActDate")));
				
				list.add(packCode);
			}while(c.moveToNext());
		}
		db.close();
		
		return list;
	}
	
	public void addPackCode(PackCode packCode){
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		ContentValues values = new ContentValues();
		values.put("ID", packCode.getId());
		values.put("OrderId", packCode.getOrderId());
		values.put("OrderItemId", packCode.getOrderItemId());
		values.put("CodeValue", packCode.getCodeValue());
		values.put("ParentCode", packCode.getParentCode());
		values.put("ByDelete", packCode.getByDelete());
		values.put("IsParentScanCode", packCode.getIsParentScanCode());
		values.put("Actor", packCode.getActor());
		values.put("ActDate", packCode.getActDate());
		
		db.insert(TABLE_NAME, null, values);
		db.close();
	}
	
	public void updatePackCodeDelStatus(String packId){
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		db.execSQL("update " + TABLE_NAME + " set ByDelete=1 where ID = " + packId);
		db.close();
	}
}
