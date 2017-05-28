package com.chemlab.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.chemlab.R;
import com.chemlab.util.HttpCallbackListener;
import com.chemlab.util.HttpUtil;
import com.chemlab.util.LogUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class SignInActivity extends Activity {

	private TextView tvDate;

	private LocationManager locationManager;
	private String provider;
	private Location location;
	private String state;

	private BootstrapButton signInButton;
	private BootstrapButton signOffButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sign);

		((TextView) findViewById(R.id.textview_title)).setText("签到/签退");
		findViewById(R.id.button_back).setOnClickListener(listener);
		tvDate = (TextView) findViewById(R.id.sign_date);

		long time = System.currentTimeMillis();
		Date date = new Date(time);
		SimpleDateFormat format = new SimpleDateFormat(
				"yyyy年MM月dd日 EEEE HH:mm ");
		// format.format(date)); "2017年5月1日  星期一 08:00"
		tvDate.setText(format.format(date));

		signInButton = (BootstrapButton) findViewById(R.id.sign_in);
		signOffButton = (BootstrapButton) findViewById(R.id.sign_off);
		signInButton.setOnClickListener(listener);
		signOffButton.setOnClickListener(listener);
		findViewById(R.id.sign_history).setOnClickListener(listener);

		// 地理位置
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		List<String> providerList = locationManager.getProviders(true);

		if (providerList.contains(LocationManager.GPS_PROVIDER)) {
			provider = LocationManager.GPS_PROVIDER;
		} else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
			provider = LocationManager.NETWORK_PROVIDER;
		} else {
			Toast.makeText(this, "请打开位置服务", Toast.LENGTH_SHORT).show();
			return;
		}

		location = locationManager.getLastKnownLocation(provider);
		LogUtil.d("Tag", location.getLatitude() + "," + location.getLongitude());

		locationManager.requestLocationUpdates(provider, 5000, 1,
				locationListener);

		String argvs = HttpUtil.createJsonStr("GetState", "\"find_id\":\""
				+ HttpUtil.ID + "\",");
		HttpUtil.sendHttpRequest(HttpUtil.ADDRESS_STUDENT_HANDLER, argvs,
				new HttpCallbackListener() {

					@Override
					public void onFinish(String response) {
						LogUtil.d("Tag", response);
						JSONObject responseObject;
						try {
							responseObject = new JSONObject(response);
							if (responseObject.getString("error").equals("0")) {
								JSONArray jsonObjArray = responseObject
										.getJSONArray("data");
								state = ((JSONObject) jsonObjArray.get(0))
										.getString("state");
								// state = responseObject.getString("state");
								if ("已签到".equals(state)) {
									signOffButton.setEnabled(true);
									signInButton.setEnabled(false);
								} else {
									signInButton.setEnabled(true);
									signOffButton.setEnabled(false);
								}
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onError(Exception e) {

					}
				});
	}

	// private static double CLAT = 23.059544;
	// private static double CLNG = 113.385452;
	// 实验室经纬度
	private static double CLAT = 22.3546724;
	private static double CLNG = 113.589442;
	private final double EARTH_RADIUS = 6378137.0;

	private double gps2m(double lat_a, double lng_a, double lat_b, double lng_b) {
		double radLat1 = (lat_a * Math.PI / 180.0);
		double radLat2 = (lat_b * Math.PI / 180.0);
		double a = radLat1 - radLat2;
		double b = (lng_a - lng_b) * Math.PI / 180.0;
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	private double getDistance(double lat, double lng) {
		double s = gps2m(CLAT, CLNG, lat, lng);
		LogUtil.d("Tag", s + "");
		return s;
	}

	private void SignInOff() {
		if (location == null) {
			Toast.makeText(SignInActivity.this, "获取不到位置信息", Toast.LENGTH_SHORT)
					.show();
		} else if (getDistance(location.getLatitude(), location.getLongitude()) <= 1000) {
			String argvs = HttpUtil.createJsonStr("ChangeState", "");

			HttpUtil.sendHttpRequest(HttpUtil.ADDRESS_STUDENT_HANDLER, argvs,
					new HttpCallbackListener() {
						@Override
						public void onFinish(String response) {
							LogUtil.d("Tag", response);
							JSONObject responseObject;
							try {
								responseObject = new JSONObject(response);
								if (responseObject.getString("error").equals(
										"0")) {
									runOnUiThread(new Runnable() {

										@Override
										public void run() {
											Toast.makeText(SignInActivity.this,
													"成功", Toast.LENGTH_SHORT)
													.show();
											if ("已签退".equals(state)) {
												signInButton.setEnabled(false);
												signOffButton.setEnabled(true);
											} else {
												signOffButton.setEnabled(false);
												// signInButton.setEnabled(false);
											}
										}
									});

								} else {
									runOnUiThread(new Runnable() {

										@Override
										public void run() {
											Toast.makeText(SignInActivity.this,
													"失败", Toast.LENGTH_SHORT)
													.show();
										}
									});
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						@Override
						public void onError(Exception e) {
							e.printStackTrace();
						}
					});
		} else {
			Toast.makeText(SignInActivity.this, "不在签到范围", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.sign_in:
				SignInOff();
				break;
			case R.id.sign_off:
				SignInOff();
				break;
			case R.id.sign_history:
				Intent intent= new Intent(SignInActivity.this,SignResultActivity.class);
				startActivity(intent);
				break;
			case R.id.button_back:
				finish();
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (locationManager != null) {
			locationManager.removeUpdates(locationListener);
		}
	}

	LocationListener locationListener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@Override
		public void onProviderEnabled(String provider) {

		}

		@Override
		public void onProviderDisabled(String provider) {

		}

		@Override
		public void onLocationChanged(Location location) {
			SignInActivity.this.location = location;
		}
	};
}
