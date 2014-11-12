package com.acctrue.tts.rpc;


import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.acctrue.tts.utils.Util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class RestAsyncTask extends AsyncTask<String, Integer, String> {
	private Context ctx;
	private ProgressDialog progressDialog;
	private int resFailed;
	private List<JsonRest> models;
	private OnCompleteListener completeListener;
	
	public RestAsyncTask(Context ctx,int resProgTitle, int resProgMsg,int resFailed,List<JsonRest> models,OnCompleteListener completeListener){
		this.ctx = ctx;
		this.resFailed = resFailed;
		this.models = models;
		this.completeListener = completeListener;
		progressDialog = new ProgressDialog(ctx);
		progressDialog.setTitle(ctx.getResources().getString(resProgTitle));
		progressDialog.setMessage(ctx.getResources().getString(resProgMsg));
		progressDialog.setCancelable(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	}
	
	private HttpResponse doPost(HttpClient client,String url) throws Exception{
		
			HttpPost httpPost=new HttpPost(url);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			
			HttpResponse response = null;;
			
			for(JsonRest item : models){
			
				StringEntity entity = new StringEntity(item.toJson());
				httpPost.setEntity(entity); 
				response = client.execute(httpPost);
			}
			
			
			return response;

	}
	
	private HttpResponse doGet(HttpClient client,String url) throws Exception{
		HttpGet httpGet=new HttpGet(url); 
		httpGet.setHeader("Accept", "application/json");
		httpGet.setHeader("Content-type", "application/json");
		return client.execute(httpGet);
	}
	
	protected String JSONTokener(String in) {
		// consume an optional byte order mark (BOM) if it exists
		if (in != null && in.startsWith("\ufeff")) {
			in = in.substring(1);
		}

		return in;
	}
	
	@Override
	protected String doInBackground(String... params) {
		HttpResponse response = null;
		String result = null;
		String method = params[0];
		HttpClient client=new DefaultHttpClient(); 
		try{
			if(method.equalsIgnoreCase("get")){
				response = doGet(client,params[1]);
			}else if(method.equalsIgnoreCase("post")){
				response = doPost(client,params[1]);
			}
			
			HttpEntity httpEntity = response.getEntity();
			if(httpEntity != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				result = JSONTokener(EntityUtils.toString(httpEntity));
			}
		}catch(Exception e){

		} finally {
			Util.logResponse(result);
			if (client != null)
				client.getConnectionManager().shutdown();
		}
		
		return result;
	}
	
	

	@Override
	protected void onPreExecute() {
		progressDialog.show();

		super.onPreExecute();
	}
	
	@Override
	protected void onPostExecute(String result) {
		boolean success = false;
		
		progressDialog.dismiss();
		
		try{
			if(result != null){
				completeListener.onComplete(result);
				success = true;
			}
		}catch(Exception e){
			
		}
		
		if(!success){
			Toast.makeText(ctx, ctx.getResources().getString(resFailed), Toast.LENGTH_LONG).show();
		}
		super.onPostExecute(result);
	}

}
