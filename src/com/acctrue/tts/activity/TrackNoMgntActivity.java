package com.acctrue.tts.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.acctrue.tts.R;
import com.acctrue.tts.db.RelationCodesDB;
import com.acctrue.tts.dto.RelationCodesRequest;
import com.acctrue.tts.model.RelationCodes;
import com.acctrue.tts.rpc.JsonRest;
import com.acctrue.tts.rpc.OnCompleteListener;
import com.acctrue.tts.rpc.RpcAsyncTask;
import com.acctrue.tts.tasks.TaskUtils;
import com.acctrue.tts.utils.Toaster;
import com.acctrue.tts.utils.ViewUtil;

/**
 * 转装上传
 * 
 * @author peng
 * 
 */
public class TrackNoMgntActivity extends Activity { // implements
													// ModifyRelationCode {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_track_no_mgnt);

		ViewUtil.initHeader(this, "转装上传");
	}

	private void init() {
		CheckBox chkAll = (CheckBox) findViewById(R.id.chkSelAll);
		chkAll.setChecked(false);
		// 初始化下拉列表
		ListView lstReps = (ListView) findViewById(R.id.lstReps);
		RelationCodesDB db = new RelationCodesDB(this);
		TrackAndRevAdapter adptRev = new TrackAndRevAdapter(this,
				db.getAllRelationCodes(), chkAll);

		lstReps.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lstReps.setAdapter(adptRev);

		// 新建
		findViewById(R.id.btnNew).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TrackNoMgntActivity.this,
						ProductWrapActivity.class);
				startActivity(intent);
				// finish();
			}
		});

		// 上传
		findViewById(R.id.btnUpload).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ListView lstReps = (ListView) findViewById(R.id.lstReps);
				TrackAndRevAdapter adpt = (TrackAndRevAdapter) lstReps
						.getAdapter();
				List<RelationCodes> delData = adpt.getCheckedData();

				if (delData.size() == 0) {
					Toaster.show(getResources().getString(
							R.string.warnning_sel_data));
					return;
				}

				// 调用接口
				uploadCode(delData);

			}
		});

		// 删除
		findViewById(R.id.btnDel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteData();
			}
		});

		// 设置返回按钮事件
		Button btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TrackNoMgntActivity.this.finish();
			}
		});

		// 修改
		findViewById(R.id.btnModify).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ListView lstReps = (ListView) findViewById(R.id.lstReps);
				TrackAndRevAdapter adpt = (TrackAndRevAdapter) lstReps
						.getAdapter();
				List<RelationCodes> chkList = adpt.getCheckedData();
				if (chkList.size() == 0) {
					Toast.makeText(
							getBaseContext(),
							getResources().getString(
									R.string.warnning_need_sel_data),
							Toast.LENGTH_LONG).show();
					return;
				}

				Intent intent = new Intent(TrackNoMgntActivity.this,
						ProductWrapActivity.class);
				intent.putExtra("chargeCode", chkList.get(0).getSqCode());
				startActivity(intent);

				// RelationCodeDialog dlg = new
				// RelationCodeDialog(chkList.get(0));
				// dlg.show(getFragmentManager(), "PROMPT_DIALOG_TAG");
			}
		});

	}

	@Override
	protected void onStart() {
		super.onStart();

		init();
	}

	private void uploadCode(List<RelationCodes> delData) {
		List<JsonRest> jsonDatas = new ArrayList<JsonRest>();
		// Map<String,List<RelationCodes>> splitMap = new
		// HashMap<String,List<RelationCodes>>();
		for (RelationCodes item : delData) {
			jsonDatas.add(new RelationCodesRequest(item));
		}

		Iterator<JsonRest> ite = jsonDatas.iterator();
		while (ite.hasNext()) {
			final RelationCodesRequest model = (RelationCodesRequest) ite
					.next();
			// jsonDatas.add(new RelationCodesRequest(ite.next()));
			RpcAsyncTask task = new RpcAsyncTask(this, model,
					new OnCompleteListener() {

						@Override
						public void onComplete(String content) {
							try {
								boolean fail = true;
								JSONObject ret = null;
								if (content != null) {
									ret = new JSONObject(content);
									fail = ret.getBoolean("IsError");

								}

								if (content == null || fail) {
									Toaster.show(ret.getString("Message"));
								} else {
									deleteData(model.getId());
									Toaster.show("上传成功!");
								}
							} catch (JSONException e) {
								Toaster.show(e.getMessage());
							}

						}
					});
			TaskUtils.execute(task, "post", "/rest/UploadRelation");
		}
	}

	private void deleteData() {
		ListView lstReps = (ListView) findViewById(R.id.lstReps);
		final TrackAndRevAdapter adpt = (TrackAndRevAdapter) lstReps
				.getAdapter();
		final List<RelationCodes> delData = adpt.getCheckedData();

		if (delData.size() == 0) {
			Toaster.show(getResources().getString(R.string.warnning_sel_data));
			return;
		}

		Dialog detailDialog = new AlertDialog.Builder(this)
				.setTitle("确认删除？")
				.setPositiveButton(R.string.btn_ok_tip,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// 删除数据库中的数据
								RelationCodesDB db = new RelationCodesDB(
										TrackNoMgntActivity.this);
								db.deleteRelationCodes(delData);

								// 刷新界面
								adpt.RemoveItems(delData);

								Toaster.show("删除成功!");
							}

						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();

							}
						}).create();
		detailDialog.show();

	}

	private void deleteData(String id) {
		ListView lstReps = (ListView) findViewById(R.id.lstReps);
		TrackAndRevAdapter adpt = (TrackAndRevAdapter) lstReps.getAdapter();
		int count = adpt.getCount();
		RelationCodes item = null;
		for (int i = 0; i < count; i++) {
			item = (RelationCodes) adpt.getItem(i);
			if (item.getId().equals(id))
				break;
			item = null;
		}
		if (item == null)
			return;

		// 删除数据库中的数据
		List<RelationCodes> tmpList = new ArrayList<RelationCodes>();
		tmpList.add(item);
		RelationCodesDB db = new RelationCodesDB(this);
		db.deleteRelationCodes(tmpList);

		// 刷新界面
		adpt.RemoveItem(item);

	}

	// @Override
	// public void doModify(RelationCodes data) {
	// // 修改并更新数据
	// RelationCodesDB db = new RelationCodesDB(this);
	// db.updateRelationCodes(data);
	//
	// ListView lstReps = (ListView) findViewById(R.id.lstReps);
	// TrackAndRevAdapter adpt = (TrackAndRevAdapter) lstReps.getAdapter();
	// adpt.updateItem(data);
	//
	// }
}

class TrackAndRevAdapter extends BaseAdapter {
	private Context ctx;
	private List<Boolean> mChecked;
	private CheckBox chkAll;
	private List<RelationCodes> datas = new ArrayList<RelationCodes>();

	public TrackAndRevAdapter(Context ctx, List<RelationCodes> lst,
			CheckBox chkAll) {
		this.ctx = ctx;
		if (lst != null)
			datas = lst;
		mChecked = new ArrayList<Boolean>();
		for (int i = 0; i < datas.size(); i++)
			mChecked.add(false);
		this.chkAll = chkAll;
		this.chkAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (int i = 0; i < mChecked.size(); i++)
					mChecked.set(i, TrackAndRevAdapter.this.chkAll.isChecked());
				notifyDataSetChanged();
			}

		});
	}

	public List<RelationCodes> getCheckedData() {
		List<RelationCodes> lst = new ArrayList<RelationCodes>();
		for (int i = 0; i < mChecked.size(); i++) {
			if (mChecked.get(i))
				lst.add(datas.get(i));
		}

		return lst;
	}

	public void RemoveItems(List<RelationCodes> dataList) {
		// 删除datas对应的数据,
		for (int i = 0; i < dataList.size(); i++) {
			datas.remove(dataList.get(i));
		}

		// 重建 mChecked
		mChecked = new ArrayList<Boolean>();
		for (int i = 0; i < datas.size(); i++) {
			mChecked.add(false);
		}

		chkAll.setChecked(false);
		notifyDataSetChanged();
	}

	public void RemoveItem(RelationCodes item) {
		// 删除datas对应的数据,
		int index = -1;
		for (int i = 0; i < datas.size(); i++) {
			if (datas.get(i).getId().equals(item.getId())) {
				index = i;
				break;
			}
		}
		if (index == -1)
			return;

		datas.remove(index);
		mChecked.remove(index);

		boolean all = true;
		if (mChecked.size() == 0) {
			all = false;
		} else {
			for (int i = 0; i < mChecked.size(); i++) {
				if (!mChecked.get(i)) {
					all = false;
					break;
				}
			}
		}

		chkAll.setChecked(all);
		notifyDataSetChanged();
	}

	public void updateItem(RelationCodes data) {
		// 更新datas对应的数据,
		for (int i = 0; i < datas.size(); i++) {
			if (datas.get(i).getId().equals(data.getId())) {
				datas.set(i, data);
			}
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {

		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(ctx).inflate(
					R.layout.track_rev_list_item, null);
			ItemViewCache item = new ItemViewCache();
			final int p = position;
			item.checkBox = (CheckBox) convertView.findViewById(R.id.chk);
			item.checkBox.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					CheckBox cb = (CheckBox) v;
					mChecked.set(p, cb.isChecked());
					if (chkAll.isChecked()) {
						chkAll.setChecked(cb.isChecked());
					} else {
						for (int i = 0; i < mChecked.size(); i++) {
							if (!mChecked.get(i))
								return;
						}
						chkAll.setChecked(true);
					}
				}
			});
			item.revno = (TextView) convertView.findViewById(R.id.revno);
			item.trackno = (TextView) convertView.findViewById(R.id.trackno);
			convertView.setTag(item);
		}
		ItemViewCache cache = (ItemViewCache) convertView.getTag();

		cache.checkBox.setChecked(mChecked.get(position));
		cache.revno.setText(datas.get(position).getSqCode());
		cache.trackno.setText(datas.get(position).getBoxCode());

		return convertView;
	}

	private static class ItemViewCache {
		public CheckBox checkBox;
		public TextView revno;
		public TextView trackno;
	}

}