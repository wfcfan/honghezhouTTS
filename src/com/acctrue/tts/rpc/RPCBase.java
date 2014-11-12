package com.acctrue.tts.rpc;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.acctrue.tts.Constants;
import com.acctrue.tts.rpc.CustomMultiPartEntity.ProgressListener;
import com.acctrue.tts.rpc.exception.ApiException;
import com.acctrue.tts.rpc.exception.NoSignalException;
import com.acctrue.tts.rpc.exception.RpcException;
import com.acctrue.tts.utils.NetworkUtil;
import com.acctrue.tts.utils.QueryString;
import com.acctrue.tts.utils.Util;

public class RPCBase {

	public static String TAG = "HTTP";

	protected Context mContext;
	protected byte sBuffer[] = new byte[512];

	public static final String BOUNDARY = "---------7d4a6d158c9";
	protected static final String MULTIPART_FORM_DATA = "multipart/form-data";
	
	public RPCBase(){
		;
	}
	
	public RPCBase(Context content){
		this();
		this.mContext = content;
	}
	
	/**
	 * 判断当前网络模式是否为CMWAP
	 * 
	 * @param context
	 * @return
	 */
	public boolean isCmwapNet(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netWrokInfo = manager.getActiveNetworkInfo();
		if (netWrokInfo == null || !netWrokInfo.isAvailable()) {
			return false;
		} else if (netWrokInfo.getTypeName().equals("mobile") && netWrokInfo.getExtraInfo() != null && netWrokInfo.getExtraInfo().equals("cmwap")) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @return
	 * @throws RpcException
	 */
	public HttpClient getHttpClient(int soTimeout, int connectTimeout) throws RpcException {
		DefaultHttpClient defaulthttpclient = new DefaultHttpClient();
		HttpParams httpparams = defaulthttpclient.getParams();
		try {
			if (isCmwapNet(mContext)) {
				HttpHost proxy = new HttpHost("10.0.0.172", 80, "http");
				httpparams.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			}

			HttpConnectionParams.setConnectionTimeout(httpparams, connectTimeout);
			HttpConnectionParams.setSoTimeout(httpparams, soTimeout);
			HttpConnectionParams.setTcpNoDelay(httpparams, true);

			httpparams.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			httpparams.setParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return defaulthttpclient;
	}

	public HttpUriRequest getHttpUriRequest(ArrayList<BasicNameValuePair> listPair, String method, String apiPath) throws RpcException {
		if (listPair != null) {
			listPair.addAll(getDefaultFormEntity());
		}

		if (method.toUpperCase().equals(HttpGet.METHOD_NAME)) {
			String param = "";
			if (listPair != null)
				param = URLEncodedUtils.format(listPair, HTTP.UTF_8);
			URI uri;
			try {
				if(apiPath.startsWith("http:")){
					uri = new URI(apiPath + param);
				}else{
					uri = new URI(Constants.URL_HOST + apiPath + param);
				}
			} catch (URISyntaxException e) {
				throw new RpcException(e);
			}
			HttpGet httpget = new HttpGet(uri);

			Util.logRequst(httpget);
			return httpget;
		} else {
			HttpPost httppost = new HttpPost(Constants.URL_HOST + apiPath);
			Log.d("RequestURL", httppost.getURI().toString());
			try {
				if (listPair != null) {
					UrlEncodedFormEntity urlencodedformentity = new UrlEncodedFormEntity(listPair, HTTP.UTF_8);
					httppost.setEntity(urlencodedformentity);
					Util.logRequst(httppost);
				}
			} catch (UnsupportedEncodingException e) {
				throw new RpcException(e);
			}
			return httppost;
		}
	}

	protected String execute(ArrayList<BasicNameValuePair> listPair, String apiPath) throws ConnectTimeoutException, RpcException {//
		return execute(listPair, apiPath, HttpGet.METHOD_NAME, Constants.httpTimeoutConnection, Constants.httpTimeoutSocket);
	}

	protected String execute(ArrayList<BasicNameValuePair> listPair, String apiPath, String method) throws ConnectTimeoutException, RpcException {
		return execute(listPair, apiPath, method, Constants.httpTimeoutConnection, Constants.httpTimeoutSocket);
	}
	
	public String execute(StringEntity entity, String apiPath) throws ConnectTimeoutException, RpcException {
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		String result = "";
		try {
			httpClient = getHttpClient(Constants.httpTimeoutConnection, Constants.httpTimeoutSocket);
			httpPost = (HttpPost) getHttpUriRequest(null, HttpPost.METHOD_NAME, apiPath);
			

			httpPost.setHeader("Accept", Constants.CONTENT_TYPE_JSON);
			httpPost.setHeader("Content-Type", Constants.CONTENT_TYPE_JSON);

			// Send it
			httpPost.setEntity(entity);

			HttpResponse response = httpClient.execute(httpPost);
			result = JSONTokener(EntityUtils.toString(response.getEntity()));

			Util.log(TAG, result);
			// if (isError(result)) {
			// ApiException apie = new ApiException();
			// parseError(result, apie);
			// throw new RpcException(apie);
			// }
			return result;
		} catch (IOException io) {
			throw new RpcException("MultiPartEntity execute!!", io);
		} finally {
			Util.logResponse(result);
			if (httpClient != null)
				httpClient.getConnectionManager().shutdown();
		}
	}
	
	public String executeGet(String apiPath) throws ConnectTimeoutException, RpcException{
		HttpClient httpClient = null;
		HttpGet httpGet = null;
		String result = "";
		try{
			httpClient = getHttpClient(Constants.httpTimeoutConnection, Constants.httpTimeoutSocket);
			httpGet = (HttpGet) getHttpUriRequest(null, HttpGet.METHOD_NAME, apiPath);
			

			httpGet.setHeader("Accept", Constants.CONTENT_TYPE_JSON);
			httpGet.setHeader("Content-Type", Constants.CONTENT_TYPE_JSON);
			
			HttpResponse response = httpClient.execute(httpGet);
			result = JSONTokener(EntityUtils.toString(response.getEntity()));
		}catch(Exception ex){
			
		}
		return result;
	}

	public String execute(ArrayList<BasicNameValuePair> listPair, String apiPath, String method, int soTimeout, int connectTimeout) throws ConnectTimeoutException, RpcException {
		if (listPair != null) {
			listPair.addAll(getDefaultFormEntity());
		}

		String result = null;
		URL url = null;
		HttpURLConnection connection = null;
		InputStreamReader in = null;
		try {
			QueryString qs = new QueryString();
			for (int i = 0; i < listPair.size(); i++) {
				BasicNameValuePair nv = listPair.get(i);
				qs.add(nv.getName(), nv.getValue());
			}

			String api_url = Constants.URL_HOST + apiPath;
			Util.log("NEWHTTP", method.toUpperCase() + " " + api_url + qs.getQuery());

			switch (NetworkUtil.getNetworkState(mContext)) {
			case NOTHING:
				throw new RpcException(new NoSignalException());
			case MOBILE:
				break;
			case WIFI:
				// SystemClock.sleep(5000);
				break;
			default:
				break;
			}

			if (HttpPost.METHOD_NAME.equals(method)) {
				url = new URL(api_url);

				connection = getHttpUrlConnection(url);

				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setRequestMethod(method);
				connection.setConnectTimeout(connectTimeout);
				connection.setReadTimeout(soTimeout);
				connection.setRequestProperty("Content-Type", Constants.CONTENT_TYPE_JSON);
				connection.setRequestProperty("Charset", "utf-8");
				connection.setRequestProperty("Content-Length", "" + Integer.toString(qs.getQuery().getBytes().length));
				connection.setUseCaches(false);

				DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
				wr.writeBytes(qs.getQuery());
				wr.flush();
				wr.close();
			} else {
				url = new URL(api_url + qs.getQuery());

				connection = getHttpUrlConnection(url);

				connection.setDoOutput(true);
				connection.setInstanceFollowRedirects(false);
				connection.setRequestMethod("GET");
				connection.setConnectTimeout(connectTimeout);
				connection.setReadTimeout(soTimeout);
				connection.setRequestProperty("Content-Type", "text/plain");
				connection.setRequestProperty("charset", "utf-8");
				connection.connect();

			}
			
			int statusCode = connection.getResponseCode();
			Log.d(TAG, String.valueOf(statusCode));
			
			in = new InputStreamReader(connection.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(in);
			StringBuffer strBuffer = new StringBuffer();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				strBuffer.append(line);
			}
			result = strBuffer.toString();
			result = JSONTokener(result);
			Util.log("NEWHTTP", method.toUpperCase() + " RESPONSE " + result);

			return result;
		} catch (ConnectTimeoutException e) {
			throw e;
		} catch (ClientProtocolException e) {
			throw new RpcException(e);
		} catch (IOException e) {
			throw new RpcException(e);
			// } catch (JSONException e) {
			// throw new RpcException(e);
		} catch (Exception e) {
			throw new RpcException(e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private HttpURLConnection getHttpUrlConnection(URL url) throws MalformedURLException, IOException {
		HttpURLConnection connection;
		if (isCmwapNet(mContext)) {
			URL newURL = new URL(url.getProtocol(), "10.0.0.172", url.getPort(), url.getFile());
			connection = (HttpURLConnection) newURL.openConnection();
			connection.setRequestProperty("X-Online-Host", url.getHost());
		} else {
			connection = (HttpURLConnection) url.openConnection();
		}
		return connection;
	}

	public static boolean isError(String s) throws JSONException {
		int resultCode = 0;
		JSONObject obj = new JSONObject(s);
		resultCode = obj.getInt("result");
		return resultCode == 0 ? true : false;
	}

	public static void parseError(String s, ApiException e) throws JSONException {
		int errorCode = 0;
		String errorDesc = "";
		JSONObject obj = new JSONObject(s);
		errorCode = obj.optInt("error_info");
		errorDesc = obj.optString("data");

		e.setServerError(errorCode, errorDesc);
	}
	
	public void addNetworkFormEntity(Context context, ArrayList<BasicNameValuePair> listPair) {
		if (listPair == null) {
			listPair = new ArrayList<BasicNameValuePair>();
		}
	}
	
	public ArrayList<BasicNameValuePair> getDefaultFormEntity() {
		ArrayList<BasicNameValuePair> listPair = new ArrayList<BasicNameValuePair>();

		listPair.add(new BasicNameValuePair(Constants.PARAM_FORMAT, Constants.DEFAULT_FORMAT));
		return listPair;
	}

	public CustomMultiPartEntity getDefaultMultiPartEntiy(Context context) throws UnsupportedEncodingException {
		ProgressListener listener = new ProgressListener() {
			@Override
			public void transferred(long num) {
			}
		};
		CustomMultiPartEntity multipartContent = new CustomMultiPartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, BOUNDARY, Charset.forName("UTF-8"), listener);
		return multipartContent;
	}

	protected static String JSONTokener(String in) {
		// consume an optional byte order mark (BOM) if it exists
		if (in != null && in.startsWith("\ufeff")) {
			in = in.substring(1);
		}

		return in;
	}
}