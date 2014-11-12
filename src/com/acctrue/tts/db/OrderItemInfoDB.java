package com.acctrue.tts.db;

import java.util.*;

import com.acctrue.tts.Constants;
import com.acctrue.tts.model.OrderItemInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class OrderItemInfoDB {

	private SQLiteDatabase db = null;
	private Context _ctx;
	private static final String TABLE_NAME = "OrderItemInfo";

	public OrderItemInfoDB(Context ctx) {
		_ctx = ctx;
	}
	
	public void addItem(OrderItemInfo item){
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		
		ContentValues values = new ContentValues();
		values.put("OrderId",item.getOrderId());
		values.put("ProduceName ",item.getProductName());
		values.put("ProduceCode", item.getProductCode());
		values.put("BatchNo", item.getBatchNo());
		values.put("EditAmount ", item.getEditAmount());
		values.put("DisplayUnit", item.getDisplayUnit());
		values.put("ScanAmount", item.getScanAmount());
		values.put("ScanMinAmount", item.getScanAmount());
		values.put("DisplayAmount", item.getDisplayAmount());
		values.put("MinAmount", item.getMinAmount());
		values.put("Unit", item.getUnit());
		
		db.insert(TABLE_NAME, null, values);
		db.close();
	}
	
	public void delItem(String storeId){
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		db.delete(TABLE_NAME, String.format("OrderId='%s'", storeId), null);
		db.close();
	}
	
	public List<OrderItemInfo> getListByOrderId(String orderId){
		List<OrderItemInfo> list = new ArrayList<OrderItemInfo>();
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		Cursor c = db.rawQuery("select * from " + TABLE_NAME + " where OrderId='" + orderId + "'", null);
		if(c.moveToFirst()){
			do{
				OrderItemInfo item = new OrderItemInfo();
				item.setId(c.getInt(c.getColumnIndex("Id")));
				item.setOrderId(c.getString(c.getColumnIndex("OrderId")));
				item.setProductName(c.getString(c.getColumnIndex("ProduceName")));
				item.setProductCode(c.getString(c.getColumnIndex("ProduceCode")));
				item.setBatchNo(c.getString(c.getColumnIndex("BatchNo")));
				item.setEditAmount(c.getFloat(c.getColumnIndex("EditAmount")));
				item.setDisplayUnit(c.getString(c.getColumnIndex("DisplayUnit")));
				item.setScanAmount(c.getFloat(c.getColumnIndex("ScanAmount")));
				item.setScanMinAmount(c.getFloat(c.getColumnIndex("ScanMinAmount")));
				item.setDisplayAmount(c.getFloat(c.getColumnIndex("DisplayAmount")));
				item.setMinAmount(c.getFloat(c.getColumnIndex("MinAmount")));
				item.setUnit(c.getString(c.getColumnIndex("Unit")));
				
				list.add(item);
			}while(c.moveToNext());
		}
		
		db.close();
		return list;
	}
}
