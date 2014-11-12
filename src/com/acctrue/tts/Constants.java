package com.acctrue.tts;

import java.io.File;


public final class Constants {
	public static final boolean DEBUGGABLE = true;
	public static final String APP_NAME = "智慧农业";
	public static int CLIENT_VERSION_CODE;

	public static String URL_HOST;
	public static final String DEFAULT_HOST = "http://sy.17ghh.cn/tts";
	public static final String DEFAULT_URL_PATH = "/Portal/phoneservice/PhoneHhzService.svc";
	public static final String DEFAULT_FORMAT = "json";
	public final static String PARAM_FORMAT = "format";
	
	public final static String CONTENT_TYPE_JSON = "application/json";
	public final static String CONTENT_TYPE_XML = "application/xml";
	public final static String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";

	public final static int httpTimeoutConnection = 30000;
	public final static int httpTimeoutSocket = 40000;

	public static int WINDOW_WIDTH = 480;
	public static int WINDOW_HEIGHT = 800;
	
	public static String PATH;
	public static String PATH_ACCOUNT;
	public static String PATH_LOG;
	public static File ACCOUNT_FILE; 

	public static final String UUID_EMPTY = "00000000-0000-0000-0000-000000000000";
	
	public static final int DATABASE_DEFAULT_VERSION = 0;
	public static final int DATABASE_CURRENT_VERSION = 0;
	public static final String DATABASE_NAME = "honghwezhou";
	public final static String EXTRA_RESULT = "EXTRA_RESULT";
	public final static int REQCODE_SCANNIN_GREQUEST_CODE = 1;
	
	
	//农产品收取
	public static final int PRODUCT_RECEIVE = 0;
	//收取码管理
	public static final int RECEIVE_NO_MGNT = 1;
	//收取入库
	public static final int RECEIVE_TO_REPOS = 2;
	//入库管理
	public static final int SAVE_REPOS_MGNT = 3;
	//农产品转装
	public static final int PRODUCT_WRAP = 4;
	//追溯码管理
	public static final int TRACK_NO_MGNT = 5;
	//电商出库
	public static final int E_BUSINESS_OUT = 6;
	//直接出库
	public static final int DIRECT_OUT_STORE = 7;
	//单据管理
	public static final int FROM_MGR = 8;
	//数据下载
	public static final int DATA_DOWNLOAD = 9;
	
	public static final String URL_USER_LOGIN = "/rest/Login";
	public static final String URL_GETFARMLANDS = "/rest/GetFarmLands";
	public static final String URL_TIME = "/rest/GetSystemTime";
	public static final String URL_GETPRODUCTBYPAGE = "/rest/GetProductByPage";
	public static final String URL_GETWAREHOUSEBYPAGE ="/rest/GetWarehouseByPage";
	public static final String URL_GETBIZCORPSPAGE = "/rest/GetBizCorpsPage";
	public static final String URL_GETSTORETYPES = "/rest/GetStoreTypes";
	public static final String URL_DOWNLOADSTORES = "/rest/DownloadStores";
	public static final String URL_UPLOADCHARGECODES = "/rest/UploadChargeCodes";
	public static final String URL_UPLOADCHARGESTOREINFO = "/rest/UploadChargeStoreInfo";
	public static final String URL_SCANCODE = "/rest/ScanCode";
	public static final String URL_SCANREMOVE = "/rest/ScanRemove";
	public static final String URL_SCANVERIFY = "/rest/ScanVerify";
	public static final String URL_UPLOADSTORE = "/rest/UploadStore";
	public static final String URL_UPDATE =  "/rest/update";

}