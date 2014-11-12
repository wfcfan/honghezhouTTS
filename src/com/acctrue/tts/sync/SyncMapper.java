package com.acctrue.tts.sync;

import java.util.HashMap;
import java.util.Map;

public final class SyncMapper {
	public static final String DOWN_PRODUCTS = "Products";
	public static final String DOWN_WAREHOUSE = "Warehouse";
	public static final String DOWN_BIZCORPS = "BizCorps";
	private final static Map<String, SyncService> _map;

	static {
		_map = new HashMap<String, SyncService>();

		_map.put(DOWN_PRODUCTS, new ProductSyncServiceImpl());
		_map.put(DOWN_WAREHOUSE, new WarehouseSyncServiceImpl());
		_map.put(DOWN_BIZCORPS, new BizCorpsSyncServiceImpl());
	}

	private static SyncMapper single = null;

	public synchronized static SyncMapper getInstance() {
		if (single == null)
			single = new SyncMapper();
		return single;
	}

	private SyncMapper() {
	};

	public SyncService getSyncService(final String name) {
		return _map.get(name);
	}

	public boolean downloadData(String name) {
		SyncService ss = this.getSyncService(name);
		return ss.downloadData();
	}

	public boolean downloadProducts() {
		return this.downloadData(DOWN_PRODUCTS);
	}

	public boolean downloadWarehouse() {
		return this.downloadData(DOWN_WAREHOUSE);
	}

	public boolean downloadBizCorps() {
		return this.downloadData(DOWN_BIZCORPS);
	}

}
