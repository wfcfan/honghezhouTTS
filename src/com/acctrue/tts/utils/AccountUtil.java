package com.acctrue.tts.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.acctrue.tts.Constants;
import com.acctrue.tts.GlobalApplication;
import com.acctrue.tts.dto.LoginResponse;
import com.acctrue.tts.dto.Sign;

public class AccountUtil {
	public final static String TAG = AccountUtil.class.getSimpleName();
	private static String USER_FILE = Constants.PATH_ACCOUNT + "user.ser";
	private static LoginResponse user = null;

	public static void clearCache() {
		AccountUtil.user = null;
		clearUserAccount();
	}
	
	public static void setUserFilePath(String filePath){
		USER_FILE = Constants.PATH_ACCOUNT + filePath + ".ser";
	}

	private static void clearUserAccount() {
		try {
			File fileUser = new File(USER_FILE);
			if (fileUser.exists()) {
				fileUser.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void saveAccount(LoginResponse u) {
		ObjectOutputStream oos = null;
		try {
			File file = new File(USER_FILE);
			if (file.exists()) {
				file.delete();
				Util.log(TAG, "file exists.");
			}

			oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(u);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static boolean isRegister(){
		File file = new File(USER_FILE);
		return file.exists();
	}

	public static void loadAccount() {
		LoginResponse u = loadUser();
		if (u != null) {
			user = u;
		} else {
			Util.log(TAG, "load empty.");
		}
	}

	public static LoginResponse getCurrentUser() {
		return user;
	}
	
	public static Sign getDefaultSign(){
		Sign sign= null;
		LoginResponse u = getCurrentUser();
		if(u != null){
			sign = new Sign();
			sign.setCorpId(u.getUserInfo().getCorpId());
			sign.setSerialNo(GlobalApplication.deviceId);
			sign.setTimestamp(DateUtil.parseDatetimeToJsonDate());
			sign.setToken(u.getNewToken());
			sign.setUserId(u.getUserInfo().getUserId());
			sign.setVersion(GlobalApplication.currentVersion);
		}
		return sign;
	}

	public static String getToken() {
		LoginResponse u = getCurrentUser();
		if (u != null) {
			return u.getNewToken();
		}
		return "";
	}

	public static boolean isLogin() {
		return user != null;
	}

	private static LoginResponse loadUser() {
		ObjectInputStream ois = null;
		try {
			File file = new File(USER_FILE);
			if (file.exists() == false) {
				Util.log(TAG, String.format("file not found [%s]", USER_FILE));
				return null;
			}
			ois = new ObjectInputStream(new FileInputStream(file));
			LoginResponse user = (LoginResponse) ois.readObject();
			Util.log(TAG, "USER=" + user);
			return user;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
