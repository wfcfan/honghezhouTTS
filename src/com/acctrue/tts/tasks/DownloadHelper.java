package com.acctrue.tts.tasks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.acctrue.tts.Constants;
import com.acctrue.tts.R;
import com.acctrue.tts.db.BizCorpsDB;
import com.acctrue.tts.db.FarmLandsDB;
import com.acctrue.tts.db.ProductDB;
import com.acctrue.tts.db.StoreDB;
import com.acctrue.tts.db.StoreTypeDB;
import com.acctrue.tts.db.WarehouseDB;
import com.acctrue.tts.dto.DownloadStoresRequest;
import com.acctrue.tts.dto.DownloadStoresResponse;
import com.acctrue.tts.dto.GetBizCorpsPageRequest;
import com.acctrue.tts.dto.GetBizCorpsPageResponse;
import com.acctrue.tts.dto.GetFarmLandsRequest;
import com.acctrue.tts.dto.GetFarmLandsResponse;
import com.acctrue.tts.dto.GetProductByPageRequest;
import com.acctrue.tts.dto.GetProductByPageResponse;
import com.acctrue.tts.dto.GetWarehouseByPageRequest;
import com.acctrue.tts.dto.GetWarehouseByPageResponse;
import com.acctrue.tts.dto.LoginResponse;
import com.acctrue.tts.dto.Sign;
import com.acctrue.tts.model.BizCorp;
import com.acctrue.tts.model.FarmLands;
import com.acctrue.tts.model.Product;
import com.acctrue.tts.model.Store;
import com.acctrue.tts.model.StoreTypes;
import com.acctrue.tts.model.Warehouse;
import com.acctrue.tts.rpc.OnCompleteListener;
import com.acctrue.tts.rpc.RpcAsyncTask;
import com.acctrue.tts.utils.AccountUtil;
import com.acctrue.tts.utils.Toaster;

public final class DownloadHelper {
	private static final int PAGE_SIZE = 10000;
	private static final int PAGE_INDEX = 0;

	private final Context ctx;
	private final Sign sign;
	private final Map<String, RpcAsyncTask> _map;
	private final Map<String,RpcAsyncTask> _map2;
	
	public DownloadHelper(Context ctx) {
		this.ctx = ctx;
		this.sign = AccountUtil.getDefaultSign();
		_map = new HashMap<String, RpcAsyncTask>();
		_map2 = new HashMap<String, RpcAsyncTask>();
		init();
	}

	public static final String TASK_FARMLANDS = "FarmLands";
	public static final String TASK_PRODUCT = "Product";
	public static final String TASK_BIZCORP = "BizCorp";
	public static final String TAKS_WAREHOUSE = "Warehouse";
	public static final String TASK_STORE = "Stores";
	public static final String TASK_STORETYPES = "StoreTypes";

	private void init() {
		_map.put(TASK_FARMLANDS, getFarmLandsTask());
		_map.put(TASK_PRODUCT, getProductTask());
		_map.put(TASK_BIZCORP, getBizCorpTask());
		//_map.put(TAKS_WAREHOUSE, getWarehouseTask());
		_map.put(TASK_STORE, getStoreTask());
		_map.put(TASK_STORETYPES, getStoreTypesTask());
		
		//==============一键下载列表
		_map2.put(Constants.URL_GETFARMLANDS, getFarmLandsTask());
		_map2.put(Constants.URL_GETPRODUCTBYPAGE, getProductTask());
		_map2.put(Constants.URL_GETBIZCORPSPAGE, getBizCorpTask());
		//_map2.put(Constants.URL_GETWAREHOUSEBYPAGE, getWarehouseTask());
		_map2.put(Constants.URL_GETSTORETYPES, getStoreTypesTask());
		_map2.put(Constants.URL_DOWNLOADSTORES,getStoreTask());
	}

	public RpcAsyncTask getTask(String taskName) {
		return _map.get(taskName);
	}
	
	public Map<String,RpcAsyncTask> getAllTasks(){
		return this._map2;
	}

	private RpcAsyncTask getFarmLandsTask() {
		GetFarmLandsRequest getFarmLands = new GetFarmLandsRequest();
		getFarmLands.setSign(sign);
		return new RpcAsyncTask(ctx, getFarmLands, new OnCompleteListener() {
			@Override
			public void onComplete(String content) {
				GetFarmLandsResponse gf;
				try {
					gf = GetFarmLandsResponse.formJson(new JSONObject(content));
				} catch (JSONException e) {
					e.printStackTrace();
					gf = null;
				}

				if (gf != null) {
					if (gf.isError()) {
						Toaster.show(gf.getMessage());
						return;
					}

					FarmLandsDB db = new FarmLandsDB(ctx);
					for (FarmLands f : gf.getFarmLandList()) {
						db.deleteFarmLands(f.getFarmLandCode());
						db.addFarmLands(f);
					}
					Toaster.show("农户农田数据下载成功!");
				}
			}
		});
	}

	private RpcAsyncTask getProductTask() {
		GetProductByPageRequest page = new GetProductByPageRequest();
		page.setSign(AccountUtil.getDefaultSign());
		page.setPageIndex(PAGE_INDEX);
		page.setPageSize(PAGE_SIZE);

		return new RpcAsyncTask(ctx, page, new OnCompleteListener() {

			@Override
			public void onComplete(String content) {
				GetProductByPageResponse gp = null;
				try {
					gp = GetProductByPageResponse.fromJson(new JSONObject(
							content));
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				if (gp != null) {
					if (gp.isError()) {
						Toaster.show(gp.getMessage());
						return;
					}

					ProductDB db = new ProductDB(ctx);
					db.deleteAll();
					for (Product prd : gp.getProducts()) {
						db.addProduct(prd);
					}
					Toaster.show("产品数据下载成功!");
				}
			}
		});
	}

	private RpcAsyncTask getBizCorpTask() {
		GetBizCorpsPageRequest page = new GetBizCorpsPageRequest();
		page.setSign(AccountUtil.getDefaultSign());
		page.setPageIndex(PAGE_INDEX);
		page.setPageSize(PAGE_SIZE);

		return new RpcAsyncTask(ctx, page, new OnCompleteListener() {

			@Override
			public void onComplete(String content) {
				GetBizCorpsPageResponse gb = null;
				try {
					gb = GetBizCorpsPageResponse.fromJson(new JSONObject(
							content));
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				if (gb != null) {
					if (gb.isError()) {
						Toaster.show(gb.getMessage());
						return;
					}
				}

				BizCorpsDB db = new BizCorpsDB(ctx);
				for (BizCorp corp : gb.getCorpList()) {
					db.deleteCorp(corp.getCorpId());
					db.addCorp(corp);
				}

				Toaster.show("往来企业数据下载成功!");
			}

		});
	}

	@SuppressWarnings("unused")
	private RpcAsyncTask getWarehouseTask() {
		GetWarehouseByPageRequest page = new GetWarehouseByPageRequest();
		page.setSign(AccountUtil.getDefaultSign());
		page.setPageIndex(PAGE_INDEX);
		page.setPageSize(PAGE_SIZE);

		return new RpcAsyncTask(ctx, page, new OnCompleteListener() {

			@Override
			public void onComplete(String content) {
				GetWarehouseByPageResponse gb = null;
				try {
					gb = GetWarehouseByPageResponse.fromJson(new JSONObject(
							content));
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				if (gb != null) {
					if (gb.isError()) {
						Toaster.show(gb.getMessage());
						return;
					}

					WarehouseDB db = new WarehouseDB(ctx);
					for (Warehouse w : gb.getWarehouses()) {
						db.deleteWarehouse(w.getCorpId());
						db.addWarehouse(w);
					}

					Toaster.show("仓库信息下载成功!");
				}

			}
		});
	}

	private RpcAsyncTask getStoreTask() {
		DownloadStoresRequest dsr = new DownloadStoresRequest();
		dsr.setSign(AccountUtil.getDefaultSign());
		LoginResponse u = AccountUtil.getCurrentUser();
		dsr.setSearch(u.getUserInfo().getCorpId(), u.getUserInfo().getUserId());

		return new RpcAsyncTask(ctx, dsr, new OnCompleteListener() {

			@Override
			public void onComplete(String content) {
				DownloadStoresResponse gr = null;
				try {
					gr = DownloadStoresResponse
							.fromJson(new JSONObject(content));
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				if (gr != null) {
					if (gr.isError()) {
						Toaster.show(gr.getMessage());
						return;
					}
				}
				
				//按所选择的下载
				List<Store> orderList = gr.getStores();
				if(orderList != null){
					final StoreDB db = new StoreDB(ctx);

					for (Store order : orderList) {
						db.delStore(order);
						db.addStoreBySelf(order);
					}
					Toaster.show(R.string.msg_download_success);
				}
				
				Toaster.show("计划任务下载成功!");
			}

		});
	}

	private RpcAsyncTask getStoreTypesTask() {

		return new RpcAsyncTask(ctx, null, new OnCompleteListener() {

			@Override
			public void onComplete(String content) {
				StoreTypes st = null;
				try{
					JSONArray arr = new JSONArray(content);
					StoreTypeDB db = new StoreTypeDB(ctx);
					db.delStoreTypes();
					
					for(int i=0;i<arr.length();i++){
						st = StoreTypes.fromJson(arr.getJSONObject(i));
						db.addStoreType(st);
					}
					
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}

		});
	}
}
