package com.acctrue.tts.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.acctrue.tts.Constants;
import com.acctrue.tts.enums.StoreSource;
import com.acctrue.tts.model.Store;
import com.acctrue.tts.model.StoreCode;
import com.acctrue.tts.model.StoreItem;

public class StoreDB {
	private SQLiteDatabase db = null;
	private Context _ctx;
	private static final String TABLE_STORE = "Store";
	private static final String TABLE_STOREITEM = "StoreItem";
	private static final String TABLE_STORECODE = "StoreCode";
	
	public StoreDB(Context ctx) {
		this._ctx = ctx;
	}

	public void addStore(Store store, StoreItem... item) {
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);

		ContentValues values = new ContentValues();
		values.put("StoreId", store.getStoreId());
		values.put("StoreNo", store.getStoreNo());
		values.put("StoreType", store.getStoreType());
		values.put("StoreTypeText", store.getStoreTypeText());
		values.put("StoreStatus", store.getStoreStatus());
		values.put("StoreStatusText", store.getStoreStatusText());
		values.put("StoreKind", store.getStoreKind());
		values.put("StoreMan", store.getStoreMan());
		values.put("StoreDate", store.getStoreDate());
		values.put("CorpId", store.getCorpId());
		values.put("CorpCode", store.getCorpCode());
		values.put("CorpName", store.getCorpName());
		values.put("BizCorpId", store.getBizCorpId());
		values.put("BizCorpCode", store.getBizCorpCode());
		values.put("BizCorpName", store.getBizCorpName());
		values.put("RecCorpId", store.getRecCorpId());
		values.put("RecCorpCode", store.getRecCorpCode());
		values.put("RecCorpName", store.getRecCorpName());
		values.put("Description", store.getDescription());
		values.put("CreateTime", store.getCreateTime());
		values.put("Source", store.getSource().getId());
		db.insert(TABLE_STORE, null, values);

		if (item != null) {
			for (int i = 0; i < item.length; i++) {
				StoreItem si = item[i];
				values = new ContentValues();
				values.put("ItemId", si.getItemId());
				values.put("StoreId", si.getStoreId());
				values.put("ProduceBatchNo", si.getProduceBatchNo());
				values.put("ProductCode", si.getProductCode());
				values.put("ProductName", si.getProductName());
				values.put("Amount", si.getAmount());
				values.put("DisplayAmount", si.getDisplayAmount());
				values.put("DisplayProductUnit", si.getDisplayProductUnit());
				db.insert(TABLE_STOREITEM, null, values);
			}
		}
		db.close();
	}
	
	public void addStoreBySelf(Store store) {
		List<StoreItem> list = store.getItem();
		StoreItem[] sis = null;
		if (list != null) {
			sis = new StoreItem[list.size()];
			for (int i = 0; i < list.size(); i++) {
				sis[i] = list.get(i);
			}
		}
		this.addStore(store, sis);
	}
	
	public void addStoreCode(StoreCode sc){
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null); 
		db.delete(TABLE_STORECODE, String.format("StoreId='%s' and ProductCode='%s'",sc.getStoreId(),sc.getProductCode()), null);//先执行删除，然后再增加
		ContentValues values = new ContentValues();
		values.put("Id", sc.getId());
		values.put("StoreId", sc.getStoreId());
		values.put("CodeId", sc.getCodeId());
		values.put("CodeCount", sc.getCodeCount());
		values.put("ParentId", sc.getParentId());
		values.put("SavedCodeId", sc.getSavedCodeId());
		values.put("ProductCode", sc.getProductCode());
		values.put("ProductName", sc.getProductName());
		values.put("InnerPacking", sc.getInnerPacking());
		values.put("ProduceBatchNo", sc.getProduceBatchNo());
		values.put("ProductUnit", sc.getProductUnit());
		values.put("CurrentAmount", sc.getCurrentAmount());
		values.put("CreateTime", sc.getCreateTime());
		values.put("IsDelete", sc.getIsDelete());
		db.insert(TABLE_STORECODE, null, values);
		db.close();
	}
	
	public void addStoreCode(List<StoreCode> scList){
		for(StoreCode sc : scList){
			StoreCode temp = getStoreCode(sc.getId());//查询该码是否存在
			if(temp != null)//如果存在，直接忽略
				continue;
			this.addStoreCode(sc);
		}
	}
	
	
	
	public void deleteStoreCode(String id){
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		ContentValues values = new ContentValues();
		values.put("IsDelete", StoreCode.DELETE_TRUE);
		
		db.update(TABLE_STORECODE, values, "Id=?", new String[] { id });
		db.close();
	}
	
	public void deleteStoreCode(String storeId,String prodCode){
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		db.delete(TABLE_STORE, String.format("StoreId='%s' and ProductCode='%s'",storeId,prodCode), null);
		db.close();
	}
	
	public void deleteStoreCode(StoreCode sc){
		deleteStoreCode(sc.getId());
	}
	
	public void addStoreItem(StoreItem si) {
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		ContentValues values = new ContentValues();
		values = new ContentValues();
		values.put("ItemId", si.getItemId());
		values.put("StoreId", si.getStoreId());
		values.put("ProduceBatchNo", si.getProduceBatchNo());
		values.put("ProductCode", si.getProductCode());
		values.put("ProductName", si.getProductName());
		values.put("Amount", si.getAmount());
		values.put("DisplayAmount", si.getDisplayAmount());
		values.put("DisplayProductUnit", si.getDisplayProductUnit());
		db.insert(TABLE_STOREITEM, null, values);
		db.close();
	}

	public void delStore(String storeId) {
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		db.delete(TABLE_STORE, String.format("StoreId='%s'", storeId), null);
		db.delete(TABLE_STOREITEM, String.format("StoreId='%s'", storeId), null);
		db.close();
	}
	
	public void delStore(Store store){
		delStore(store.getStoreId());
	}
	
	public void delStore(List<Store> stores){
		for(Store s : stores)
			this.delStore(s);
	}
	
	public Store getStoreByNo(String storeNo){
		Store s = null;
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		Cursor c = db.query(TABLE_STORE, new String[] { "StoreId", "StoreNo",
				"StoreType", "StoreTypeText", "StoreStatus", "StoreStatusText",
				"StoreKind", "StoreMan", "StoreDate", "CorpId", "CorpCode",
				"CorpName", "BizCorpId", "BizCorpCode", "BizCorpName",
				"RecCorpId", "RecCorpCode", "RecCorpName", "Description",
				"CreateTime", "Source" }, String.format("StoreNo='%s'", storeNo), null, null, null, null);
		while (c.moveToNext()) {
			s = new Store(c);
			s.setItem(this.getStoreItems(s.getStoreId(), db));
		}
		
		c.close();
		db.close();
		return s;
	}

	public Store getStore(String storeId){
		Store s = null;
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		Cursor c = db.query(TABLE_STORE, new String[] { "StoreId", "StoreNo",
				"StoreType", "StoreTypeText", "StoreStatus", "StoreStatusText",
				"StoreKind", "StoreMan", "StoreDate", "CorpId", "CorpCode",
				"CorpName", "BizCorpId", "BizCorpCode", "BizCorpName",
				"RecCorpId", "RecCorpCode", "RecCorpName", "Description",
				"CreateTime", "Source" }, String.format("StoreId='%s'", storeId), null, null, null, null);
		while (c.moveToNext()) {
			s = new Store(c);
			s.setItem(this.getStoreItems(s.getStoreId(), db));
		}
		
		c.close();
		db.close();
		return s;
	}
	
	public List<Store> getStores(StoreSource source) {
		List<Store> list = new ArrayList<Store>();
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);

		String cond = null;
		if (source != null)
			cond = String.format("Source=%d", source.getId());

		Cursor c = db.query(TABLE_STORE, new String[] { "StoreId", "StoreNo",
				"StoreType", "StoreTypeText", "StoreStatus", "StoreStatusText",
				"StoreKind", "StoreMan", "StoreDate", "CorpId", "CorpCode",
				"CorpName", "BizCorpId", "BizCorpCode", "BizCorpName",
				"RecCorpId", "RecCorpCode", "RecCorpName", "Description",
				"CreateTime", "Source" }, cond, null, null, null, null);
		while (c.moveToNext()) {
			Store s = new Store(c);
			s.setItem(this.getStoreItems(s.getStoreId(), db));
			s.setCodes(this.getStoreCodes(s.getStoreId(), StoreCode.DELETE_FALSE));
			s.setRemoveCodes(this.getStoreCodes(s.getStoreId(), StoreCode.DELETE_TRUE));
			list.add(s);
		}
		c.close();
		db.close();
		return list;
	}
	
	public List<Store> searchStores(CharSequence keyword){
		List<Store> list = new ArrayList<Store>();
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		String cond = null;
		if(!TextUtils.isEmpty(keyword)){
			cond = " StoreNo like '%" + keyword + "%'";
		}
		
		Cursor c = db.query(TABLE_STORE, new String[] { "StoreId", "StoreNo",
				"StoreType", "StoreTypeText", "StoreStatus", "StoreStatusText",
				"StoreKind", "StoreMan", "StoreDate", "CorpId", "CorpCode",
				"CorpName", "BizCorpId", "BizCorpCode", "BizCorpName",
				"RecCorpId", "RecCorpCode", "RecCorpName", "Description",
				"CreateTime", "Source" }, cond, null, null, null, null);
		while (c.moveToNext()) {
			Store s = new Store(c);
			s.setItem(this.getStoreItems(s.getStoreId(), db));
			s.setCodes(getStoreCodes(s.getStoreId(), StoreCode.DELETE_FALSE));
			s.setRemoveCodes(getStoreCodes(s.getStoreId(), StoreCode.DELETE_TRUE));
			list.add(s);
		}
		c.close();
		db.close();
		return list;
	}

	private List<StoreItem> getStoreItems(String storeId, SQLiteDatabase db) {
		List<StoreItem> list = new ArrayList<StoreItem>();
		Cursor c = db.query(TABLE_STOREITEM, new String[] { "ItemId", "StoreId",
				"ProduceBatchNo", "ProductCode", "ProductName", "Amount",
				"DisplayAmount", "DisplayProductUnit" },
				String.format("StoreId='%s'", storeId), null, null, null, null);
		
		while (c.moveToNext()) {
			StoreItem si = new StoreItem();
			si.setItemId(c.getString(c.getColumnIndex("ItemId")));
			si.setProduceBatchNo(c.getString(c.getColumnIndex("ProduceBatchNo")));
			si.setProductCode(c.getString(c.getColumnIndex("ProductCode")));
			si.setProductName(c.getString(c.getColumnIndex("ProductName")));
			si.setAmount(c.getInt(c.getColumnIndex("Amount")));
			si.setDisplayAmount(c.getInt(c.getColumnIndex("DisplayAmount")));
			si.setDisplayProductUnit(c.getString(c.getColumnIndex("DisplayProductUnit")));
			list.add(si);
		}
		c.close();
		return list;
	}
	
	public int getAmount(String storeId){
		int a = 0;
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		String sql = String.format("SELECT SUM(CurrentAmount) FROM %s WHERE StoreId='%s' AND IsDelete=0", TABLE_STORECODE,storeId);
		Cursor c = db.rawQuery(sql, null);
		while(c.moveToNext()){
			a = c.getInt(0);
		}
		c.close();
		db.close();
		return a;
	}
	
	public List<StoreCode> getStoreCodes(String storeId,Integer isDel){
		List<StoreCode> list = new ArrayList<StoreCode>();
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		
		String cond = String.format(" StoreId='%s' ", storeId);
		if (isDel != null) {
			cond += String.format(" AND IsDelete=%d", isDel);
		}
		Cursor c = db.query(TABLE_STORECODE, new String[] { "Id", "StoreId",
				"CodeId", "CodeCount", "ParentId", "SavedCodeId",
				"ProductCode", "ProductName", "InnerPacking", "ProduceBatchNo",
				"ProductUnit", "CurrentAmount", "CreateTime", "IsDelete" },
				cond, null, null, null, null);
		while (c.moveToNext()) {
			StoreCode sc = getStoreCode(c);
			list.add(sc);
		}
		c.close();
		db.close();
		return list;
	}
	
	public StoreCode getStoreCode(String id) {
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		StoreCode sc = null;
		Cursor c = db.query(TABLE_STORECODE, new String[] { "Id", "StoreId",
				"CodeId", "CodeCount", "ParentId", "SavedCodeId",
				"ProductCode", "ProductName", "InnerPacking", "ProduceBatchNo",
				"ProductUnit", "CurrentAmount", "CreateTime", "IsDelete" },
				String.format("Id='%s'", id), null, null, null, null);
		while (c.moveToNext()) {
			sc = getStoreCode(c);
		}
		c.close();
		db.close();
		return sc;
	}
	
	private StoreCode getStoreCode(Cursor c){
		StoreCode sc = new StoreCode();
		 sc.setId(c.getString(c.getColumnIndex("Id")));
		 sc.setStoreId(c.getString(c.getColumnIndex("StoreId")));
		 sc.setCodeId(c.getString(c.getColumnIndex("CodeId")));
		 sc.setCodeCount(c.getInt(c.getColumnIndex("CodeCount")));
		 sc.setParentId(c.getString(c.getColumnIndex("ParentId")));
		 sc.setSavedCodeId(c.getString(c.getColumnIndex("SavedCodeId")));
		 sc.setProductCode(c.getString(c.getColumnIndex("ProductCode")));
		 sc.setProductName(c.getString(c.getColumnIndex("ProductName")));
		 sc.setInnerPacking(c.getString(c.getColumnIndex("InnerPacking")));
		 sc.setProduceBatchNo(c.getString(c.getColumnIndex("ProduceBatchNo")));
		 sc.setProductUnit(c.getString(c.getColumnIndex("ProductUnit")));
		 sc.setCurrentAmount(c.getInt(c.getColumnIndex("CurrentAmount")));
		 sc.setCreateTime(c.getString(c.getColumnIndex("CreateTime")));
		 sc.setIsDelete(c.getInt(c.getColumnIndex("IsDelete")));
		 return sc;
	}
	

}
