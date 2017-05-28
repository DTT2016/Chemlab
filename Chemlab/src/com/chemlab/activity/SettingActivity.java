package com.chemlab.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.chemlab.R;
import com.chemlab.about.AboutApp;
import com.chemlab.util.MyApplication;

public class SettingActivity extends BaseActivity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting);

		((TextView) findViewById(R.id.textview_title)).setText("设置");
		findViewById(R.id.button_back).setOnClickListener(this);
		findViewById(R.id.reset_pass).setOnClickListener(this);
		findViewById(R.id.about_app).setOnClickListener(this);
		findViewById(R.id.quit_login).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.reset_pass:
			break;
		case R.id.about_app:
			startAction(AboutApp.class);
			break;
		case R.id.quit_login:
			MyApplication.saveString("user_id", "");
			MyApplication.saveString("user_password", "");
			startAction(LoginActivity.class);
			break;
		case R.id.button_back:
			finish();
			break;
		default:
			break;
		}
	}
	
	private void startAction(Class<?> clazz) {
		Intent intent = new Intent(SettingActivity.this, clazz);
		startActivity(intent);
	}

}
