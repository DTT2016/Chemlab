package com.chemlab.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chemlab.R;
import com.chemlab.adapter.SignInfoAdapter;
import com.chemlab.managers.JsonManager;
import com.chemlab.objs.WorkInfo;
import com.chemlab.util.HttpCallbackListener;
import com.chemlab.util.HttpUtil;
import com.chemlab.util.LogUtil;

@SuppressLint("SimpleDateFormat")
public class SignResultActivity extends Activity {

	private static SignInfoAdapter adapter;
	private static List<WorkInfo> signList;
	private ListView signListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sign_result);

		((TextView) findViewById(R.id.textview_title)).setText("签到记录");
		findViewById(R.id.button_back).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		
		// 获取记录
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		String date_to = format.format(calendar.getTime());
		calendar.add(Calendar.DATE, -31);
		String date_from = format.format(calendar.getTime());

		HttpUtil.sendHttpRequest(
				HttpUtil.ADDRESS_STUDENT_HANDLER,
				HttpUtil.createJsonStr("GetSignInfoByID", "\"from\":\""
						+ date_from + "\",\"to\":\"" + date_to
						+ "\",\"find_id\":\"" + HttpUtil.ID + "\","),
				new HttpCallbackListener() {

					@Override
					public void onFinish(String response) {
						LogUtil.d("Tag", response);
						JSONObject responseObject;
						try {
							responseObject = new JSONObject(response);

							if (responseObject.getString("error").equals("0")) {
								JSONArray jsonObjArray = responseObject.getJSONArray("data");
								signList = JsonManager.getWorkInfoInfoArray(jsonObjArray,JsonManager.TYPE_SIGN);

								if (signList != null && !signList.isEmpty()) {
									runOnUiThread(new Runnable() {

										@Override
										public void run() {
											adapter = new SignInfoAdapter(
													SignResultActivity.this,
													R.layout.item_two_col,
													signList);
											signListView = (ListView) findViewById(R.id.list_sign_result);
											signListView.setAdapter(adapter);
											adapter.notifyDataSetChanged();
										}
									});
								} else {
									runOnUiThread(new Runnable() {

										@Override
										public void run() {
											Toast.makeText(SignResultActivity.this, "获取签到信息失败", Toast.LENGTH_LONG).show();
										}
									});
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onError(Exception e) {
						e.printStackTrace();
					}

				});

	}
}
