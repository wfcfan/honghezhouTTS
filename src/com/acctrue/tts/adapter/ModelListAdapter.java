package com.acctrue.tts.adapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.acctrue.tts.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class ModelListAdapter<T> extends BaseAdapter{
	
	public static final int TYPE_TEXTVIEW = 0;
	public static final int TYPE_EDITTEXT = 1;
	public static final int TYPE_CHECKBOX = 2;

	private Context ctx;
	private List<Boolean> mChecked;
	private List<T> items = new ArrayList<T>();
	private int rowResId;
	private int chkResId;
	private String[] fieldNames;
	private int[] cellResIds;
	private int[] cellTypes;
	private List<Field> refFields;
	
	public ModelListAdapter(Context ctx, List<T> lst,int rowResId,String[] fields,int[] cellResIds,int[] cellTypes){
		this(ctx,R.id.selecter,lst,rowResId,fields,cellResIds,cellTypes);
	}
	
	public ModelListAdapter(Context ctx, int chkResId, List<T> lst,int rowResId,String[] fields,int[] cellResIds,int[] cellTypes){
		this.ctx = ctx;
		this.items = lst == null ? items : lst;
		this.rowResId = rowResId;
		this.fieldNames = fields;
		this.cellResIds = cellResIds;
		this.cellTypes = cellTypes;
		this.chkResId = chkResId;
		initCheckBoxList();
		initGenericField();
	}
	
	private void initCheckBoxList(){
		int size = items.size();
		if(mChecked == null){
			mChecked = new ArrayList<Boolean>();
		}
		
		for(int i=0;i<size;i++){
			mChecked.add(false);
		}
			
	}

	private void initGenericField(){
		if(refFields != null)
			return;
		
		if(items.size() > 0){
			Class<?> clazz = items.get(0).getClass();
			refFields = new ArrayList<Field>();
			for(int i=0;i<this.fieldNames.length;i++){
				try {
					refFields.add(clazz.getField(fieldNames[i]));
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = LayoutInflater.from(ctx).inflate(rowResId, null);
			//ItemViewCache item = new ItemViewCache();
			final int p = position;
			CheckBox checkBox = (CheckBox)convertView.findViewById(this.chkResId);
			checkBox.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					CheckBox cb = (CheckBox) v;
					mChecked.set(p, cb.isChecked());
				}
			});
			convertView.setTag(R.id.selecter, checkBox);
			for(int i=0;i<cellResIds.length;i++){
				convertView.setTag(cellResIds[i], convertView.findViewById(cellResIds[i]));
			}
		}
		((CheckBox)convertView.getTag(R.id.selecter)).setChecked(mChecked.get(position));
		for(int i=0;i<cellTypes.length;i++){
			Object value;
			try {
				value = refFields.get(i).get(items.get(i));
			} catch (Exception e) {
				continue;
			}
			switch(cellTypes[i]){
			case TYPE_TEXTVIEW:
				TextView txt = (TextView)convertView.getTag(cellResIds[i]);
				txt.setText(value.toString());
				break;
			case TYPE_EDITTEXT:
				EditText edit = (EditText)convertView.getTag(cellResIds[i]);
				edit.setText(value.toString());
				break;
			case TYPE_CHECKBOX:
				CheckBox chk = (CheckBox)convertView.getTag(cellResIds[i]);
				chk.setChecked((Boolean)value);
				break;
			}
		}
		
		return convertView;
	}

	public List<T> getAllData(){
		return this.items;
	}
	
	public void addItem(T data){
		items.add(data);
		mChecked.add(false);
		
		initGenericField();
		notifyDataSetChanged();
	}
	
	public void addDataList(List<T> dataList){
		items.addAll(dataList);
		for(int i=0;i<dataList.size();i++){
			mChecked.add(false);
		}
		
		initGenericField();
		
		notifyDataSetChanged();
	}
	
	public List<T> getCheckedData(){
		List<T> lst = new ArrayList<T>();
		for(int i=0;i<mChecked.size();i++){
			if(mChecked.get(i))
				lst.add(items.get(i));
		}
		
		return lst;
	}
	
	public void RemoveItems(List<T> dataList){
		//删除items对应的数据,
		for(int i=0;i<dataList.size();i++){
			items.remove(dataList.get(i));
		}
		
		//重建 mChecked
		mChecked = new ArrayList<Boolean>();
		for(int i=0;i<items.size();i++){
			mChecked.add(false);
		}
		notifyDataSetChanged();
	}
}
