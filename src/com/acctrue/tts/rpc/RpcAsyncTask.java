package com.acctrue.tts.rpc;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.acctrue.tts.R;
import com.acctrue.tts.utils.Toaster;

public class RpcAsyncTask extends AsyncTask<String, Integer, String> {
	private final Context ctx;
	private final ProgressDialog progressDialog;
	private final JsonRest model;
	private final OnCompleteListener completeListener;
	private final RPCBase rpc;
	private final boolean showProgressDialog;

	public RpcAsyncTask(Context ctx, JsonRest model,
			OnCompleteListener completeListener) {
		this(ctx, model, completeListener, true);
	}

	public RpcAsyncTask(Context ctx, JsonRest model,
			OnCompleteListener completeListener, boolean showProgressDialog) {
		this.ctx = ctx;
		this.model = model;
		this.completeListener = completeListener;
		rpc = new RPCBase(ctx);
		this.showProgressDialog = showProgressDialog;

		progressDialog = new ProgressDialog(ctx);
		if (showProgressDialog) {
			this.init();
		}
	}

	private void init() {
		progressDialog.setTitle(R.string.tip_title);
		progressDialog.setMessage(ctx.getResources().getString(
				R.string.data_loadding));
		progressDialog.setCancelable(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	}

	private String doPost(String apiPath) throws Exception {
		StringEntity strContent = null;
		String result = "";
		try {
			strContent = new StringEntity(model.toJson());
			Log.i("RequestJSON", model.toJson());
			strContent.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			result = rpc.execute(strContent, apiPath);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return result;
	}

	private String doGet(String apiPath) throws Exception {
		return rpc.executeGet(apiPath);
	}

	@Override
	protected String doInBackground(String... params) {
		String result = "";
		final String method = params[0];
		final String apiUrl = params[1];
		try {
			if (method.equalsIgnoreCase(HttpPost.METHOD_NAME))
				result = doPost(apiUrl);
			else if (method.equalsIgnoreCase(HttpGet.METHOD_NAME))
				result = doGet(apiUrl);
			else
				result = "";
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return result;
	}

	@Override
	protected void onPreExecute() {
		if (showProgressDialog) {
			progressDialog.show();
		}

		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(String result) {
		boolean success = false;
		if (showProgressDialog) {
			progressDialog.dismiss();
		}

		try {
			if (result != null) {
				completeListener.onComplete(result);
				success = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toaster.show(e.getMessage());
		}

		if (!success) {
			Toaster.show(R.string.data_failed, Toast.LENGTH_LONG);
		}
		super.onPostExecute(result);
	}

}
