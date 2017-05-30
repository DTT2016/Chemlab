package com.chemlab.systems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.chemlab.R;
import com.chemlab.util.HttpCallbackListener;
import com.chemlab.util.HttpUtil;
import com.chemlab.util.LogUtil;
import com.chemlab.view.InfoDispView;

public class EquipmentLocActivity extends Activity {
	
	/*
	 * equip_name 仪器名
	 * fixed_assets_NO  固定资产号（唯一）
	 * factory_NO  公司编号
	 * time_buying 购买时间
	 * people 操作人
	 * position 位置 
	 * state 状态（在用 闲置 损坏 报废）
	 * state_explane 状态说明
	 * edit_time 编辑时间
	 */
	
	private InfoDispView locPPE;
	private InfoDispView locFactoryNO;
	private InfoDispView locBuyingTime;
	private InfoDispView locOperator;
	private InfoDispView locPosition;
	private InfoDispView locState;
	private InfoDispView locStateExplane;
	private InfoDispView locEditTime;

	private String equipString;

	public static void actionStart(Context context, String equipment) {
		Intent intent = new Intent(context, EquipmentLocActivity.class);
		intent.putExtra("equipment", equipment);
		context.startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.system_equipment_loc);
		
		equipString = getIntent().getStringExtra("equipment");
		
		((TextView) findViewById(R.id.textview_title)).setText(equipString);
		findViewById(R.id.button_back).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		
		initLocInfoViews();
		updateLocInfo();
	}

	private void initLocInfoViews() {
		
		locPPE = (InfoDispView) findViewById(R.id.loc_equip_ppe);
		locBuyingTime = (InfoDispView) findViewById(R.id.loc_equip_buytime);
		locFactoryNO = (InfoDispView) findViewById(R.id.loc_equip_fac);
		locPosition = (InfoDispView) findViewById(R.id.loc_equip_pos);
		locState = (InfoDispView) findViewById(R.id.loc_equip_state);
		locStateExplane = (InfoDispView) findViewById(R.id.loc_equip_explane);
		locOperator = (InfoDispView) findViewById(R.id.loc_equip_op);
		locEditTime = (InfoDispView) findViewById(R.id.loc_equip_edittime);

		locPPE.setTitleText("固定资产号");
		locFactoryNO.setTitleText("公司编号");
		locBuyingTime.setTitleText("购买时间");
		locPosition.setTitleText("位置");
		locState.setTitleText("状态");
		locStateExplane.setTitleText("状态说明");
		locOperator.setTitleText("操作人");
		locEditTime.setTitleText("编辑时间");
	}
	
	private void updateLocInfo() {
		String httpLink = HttpUtil.ADDRESS_EQUIPMENT_HANDLER;
		String argvs = HttpUtil.createJsonStr("GetEquipLocDetail", "\"equip_name\":\""
				+ equipString + "\",");

		HttpUtil.sendHttpRequest(httpLink, argvs, new HttpCallbackListener() {

			@Override
			public void onFinish(String response) {
				LogUtil.d("Tag", response);
				JSONObject responseObject;
				try {
					responseObject = new JSONObject(response);

					if (responseObject.getString("error").equals("0")) {
						JSONArray jsonObjArray = responseObject
								.getJSONArray("data");
						/*
						 * equip_name 仪器名
						 * fixed_assets_NO  固定资产号（唯一）
						 * factory_NO  公司编号
						 * time_buying 购买时间
						 * people 操作人
						 * position 位置 
						 * state 状态（在用 闲置 损坏 报废）
						 * state_explane 状态说明
						 * edit_time 编辑时间
						 */
						if (jsonObjArray.length() > 0) {
							final JSONObject locInfo = (JSONObject) jsonObjArray
									.get(0);
							//LogUtil.d(locInfo.toString());
							runOnUiThread(new Runnable() {
								public void run() {
									try {
										locPPE.setContentText(locInfo.getString("fixed_assets_NO"));
										locBuyingTime.setContentText(locInfo.getString("time_buying"));
										locFactoryNO.setContentText(locInfo.getString("factory_NO"));
										locPosition.setContentText(locInfo.getString("position"));
										locState.setContentText(locInfo.getString("state"));
										locStateExplane.setContentText(locInfo.getString("state_explane"));
										locOperator.setContentText(locInfo.getString("people"));
										locEditTime.setContentText(locInfo.getString("edit_time"));
									} catch (JSONException e) {
										e.printStackTrace();
									}
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
