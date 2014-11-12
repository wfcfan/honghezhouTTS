package com.acctrue.tts.db;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.acctrue.tts.Constants;
import com.acctrue.tts.model.RelationCodes;
import com.acctrue.tts.utils.AccountUtil;
import com.acctrue.tts.utils.DateUtil;

public class RelationCodesDB {
	private SQLiteDatabase db = null;
	private Context _ctx;
	
	public RelationCodesDB(Context ctx){
		_ctx = ctx;
	}
	
	public void addRelationCodes(String revCode,String trackCodes){
		//String[] trackCode = trackCodes.split(",");
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,Constants.DATABASE_CURRENT_VERSION, null);
		
		ContentValues values = new ContentValues();
		values.put("Id", UUID.randomUUID().toString());
		values.put("BoxCode", trackCodes);
		values.put("SQCode", revCode);
		values.put("CreateDate", DateUtil.getDatetime());
		values.put("Man", AccountUtil.getCurrentUser().getUserInfo().getUserName());
		db.insert("RelationCodes", null, values);
		db.close();
	}
	
	public List<RelationCodes> getAllRelationCodes(){
		List<RelationCodes> lst = new ArrayList<RelationCodes>();
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,Constants.DATABASE_CURRENT_VERSION, null);
		Cursor c = db.rawQuery("select Id,BoxCode,SQCode,CreateDate,Man from RelationCodes", null);
		if(c.moveToFirst()){
			do{
				RelationCodes m = new RelationCodes();
				m.setId(c.getString(0));
				m.setBoxCode(c.getString(1));
				m.setSqCode(c.getString(2));
				m.setCreateDate(c.getString(3));
				m.setMan(c.getString(4));
				lst.add(m);
			}while(c.moveToNext());
		}
		
		return lst;
	}
	
	public void deleteRelationCodes(List<RelationCodes> datas){
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,Constants.DATABASE_CURRENT_VERSION, null);
		for(int i=0;i<datas.size();i++){
			db.delete("RelationCodes", "Id=?", new String[]{datas.get(i).getId()});
		}
		db.close();
	}
	
	public void updateRelationCodes(RelationCodes data){
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,Constants.DATABASE_CURRENT_VERSION, null);
		
		ContentValues values = new ContentValues();
		values.put("BoxCode", data.getBoxCode());
		values.put("SQCode", data.getSqCode());
		
		db.update("RelationCodes", values, "Id=?", new String[]{data.getId()});
		
		db.close();
	}
	
	public RelationCodes getRelationCodes(String chargeCode){
		RelationCodes m = null;
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,Constants.DATABASE_CURRENT_VERSION, null);
		Cursor c = db.rawQuery("select Id,BoxCode,SQCode,CreateDate,Man from RelationCodes where SQCode='" + chargeCode + "'", null);
		if(c.moveToFirst()){
			m = new RelationCodes();
			m.setId(c.getString(0));
			m.setBoxCode(c.getString(1));
			m.setSqCode(c.getString(2));
			m.setCreateDate(c.getString(3));
			m.setMan(c.getString(4));
		}
		return m;
	}
}
