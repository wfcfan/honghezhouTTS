package com.acctrue.tts.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.acctrue.tts.Constants;
import com.acctrue.tts.R;
import com.acctrue.tts.db.RelationCodesDB;
import com.acctrue.tts.model.RelationCodes;
import com.acctrue.tts.utils.Toaster;
import com.acctrue.tts.utils.ViewUtil;

/**
 * 农产品转装
 * @author peng
 *
 */
public class ProductWrapActivity extends Activity {
	//TextView scanEdit;
	TextView revEdit;
	TextView trackEdit;
	TextView trackCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_product_wrap);
		ViewUtil.initHeader(this, "农产品转装");
		init();
	}

	private void init(){
		//scanEdit = (TextView)findViewById(R.id.scanEdit);
		revEdit = (TextView)findViewById(R.id.edtRevNo);
		trackEdit = (TextView)findViewById(R.id.edtTrackNo);
		trackCount = (TextView)findViewById(R.id.trackCount);
		
		//
		Intent intent = getIntent();
		if(intent != null && intent.hasExtra("chargeCode")){
			String chargeCode = intent.getExtras().getString("chargeCode");
			RelationCodesDB db = new RelationCodesDB(ProductWrapActivity.this);
			RelationCodes model = db.getRelationCodes(chargeCode);
			revEdit.setText(model.getSqCode());
			String tracks = model.getBoxCode();
			trackEdit.setText(tracks);
			if(tracks == null || tracks.length() == 0){
				trackCount.setText("0");
			}else{
				trackCount.setText("" + tracks.split(",").length);
			}
		}	
			
		
		Button btnScan = (Button)findViewById(R.id.btnScan);
		btnScan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = new Intent();
				intent.setClass(ProductWrapActivity.this, CaptureActivity.class);
				startActivityForResult(intent,Constants.REQCODE_SCANNIN_GREQUEST_CODE);
			}
			
		});
		
		//确定，建立关联关系 
		findViewById(R.id.btnNew)
			.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//获取收取码和追溯码
				String revNo = revEdit.getText().toString().trim();
				String trackNo = trackEdit.getText().toString().trim();
				if(revNo.length() == 0 || trackNo.length() == 0){
					Toast.makeText(getBaseContext(), 
							getResources().getString(R.string.warnning_need_code), 
							Toast.LENGTH_LONG).show();
					return;
				}
				//访问数据库保存关系
				RelationCodesDB db = new RelationCodesDB(ProductWrapActivity.this);
				//不需要判断收取码是否已经存在
				//RelationCodes model = db.getRelationCodes(revNo);
				db.addRelationCodes(revNo, trackNo);
				
				Toaster.show("保存成功");

				revEdit.setText("");
				trackEdit.setText("");
			}
		});
		
		
		//返回
		findViewById(R.id.btnBack)
			.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ProductWrapActivity.this.finish();
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (resultCode != Activity.RESULT_OK) {
				return;
			}
			if (requestCode != Constants.REQCODE_SCANNIN_GREQUEST_CODE) {
				return;
			}
			String scanResult = data.getStringExtra(Constants.EXTRA_RESULT);
			//EditText et1 = (EditText)this.findViewById(R.id.scanEdit);
			//et1.setText(scanResult);
			doScan(scanResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void doScan(String scanno){
		
		//如果收取码为空，就添加到收取码中
		//如果收取码不为空，就添加到追溯码
		//trackCount
		if(revEdit.getText().length() == 0){
			revEdit.setText(scanno);
		}else{
			trackEdit.setEnabled(true);
			String oldStr = trackEdit.getText().toString().trim();
			int count = 0;
			if(oldStr.length() == 0){
				oldStr = scanno;
				count = 1;
			}else{
				oldStr += "," + scanno;
				count = oldStr.split(",").length;
			}
			
			trackCount.setText("" + count);
			trackEdit.setText(oldStr);
		}
		findViewById(R.id.btnNew).setFocusable(true);
	}
}
