package com.chemlab.systems;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.chemlab.R;
import com.chemlab.activity.SearchActivity;
import com.chemlab.adapter.ResultListAdapter;
import com.chemlab.managers.JsonManager;
import com.chemlab.objs.Drug;
import com.chemlab.objs.DrugMix;
import com.chemlab.objs.Equipment;
import com.chemlab.objs.Result;
import com.chemlab.util.HttpCallbackListener;
import com.chemlab.util.HttpUtil;

public class ResultListActivity extends Activity {

	private List<Result> resultList;
	private ResultListAdapter adapter;

	private ListView resuListView;

	private int showType = 0;
	private String titleString;
	private String httpLink;
	private String argvs;
	List<?> list;

	public static void actionStart(Context context, int type) {
		Intent intent = new Intent(context, ResultListActivity.class);
		intent.putExtra("type", type);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.system_list_result);

		showType = getIntent().getIntExtra("type", 0);

		initGlobalVaribles(showType);

		((TextView) findViewById(R.id.textview_title)).setText(titleString);

		findViewById(R.id.button_back).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});

		findViewById(R.id.searchbar).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SearchActivity.actionStart(ResultListActivity.this, showType);
			}
		});

		resultList = new ArrayList<Result>();
		adapter = new ResultListAdapter(ResultListActivity.this,
				R.layout.item_result_list, resultList);
		resuListView = (ListView) findViewById(R.id.result_list);
		resuListView.setAdapter(adapter);

		resuListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos,
					long id) {
				gotoDetailActivity(pos);
			}

		});

		updateResult();
	}

	private void initGlobalVaribles(int type) {
		switch (type) {
		case SearchActivity.SEARCH_DRUG:
			titleString = "药品信息管理";
			httpLink = HttpUtil.ADDRESS_DRUG_HANDLER;
			argvs = HttpUtil.createJsonStr("GetDrug", 
					"\"drug\":\"\","); 
			//list = new ArrayList<Drug>();
			break;
		case SearchActivity.SEARCH_MIX:
			titleString = "试剂信息管理";
			httpLink = HttpUtil.ADDRESS_DRUG_HANDLER;
			argvs = HttpUtil.createJsonStr("GetDrugMix", 
					"\"mix\":\"\",");
			//list = new ArrayList<Drug>();
			break;
		case SearchActivity.SEARCH_LAB:
			titleString = "实验信息管理";
			httpLink = HttpUtil.ADDRESS_EXPERIMENT_HANDLER;
			argvs = "";
			//list = new ArrayList<Drug>();
			break;
		case SearchActivity.SEARCH_COURSE:
			titleString = "课程信息管理";
			httpLink = HttpUtil.ADDRESS_COURSE_HANDLER;
			argvs = "";
			//list = new ArrayList<Drug>();
			break;
		case SearchActivity.SEARCH_EQUIPMENT:
			titleString = "仪器信息管理";
			httpLink = HttpUtil.ADDRESS_EQUIPMENT_HANDLER;
			argvs = HttpUtil.createJsonStr("GetEquip", 
					"\"equip_name\":\"\",");
			//list = new ArrayList<Drug>();
			break;
		default:
			titleString = "Something Wrong";
			httpLink = null;
			argvs = null;
			list = null;
			break;
		}
	}

	private void updateResultList(JSONArray jsonObjArray) {
		switch (showType) {
		case SearchActivity.SEARCH_DRUG:
			list = JsonManager.getDrugArray(jsonObjArray);
			//LogUtil.d("Tag", ((Drug) list.get(0)).getDrug_name());
			if (list!=null && !list.isEmpty()) {
				Drug drugTemp;
				for (int i = 0; i < list.size(); i++) {
					Result result = new Result();
					drugTemp = (Drug) list.get(i);
					result.setName(drugTemp.getDrug_name());
					result.setProp1(drugTemp.getDrug_another_name());
					result.setProp2(drugTemp.getFen_zi_shi());
					
					resultList.add(result);
				}
				runOnUiThread(new Runnable() {
					public void run() {
						adapter.notifyDataSetChanged();
					}
				});
			}
			
			break;
		case SearchActivity.SEARCH_MIX:
			list = JsonManager.getDrugMixArray(jsonObjArray);
			//LogUtil.d("Tag", ((Drug) list.get(0)).getDrug_name());
			if (list!=null && !list.isEmpty()) {
				DrugMix temp;
				for (int i = 0; i < list.size(); i++) {
					Result result = new Result();
					temp = (DrugMix) list.get(i);
					result.setName(temp.getMix_name());
					result.setProp1(temp.getStandard());
					result.setProp2(temp.getAttention());
					
					resultList.add(result);
				}
				runOnUiThread(new Runnable() {
					public void run() {
						adapter.notifyDataSetChanged();
					}
				});
			}
			break;
		case SearchActivity.SEARCH_LAB:
			
			break;
		case SearchActivity.SEARCH_COURSE:
			
			break;
		case SearchActivity.SEARCH_EQUIPMENT:
			list = JsonManager.getEquipmentArray(jsonObjArray);
			
			if (list!=null && !list.isEmpty()) {
				Equipment temp;
				for (int i = 0; i < list.size(); i++) {
					Result result = new Result();
					temp = (Equipment) list.get(i);
					result.setName(temp.getName());
					result.setProp1(temp.getPrice());
					result.setProp2(temp.getFactory());
					
					resultList.add(result);
				}
				runOnUiThread(new Runnable() {
					public void run() {
						adapter.notifyDataSetChanged();
					}
				});
			}
			break;
		default:
			
			break;
		}
	}
	
	private void updateResult() {
		if (httpLink != null) {
			
			HttpUtil.sendHttpRequest(httpLink, argvs,
					new HttpCallbackListener() {

						@Override
						public void onFinish(String response) {
							JSONObject responseObject;
							try {
								responseObject = new JSONObject(response);

								if (responseObject.getString("error").equals("0")) {
									JSONArray jsonObjArray = responseObject
											.getJSONArray("data");
									updateResultList(jsonObjArray);
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
	
	private void gotoDetailActivity(int pos) {
		switch (showType) {
		case SearchActivity.SEARCH_DRUG:
			DrugDetailActivity.actionStart(ResultListActivity.this, ((Result)resultList.get(pos)).getName());
			break;
		case SearchActivity.SEARCH_MIX:
			DrugMixDetailActivity.actionStart(ResultListActivity.this,((Result)resultList.get(pos)).getName());
			break;
		case SearchActivity.SEARCH_LAB:
			
			break;
		case SearchActivity.SEARCH_COURSE:
			
			break;
		case SearchActivity.SEARCH_EQUIPMENT:
			EquipmentDetailActivity.actionStart(ResultListActivity.this, ((Result)resultList.get(pos)).getName());
			break;
		default:
			
			break;
		}
	}
	
	/*private void gotoActivity(Class<?> clazz) {
		Intent intent = new Intent(ResultListActivity.this, clazz);
		startActivity(intent);
	}*/
}
