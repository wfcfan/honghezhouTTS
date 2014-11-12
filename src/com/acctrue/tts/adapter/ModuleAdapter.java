package com.acctrue.tts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.acctrue.tts.R;

public class ModuleAdapter extends BaseAdapter {

	private Context _context;
	
	public ModuleAdapter(Context context){
		this._context = context;
	}
	
//	private final static String[] moduleTxts = { 
//		"数据下载",
//		"农产品收取",
//		"收取码管理",
//		"收取入库",
//		"入库管理",
//		"农产品转装",
//		"追溯码管理",
//		"电商出库",
//		"直接出库" 
//	};
	
	private final static int[] moduleImgs = {
		R.drawable.btn_nongchanpinshouqu,
		R.drawable.btn_shouqumaguanli,
		R.drawable.btn_shouquruku,
		R.drawable.btn_rukuguanli,
		R.drawable.btn_nongchanpinzhuanzhuang,
		R.drawable.btn_zhuisumaguanli,
		R.drawable.btn_dianshangchuku,
		R.drawable.btn_zhijiechuku,
		R.drawable.btn_danjuguanli,
		R.drawable.btn_shujuxiazai,
	};
	
//	private static final SparseArray<Integer> moduleArray;
//	static {
//		moduleArray = new SparseArray<Integer>();
//		
//		moduleArray.put(Constants.DATA_DOWNLOAD, R.drawable.btn_shujuxiazai);
//		moduleArray.put(Constants.PRODUCT_RECEIVE, R.drawable.btn_nongchanpinshouqu);
//		moduleArray.put(Constants.RECEIVE_NO_MGNT, R.drawable.btn_shouqumaguanli);
//		moduleArray.put(Constants.RECEIVE_TO_REPOS, R.drawable.btn_shouquruku);
//		moduleArray.put(Constants.SAVE_REPOS_MGNT, R.drawable.btn_rukuguanli);
//		moduleArray.put(Constants.PRODUCT_WRAP, R.drawable.btn_nongchanpinzhuanzhuang);
//		moduleArray.put(Constants.TRACK_NO_MGNT, R.drawable.btn_zhuisumaguanli);
//		moduleArray.put(Constants.E_BUSINESS_OUT, R.drawable.btn_dianshangchuku);
//		moduleArray.put(Constants.DIRECT_OUT_STORE, R.drawable.btn_zhijiechuku);
//		
//	}

	@Override
	public int getCount() {
		return moduleImgs.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewgroup) {
		GridTemp gt;
		if(view == null){
			gt = new GridTemp();
			LayoutInflater inflater = LayoutInflater.from(_context);
			view = inflater.inflate(R.layout.girdview_item, null);
			view.setTag(gt);
			
		}else{
			gt = (GridTemp)view.getTag();
		}
		
		gt.imageView = (ImageView)view.findViewById(R.id.module_img);
		gt.imageView.setImageResource(moduleImgs[position]);
		
//		gt.textView = (TextView)view.findViewById(R.id.module_title);
//		gt.textView.setText(moduleTxts[position]);
		
		return view;
	}
	
	public class GridTemp{
		ImageView imageView;  
		TextView textView; 
	}

}
