package com.chemlab.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chemlab.R;
import com.chemlab.util.HttpCallbackListener;
import com.chemlab.util.HttpUtil;
import com.chemlab.util.MyApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity{

	public static final int SUCCEESS = 1;
	public static final int FAIL = 2;
	
	private EditText nameText;
	private EditText passText;
	private Button loginButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		nameText = (EditText) findViewById(R.id.loginName);
		passText = (EditText) findViewById(R.id.loginPasswd);
		loginButton = (Button) findViewById(R.id.btLogin);
		
		loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final String name = nameText.getText().toString();
				final String pass = passText.getText().toString();
				
				if ("".equals(name)||"".equals(pass)) {
					Toast.makeText(LoginActivity.this, "输入不完整", 
		                     Toast.LENGTH_SHORT).show();
				}else {
					login(name,pass);
				}
				
			}
		});
	}
	
	public void login(final String id,final String pw){
		String jsonString="json={" +
				"\"type\":\"GetInfoByID\"," +
				"\"id\":\""+id+"\"," +
				"\"find_id\":\""+id+"\"," +
				"\"pw\":\""+pw+"\"}";
		
		HttpUtil.sendHttpRequest(HttpUtil.ADDRESS_LOGIN_HANDLER, jsonString, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				JSONObject responseObject;
				try {
					responseObject = new JSONObject(response);

					if (responseObject.getString("error").equals("0")) {
						JSONArray jsonObjArray = responseObject
								.getJSONArray("data");
						
						JSONObject meInfo = jsonObjArray.getJSONObject(0);
						MyApplication.ID = id;
						MyApplication.PW = pw;
						HttpUtil.ID = id;
						HttpUtil.PW = pw;
						
						MyApplication.saveString("user_id", id);
						MyApplication.saveString("user_password", pw);
						MyApplication.saveString("user_name", meInfo.getString("name"));
						MyApplication.saveString("user_idebtity", meInfo.getString("idebtity"));
						MyApplication.saveString("user_telephone", meInfo.getString("phonenumber_long"));
						MyApplication.saveString("user_phone_short", meInfo.getString("phonenumber_short"));
						MyApplication.saveString("user_qq_number", meInfo.getString("QQ"));
						MyApplication.saveString("user_email", meInfo.getString("email"));
						MyApplication.saveString("user_address", meInfo.getString("address"));
						MyApplication.saveString("user_create_time", meInfo.getString("creat_time"));
						MyApplication.saveString("user_last_time", meInfo.getString("last_time"));
						
						MyApplication.setMEInfo();
						
						runOnUiThread(new Runnable() {
							public void run() {
								Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			                    startActivity(intent);
			                    finish();
							}
						});
						
					}else {
						runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(LoginActivity.this, "登录失败", 
					                     Toast.LENGTH_SHORT).show();
							}
						});
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onError(Exception e) {
				
			}
		});
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ActivityCollector.finishAll();
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
