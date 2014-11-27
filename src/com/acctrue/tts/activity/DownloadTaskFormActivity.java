package com.acctrue.tts.activity;

import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.acctrue.tts.Constants;
import com.acctrue.tts.R;
import com.acctrue.tts.adapter.DownTaskFormAdapter;
import com.acctrue.tts.db.StoreDB;
import com.acctrue.tts.dto.DownloadStoresRequest;
import com.acctrue.tts.dto.DownloadStoresResponse;
import com.acctrue.tts.dto.LoginResponse;
import com.acctrue.tts.model.Store;
import com.acctrue.tts.model.StoreItem;
import com.acctrue.tts.rpc.OnCompleteListener;
import com.acctrue.tts.rpc.RpcAsyncTask;
import com.acctrue.tts.tasks.DownloadHelper;
import com.acctrue.tts.tasks.TaskUtils;
import com.acctrue.tts.utils.AccountUtil;
import com.acctrue.tts.utils.Toaster;
import com.acctrue.tts.utils.ViewUtil;

/**
 * 下载单据
 * @author wangfeng
 *
 */
public class DownloadTaskFormActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_down_taskform);
		
		ViewUtil.initHeader(this, "下载入出库单");
		init();
	}
	
	void init() {
		// 初始化下拉列表
		final ListView lstReps = (ListView) findViewById(R.id.lstReps);
		//OrderInfoDB db = new OrderInfoDB(this);
		
		DownloadHelper helper = new DownloadHelper(this);
		RpcAsyncTask task = helper.getTask(DownloadHelper.TASK_STORE);
		
		DownloadStoresRequest dsr = new DownloadStoresRequest();
		dsr.setSign(AccountUtil.getDefaultSign());
		LoginResponse u = AccountUtil.getCurrentUser();
		dsr.setSearch(u.getUserInfo().getCorpId(), u.getUserInfo().getUserId());
		
		task = new RpcAsyncTask(this, dsr, new OnCompleteListener() {

			@Override
			public void onComplete(String content) {
				DownloadStoresResponse gr = null;
				try {
					gr = DownloadStoresResponse.fromJson(new JSONObject(content));
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				if (gr != null) {
					if (gr.isError()) {
						Toaster.show(gr.getMessage());
						return;
					}
					
					List<Store> storeList = gr.getStores();
					
					final DownTaskFormAdapter adptRev = new DownTaskFormAdapter(
							DownloadTaskFormActivity.this, storeList);

					lstReps.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
					lstReps.setAdapter(adptRev);
					lstReps.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							Store store = (Store)adptRev.getItem(position);
							showDetail(store);
						}

					});
				}
			}
			
		});
		
		TaskUtils.execute(task, TaskUtils.POST, Constants.URL_DOWNLOADSTORES);

		Button btnBack = (Button)this.findViewById(R.id.btn_back);
		btnBack.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				finish();
			}
			
		});
		
		Button btnDown = (Button)this.findViewById(R.id.btn_down);
		btnDown.setOnClickListener(this);
	}
	
	final void showDetail(Store store){
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		
		final View detailView = layoutInflater.inflate(R.layout.orderinfo_detail_view, null);
		
		this.setViewText((TextView)detailView.findViewById(R.id.textView1), store.getStoreNo());
		this.setViewText((TextView)detailView.findViewById(R.id.textView2), store.getStoreTypeText());
		this.setViewText((TextView)detailView.findViewById(R.id.textView3), store.getStoreStatusText());
		this.setViewText((TextView)detailView.findViewById(R.id.textView4), store.getStoreMan());
		this.setViewText((TextView)detailView.findViewById(R.id.textView5), store.getStoreDateText());
		this.setViewText((TextView)detailView.findViewById(R.id.textView6), store.getCorpName());
		this.setViewText((TextView)detailView.findViewById(R.id.textView7), store.getDescription());
		String ivs = StoreItem.toArrayString(store.getItem());
		this.setViewText((TextView)detailView.findViewById(R.id.textView8), ivs);
		//this.setViewText((TextView)detailView.findViewById(R.id.textView8), sotre.getDescription());
		//this.setViewText((TextView)detailView.findViewById(R.id.textView9), sotre.getStoreKindText());
		
		Dialog detailDialog = new AlertDialog.Builder(this)
		.setTitle("详情")
		.setView(detailView)
		.setPositiveButton(R.string.btn_close, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
			}
			
		})
		.create();
		detailDialog.show();
	}
	
	void setViewText(TextView tv,String v){
		tv.setText(v);
	}
	
	void setViewText(TextView tv,int v){
		tv.setText(String.valueOf(v));
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.btn_down){
			final ListView lstReps = (ListView) findViewById(R.id.lstReps);
			final DownTaskFormAdapter adptRev = (DownTaskFormAdapter)lstReps
					.getAdapter();
			
			if(adptRev.getCheckedData().size() == 0){
				Toaster.show("请先选择数据行!");
				return;
			}
			
			//按所选择的下载
			List<Store> orderList = adptRev.getCheckedData();
			if(orderList != null){
				final StoreDB db = new StoreDB(this);

				for (Store order : orderList) {
					db.delStore(order);
					db.addStoreBySelf(order);
				}
				Toaster.show(R.string.msg_download_success);
			}
		}
	}

}
