package com.biyanzhi.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.biyanzhi.applation.MyApplation;
import com.biyanzhi.utils.Utils;

public class BaseActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		MyApplation.addActivity(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finishThisActivity();
		}
		return super.onKeyDown(keyCode, event);
	}

	protected void finishThisActivity() {
		finish();
		Utils.rightOut(this);
	}

	@Override
	public void onClick(View v) {

	}
}
