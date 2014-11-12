package com.acctrue.tts.views;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.acctrue.tts.R;
import com.acctrue.tts.utils.Util;

public class ProgressView extends View {
	public static ProgressView self;

	Context mContext;
	WindowManager mWM; // WindowManager

	View win;
	TextView textView;
	View progressView;
	Animation mAnim;

	public ProgressView(Context context, int text) {
		this(context, context.getString(text));
	}

	public ProgressView(Context context, String text) {
		super(context);
		mContext = context;

		mWM = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		win = LayoutInflater.from(mContext).inflate(R.layout.progress_view, null);
		progressView = win.findViewById(R.id.progressView);
//		textView = (TextView) win.findViewById(R.id.text);
//		textView.setText(text);

		mAnim = AnimationUtils.loadAnimation(mContext, R.anim.wave1);
		progressView.startAnimation(mAnim);

		EditText emptyEdit = (EditText) win.findViewById(R.id.emptyEdit);
		emptyEdit.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_BACK)) {
					hide();
				}
				return false;
			}
		});
	}

	public void hide() {
		try {
			if (null != progressView) {
				progressView.clearAnimation();
				mWM.removeView(win);
				self = null;
			}
		} catch (Exception e) {
		}
	}

	public void startAnimation() {
		if (null != mAnim && null != progressView)
			progressView.startAnimation(mAnim);
	}

	public void show() {
		try {
			hide();
			startAnimation();
			mWM.addView(win, getDefaultLayoutParams());
			self = this;
		} catch (Exception e) {
			Util.log(e);
		}
	}

	public WindowManager.LayoutParams getDefaultLayoutParams() {
		WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
		wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
		// wmParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		// wmParams.dimAmount = 0.0f;
		wmParams.format = PixelFormat.TRANSLUCENT;
		return wmParams;
	}
}
