package com.acctrue.tts.sync;

import java.util.List;

import com.acctrue.tts.db.BizCorpsDB;
import com.acctrue.tts.model.BizCorp;
import com.acctrue.tts.testdata.TestAPI;

public class BizCorpsSyncServiceImpl extends SyncServiceBase implements SyncService {

	@Override
	protected boolean process() {
		List<BizCorp> list =TestAPI.getBizCorps();
		
		BizCorpsDB db = new BizCorpsDB(this._ctx);
		for(BizCorp bc : list){
			db.deleteCorp(bc.getCorpId());
			db.addCorp(bc);
		}
		
		return true;
	}

}
