package com.acctrue.tts.dto;

import java.io.Serializable;

import org.json.JSONObject;

@SuppressWarnings("serial")
public class LoginResponse implements Serializable {

	private String errorCode;
	private boolean isError;
	private String message;
	private String newToken;
	private boolean updateToken;
	private UserInfo userInfo;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public boolean isError() {
		return isError;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getNewToken() {
		return newToken;
	}

	public void setNewToken(String newToken) {
		this.newToken = newToken;
	}

	public boolean isUpdateToken() {
		return updateToken;
	}

	public void setUpdateToken(boolean updateToken) {
		this.updateToken = updateToken;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public static class UserInfo implements Serializable {
		private String corpCode;
		private int corpId;
		private String corpName;
		private int corpType;
		private String userDisplayName;
		private int userId;
		private String userName;

		public String getCorpCode() {
			return corpCode;
		}

		public void setCorpCode(String corpCode) {
			this.corpCode = corpCode;
		}

		public int getCorpId() {
			return corpId;
		}

		public void setCorpId(int corpId) {
			this.corpId = corpId;
		}

		public String getCorpName() {
			return corpName;
		}

		public void setCorpName(String corpName) {
			this.corpName = corpName;
		}

		public int getCorpType() {
			return corpType;
		}

		public void setCorpType(int corpType) {
			this.corpType = corpType;
		}

		public String getUserDisplayName() {
			return userDisplayName;
		}

		public void setUserDisplayName(String userDisplayName) {
			this.userDisplayName = userDisplayName;
		}

		public int getUserId() {
			return userId;
		}

		public void setUserId(int userId) {
			this.userId = userId;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

	}

	public static LoginResponse fromJson(JSONObject obj) {
		LoginResponse lr = null;
		try {
			lr = new LoginResponse();
			lr.errorCode = obj.getString("ErrorCode");
			lr.isError = obj.getBoolean("IsError");
			lr.message = obj.getString("Message");
			lr.newToken = obj.getString("NewToken");
			lr.updateToken = obj.getBoolean("UpdateToken");
			JSONObject userObj = obj.getJSONObject("UserInfo");
			UserInfo u = new UserInfo();
			u.corpCode = userObj.getString("CorpCode");
			u.corpId = userObj.getInt("CorpId");
			u.corpName = userObj.getString("CorpName");
			u.corpType = userObj.getInt("CorpType");
			u.userDisplayName = userObj.getString("UserDisplayName");
			u.userId = userObj.getInt("UserId");
			u.userName = userObj.getString("UserName");
			lr.userInfo = u;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return lr;
	}
}
