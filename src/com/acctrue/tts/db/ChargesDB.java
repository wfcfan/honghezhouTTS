package com.acctrue.tts.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.acctrue.tts.Constants;
import com.acctrue.tts.enums.ChargesStatusEnum;
import com.acctrue.tts.model.Charges;

public class ChargesDB {
	private SQLiteDatabase db = null;
	private Context _ctx;
	protected static final String TABLE_CHARGES = "Charges";
	
	public ChargesDB(Context ctx){
		_ctx = ctx;
	}
	
	public void addCharges(Charges charges){
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,Constants.DATABASE_CURRENT_VERSION, null);
		ContentValues values = new ContentValues();
		values.put("Id", charges.getId());
		values.put("batchno", charges.getBatchno());
		values.put("ManNo", charges.getManNo());
		values.put("FarmlandNo", charges.getFarmlandNo());
		values.put("ProductId", charges.getProductId());
		values.put("CreateDate", charges.getCreateDate());
		values.put("Man", charges.getMan());
		values.put("State", charges.getState());
		values.put("IsPackCode", charges.getIsPackCode());
		values.put("Weight", charges.getWeight());
		
		values.put("ManName", charges.getManName());
		values.put("FarmlandName", charges.getFarmlandName());
		values.put("ProductName", charges.getProductName());
		db.insert(TABLE_CHARGES, null, values);
		db.close();
	}
	
	public void updateCharges(Charges charges){
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,Constants.DATABASE_CURRENT_VERSION, null);
		ContentValues values = new ContentValues();
		values.put("batchno", charges.getBatchno());
		values.put("ManNo", charges.getManNo());
		values.put("FarmlandNo", charges.getFarmlandNo());
		values.put("ProductId", charges.getProductId());
		values.put("Man", charges.getMan());
		values.put("IsPackCode", charges.getIsPackCode());
		values.put("ManName", charges.getManName());
		values.put("FarmlandName", charges.getFarmlandName());
		values.put("ProductName", charges.getProductName());
		
		db.update(TABLE_CHARGES, values, String.format("Id='%s'",charges.getId()), null);
		db.close();
	}
	
	public void deleteCharges(String id){
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME, Constants.DATABASE_CURRENT_VERSION, null);
		db.delete(TABLE_CHARGES, String.format("Id='%s'",id), null);
		db.close();
	}
	
	public void modifyChangeState(String id,ChargesStatusEnum state){
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME, Constants.DATABASE_CURRENT_VERSION, null);
		ContentValues values = new ContentValues();
		values.put("State", state.getStateId());
		
		db.update(TABLE_CHARGES, values, String.format("Id='%s'",id), null);
		db.close();
	}
	
	public Charges getCharge(String id){
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME, Constants.DATABASE_CURRENT_VERSION, null);
		Cursor c = db.query(TABLE_CHARGES, new String[] { "Id",
				"batchno","ManNo","FarmlandNo","ProductId","CreateDate","Man","State","IsPackCode","Weight","ManName","FarmlandName","ProductName"}, String.format("Id='%s'", id), null, null, null, null);
		Charges charges = null;
		while(c.moveToNext()){
			charges = new Charges();
			charges.setId(c.getString(c.getColumnIndex("Id")));
			charges.setBatchno(c.getString(c.getColumnIndex("batchno")));
			charges.setManNo(c.getString(c.getColumnIndex("ManNo")));
			charges.setFarmlandNo(c.getString(c.getColumnIndex("FarmlandNo")));
			charges.setProductId(c.getString(c.getColumnIndex("ProductId")));
			charges.setCreateDate(c.getString(c.getColumnIndex("CreateDate")));
			charges.setMan(c.getString(c.getColumnIndex("Man")));
			charges.setState(c.getInt(c.getColumnIndex("State")));
			charges.setIsPackCode(c.getInt(c.getColumnIndex("IsPackCode")));
			charges.setWeight(c.getString(c.getColumnIndex("Weight")));
			
			charges.setManName(c.getString(c.getColumnIndex("ManName")));
			charges.setFarmlandName(c.getString(c.getColumnIndex("FarmlandName")));
			charges.setProductName(c.getString(c.getColumnIndex("ProductName")));
		}
		c.close();
		db.close();
		return charges;
	}
	
	public List<Charges> getCharges(){
		List<Charges> list = new ArrayList<Charges>();
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME, Constants.DATABASE_CURRENT_VERSION, null);
		Cursor c = db.query(TABLE_CHARGES, new String[] { "Id",
				"batchno","ManNo","FarmlandNo","ProductId","CreateDate","Man","State","IsPackCode","Weight","ManName","FarmlandName","ProductName"}, null, null, null, null, "CreateDate desc");
		while(c.moveToNext()){
			Charges charges = new Charges();
			charges.setId(c.getString(c.getColumnIndex("Id")));
			charges.setBatchno(c.getString(c.getColumnIndex("batchno")));
			charges.setManNo(c.getString(c.getColumnIndex("ManNo")));
			charges.setFarmlandNo(c.getString(c.getColumnIndex("FarmlandNo")));
			charges.setProductId(c.getString(c.getColumnIndex("ProductId")));
			charges.setCreateDate(c.getString(c.getColumnIndex("CreateDate")));
			charges.setMan(c.getString(c.getColumnIndex("Man")));
			charges.setState(c.getInt(c.getColumnIndex("State")));
			charges.setIsPackCode(c.getInt(c.getColumnIndex("IsPackCode")));
			charges.setWeight(c.getString(c.getColumnIndex("Weight")));
			
			charges.setManName(c.getString(c.getColumnIndex("ManName")));
			charges.setFarmlandName(c.getString(c.getColumnIndex("FarmlandName")));
			charges.setProductName(c.getString(c.getColumnIndex("ProductName")));
			list.add(charges);
		}
		c.close();
		db.close();
		return list;
	}
	
	public Charges getChargesByBatchNo(String batchNo){
		Charges charges = null;
		for(Charges c : this.getCharges()){
			if(c.getBatchno().equals(batchNo)){
				charges = c;
				break;
			}
		}
		return charges;
	}
}
