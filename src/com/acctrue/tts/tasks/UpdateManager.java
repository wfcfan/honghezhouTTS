package com.acctrue.tts.tasks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.acctrue.tts.R;
import com.acctrue.tts.utils.Toaster;

public class UpdateManager {
	private Context ctx;
	private String updateMsg = "有最新的软件包，请下载！";
	private String apkUrl = "http://192.168.137.1:8080/report/honghezhouTTS.apk";
	private Dialog noticeDialog;
	private Dialog downloadDialog;
	//private static final String savePath = "/data/data/;
	private static final String saveFileName = "myapp.apk";
	private ProgressBar mProgress;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;
	private static final int DOWN_FAIL = 3;
	private int progress;
	// 当前进度
	private Thread downLoadThread; 
	// 下载线程 
	private boolean interceptFlag = false;
	// 用户取消下载 // 通知处理刷新界面的handler
	private Handler mHandler = new Handler() {  
		@SuppressLint("HandlerLeak")  
		@Override  public void handleMessage(Message msg) {   
			switch (msg.what) {   
			case DOWN_UPDATE:    
				mProgress.setProgress(progress);    
				break;   
			case DOWN_OVER:   
				downloadDialog.dismiss();
				installApk();    
				break; 
			case DOWN_FAIL:
				downloadDialog.dismiss();
				Toaster.show("下载失败，稍后重试");
				break;
			}   
			super.handleMessage(msg);  
			} }; 
	public UpdateManager(Context context,String apkUrl) {  
		this.ctx = context; 
		if(apkUrl != null)
			this.apkUrl = apkUrl;
	}
	
	public void setUpdateMsg(String msg){
		updateMsg = msg;
	}
	private String getApkPath(){
		//return "/data/data/" + ctx.getPackageName() + "/apktemp/";
		return Environment.getExternalStorageDirectory() + "/download/";
	}
	// 显示更新程序对话框，供主程序调用 
	public void checkUpdateInfo() {
		showNoticeDialog(); 
	} 
	
	private void showNoticeDialog() {  
		
		LayoutInflater layoutInflater = LayoutInflater.from(ctx);
		final View dialogView = layoutInflater.inflate(R.layout.update_dialog_view,null);
		TextView tv = (TextView)dialogView.findViewById(R.id.updateContent);
		tv.setText(updateMsg);
		
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ctx);
		// Builder，可以通过此builder设置改变AleartDialog的默认的主题样式及属性相关信息  
		builder.setTitle("软件版本更新");
		builder.setView(dialogView);
		//builder.setMessage(updateMsg);  
		builder.setPositiveButton("下载", new OnClickListener() {   
			@Override   
			public void onClick(DialogInterface dialog, int which) {    
				dialog.dismiss();
				// 当取消对话框后进行操作一定的代码？取消对话框    
				showDownloadDialog();   
				}  
			});  
		builder.setNegativeButton("以后再说", new OnClickListener() {   
			@Override   
			public void onClick(DialogInterface dialog, int which) {    
				dialog.dismiss();   
				}  
			});  
		noticeDialog = builder.create();  
		noticeDialog.show(); 
	}
	protected void showDownloadDialog() {  
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ctx);  
		builder.setTitle("软件版本更新");  
		final LayoutInflater inflater = LayoutInflater.from(ctx);  
		View v = inflater.inflate(R.layout.progress, null);  
		mProgress = (ProgressBar) v.findViewById(R.id.progress);  
		builder.setView(v);
		// 设置对话框的内容为一个
		builder.setNegativeButton("取消", new OnClickListener() {   
			@Override   
			public void onClick(DialogInterface dialog, int which) {    
				dialog.dismiss();    
				interceptFlag = true;   
				}  
			});  
		downloadDialog = builder.create();  
		downloadDialog.show();  
		downloadApk(); 
	} 
	private void downloadApk() {  
		downLoadThread = new Thread(mdownApkRunnable);  
		downLoadThread.start(); 
	}
	protected void installApk() {  
		//String sdpath = Environment.getExternalStorageDirectory().toString();
		File apkfile = new File(ctx.getApplicationContext().getFilesDir().getAbsolutePath() + "/" + saveFileName);  
		if (!apkfile.exists()) {   
			return;  
		}  
		Intent i = new Intent(Intent.ACTION_VIEW);  
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		// File.toString()会返回路径信息  
		ctx.startActivity(i); 
	} 
	
	private Runnable mdownApkRunnable = new Runnable() {  
		@Override  
		public void run() {   
			URL url;   
			try {    
				url = new URL(apkUrl);    
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();    
				conn.connect();    
				int length = conn.getContentLength();    
				InputStream ins = conn.getInputStream(); 
				String savePath = getApkPath();
				File file = new File(savePath);    
				if (!file.exists()) {     
					file.mkdir();    
				}   
				
				
				//String apkFile = saveFileName;    
				//File ApkFile = new File(apkFile);  
				FileOutputStream outStream = ctx.openFileOutput(saveFileName, Context.MODE_WORLD_READABLE);    
				int count = 0;    
				byte buf[] = new byte[2048];    
				do {     
					int numread = ins.read(buf);     
					count += numread;     
					progress = (int) (((float) count / length) * 100);     
					// 下载进度     
					mHandler.sendEmptyMessage(DOWN_UPDATE);     
					if (numread <= 0) {      
						// 下载完成通知安装    
						mHandler.sendEmptyMessage(DOWN_OVER);      
						break;     
					}     
					outStream.write(buf, 0, numread);    
				} while (!interceptFlag);
				// 点击取消停止下载    
				outStream.close();    
				ins.close();   
				} catch (Exception e) {    
					e.printStackTrace();   
					mHandler.sendEmptyMessage(DOWN_FAIL);
				}
		}
	};
}
