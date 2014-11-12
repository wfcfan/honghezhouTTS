package com.acctrue.tts.sync;

import android.content.Context;

import com.acctrue.tts.GlobalApplication;
import com.acctrue.tts.R;
import com.acctrue.tts.views.ProgressView;

public abstract class SyncServiceBase implements SyncService {

	protected Context _ctx;

	public SyncServiceBase() {

		_ctx = GlobalApplication.mApp;

	}

	@Override
	public boolean downloadData() {
		ProgressView pv = new ProgressView(_ctx, R.string.loding_tip);
		pv.show();
		boolean p = this.process();
		pv.hide();
		return p;
	}

	protected abstract boolean process();

}
