package com.chemlab.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import com.chemlab.R;
import com.chemlab.adapter.ResultListAdapter;
import com.chemlab.managers.JsonManager;
import com.chemlab.objs.Drug;
import com.chemlab.objs.DrugMix;
import com.chemlab.objs.Result;
import com.chemlab.systems.DrugDetailActivity;
import com.chemlab.systems.DrugMixDetailActivity;
import com.chemlab.util.HttpCallbackListener;
import com.chemlab.util.HttpUtil;
import com.chemlab.util.LogUtil;

public class SearchActivity extends Activity {

	public static final int SEARCH_DRUG = 1;
	public static final int SEARCH_MIX = 2;
	public static final int SEARCH_CONTACT = 3;
	public static final int SEARCH_LAB = 4;
	public static final int SEARCH_COURSE = 5;
	public static final int SEARCH_EQUIPMENT = 6;

	public static final int MIN_CLICK_DELAY_TIME = 1000;//两次输入之间最小时间间隔
	private long lastClickTime = 0; 
	
	private int serachType = 0;
	private ImageView ivDeleteText;
	private Button cancelButton;
	private EditText editText;
	private ListView resultLv;

	private ResultListAdapter adapter;
	private List<Result> resultList;
	private List<?> list;

	private String hintString;
	private String httpLink;
	private String argvs;

	public static void actionStart(Context context, int type) {
		Intent intent = new Intent(context, SearchActivity.class);
		intent.putExtra("type", type);
		context.startActivity(intent);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search);

		serachType = getIntent().getIntExtra("type", 0);

		initGlobalVaribles();

		resultList = new ArrayList<Result>();
		adapter = new ResultListAdapter(this, R.layout.item_result_list,
				resultList);

		editText = (EditText) findViewById(R.id.edit_search);
		editText.setHint(hintString);

		cancelButton = (Button) findViewById(R.id.search_cancel);
		ivDeleteText = (ImageView) findViewById(R.id.ivDeleteText);

		resultLv = (ListView) findViewById(R.id.search_result_list);
		resultLv.setAdapter(adapter);

		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		resultLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				gotoDetailActivity(position);
			}
		});

		ivDeleteText.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				editText.setText("");
			}
		});
		editText.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				long currentTime = Calendar.getInstance().getTimeInMillis(); 
			    if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
			    	lastClickTime = currentTime; 
			    	String nameSearch = editText.getText().toString().trim();
					if (!nameSearch.equals("")) {
						search(nameSearch);
					} 
			    }
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			public void afterTextChanged(Editable s) {
				if (s.length() == 0) {
					ivDeleteText.setVisibility(View.GONE);
					//list.clear();
					resultList.clear();
					adapter.notifyDataSetChanged();
				} else {
					ivDeleteText.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	private void initGlobalVaribles() {
		switch (serachType) {
		case SEARCH_DRUG:
			hintString = "请输入药品相关信息";
			httpLink = HttpUtil.ADDRESS_DRUG_HANDLER;
			break;
		case SEARCH_MIX:
			hintString = "请输入试剂相关信息";
			httpLink = HttpUtil.ADDRESS_DRUG_HANDLER;

			break;
		case SEARCH_CONTACT:
			hintString = "请输入联系人相关信息";
			httpLink = HttpUtil.ADDRESS_LOGIN_HANDLER;
			break;
		case SEARCH_LAB:
			hintString = "请输入实验相关信息";
			httpLink = HttpUtil.ADDRESS_EXPERIMENT_HANDLER;
			break;
		case SEARCH_COURSE:
			hintString = "请输入课程相关信息";
			httpLink = HttpUtil.ADDRESS_COURSE_HANDLER;
			break;
		case SEARCH_EQUIPMENT:
			hintString = "请输入仪器相关信息";
			httpLink = HttpUtil.ADDRESS_EQUIPMENT_HANDLER;
			break;
		default:
			break;
		}
	}

	private void search(String name) {
		// result.add(name);
		// adapter.notifyDataSetChanged();
		switch (serachType) {
		case SEARCH_DRUG:
			argvs = HttpUtil.createJsonStr("GetDrug", "\"drug\":\"" + name
					+ "\",");

			break;
		case SEARCH_MIX:
			argvs = HttpUtil.createJsonStr("GetDrugMix", "\"mix\":\"" + name
					+ "\",");

			break;
		case SEARCH_CONTACT:
			break;
		case SEARCH_LAB:
			break;
		case SEARCH_COURSE:
			break;
		case SEARCH_EQUIPMENT:
			break;
		default:
			break;
		}
		requestResult();
	}

	private void updateResultList(JSONArray jsonObjArray) {
		LogUtil.d("Tag", jsonObjArray.toString());
		switch (serachType) {
		case SEARCH_DRUG:
			//list.clear();
			if (list != null && list.isEmpty()) {
				list.clear();
			}
			list = JsonManager.getDrugArray(jsonObjArray);
			if (list != null && !list.isEmpty()) {
				Drug drugTemp;
				resultList.clear();
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
		case SEARCH_MIX:
			//list.clear();
			if (list != null && list.isEmpty()) {
				list.clear();
			}
			list = JsonManager.getDrugMixArray(jsonObjArray);
			// LogUtil.d("Tag", ((Drug) list.get(0)).getDrug_name());
			if (list != null && !list.isEmpty()) {
				DrugMix temp;
				resultList.clear();
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
		case SEARCH_LAB:

			break;
		case SEARCH_COURSE:

			break;
		case SEARCH_EQUIPMENT:

			break;
		default:

			break;
		}
	}

	private void requestResult() {
		if (httpLink != null) {

			HttpUtil.sendHttpRequest(httpLink, argvs,
					new HttpCallbackListener() {

						@Override
						public void onFinish(String response) {
							JSONObject responseObject;
							try {
								responseObject = new JSONObject(response);

								if (responseObject.getString("error").equals(
										"0")) {
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
		switch (serachType) {
		case SEARCH_DRUG:
			DrugDetailActivity.actionStart(SearchActivity.this, ((Result)resultList.get(pos)).getName());
			break;
		case SEARCH_MIX:
			DrugMixDetailActivity.actionStart(SearchActivity.this,((Result)resultList.get(pos)).getName());
			break;
		case SEARCH_LAB:
			
			break;
		case SEARCH_COURSE:
			
			break;
		case SEARCH_EQUIPMENT:
			
			break;
		default:
			
			break;
		}
	}

}
