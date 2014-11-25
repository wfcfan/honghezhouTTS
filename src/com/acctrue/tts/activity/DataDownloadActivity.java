package com.acctrue.tts.activity;

import java.util.Iterator;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.acctrue.tts.Constants;
import com.acctrue.tts.R;
import com.acctrue.tts.rpc.RpcAsyncTask;
import com.acctrue.tts.tasks.DownloadHelper;
import com.acctrue.tts.tasks.TaskUtils;
import com.acctrue.tts.utils.ViewUtil;

/**
 * 数据下载
 * 
 * @author wangfeng
 * 
 */
public class DataDownloadActivity extends ActivityBase implements
		OnClickListener {

	private final String TAG = DataDownloadActivity.class.getSimpleName();

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_download);

		Button btnBack = (Button) this.findViewById(R.id.btnlogout);
		btnBack.setOnClickListener(this);

		// 农户信息下载
		Button btnNongHu = (Button) this.findViewById(R.id.btn_nonghu);
		btnNongHu.setOnClickListener(this);

		// 商品信息下载
		Button btnShangPin = (Button) this.findViewById(R.id.btn_shangpin);
		btnShangPin.setOnClickListener(this);

		// 仓库信息下载
		Button btnChangku = (Button) this.findViewById(R.id.btn_changku);
		btnChangku.setOnClickListener(this);

		// 下载往来企业
		Button btnCorp = (Button) this.findViewById(R.id.btn_corp);
		btnCorp.setOnClickListener(this);

		// 全部下载
		Button btnDownAll = (Button) this.findViewById(R.id.btnDownall);
		btnDownAll.setOnClickListener(this);

		// 后退
		Button btnback = (Button) this.findViewById(R.id.btnBack);
		btnback.setOnClickListener(this);

		// 下载计划任务
		Button btnTask = (Button) this.findViewById(R.id.btn_task);
		btnTask.setOnClickListener(this);

		// 上传入出库单
		Button btnUploadFrom = (Button) this.findViewById(R.id.btn_uploadform1);
		btnUploadFrom.setOnClickListener(this);
		btnUploadFrom.setVisibility(View.GONE);//与主界面的单据管理功能重复，先隐藏了。
		
		//版本更新测试
		Button btnVersion = (Button)this.findViewById(R.id.btnVersion);
		btnVersion.setOnClickListener(this);
		btnVersion.setVisibility(View.GONE);
		
		ViewUtil.initHeader(this, "数据下载");
	}

	@Override
	public void onClick(View view) {
		Intent intent = null;

		DownloadHelper helper = new DownloadHelper(this);
		RpcAsyncTask task = null;

		switch (view.getId()) {
		case R.id.btnlogout:
		case R.id.btnBack:
		case R.id.lblUser:
			finish();
			return;
		case R.id.btn_nonghu:
			task = helper.getTask(DownloadHelper.TASK_FARMLANDS);
			TaskUtils.execute(task, TaskUtils.POST, Constants.URL_GETFARMLANDS);
			break;
		case R.id.btn_shangpin:
			task = helper.getTask(DownloadHelper.TASK_PRODUCT);
			TaskUtils.execute(task, TaskUtils.POST,
					Constants.URL_GETPRODUCTBYPAGE);
			break;
		case R.id.btn_changku:
			task = helper.getTask(DownloadHelper.TAKS_WAREHOUSE);
			TaskUtils.execute(task, TaskUtils.POST,
					Constants.URL_GETWAREHOUSEBYPAGE);
			break;
		case R.id.btn_corp:
			task = helper.getTask(DownloadHelper.TASK_BIZCORP);
			TaskUtils.execute(task, TaskUtils.POST,
					Constants.URL_GETBIZCORPSPAGE);
			break;
		case R.id.btn_task:
			// 选执行单据类型下载
			task = helper.getTask(DownloadHelper.TASK_STORETYPES);
			TaskUtils.execute(task, TaskUtils.GET, Constants.URL_GETSTORETYPES);

			intent = new Intent(DataDownloadActivity.this,
					DownloadTaskFormActivity.class);
			startActivity(intent);
			// finish();
			return;
		case R.id.btn_uploadform1:
//			intent = new Intent(DataDownloadActivity.this,
//					UploadTaskFormActivity.class);
//			startActivity(intent);
			return;
		case R.id.btnDownall:
			Map<String, RpcAsyncTask> taskMap = helper.getAllTasks();
			Iterator<String> it = taskMap.keySet().iterator();
			while (it.hasNext()) {
				String url = it.next();
				if(url.equals(Constants.URL_GETSTORETYPES)){
					TaskUtils.execute(taskMap.get(url), TaskUtils.GET, url);
				}else{
					TaskUtils.execute(taskMap.get(url), TaskUtils.POST, url);
				}
			}
			Log.d(TAG, String.format("task count:%d", taskMap.size()));
			break;
		}
	}
}
