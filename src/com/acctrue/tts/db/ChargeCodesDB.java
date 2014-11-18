package com.acctrue.tts.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.acctrue.tts.Constants;
import com.acctrue.tts.model.ChargeCodes;

public class ChargeCodesDB {
	private SQLiteDatabase db = null;
	private Context _ctx;
	protected static final String TABLE_CHARGECODES = "ChargeCodes";
	

	public ChargeCodesDB(Context ctx) {
		_ctx = ctx;
	}

	/**
	 * 6. 收取入库 获取所有的收取码，排除已经入库的码
	 */
	public List<ChargeCodes> getChargeCodes() {
		List<ChargeCodes> lst = new ArrayList<ChargeCodes>();
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		Cursor cursor = db.rawQuery("select a.ChargeId,a.Code,a.IsStoreIn,b.State from ChargeCodes a left join Charges b on a.ChargeId=b.Id where b.IsPackCode=0", null);
		if(cursor.moveToFirst()){
			do{
				ChargeCodes m = new ChargeCodes();
				m.setChargeId(cursor.getString(0));
				m.setCode(cursor.getString(1));
				m.setStoreIn(cursor.getInt(2) == 1);
				m.setState(cursor.getInt(3));
				lst.add(m);
			}while(cursor.moveToNext());
		}
		db.close();
//		List<ChargeCodes> lst = new ArrayList<ChargeCodes>();
//
//		ChargeCodes m = new ChargeCodes();
//		m.setChargeId(UUID.randomUUID().toString());
//		m.setCode("1234563");
//		m.setState(0);
//		m.setStoreIn(false);
//		lst.add(m);
//
//		m = new ChargeCodes();
//		m.setChargeId(UUID.randomUUID().toString());
//		m.setCode("98332340");
//		m.setState(0);
//		m.setStoreIn(false);
//		lst.add(m);
//
//		m = new ChargeCodes();
//		m.setChargeId(UUID.randomUUID().toString());
//		m.setCode("32412335");
//		m.setState(1);
//		m.setStoreIn(false);
//		lst.add(m);
//
//		m = new ChargeCodes();
//		m.setChargeId(UUID.randomUUID().toString());
//		m.setCode("4365243543");
//		m.setState(2);
//		m.setStoreIn(false);
//		lst.add(m);

		return lst;
	}

	public void delChargeCode(String code) {
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_CURRENT_VERSION, null);
		db.delete(TABLE_CHARGECODES, "Code=" + code, null);
		db.close();
	}

	public void addChargeCode(ChargeCodes cc) {
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_CURRENT_VERSION, null);
		ContentValues values = new ContentValues();
		values.put("ChargeId", cc.getChargeId());
		values.put("Code", cc.getCode());
		values.put("IsStoreIn", cc.isStoreIn());
		db.insert(TABLE_CHARGECODES, null, values);
		db.close();
	}

	public List<ChargeCodes> getChargeCodes2() {
		List<ChargeCodes> list = new ArrayList<ChargeCodes>();
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_CURRENT_VERSION, null);
		Cursor c = db.query(TABLE_CHARGECODES, new String[] { "ChargeId",
				"Code", "IsStoreIn" }, null, null, null, null, null);
		while (c.moveToNext()) {
			ChargeCodes cc = new ChargeCodes();
			cc.setChargeId(c.getString(c.getColumnIndex("ChargeId")));
			cc.setCode(c.getString(c.getColumnIndex("Code")));
			Log.d(TABLE_CHARGECODES, c.getString(c.getColumnIndex("IsStoreIn")));
			list.add(cc);
		}
		c.close();
		db.close();
		return list;
	}
	
	public List<String> getChargeCodes2String(String chargeId){
		List<String> ccList = new ArrayList<String>();
		for(ChargeCodes c : getChargeCodes2(chargeId)){
			ccList.add(c.getCode());
		}
		return ccList;
	}
	
	public List<ChargeCodes> getChargeCodes2(String chargeId) {
		List<ChargeCodes> list = new ArrayList<ChargeCodes>();
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_CURRENT_VERSION, null);
		Cursor c = db.query(TABLE_CHARGECODES, new String[] { "ChargeId",
				"Code", "IsStoreIn" }, String.format("ChargeId='%s'",chargeId), null, null,
				null, null);
		while (c.moveToNext()) {
			ChargeCodes cc = new ChargeCodes();
			cc.setChargeId(c.getString(c.getColumnIndex("ChargeId")));
			cc.setCode(c.getString(c.getColumnIndex("Code")));
			list.add(cc);
		}
		c.close();
		db.close();
		return list;
	}
	
	public ChargeCodes getChargeCodes(String chargeId,String code){
		ChargeCodes chargeCodes = null;
		for(ChargeCodes cc : getChargeCodes2(chargeId)){
			if(cc.getCode().equals(code)){
				chargeCodes = cc;
				break;
			}
		}
		return chargeCodes;
	}

}
