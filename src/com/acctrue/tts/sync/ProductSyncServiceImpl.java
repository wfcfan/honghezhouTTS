package com.acctrue.tts.sync;

import java.util.List;

import com.acctrue.tts.db.ProductDB;
import com.acctrue.tts.model.Product;
import com.acctrue.tts.testdata.TestAPI;

public class ProductSyncServiceImpl extends SyncServiceBase implements SyncService {
	

	@Override
	protected boolean process() {
		List<Product> list = TestAPI.getTestProducts();
		ProductDB db = new ProductDB(this._ctx);
		for(Product p : list){
			db.deleteProduct(Integer.valueOf(p.getProductId()));
			db.addProduct(p);
		}
		return true;
	}

}
