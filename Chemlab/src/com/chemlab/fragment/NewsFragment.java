package com.chemlab.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chemlab.R;
import com.chemlab.activity.NewsDispActivity;
import com.chemlab.activity.NewsListActivity;
import com.chemlab.activity.SearchSelectActivity;
import com.chemlab.managers.JsonManager;
import com.chemlab.objs.News;
import com.chemlab.util.HttpCallbackListener;
import com.chemlab.util.HttpUtil;
import com.chemlab.util.LogUtil;
import com.chemlab.view.BannerView;

public class NewsFragment extends Fragment {

	private static final int NEWS_UPDATED = 0;
	private static final int NOTICE_UPDATED = 1;

	private View view;

	// private EditText drugNameText;
	// private Button drugSearchButton;

	private ListView newsListView;
	private ArrayAdapter<String> adapter;
	private List<String> newsTitle;
	private List<News> newsList;
	private List<News> noticeList;
	
	private ImageView newsUpdate;
	private TextView viewMoreNews;
	private TextView viewOtherNews;

	private ImageView noticeUpdate;
	private TextView viewLastNotice;
	private TextView viewAllNotice;

	private TextView noticeBodyText;

	private BannerView bannerImg;

	private int newsIndex;
	private int noticeIndex;
	private String newsTitleString = "新闻";
	private String noticeTitleString = "公告";

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NEWS_UPDATED:
				adapter.notifyDataSetChanged();
				break;
			case NOTICE_UPDATED:
				noticeBodyText.setText(noticeList.get(0).getTitle());
				noticeIndex = 1;
				break;	
			default:
				break;
			}
		};
	};

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (!HttpUtil.isNetworkAvailable()) {
			Toast.makeText(getActivity(), "Network is unavailable!",
					Toast.LENGTH_SHORT).show();
		}

		/*
		 * if (list == null) { list = new ArrayList<News>(); }
		 */

		if (newsTitle == null) {
			newsTitle = new ArrayList<String>();
			newsTitle.add("load...");
			newsTitle.add("load...");
			newsTitle.add("load...");
			newsIndex = 3;
		}
		noticeIndex = 0;
		
		adapter = new ArrayAdapter<String>(this.getActivity(),
				R.layout.item_news_title_list, newsTitle);

		updateNews();
		updateNotice();
	}

	private void updateNews() {
		String argvs = HttpUtil.createJsonStr("GetNewsLate",
				"\"num\":\"50\",\"news_type\":\"1\",");
		HttpUtil.sendHttpRequest(HttpUtil.ADDRESS_NEWS_HANDLER, argvs,
				new HttpCallbackListener() {

					@Override
					public void onFinish(String response) {
						//LogUtil.d("JsonManager", response);
						JSONObject responseObject;
						try {
							responseObject = new JSONObject(response);

							if (responseObject.getString("error").equals("0")) {
								JSONArray jsonObjArray = responseObject
										.getJSONArray("data");
								newsList = JsonManager.getNewsArray(jsonObjArray);

								if (!newsList.isEmpty()) {
									newsTitle.clear();
									newsTitle.add(newsList.get(0).getTitle());
									newsTitle.add(newsList.get(1).getTitle());
									newsTitle.add(newsList.get(2).getTitle());
									newsIndex = 3;
									sendHandleMessage(NEWS_UPDATED);
								}

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onError(Exception e) {
						LogUtil.d("Tag", "error");
					}
				});
	}

	private void updateNotice() {
		String argvs = HttpUtil.createJsonStr("GetNewsLate",
				"\"num\":\"50\",\"news_type\":\"2\",");
		HttpUtil.sendHttpRequest(HttpUtil.ADDRESS_NEWS_HANDLER, argvs,
				new HttpCallbackListener() {

					@Override
					public void onFinish(String response) {
						//LogUtil.d("JsonManager", response);
						JSONObject responseObject;
						try {
							responseObject = new JSONObject(response);

							if (responseObject.getString("error").equals("0")) {
								JSONArray jsonObjArray = responseObject
										.getJSONArray("data");
								noticeList = JsonManager.getNewsArray(jsonObjArray);

								if (!noticeList.isEmpty() && noticeList != null) {
									sendHandleMessage(NOTICE_UPDATED);
								}

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onError(Exception e) {
						LogUtil.d("JsonManager", "error");
					}
				});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// return super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.fragment_news, container, false);
		// drugNameText = (EditText) view.findViewById(R.id.news_drug_name);
		// drugSearchButton = (Button) view.findViewById(R.id.news_bt_search);

		view.findViewById(R.id.searchbar).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(),
								SearchSelectActivity.class);
						startActivity(intent);
					}
				});

		newsUpdate = (ImageView) view.findViewById(R.id.news_update);
		viewMoreNews = (TextView) view.findViewById(R.id.view_all_news);
		viewOtherNews = (TextView) view.findViewById(R.id.view_other_news);

		noticeUpdate = (ImageView) view.findViewById(R.id.notice_update);
		viewLastNotice = (TextView) view.findViewById(R.id.view_last_notice);
		viewAllNotice = (TextView) view.findViewById(R.id.view_all_notice);
		noticeBodyText = (TextView) view.findViewById(R.id.notice_body);
		noticeBodyText.setText("无法加载通知");
		noticeIndex = 1;
		
		newsListView = (ListView) view.findViewById(R.id.news_title);
		newsListView.setAdapter(adapter);

		bannerImg = (BannerView) view.findViewById(R.id.mainBanner);
		List<String> imgs = new ArrayList<String>();

		/*
		 * imgs.add(0,"http://"+HttpUtil.HOST+"/img/banner1.jpg");
		 * imgs.add(1,"http://"+HttpUtil.HOST+"/img/banner2.jpg");
		 * imgs.add(2,"http://"+HttpUtil.HOST+"/img/banner3.jpg");
		 * imgs.add(3,"http://"+HttpUtil.HOST+"/img/banner4.jpg");
		 */

		imgs.add(0, "drawable://" + R.drawable.banner1);
		imgs.add(1, "drawable://" + R.drawable.banner2);
		imgs.add(2, "drawable://" + R.drawable.banner3);
		imgs.add(3, "drawable://" + R.drawable.banner4);
		bannerImg.setImageUris(imgs);

		newsListView.setOnItemClickListener(listener);

		// drugNameText.setOnClickListener(searchListener);
		// drugSearchButton.setOnClickListener(searchListener);

		newsUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				updateNews();
			}
		});

		noticeUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				updateNotice();
			}
		});

		viewMoreNews.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (newsList != null && !newsList.isEmpty()) {
					NewsListActivity.actionStart(getActivity(),newsTitleString, newsList);
				}
			}
		});

		viewOtherNews.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (newsList != null && !newsList.isEmpty()) {
					newsTitle.clear();

					for (int i = 0; i < 3; i++) {
						if (newsIndex >= newsList.size()) {
							newsIndex = 0;
						}
						newsTitle.add(newsList.get(newsIndex).getTitle());
						newsIndex++;
					}
					adapter.notifyDataSetChanged();
				}
			}
		});

		viewLastNotice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (noticeList != null && !noticeList.isEmpty()) {
					if (noticeIndex >= noticeList.size()) {
						noticeIndex = 0;
					}
					
					noticeBodyText.setText(noticeList.get(noticeIndex).getTitle());
					noticeIndex++;
				}
			}
		});

		viewAllNotice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (noticeList != null && !noticeList.isEmpty()) {
				    NewsListActivity.actionStart(getActivity(),noticeTitleString, noticeList);
				}
			}
		});
		
		noticeBodyText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (noticeList != null && !noticeList.isEmpty()) {
					NewsDispActivity.actionStart(getActivity(),noticeTitleString, noticeList.get(noticeIndex-1));
				}
			}
		});

		return view;
	}

	private OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> content, View view,
				int position, long id) {
			int pos = position - 3 + newsIndex;
			if (pos<0) {
				pos += newsList.size();
			}
			if (newsList != null && !newsList.isEmpty()) {
				NewsDispActivity.actionStart(getActivity(),newsTitleString, newsList.get(pos));
			} else {
				Toast.makeText(getActivity(), "News list is empty!",
						Toast.LENGTH_SHORT).show();
			}
		}

	};

	private void sendHandleMessage(int mark) {
		Message message = new Message();
		message.what = mark;
		handler.sendMessage(message);
	}
}
