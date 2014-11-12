package com.acctrue.tts.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.acctrue.tts.Constants;
import com.acctrue.tts.model.OrderInfo;
import com.acctrue.tts.utils.DateUtil;

public class OrderInfoDB {

	private SQLiteDatabase db = null;
	private Context _ctx;
	private static final String TABLE_ORDERINFO = "OrderInfo";

	public OrderInfoDB(Context ctx) {
		_ctx = ctx;
	}

	public void addOrderInfo(OrderInfo order) {
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		ContentValues values = new ContentValues();
		values.put("StoreId", order.getStoreId());
		values.put("CorpId", order.getCorpId());
		values.put("OrderNo", order.getOrderNo());
		values.put("OrderType", order.getOrderType());
		values.put("OrderKind", order.getOrderKind());
		values.put("OrderTypeText", order.getOrderTypeText());
		values.put("BizCorpCode", order.getBizCorpCode());
		values.put("BizCorpName", order.getBizCorpName());
		values.put("BizCorpCodeZD", order.getBizCorpCodeZD());
		values.put("BizCorpNameZD", order.getBizCorpNameZD());
		values.put("Actor", order.getActor());
		values.put("ActDate", order.getActDate());
		values.put("StoreStatus", order.getStoreStatus());
		values.put("Flag", order.getFlag());
		values.put("UserId", order.getUserId());

		db.insert(TABLE_ORDERINFO, null, values);
		db.close();
	}

	public void deleteOrderInfo(String storeId) {
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		db.delete(TABLE_ORDERINFO, String.format("StoreId='%s'", storeId), null);
		db.close();
	}
	
	public void deleteAllOrderInfo(List<OrderInfo> list){
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		for(int i =0;i<list.size();i++){
			if(i != 0)sb.append(',');
			sb.append(String.format("'%s'", list.get(i).getStoreId()));
		}
		sb.append(')');
		String cond = sb.toString();
		db.delete("PackCode", "OrderId in " + cond, null);
		db.delete("OrderItemInfo", "OrderId in " + cond, null);
		db.delete(TABLE_ORDERINFO, "StoreId in " + cond, null);
		db.close();
	}
	
	public void deleteAllOrderInfo(String storeId){
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		db.delete("PackCode", String.format("OrderId='%s'", storeId), null);
		db.delete("OrderItemInfo", String.format("OrderId='%s'", storeId), null);
		db.delete(TABLE_ORDERINFO, String.format("StoreId='%s'", storeId), null);
		db.close();
	}

	public OrderInfo getOrderInfoById(String id){
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		OrderInfo order = null;
		Cursor c = db.rawQuery("select * from " + TABLE_ORDERINFO + " where StoreId=?", new String[]{id});
		if(c.moveToFirst()){
			order = new OrderInfo();
			order.setStoreId(c.getString(c.getColumnIndex("StoreId")));
			order.setCorpId(c.getInt(c.getColumnIndex("CorpId")));
			order.setOrderNo(c.getString(c.getColumnIndex("OrderNo")));
			order.setOrderType(c.getInt(c.getColumnIndex("OrderType")));
			order.setOrderKind(c.getString(c.getColumnIndex("OrderKind")));
			order.setOrderTypeText(c.getString(c.getColumnIndex("OrderTypeText")));
			order.setBizCorpCode(c.getString(c.getColumnIndex("BizCorpCode")));
			order.setBizCorpName(c.getString(c.getColumnIndex("BizCorpName")));
			order.setBizCorpCodeZD(c.getString(c.getColumnIndex("BizCorpCodeZD")));
			order.setBizCorpNameZD(c.getString(c.getColumnIndex("BizCorpNameZD")));
			order.setActor(c.getString(c.getColumnIndex("Actor")));
			order.setActDate(c.getString(c.getColumnIndex("ActDate")));
			order.setStoreStatus(c.getInt(c.getColumnIndex("StoreStatus")));
			order.setFlag(c.getInt(c.getColumnIndex("Flag")));
			order.setUserId(c.getInt(c.getColumnIndex("UserId")));
		}
		db.close();
		
		return order;
	}
	
	public List<OrderInfo> getOrderInfo(boolean isEBusiness) {
		List<OrderInfo> list = new ArrayList<OrderInfo>();
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		String cond = isEBusiness ? "Flag != ?" : null;
		String[] condVal = isEBusiness ? new String[]{"0"} : null;
		Cursor c = db.query(TABLE_ORDERINFO, new String[] { "StoreId",
				"CorpId", "OrderNo", "OrderType", "OrderKind", "OrderTypeText",
				"BizCorpCode", "BizCorpName", "BizCorpCodeZD", "BizCorpNameZD",
				"Actor", "ActDate","StoreStatus", "Flag", "UserId" }, cond, condVal, null, null,
				null);
		while (c.moveToNext()) {
			OrderInfo o = new OrderInfo();

			o.setStoreId(c.getString(c.getColumnIndex("StoreId")));
			o.setCorpId(c.getInt(c.getColumnIndex("CorpId")));
			o.setOrderNo(c.getString(c.getColumnIndex("OrderNo")));
			o.setOrderType(c.getInt(c.getColumnIndex("OrderType")));
			o.setOrderKind(c.getString(c.getColumnIndex("OrderKind")));
			o.setOrderTypeText(c.getString(c.getColumnIndex("OrderTypeText")));
			o.setBizCorpCode(c.getString(c.getColumnIndex("BizCorpCode")));
			o.setBizCorpName(c.getString(c.getColumnIndex("BizCorpName")));
			o.setBizCorpCodeZD(c.getString(c.getColumnIndex("BizCorpCodeZD")));
			o.setBizCorpNameZD(c.getString(c.getColumnIndex("BizCorpNameZD")));
			o.setActor(c.getString(c.getColumnIndex("Actor")));
			o.setActDate(c.getString(c.getColumnIndex("ActDate")));
			o.setStoreStatus(c.getInt(c.getColumnIndex("StoreStatus")));
			o.setFlag(c.getInt(c.getColumnIndex("Flag")));
			o.setUserId(c.getInt(c.getColumnIndex("UserId")));

			list.add(o);
		}
		c.close();
		db.close();

		return list;
	}
	
	public List<OrderInfo> getOrderInfo() {
		return getOrderInfo(false);
	}

	public List<OrderInfo> getListByNo(String cond) {
		List<OrderInfo> list = new ArrayList<OrderInfo>();
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		//OrderInfo order = null;
		Cursor c = db.rawQuery("select * from " + TABLE_ORDERINFO + " where OrderNo like '%" + cond + "%' " +
				"or BizCorpName like '%" + cond + "%'", null);
		
		while (c.moveToNext()) {
			OrderInfo o = new OrderInfo();

			o.setStoreId(c.getString(c.getColumnIndex("StoreId")));
			o.setCorpId(c.getInt(c.getColumnIndex("CorpId")));
			o.setOrderNo(c.getString(c.getColumnIndex("OrderNo")));
			o.setOrderType(c.getInt(c.getColumnIndex("OrderType")));
			o.setOrderKind(c.getString(c.getColumnIndex("OrderKind")));
			o.setOrderTypeText(c.getString(c.getColumnIndex("OrderTypeText")));
			o.setBizCorpCode(c.getString(c.getColumnIndex("BizCorpCode")));
			o.setBizCorpName(c.getString(c.getColumnIndex("BizCorpName")));
			o.setBizCorpCodeZD(c.getString(c.getColumnIndex("BizCorpCodeZD")));
			o.setBizCorpNameZD(c.getString(c.getColumnIndex("BizCorpNameZD")));
			o.setActor(c.getString(c.getColumnIndex("Actor")));
			o.setActDate(c.getString(c.getColumnIndex("ActDate")));
			o.setStoreStatus(c.getInt(c.getColumnIndex("StoreStatus")));
			o.setFlag(c.getInt(c.getColumnIndex("Flag")));
			o.setUserId(c.getInt(c.getColumnIndex("UserId")));

			list.add(o);
		}
		c.close();
		db.close();
		
		return list;
	}

}
