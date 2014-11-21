package com.acctrue.tts.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.acctrue.tts.Constants;

public class HongHeZhouDB {
	
	private final String TAG =  HongHeZhouDB.class.getSimpleName();
	private SQLiteDatabase db;
	
	private static final String CREATE_TABLE_CHARGECODES = "create table if not exists ChargeCodes(ChargeId char(36),Code text,IsStoreIn integer,createTime text)";
	private static final String CREATE_TABLE_CHARGES = "create table if not exists Charges(Id char(36) primary key,batchno text,ManNo text,ManName text,FarmlandNo text,FarmlandName text,ProductId text,ProductName text, CreateDate text,Man text,State INTEGER,IsPackCode INTEGER,Weight text)";
	private static final String CREATE_TABLE_CHARGESTOREIN = "create table if not exists ChargeStoreIn(Id text primary key,WarehouseId INTEGER,warehouseName text,Actor text,ActDate text)";
	private static final String CREATE_TABLE_CHARGESTOREINCODE = "create table if not exists ChargeStoreInCode(StoreInId text,Code text,IsStoreIn integer)";
	private static final String CREATE_TABLE_FARMLANDS = "create table if not exists FarmLands(FarmLandCode text,FarmLandName text,UserOwnerId integer,UserOwnerDisplayName text)";
	private static final String CREATE_TABLE_PRODUCTS = "create table if not exists Products(ProductId integer,ProductName text)";
	private static final String CREATE_TABLE_RELATIONCODES = "create table if not exists RelationCodes(Id text primary key,BoxCode text,SQCode text,CreateDate text,Man text)";
	private static final String CREATE_TABLE_WAREHOUSE = "create table if not exists Warehouse(CorpId integer,WarehouseId integer,WarehouseName text)";
	private static final String CREATE_TABLE_BIZCORPS = "create table if not exists BizCorps(CorpId integer,CurrentCorpId integer,CorpName text,CorpCode text,CorpPinYin text,CorpNumber text)";
	private static final String CREATE_TABLE_ORDERINFO = "create table if not exists OrderInfo(StoreId text,CorpId integer,OrderNo text,OrderType integer,OrderKind text,OrderTypeText text,BizCorpCode text,BizCorpName text,BizCorpCodeZD text,BizCorpNameZD text,Actor text,ActDate text,StoreStatus INTEGER,Flag INTEGER,UploadBy INTEGER,UserId INTEGER)";
	private static final String CREATE_TABLE_ORDERITEMINFO = "create table if not exists OrderItemInfo(Id integer primary key autoincrement,OrderId text,ProduceName text,ProduceCode text,BatchNo text,EditAmount text,DisplayUnit text,ScanAmount INTEGER,ScanMinAmount INTEGER,DisplayAmount INTEGER,MinAmount INTEGER,Unit text)";
	private static final String CREATE_TABLE_PACKCODE = "create table if not exists PackCode(ID text primary key,OrderId text,OrderItemId integer,CodeValue text,ParentCode text,ByDelete INTEGER,IsParentScanCode INTEGER,Actor text,ActDate text)";
	private static final String CREATE_TABLE_STORETYPES = "create table if not exists StoreTypes(DownStoreType integer,HasBizCorp integer,HasRecCorp integer,StoreKind integer,StoreSort integer,StoreType integer,StoreTypeKey text,StoreTypeText text,UpStoreType integer);";
	private static final String CREATE_TABLE_STORECODE = "create table if not exists StoreCode(Id text primary key,StoreId text,CodeId text,CodeCount integer,ParentId text,SavedCodeId text,ProductCode text,ProductName text,InnerPacking text,ProduceBatchNo text,ProductUnit text,CurrentAmount integer,CreateTime text,IsDelete integer)";
	private static final String CREATE_TABLE_STORE = "create table if not exists Store(StoreId text primary key,StoreNo text,StoreType integer,StoreTypeText text,StoreStatus integer,StoreStatusText text,StoreKind integer,StoreMan text,StoreDate text,CorpId integer,CorpCode text,CorpName text,BizCorpId integer,BizCorpCode text,BizCorpName text,RecCorpId integer,RecCorpCode text,RecCorpName text,Description text,CreateTime text,source integer)";
	private static final String CREATE_TABLE_STOREITEM = "create table if not exists StoreItem(ItemId text primary key,StoreId text,ProduceBatchNo text,ProductCode text,ProductName text,Amount integer,DisplayAmount integer,DisplayProductUnit text)";
	
	public HongHeZhouDB(Context ctx) {
		db = ctx.openOrCreateDatabase(Constants.DATABASE_NAME, Constants.DATABASE_DEFAULT_VERSION, null);
		
		db.execSQL(CREATE_TABLE_CHARGECODES);
		db.execSQL(CREATE_TABLE_CHARGES);
		db.execSQL(CREATE_TABLE_CHARGESTOREIN);
		db.execSQL(CREATE_TABLE_CHARGESTOREINCODE);
		db.execSQL(CREATE_TABLE_FARMLANDS);
		db.execSQL(CREATE_TABLE_PRODUCTS);
		db.execSQL(CREATE_TABLE_RELATIONCODES);
		db.execSQL(CREATE_TABLE_WAREHOUSE);
		db.execSQL(CREATE_TABLE_BIZCORPS);
		db.execSQL(CREATE_TABLE_ORDERINFO);
		db.execSQL(CREATE_TABLE_ORDERITEMINFO);
		db.execSQL(CREATE_TABLE_PACKCODE);
		db.execSQL(CREATE_TABLE_STORETYPES);
		
		db.execSQL(CREATE_TABLE_STORE);
		db.execSQL(CREATE_TABLE_STOREITEM);
		db.execSQL(CREATE_TABLE_STORECODE);
		
		Log.d(TAG, "database is created!");
		
//		if (db.getVersion() == 0) {//数据库升级v1.0,把收取表的主键改为UUID的方式
//			db.execSQL("drop table ChargeCodes");
//			db.execSQL("drop table Charges");
//			db.execSQL(CREATE_TABLE_CHARGECODES);
//			db.execSQL(CREATE_TABLE_CHARGES);
//			db.setVersion(1);
//		}

		db.close();
	}
	
	void delDB(){
		
	}

	
}
