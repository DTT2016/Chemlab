package com.chemlab.managers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import com.chemlab.objs.News;

public class NewsManager {
	
	public NewsManager() {
	}
	
	public static List<News> getNewsArray(String jsonData) {
		
		if (jsonData==null) {
			return null;
		}
		
		List<News> list = new ArrayList<News>();

		try {
			JSONObject jsonObject = new JSONObject(jsonData);
			News news;
			for (int i = 0; i < jsonObject.length(); i++) {
				JSONObject newsInfo = jsonObject.getJSONObject("" + i);
				Log.i("Tag", newsInfo.toString());
				news = new News();

				news.setArticleID(newsInfo.getString("ArticleID"));
				news.setTitle(newsInfo.getString("Title"));
				news.setContent_1(newsInfo.getString("Content_1"));
				news.setDateTime(newsInfo.getString("DateTime"));
				news.setHits(newsInfo.getString("Hits"));
				news.setMember_Name(newsInfo.getString("Member_Name"));
				news.setType(newsInfo.getString("type"));
				news.setInfor(newsInfo.getString("infor"));

				list.add(news);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return list;
	}
}
