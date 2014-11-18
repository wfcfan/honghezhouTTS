package com.acctrue.tts.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.acctrue.tts.Constants;
import com.acctrue.tts.model.FarmLands;
import com.acctrue.tts.model.Farmers;

public class FarmLandsDB {
	private SQLiteDatabase db = null;
	private Context _ctx;
	protected static final String TABLE_FARMLANDS = "FarmLands";
	
	public FarmLandsDB(Context ctx) {
		_ctx = ctx;
	}
	
	public void addFarmLands(FarmLands f){
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		ContentValues values = new ContentValues();
		values.put("FarmLandCode", f.getFarmLandCode());
		values.put("FarmLandName", f.getFarmLandName());
		values.put("UserOwnerId", f.getUserOwnerId());
		values.put("UserOwnerDisplayName", f.getUserOwnerDisplayName());
		db.insert(TABLE_FARMLANDS, null, values);
		db.close();
	}
	
	public void deleteFarmLands(String code){
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_CURRENT_VERSION, null);
		db.delete(TABLE_FARMLANDS, String.format("FarmLandCode='%s'",code), null);
		db.close();
	}

	public List<FarmLands> getFarmLandsList(){
		List<FarmLands> list = new ArrayList<FarmLands>();
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_CURRENT_VERSION, null);
		Cursor c = db.query(TABLE_FARMLANDS, new String[] { "FarmLandCode",
				"FarmLandName", "UserOwnerId","UserOwnerDisplayName" }, null, null, null, null, null);
		while (c.moveToNext()) {
			FarmLands f = new FarmLands();
			f.setFarmLandCode(c.getString(c.getColumnIndex("FarmLandCode")));
			f.setFarmLandName(c.getString(c.getColumnIndex("FarmLandName")));
			f.setUserOwnerId(c.getInt(c.getColumnIndex("UserOwnerId")));
			f.setUserOwnerDisplayName(c.getString(c.getColumnIndex("UserOwnerDisplayName")));
			list.add(f);
		}
		c.close();
		db.close();
		return list;
	}
	
	public List<FarmLands> getFarmLandsList(String userWnerId){
		return getFarmLandsList(Integer.valueOf(userWnerId));
	}
	
	public List<FarmLands> getFarmLandsList(int userWnerId){
		List<FarmLands> list = new ArrayList<FarmLands>();
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_CURRENT_VERSION, null);
		Cursor c = db.query(TABLE_FARMLANDS, new String[] { "FarmLandCode",
				"FarmLandName", "UserOwnerId","UserOwnerDisplayName" }, String.format("UserOwnerId=%d", userWnerId), null, null, null, null);
		while (c.moveToNext()) {
			FarmLands f = new FarmLands();
			f.setFarmLandCode(c.getString(c.getColumnIndex("FarmLandCode")));
			f.setFarmLandName(c.getString(c.getColumnIndex("FarmLandName")));
			f.setUserOwnerId(c.getInt(c.getColumnIndex("UserOwnerId")));
			f.setUserOwnerDisplayName(c.getString(c.getColumnIndex("UserOwnerDisplayName")));
			list.add(f);
		}
		c.close();
		db.close();
		return list;
	}
	
	/**
	 * 农户
	 * 同农田，去重
	 */
	public List<Farmers> getFarmersList(){
		List<Farmers> fs = new ArrayList<Farmers>();
		List<FarmLands> list = this.getFarmLandsList();
		
		for(FarmLands f : list)
		{
			Farmers farmers = new Farmers(f.getUserOwnerId(),f.getUserOwnerDisplayName());
			if(!fs.contains(farmers))
				fs.add(farmers);
		}
		
		return fs;
	}
}
