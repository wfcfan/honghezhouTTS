package com.acctrue.tts.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

public class SIMCardInfo {
	private TelephonyManager telephonyManager = null;
	private String phoneNO = null;
	private String serviceProvider = null;
	
	public SIMCardInfo(Context context) {  
        telephonyManager = (TelephonyManager) context  
                .getSystemService(Context.TELEPHONY_SERVICE);  
    } 
	
	public  String getNativePhoneNumber() {  
		if(phoneNO == null)
			phoneNO = telephonyManager.getLine1Number();
        return phoneNO; 
    }
	
	public String getServiceProvider() {  
		if(serviceProvider == null){
	        // 返回唯一的用户ID;就是这张卡的编号
	        String IMSI = telephonyManager.getSubscriberId();  
	        // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。  
	        System.out.println(IMSI);  
	        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {  
	        	serviceProvider = "中国移动";  
	        } else if (IMSI.startsWith("46001")) {  
	        	serviceProvider = "中国联通";  
	        } else if (IMSI.startsWith("46003")) {  
	        	serviceProvider = "中国电信";  
	        }  
		}
        return serviceProvider;  
    } 
}
