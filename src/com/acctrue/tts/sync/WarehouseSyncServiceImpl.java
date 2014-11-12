package com.acctrue.tts.sync;

import java.util.List;

import com.acctrue.tts.db.WarehouseDB;
import com.acctrue.tts.model.Warehouse;
import com.acctrue.tts.testdata.TestAPI;

public class WarehouseSyncServiceImpl extends SyncServiceBase implements
		SyncService {

	@Override
	protected boolean process() {
		List<Warehouse> list = TestAPI.getWarehouseList();
		WarehouseDB db = new WarehouseDB(this._ctx);
		for (Warehouse w : list) {
			db.deleteWarehouse(w.getWarehouseId());
			db.addWarehouse(w);
		}
		return true;
	}

}
