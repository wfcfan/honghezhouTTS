package com.acctrue.tts.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.acctrue.tts.Constants;
import com.acctrue.tts.dto.LoginResponse.UserInfo;
import com.acctrue.tts.utils.DateUtil;

public class UserInfoDB {
	private SQLiteDatabase db = null;
	private Context _ctx;
	private static final String TABLE_USERINFO = "UserInfo";

	public UserInfoDB(Context ctx) {
		_ctx = ctx;
	}

	public void addUserInfo(UserInfo ui) {
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		// 先删除，再新插入
		db.delete(TABLE_USERINFO, String.format("userId=%d", ui.getUserId()),
				null);

		ContentValues values = new ContentValues();
		values.put("corpCode", ui.getCorpCode());
		values.put("corpId", ui.getCorpId());
		values.put("corpName", ui.getCorpName());
		values.put("corpType", ui.getCorpType());
		values.put("userDisplayName", ui.getUserDisplayName());
		values.put("userId", ui.getUserId());
		values.put("userName", ui.getUserName());
		values.put("password", ui.getPassword());
		values.put("createTime ", DateUtil.getDatetime());
		db.insert(TABLE_USERINFO, null, values);
		db.close();
	}

	public UserInfo getUserInfo(int userId) {
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		// List<UserInfo> list = new ArrayList<UserInfo>();
		UserInfo ui = null;
		Cursor c = db.query(TABLE_USERINFO, new String[] { "corpCode",
				"corpId", "corpName", "corpType", "userDisplayName", "userId",
				"userName", "password" }, String.format("userId=%d", userId),
				null, null, null, null);
		while (c.moveToNext()) {
			ui = new UserInfo();
			ui.setCorpCode(c.getString(c.getColumnIndex("corpCode")));
			ui.setCorpId(c.getInt(c.getColumnIndex("corpId")));
			ui.setCorpName(c.getString(c.getColumnIndex("corpName")));
			ui.setCorpType(c.getInt(c.getColumnIndex("corpType")));
			ui.setUserDisplayName(c.getString(c
					.getColumnIndex("userDisplayName")));
			ui.setUserId(c.getInt(c.getColumnIndex("userId")));
			ui.setUserName(c.getString(c.getColumnIndex("userName")));
			ui.setPassword(c.getString(c.getColumnIndex("password")));
		}
		c.close();
		db.close();
		return ui;
	}

	public UserInfo getUserInfo(String userName, String password) {
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		// List<UserInfo> list = new ArrayList<UserInfo>();
		UserInfo ui = null;
		Cursor c = db.query(TABLE_USERINFO, new String[] { "corpCode",
				"corpId", "corpName", "corpType", "userDisplayName", "userId",
				"userName", "password" }, String.format(
				"userName='%s' and password='%s'", userName, password), null,
				null, null, null);
		while (c.moveToNext()) {
			ui = new UserInfo();
			ui.setCorpCode(c.getString(c.getColumnIndex("corpCode")));
			ui.setCorpId(c.getInt(c.getColumnIndex("corpId")));
			ui.setCorpName(c.getString(c.getColumnIndex("corpName")));
			ui.setCorpType(c.getInt(c.getColumnIndex("corpType")));
			ui.setUserDisplayName(c.getString(c
					.getColumnIndex("userDisplayName")));
			ui.setUserId(c.getInt(c.getColumnIndex("userId")));
			ui.setUserName(c.getString(c.getColumnIndex("userName")));
			ui.setPassword(c.getString(c.getColumnIndex("password")));
		}
		c.close();
		db.close();
		return ui;
	}

	public UserInfo getUserInfo(String userName) {
		db = this._ctx.openOrCreateDatabase(Constants.DATABASE_NAME,
				Constants.DATABASE_DEFAULT_VERSION, null);
		// List<UserInfo> list = new ArrayList<UserInfo>();
		UserInfo ui = null;
		Cursor c = db.query(TABLE_USERINFO, new String[] { "corpCode",
				"corpId", "corpName", "corpType", "userDisplayName", "userId",
				"userName", "password" },
				String.format("userName='%s'", userName), null, null, null,
				null);
		while (c.moveToNext()) {
			ui = new UserInfo();
			ui.setCorpCode(c.getString(c.getColumnIndex("corpCode")));
			ui.setCorpId(c.getInt(c.getColumnIndex("corpId")));
			ui.setCorpName(c.getString(c.getColumnIndex("corpName")));
			ui.setCorpType(c.getInt(c.getColumnIndex("corpType")));
			ui.setUserDisplayName(c.getString(c
					.getColumnIndex("userDisplayName")));
			ui.setUserId(c.getInt(c.getColumnIndex("userId")));
			ui.setUserName(c.getString(c.getColumnIndex("userName")));
			ui.setPassword(c.getString(c.getColumnIndex("password")));
		}
		c.close();
		db.close();
		return ui;
	}

}
